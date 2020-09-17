package com.example.lamp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lamp.Comments.Datum;
import com.example.lamp.R;

import java.util.ArrayList;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.viewHolder> {
    private Context context;
    private ArrayList<Datum> commentsList;

    public CommentsAdapter(Context context, ArrayList<Datum> commentsList) {
        this.context = context;
        this.commentsList = commentsList;
    }

    @NonNull
    @Override
    public CommentsAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view, parent, false);
        return new CommentsAdapter.viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.viewHolder holder, int position) {

        final Datum commentsInfo = commentsList.get(position);
        holder.comment.setText(commentsInfo.getMessage());
        holder.commentDate.setText(commentsInfo.getUpdatedAt());
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView commentDate, comment;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            commentDate = itemView.findViewById(R.id.commentDate);
            comment = itemView.findViewById(R.id.comments);
        }
    }
}
