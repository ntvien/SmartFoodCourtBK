package com.example.smartfoodcourt;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Adapter.CartAdapter;
import com.example.smartfoodcourt.Adapter.CartStallAdapter;
import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Database.Database;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.CartStallItem;
import com.example.smartfoodcourt.Model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requestList;

    TextView txtTotalPrice;
    FButton btnPay;

    List<CartStallItem> cartStallItemList = new ArrayList<>();

    CartStallAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requestList = database.getReference("Order");

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPay = (FButton)findViewById(R.id.btnPayment);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(CartStallItem t: cartStallItemList){
                    Order order =  new Order(Common.currentUser.getPhone(), t);
                    requestList.child(String.valueOf(System.currentTimeMillis())).setValue(order);
                }
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Order confirmed", Toast.LENGTH_SHORT).show();
                finish();
    }
});

        loadCart();
    }


    private void loadCart() {
        cartStallItemList = new Database(this).getCart();
        adapter = new CartStallAdapter(cartStallItemList, this);
        recyclerView.setAdapter(adapter);

        //Calculate Total
        float total = 0;
        for (CartStallItem cartStallItem :cartStallItemList) {
             total += cartStallItem.getTotal();
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(total));
    }
}