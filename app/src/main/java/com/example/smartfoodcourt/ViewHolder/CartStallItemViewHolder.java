package com.example.smartfoodcourt.ViewHolder;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.R;

public class CartStallItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtName, txtTotal, txtType;
    public RecyclerView foodList;

    public CartStallItemViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName = (TextView)itemView.findViewById(R.id.txtName);
        txtTotal = (TextView)itemView.findViewById(R.id.txtTotal);
        txtType = (TextView) itemView.findViewById(R.id.txtStatusOrderBy);
        foodList = (RecyclerView)itemView.findViewById(R.id.foodList);
    }

    @Override
    public void onClick(View view) {

    }
}