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
import com.atypon.asemrin.chillchat.model.User;
import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<User> users;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
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

        public ViewHolder(View itemView) {
            super(itemView);

            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            imageViewProfileImage = itemView.findViewById(R.id.profileImage);
        }
    }
}
