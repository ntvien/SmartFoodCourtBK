package com.example.smartfoodcourt.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.R;

public class CartStallItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtName, txtTotal;
    public RecyclerView foodList;
    public Button btnChangeType;

    public CartStallItemViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName = (TextView)itemView.findViewById(R.id.txtName);
        txtTotal = (TextView)itemView.findViewById(R.id.txtTotal);
        btnChangeType = itemView.findViewById(R.id.btnChangeType);
        foodList = (RecyclerView)itemView.findViewById(R.id.foodList);


    }
    @Override
    public void onClick(View view) {

    }
}