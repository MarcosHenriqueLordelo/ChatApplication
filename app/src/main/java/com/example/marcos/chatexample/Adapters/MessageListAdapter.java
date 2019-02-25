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
import com.example.marcos.chatexample.Models.Chat;
import com.example.marcos.chatexample.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    public static final int MESSAGE_TYPE_SENT = 0;
    public static final int MESSAGE_TYPE_RECEIVED = 1;
    private ArrayList<Chat> chatList;
    private Context context;

    public MessageListAdapter(ArrayList<Chat> chatList){
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        View item;

        if (viewType == MESSAGE_TYPE_SENT){
             item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_sent, viewGroup, false);
        }else item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_item_received, viewGroup, false);

        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Chat chat = chatList.get(position);

        viewHolder.lblMessage.setText(chat.getMessage());
        viewHolder.lblTime.setText(chat.getTime());

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (chatList.get(position).getSender().equals(currentUser.getUid())){
            return MESSAGE_TYPE_SENT;
        }else return MESSAGE_TYPE_RECEIVED;

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView lblMessage, lblTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lblMessage = itemView.findViewById(R.id.lblMessage);
            lblTime = itemView.findViewById(R.id.lblTime);

        }
    }

}
