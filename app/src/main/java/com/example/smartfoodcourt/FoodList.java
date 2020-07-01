package com.example.smartfoodcourt;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.Model.Category;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryID="";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

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
                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
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
//                        Intent foodDetail = new Intent(FoodList.this, FoodDetail.class);
//                        foodDetail.putExtra("FoodID", adapter.getRef(position).getKey());
//                        startActivity(foodDetail);
//                    }
//                });
//            }
//        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
       //adapter.stopListening();
    }
}