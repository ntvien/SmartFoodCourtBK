package com.example.smartfoodcourt.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.R;

public class StallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtStall;
    public ImageView imgStall;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public StallViewHolder(@NonNull View itemView) {
        super(itemView);
        txtStall = (TextView)itemView.findViewById(R.id.txtStall);
        imgStall = (ImageView)itemView.findViewById(R.id.imgStall);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());
    }
}
