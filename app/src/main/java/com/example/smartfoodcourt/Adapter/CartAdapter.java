package com.example.smartfoodcourt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.CartItemViewHolder;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartItemViewHolder> {

    private List<CartItem> cartItemList = new ArrayList<>();
    private Context context;

    public CartAdapter(List<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_item_layout, parent, false);
        return new CartItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {

        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        float price = (Float.parseFloat(cartItemList.get(position).getPrice())*(1 - Float.parseFloat(cartItemList.get(position).getDiscount())/100))*(Integer.parseInt(cartItemList.get(position).getQuantity()));
        holder.txtPrice.setText(fmt.format(price));
        holder.txtQuantity.setText(cartItemList.get(position).getQuantity());
        holder.txtName.setText(cartItemList.get(position).getFoodName());
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
}
