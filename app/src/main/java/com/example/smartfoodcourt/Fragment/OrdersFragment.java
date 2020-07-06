package com.example.smartfoodcourt.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Model.Order;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrdersFragment extends Fragment {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Order, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference orders;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);

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

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>().setQuery(orders.orderByChild("phone").equalTo(phone), Order.class).build();
        adapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, final int position, @NonNull final Order order) {
                orderViewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                orderViewHolder.txtOrderStatus.setText(convertCodeToStatus(order.getStatus()));
                orderViewHolder.txtOrderPhone.setText(order.getPhone());
                orderViewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (adapter.getItem(position).getStatus().equals("1"))
                            confirmOrder(adapter.getRef(position).getKey(), order);

                    }
                });

            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout, parent, false);
                return new OrderViewHolder(itemView);
            }
        };

        adapter.notifyDataSetChanged();
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void confirmOrder(final String key, Order order) {
        database.getReference("ConfirmedOrder/" + order.getSupplierID()).push().setValue(order.getFoods());
        orders.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getContext(),new StringBuilder("Order").append(key).append("has been confirmed").toString(),Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private String convertCodeToStatus(String status) {
        //0: preparing, 1: ready, 2: received
        if (status.equals("0")) return "Preparing";
        else if(status.equals("1")) return "Ready";
        else return "Received";
    }
}