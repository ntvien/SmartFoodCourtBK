package com.example.smartfoodcourt.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Common;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.CartStallItem;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.CartStallItemViewHolder;

import java.text.NumberFormat;
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
    public void onBindViewHolder(@NonNull final CartStallItemViewHolder holder, final int position) {

        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        Integer total = cartStallItemList.get(position).getTotal();
        holder.txtTotal.setText(String.format("Total: %s", fmt.format(total)));
        holder.txtName.setText(String.format("Stall: %s", cartStallItemList.get(position).getSupplierID()));
        final List<CartItem> cartItemList = cartStallItemList.get(position).getCartItemList();
        holder.foodList.setLayoutManager(new LinearLayoutManager(context));
        holder.foodList.setAdapter(new CartAdapter(cartItemList, this.context));
        holder.btnChangeType.setText(Common.convertCodeToType(cartStallItemList.get(position).getType()));
        holder.btnChangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog orderDialog;
                final String[] list;
                list = new String[]{"Eat in", "Take away"};
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                mBuilder.setTitle("Order By ???");
                mBuilder.setIcon(R.drawable.ic_baseline_table_chart_24);
                mBuilder.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int index = ((AlertDialog)dialogInterface).getListView().getCheckedItemPosition();
                        cartStallItemList.get(position).setType(String.valueOf(index));
                        holder.btnChangeType.setText(Common.convertCodeToType(cartStallItemList.get(position).getType()));
                    }
                });
                orderDialog = mBuilder.create();
                orderDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartStallItemList.size();
    }
}
