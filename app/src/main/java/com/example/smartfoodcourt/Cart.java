package com.example.smartfoodcourt;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Adapter.CartAdapter;
import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Database.Database;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.Request;
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

    List<CartItem> cartItemList = new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = FirebaseDatabase.getInstance();
        requestList = database.getReference("Requests");

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPay = (FButton)findViewById(R.id.btnPayment);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request request =  new Request(Common.currentUser.getPhone(), txtTotalPrice.getText().toString(), "0", cartItemList);
                requestList.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Order confirmed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        loadCart();
    }


    private void loadCart() {
        cartItemList = new Database(this).getCart();
        adapter = new CartAdapter(cartItemList, this);
        recyclerView.setAdapter(adapter);

        //Calculate Total
        float total = 0;
        for (CartItem cartItem :cartItemList) {
             total += (Integer.parseInt(cartItem.getPrice())) * (1 - (Float.parseFloat(cartItem.getDiscount()))/100) * (Integer.parseInt(cartItem.getQuantity()));
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(total));
    }
}