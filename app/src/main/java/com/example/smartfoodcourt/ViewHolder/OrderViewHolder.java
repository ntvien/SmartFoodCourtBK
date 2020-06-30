package com.example.smartfoodcourt.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone;
    public Button btnEdit, btnDetail, btnRemove;

    private ItemClickListener itemClickListener;

    public ImageView btn_delete;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOrderId = (TextView)itemView.findViewById(R.id.order_id);
        txtOrderStatus = (TextView)itemView.findViewById(R.id.order_status);
        txtOrderPhone = (TextView)itemView.findViewById(R.id.order_phone);

        btn_delete = (ImageView)itemView.findViewById(R.id.btn_delete);
//        btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
//        btnDetail = (Button)itemView.findViewById(R.id.btnDetail);
//        btnRemove = (Button)itemView.findViewById(R.id.btnRemove);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}