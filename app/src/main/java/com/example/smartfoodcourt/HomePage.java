package com.example.smartfoodcourt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Adapter.AsiaFoodAdapter;
import com.example.smartfoodcourt.Adapter.PopularFoodAdapter;
import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.Model.AsiaFood;
import com.example.smartfoodcourt.Model.Category;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.PopularFood;
import com.example.smartfoodcourt.ViewHolder.FoodViewHolder;
import com.example.smartfoodcourt.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomePage extends AppCompatActivity {

    RecyclerView popularRecycler, asiaRecycler;
    PopularFoodAdapter popularFoodAdapter;
    AsiaFoodAdapter asiaFoodAdapter;
    Button btnMenu;

    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryID="";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btnContinue = new Intent(HomePage.this, Home.class);
                startActivity(btnContinue);
            }
        });

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        popularRecycler = (RecyclerView) findViewById(R.id.recycler_food);
        popularRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        popularRecycler.setLayoutManager(layoutManager);

        //Get Intent Here
        if (getIntent() != null)
            categoryID = getIntent().getStringExtra("CategoryID");
        if (!categoryID.isEmpty() && categoryID != null) {
            loadListFood(categoryID);
        }

    }

    private void loadListFood(String categoryID) {

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodList.orderByChild("menuID").equalTo(categoryID), Food.class).build();
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull Food food) {
                foodViewHolder.food_name.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.food_image);

                final Food local = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Start new activity
                        Intent foodDetail = new Intent(HomePage.this, FoodDetail.class);
                        foodDetail.putExtra("FoodID", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(itemView);
            }
        };

//        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
//                R.layout.food_item,
//                FoodViewHolder.class,
//                foodList.orderByChild("menuID").equalTo(categoryID)) {
//            @Override
//            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food model, int i) {
//                foodViewHolder.food_name.setText(model.getName());
//                Picasso.with(getBaseContext()).load(model.getImage()).into(foodViewHolder.food_image);
//
//                final Food local = model;
//                foodViewHolder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        //Start new activity
//                        Intent foodDetail = new Intent(HomePage.this, FoodDetail.class);
//                        foodDetail.putExtra("FoodID", adapter.getRef(position).getKey());
//                        startActivity(foodDetail);
//                    }
//                });
//            }
//        };
        adapter.startListening();
        popularRecycler.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
       // adapter.stopListening();
    }

    private void setAsiaRecycler(List<AsiaFood> asiaFoodList) {
        asiaRecycler = findViewById(R.id.asia_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        asiaRecycler.setLayoutManager(layoutManager);
        asiaFoodAdapter = new AsiaFoodAdapter(this, asiaFoodList);
        asiaRecycler.setAdapter(asiaFoodAdapter);

    }

    private void setPopularRecycler(List<PopularFood> popularFoodList) {
        popularRecycler = findViewById(R.id.popular_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        popularRecycler.setLayoutManager(layoutManager);
        popularFoodAdapter = new PopularFoodAdapter(this, popularFoodList);
        popularRecycler.setAdapter(popularFoodAdapter);
    }
}