package com.example.smartfoodcourt.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.R;

public class CartItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtName, txtPrice, txtQuantity;

    public CartItemViewHolder(@NonNull View itemView) {
        super(itemView);
        txtName= (TextView)itemView.findViewById(R.id.cart_item_name);
        txtPrice = (TextView)itemView.findViewById(R.id.cart_item_price);
        txtQuantity = itemView.findViewById(R.id.cart_item_quantity);
    }

    @Override
    public void onClick(View view) {

    }
}