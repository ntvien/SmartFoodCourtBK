package com.example.smartfoodcourt.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
    public TextView txtUserName, txtComment;
    public RatingBar ratingBarDetail;

    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        txtUserName = (TextView)itemView.findViewById(R.id.txtUserName);
        txtComment = (TextView)itemView.findViewById(R.id.txtComment);
        ratingBarDetail = (RatingBar)itemView.findViewById(R.id.ratingBarDetail);
    }
}
