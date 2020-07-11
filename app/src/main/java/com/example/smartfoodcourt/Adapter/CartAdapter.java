package com.example.smartfoodcourt.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Common;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.CartGroupItem;
import com.example.smartfoodcourt.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> implements View.OnClickListener {

    private List<CartGroupItem> cartGroupItemList;
    private Context context;
    private CartGroupItemListener listener;
    public CartAdapter(List<CartGroupItem> cartGroupItemList, CartGroupItemListener listener) {
        this.cartGroupItemList = cartGroupItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.cart_group_item_layout, parent, false);
        return new ViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        Integer total = cartGroupItemList.get(position).getTotal();
        holder.txtTotal.setText(String.format("Total: %s", fmt.format(total)));
        holder.txtName.setText(String.format("Stall: %s", cartGroupItemList.get(position).getSupplierID()));
        final List<CartItem> cartItemList = cartGroupItemList.get(position).getCartItemList();
        holder.foodList.setLayoutManager(new LinearLayoutManager(context));
        holder.foodList.setAdapter(new CartGroupItemAdapter(cartItemList, this.context));
        holder.btnChangeType.setText(Common.convertCodeToType(cartGroupItemList.get(position).getType()));
    }

    @Override
    public int getItemCount() {
        return cartGroupItemList.size();
    }

    @Override
    public void onClick(View view) {

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtName, txtTotal;
        RecyclerView foodList;
        Button btnChangeType;
        CartGroupItemListener listener;
        public ViewHolder(@NonNull View itemView, CartGroupItemListener listener) {
            super(itemView);
            this.listener = listener;
            txtName = (TextView)itemView.findViewById(R.id.txtName);
            txtTotal = (TextView)itemView.findViewById(R.id.txtTotal);
            btnChangeType = itemView.findViewById(R.id.btnChangeType);
            foodList = (RecyclerView)itemView.findViewById(R.id.foodList);
            btnChangeType.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btnChangeType){
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
                        listener.onTypeChangeClick(getAdapterPosition(), String.valueOf(index));
                    }
                });
                orderDialog = mBuilder.create();
                orderDialog.show();
            }
        }
    }
    public interface CartGroupItemListener{
        void onTypeChangeClick(int position, String newType);
        Context getContext();
    }
}
