package com.example.marcos.chatexample.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marcos.chatexample.DownloadImageTask;
import com.example.marcos.chatexample.MessageActivity;
import com.example.marcos.chatexample.R;
import com.example.marcos.chatexample.Models.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {

    private ArrayList<User> userList;
    private Context context;

    public UserListAdapter(ArrayList<User> userList){
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        context = viewGroup.getContext();

        View item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_list_item, viewGroup, false);

        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final User user = userList.get(position);
        viewHolder.userName.setText(user.getName());
        viewHolder.userStatus.setText(user.getStatus());
        new DownloadImageTask(viewHolder.userImage).execute(user.getPhotoUrl());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userUid", user.getUid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userName, userStatus;
        public CircleImageView userImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.lblUserListName);
            userImage = itemView.findViewById(R.id.imgUserList);
            userStatus = itemView.findViewById(R.id.lblUserListStatus);

        }
    }

}
