package com.example.smartfoodcourt.ui.orders;

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
import com.example.smartfoodcourt.Model.Request;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrdersFragment extends Fragment {

    private OrdersViewModel slideshowViewModel;


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

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
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView)root.findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currentUser.getPhone());


        return root;
    }

    private void loadOrders(String phone) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder orderViewHolder, Request model, int position) {
                orderViewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                orderViewHolder.txtOrderStatus.setText(convertCodeToStatus(model.getStatus()));
                orderViewHolder.txtOrderPhone.setText(model.getPhone());
            }
        };
        recyclerView.setAdapter(adapter);
    }

    private String convertCodeToStatus(String status) {
        if (status == "0") return "In Queue";
        else if (status == "1") return "Processing...";
        else return "Complete";
    }
}