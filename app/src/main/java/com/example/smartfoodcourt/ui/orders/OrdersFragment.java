package com.example.smartfoodcourt.ui.orders;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Model.Order;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrdersFragment extends Fragment {

    private OrdersViewModel slideshowViewModel;


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference orders;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(OrdersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
//        final TextView textView = root.findViewById(R.id.);
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        database = FirebaseDatabase.getInstance();
        orders = database.getReference("Order");

        recyclerView = (RecyclerView)root.findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());


        return root;
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(
                Order.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                orders.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Order model, final int position) {
                orderViewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                orderViewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                orderViewHolder.txtOrderPhone.setText(model.getPhone());

                orderViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });

                orderViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                orderViewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private void deleteOrder(String key) {
        orders.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }

//    private void showUpdateDialog(String key, Order item) {
//        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrdersFragment.class);
//        alertDialog.setTitle("Update Order");
//        alertDialog.setMessage("Please choose status");
//
//        LayoutInflater inflater = this.getLayoutInflater();
//        final View view = inflater.inflate(R.layout., null);
//    }

    private String convertCodeToStatus(String status) {
        //0: preparing, 1: ready, 2: received, 3: cancel
        if (status.equals("0")) return "Preparing";
        else if(status.equals("1")) return "Ready";
        else if(status.equals("2")) return "Received";
        else return "Cancel";
    }
}