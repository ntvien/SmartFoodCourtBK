package com.example.smartfoodcourt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.CartStallItem;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.CartStallItemViewHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartStallAdapter extends RecyclerView.Adapter<CartStallItemViewHolder> {

    private List<CartStallItem> cartStallItemList;
    private Context context;

    public CartStallAdapter(List<CartStallItem> cartStallItemList, Context context) {
        this.cartStallItemList = cartStallItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartStallItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_stall_item_layout, parent, false);
        return new CartStallItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartStallItemViewHolder holder, int position) {

        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        Integer total = cartStallItemList.get(position).getTotal();
        holder.txtTotal.setText("Total: " + fmt.format(total));
        holder.txtName.setText("Stall: " + cartStallItemList.get(position).getSupplierID());
        List<CartItem> cartItemList = cartStallItemList.get(position).getCartItemList();
        holder.foodList.setLayoutManager(new LinearLayoutManager(context));
        holder.foodList.setAdapter(new CartAdapter(cartItemList, this.context));
    }

    @Override
    public int getItemCount() {
        return cartStallItemList.size();
    }
}
