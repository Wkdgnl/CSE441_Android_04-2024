package com.example.book.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.book.Domain.Comment;
import com.example.book.Domain.User;
import com.example.book.R;

import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> comments;
    private Map<String, User> users;

    public CommentAdapter(List<Comment> comments, Map<String, User> users) {
        this.comments = comments;
        this.users = users;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.e("CommentAdapter", "onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        User user = users.get(comment.getUserID());
        if (user != null) {
            String name = user.getName();
            holder.userNameTxt.setText(name);
        } else {
            // Handle the case where user is null
        }

        holder.commentTxt.setText(comment.getContent());
        holder.timestampTxt.setText(comment.getTimestamp());

        Glide.with(holder.avatarUser.getContext())
                .load(user.getAvatar())
                .circleCrop()
                .into(holder.avatarUser);
    }

    @Override
    public int getItemCount() {
        Log.e("CommentAdapter", "getItemCount called, size: " + comments.size());
        return comments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTxt, commentTxt, timestampTxt;
        ImageView avatarUser;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTxt = itemView.findViewById(R.id.userNameTxt);
            commentTxt = itemView.findViewById(R.id.commentTxt);
            timestampTxt = itemView.findViewById(R.id.timestampTxt);
            avatarUser = itemView.findViewById(R.id.avatarUser);
        }
    }
}
