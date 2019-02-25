package com.example.marcos.chatexample;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marcos.chatexample.Adapters.MessageListAdapter;
import com.example.marcos.chatexample.Models.Chat;
import com.example.marcos.chatexample.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private TextView userName;
    private CircleImageView userImage;

    private String receiverUserId, currentUserId, chatId;

    private Intent intent;

    private FirebaseUser currentUser;
    private DatabaseReference dbRef;

    private EditText messageText;
    private ImageButton btnSend;
    private Button btnBack;

    private MessageListAdapter messageListAdapter;
    private ArrayList<Chat> chatMessages;
    private RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        userName = findViewById(R.id.lblUserProfileName);
        userImage = findViewById(R.id.userProfileImg);
        messageText = findViewById(R.id.messageEditor);
        btnSend = findViewById(R.id.btnSend);
        recView = findViewById(R.id.recViewMessages);
        btnBack = findViewById(R.id.btnBack);

        recView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recView.setLayoutManager(linearLayoutManager);

        chatMessages = new ArrayList<>();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intent = getIntent();
        receiverUserId = intent.getStringExtra("userUid");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = currentUser.getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(receiverUserId);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName.setText(user.getName());
                new DownloadImageTask(userImage).execute(user.getPhotoUrl());
                
                if( currentUserId.compareTo(receiverUserId) > 0) chatId = currentUserId + receiverUserId;
                else chatId = receiverUserId + currentUserId;
                readMessages(currentUserId, receiverUserId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                String message = String.valueOf(messageText.getText());
                String time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY) +
                        ":" + Calendar.getInstance().get(Calendar.MINUTE);
                String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                        calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);

                if (!message.equals("")){
                    sendMessage(currentUser.getUid(), receiverUserId, message, time, date);
                }else Toast.makeText(MessageActivity.this, "Menssagem Vazia", Toast.LENGTH_SHORT).show();

                messageText.setText("");
            }
        });
    }

    private void sendMessage(String sender, String receiver, String message, String time, String date){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Sender", sender);
        hashMap.put("Receiver", receiver);
        hashMap.put("Message", message);
        hashMap.put("Time", time);
        hashMap.put("Date", date);

        dbRef.child("Chats").child(chatId).push().setValue(hashMap);
    }

    private void readMessages(final String myId, final String herId){
        dbRef = FirebaseDatabase.getInstance().getReference("Chats").child(chatId);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatMessages.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat message = snapshot.getValue(Chat.class);
                    if (message.getSender().equals(myId) && message.getReceiver().equals(herId)
                            || message.getReceiver().equals(myId) && message.getSender().equals(herId) ){
                        chatMessages.add(message);
                    }
                    messageListAdapter = new MessageListAdapter(chatMessages);
                    recView.setAdapter(messageListAdapter);                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status){
        if (currentUser != null) {
            dbRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Status", status);

            dbRef.updateChildren(hashMap);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }
}
