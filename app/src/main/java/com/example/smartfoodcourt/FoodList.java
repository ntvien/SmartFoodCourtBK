package com.example.smartfoodcourt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuID").equalTo(categoryID)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food model, int i) {
                foodViewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(foodViewHolder.food_image);

                final Food local = model;
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
        };
        recyclerView.setAdapter(adapter);
    }
}