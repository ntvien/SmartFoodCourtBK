package com.example.smartfoodcourt.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Common;
import com.example.smartfoodcourt.Model.Order;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Order, OrderViewHolder> adapterOrder;
    DatabaseReference orderReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_order, container, false);

        orderReference = FirebaseDatabase.getInstance().getReference("Order/CurrentOrder/List");
        recyclerView = root.findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        loadOrder(Common.user.getPhone());

        return root;
    }

    private void loadOrder(String phone) {

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>().setQuery(orderReference.orderByChild("phone").equalTo(phone), Order.class).build();
        adapterOrder = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, final int position, @NonNull final Order order) {
                orderViewHolder.txtOrderId.setText(String.format("Stall: %s", order.getSupplierID()));
                orderViewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(order.getStatus()));
                orderViewHolder.txtTotal.setText(String.format("Tổng tiền: %s", Common.convertPriceToVND(order.getTotal())));
                orderViewHolder.btnReceive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (adapterOrder.getItem(position).getStatus().equals("1"))
                            adapterOrder.getRef(position).child("status").setValue("2");
                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
                return new OrderViewHolder(itemView);
            }
        };

        adapterOrder.notifyDataSetChanged();
        recyclerView.setAdapter(adapterOrder);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterOrder.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterOrder.stopListening();
    }

}