package com.example.marcos.chatexample.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.marcos.chatexample.Adapters.UserListAdapter;
import com.example.marcos.chatexample.Models.Chat;
import com.example.marcos.chatexample.Models.User;
import com.example.marcos.chatexample.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatsFragment extends Fragment {

    private RecyclerView recView;

    private ArrayList<String> uidList;
    private ArrayList<User> userList;
    private UserListAdapter userListAdapter;

    private FirebaseUser currentUser;
    private DatabaseReference dbRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recView = view.findViewById(R.id.recViewChats);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        uidList = new ArrayList<>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uidList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String chat = snapshot.getKey();
                    if (chat.contains(currentUser.getUid())) uidList.add(chat.replace(currentUser.getUid(), ""));
                    readChats();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void readChats(){
        userList = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference("Users");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for(String uid : uidList){
                        if (user.getUid().equals(uid)){
                            if (userList.size() != 0){
                                for(User user1 : userList){
                                    if (!user.getUid().equals(user1.getUid())) userList.add(user);
                                }
                            }else userList.add(user);
                        }
                    }
                }
                userListAdapter = new UserListAdapter(userList);
                recView.setAdapter(userListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
