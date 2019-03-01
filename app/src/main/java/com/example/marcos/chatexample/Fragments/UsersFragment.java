package com.example.marcos.chatexample.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.marcos.chatexample.MainActivity;
import com.example.marcos.chatexample.R;
import com.example.marcos.chatexample.Models.User;
import com.example.marcos.chatexample.Adapters.UserListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersFragment extends Fragment {

    private RecyclerView recView;
    private UserListAdapter userListAdapter;
    private ArrayList<User> userList;
    private EditText searchEdt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);

        searchEdt = view.findViewById(R.id.editTextSearch);

        recView = view.findViewById(R.id.recViewUsers);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        recView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        userList = new ArrayList<>();
        getUsersFromDb();

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                searchUSers(String.valueOf(text));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void getUsersFromDb(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if (!currentUser.getUid().equals(user.getUid())){
                        userList.add(user);
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

    private void searchUSers(final String text){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if (!currentUser.getUid().equals(user.getUid()) && user.getName().toLowerCase().contains(text.toLowerCase())){
                        userList.add(user);
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
