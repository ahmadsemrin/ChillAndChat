package com.atypon.asemrin.chillchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.atypon.asemrin.chillchat.R;
import com.atypon.asemrin.chillchat.activity.MessageActivity;
import com.atypon.asemrin.chillchat.model.Chat;
import com.atypon.asemrin.chillchat.model.User;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> users;
    private boolean isChat;
    private String lastMessage;

    public UserAdapter(Context context, List<User> users, boolean isChat) {
        this.context = context;
        this.users = users;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = users.get(position);
        holder.textViewFullName.setText(user.getFullName());
        holder.textViewUsername.setText(user.getUsername());
        holder.textViewEmail.setText(user.getEmail());
        if (user.getImageURL().equals("default")) {
            holder.imageViewProfileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(user.getImageURL()).into(holder.imageViewProfileImage);
        }

        if (isChat) {
            lastMessage(user.getId(), holder.textViewLastMessage);

            holder.textViewUsername.setVisibility(View.GONE);
            holder.textViewEmail.setVisibility(View.GONE);
        } else {
            holder.textViewLastMessage.setVisibility(View.GONE);
        }

        if (isChat) {
            if (user.getStatus().equals("online")) {
                holder.imageViewOnline.setVisibility(View.VISIBLE);
                holder.imageViewOffline.setVisibility(View.GONE);
            } else {
                holder.imageViewOnline.setVisibility(View.GONE);
                holder.imageViewOffline.setVisibility(View.VISIBLE);
            }
        } else {
            holder.imageViewOnline.setVisibility(View.GONE);
            holder.imageViewOffline.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userID", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewFullName;
        public TextView textViewUsername;
        public TextView textViewEmail;
        public ImageView imageViewProfileImage;
        private ImageView imageViewOnline;
        private ImageView imageViewOffline;
        private TextView textViewLastMessage;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            imageViewProfileImage = itemView.findViewById(R.id.profileImage);
            imageViewOnline = itemView.findViewById(R.id.circleImageViewStatusOn);
            imageViewOffline = itemView.findViewById(R.id.circleImageViewStatusOff);
            textViewLastMessage = itemView.findViewById(R.id.textViewLastMessage);
        }
    }

    private void lastMessage(final String userID, final TextView textViewLastMessage) {
        lastMessage = "default";

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userID) ||
                            chat.getReceiver().equals(userID) && chat.getSender().equals(firebaseUser.getUid())) {
                        lastMessage = chat.getMessage();
                    }
                }

                if ("default".equals(lastMessage)) {
                    textViewLastMessage.setText("No messages");
                } else {
                    textViewLastMessage.setText(lastMessage);
                }

                lastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
