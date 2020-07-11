package com.example.smartfoodcourt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CartGroupItemAdapter extends RecyclerView.Adapter<CartGroupItemAdapter.ViewHolder> {

    private List<CartItem> cartItemList;
    private Context context;

    public CartGroupItemAdapter(List<CartItem> cartItemList, Context context) {
        this.cartItemList = cartItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        Integer price = (int) (Float.parseFloat(cartItem.getPrice())*(1 - Float.parseFloat(cartItem.getDiscount())/100)*Float.parseFloat(cartItem.getQuantity()));
        holder.txtPrice.setText(fmt.format(price));
        holder.txtQuantity.setText(cartItem.getQuantity());
        holder.txtName.setText(cartItem.getName());
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPrice, txtQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName= (TextView)itemView.findViewById(R.id.cart_item_name);
            txtPrice = (TextView)itemView.findViewById(R.id.cart_item_price);
            txtQuantity = (TextView)itemView.findViewById(R.id.cart_item_quantity);
        }
    }
}