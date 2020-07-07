package com.example.smartfoodcourt;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.andremion.counterfab.CounterFab;
import com.example.smartfoodcourt.Database.Database;
import com.google.android.material.navigation.NavigationView;

import com.example.smartfoodcourt.Service.CompletedOrder;
import io.paperdb.Paper;

public class Home extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    CounterFab btnCart;
    Intent service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Paper.init(this);

        btnCart = findViewById(R.id.btnAddCart);
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        btnCart.setCount(new Database(this).getCountCart());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        //Set name for user
        View headerView = navigationView.getHeaderView(0);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_food, R.id.nav_order, R.id.nav_sign_out)
               .setDrawerLayout(drawer).build();
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int id = destination.getId();
                if (id == R.id.nav_sign_out){
                    btnCart.hide();
                    Intent signIn = new Intent(Home.this, SignIn.class);
                    signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(signIn);
                    Paper.book().destroy();
                    finish();
                }
                if(id == R.id.nav_home){
                    btnCart.show();
                    Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                }
                else if (id == R.id.nav_food){
                    btnCart.show();
                    Toast.makeText(getApplicationContext(), "Food", Toast.LENGTH_SHORT).show();

                }else if (id == R.id.nav_order){
                    btnCart.hide();
                    Toast.makeText(getApplicationContext(), "Order", Toast.LENGTH_SHORT).show();

                }
            }
        });
        service = new Intent(Home.this, CompletedOrder.class);
        startService(service);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnCart.setCount(new Database(this).getCountCart());
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(service);
    }
}