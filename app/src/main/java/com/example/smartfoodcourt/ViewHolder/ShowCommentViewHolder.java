package com.example.smartfoodcourt.ViewHolder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
    public TextView txtUserPhone, txtComment;
    public RatingBar ratingBarDetail;

    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        txtUserPhone = (TextView)itemView.findViewById(R.id.txtUserPhone);
        txtComment = (TextView)itemView.findViewById(R.id.txtComment);
        ratingBarDetail = (RatingBar)itemView.findViewById(R.id.ratingBarDetail);
    }
}
