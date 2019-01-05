package com.atypon.asemrin.chillchat.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.atypon.asemrin.chillchat.R;
import com.atypon.asemrin.chillchat.adapter.MessageAdapter;
import com.atypon.asemrin.chillchat.model.Chat;
import com.atypon.asemrin.chillchat.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private CircleImageView profileImage;
    private ImageButton imageButtonSend;
    private TextView textViewUsername;
    private EditText editTextMessage;
    private RecyclerView recyclerView;
    private TextView textViewEmpty;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private MessageAdapter messageAdapter;
    private List<Chat> chatList;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MessageActivity.this, ProfileActivity.class).setFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        profileImage = findViewById(R.id.profile_image);
        imageButtonSend = findViewById(R.id.imageButtonSend);
        textViewUsername = findViewById(R.id.textViewUsername);
        editTextMessage = findViewById(R.id.editTextMessage);
        textViewEmpty = findViewById(R.id.textViewEmpty);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();
        final String userID = intent.getStringExtra("userID");

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = editTextMessage.getText().toString();
                if (!message.equals("")) {
                    sendMessage(firebaseUser.getUid(), userID, message);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send an empty message",
                            Toast.LENGTH_SHORT).show();
                }
                editTextMessage.setText("");
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                textViewUsername.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(profileImage);
                }

                readMessages(firebaseUser.getUid(), userID, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> messageInfo = new HashMap<>();
        messageInfo.put("sender", sender);
        messageInfo.put("receiver", receiver);
        messageInfo.put("message", message);

        databaseReference.child("Chats").push().setValue(messageInfo);
    }

    private void readMessages(final String myID, final String userID, final String imageURL) {
        chatList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myID) && chat.getSender().equals(userID) ||
                            chat.getReceiver().equals(userID) && chat.getSender().equals(myID)) {
                        chatList.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, chatList, imageURL);
                    recyclerView.setAdapter(messageAdapter);
                }

                if (chatList.size() == 0) {
                    textViewEmpty.setVisibility(View.VISIBLE);
                } else {
                    textViewEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("status", status);
        databaseReference.updateChildren(userInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();

        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();

        status("offline");
    }
}
