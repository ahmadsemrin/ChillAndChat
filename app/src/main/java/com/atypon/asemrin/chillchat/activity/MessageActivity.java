package com.atypon.asemrin.chillchat.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.atypon.asemrin.chillchat.R;
import com.atypon.asemrin.chillchat.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profileImage;
    ImageButton imageButtonSend;
    TextView textViewUsername;
    EditText editTextMessage;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    Intent intent;

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
                finish();
            }
        });

        profileImage = findViewById(R.id.profile_image);
        imageButtonSend = findViewById(R.id.imageButtonSend);
        textViewUsername = findViewById(R.id.textViewUsername);
        editTextMessage = findViewById(R.id.editTextMessage);

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
}
