package com.example.smartfoodcourt.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.FoodDetail;
import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodFragment extends Fragment {

    private FoodViewModel galleryViewModel;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryID="";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(FoodViewModel.class);
        View root = inflater.inflate(R.layout.fragment_food, container, false);


        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        recyclerView = (RecyclerView)root.findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

       loadFoodList();
        return root;
    }

    private void loadFoodList() {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {

                Locale locale = new Locale("vi", "VN");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                foodViewHolder.food_name.setText(food.getName());
                foodViewHolder.food_price.setText(fmt.format(Integer.parseInt(food.getPrice())));
                Picasso.with(getContext()).load(food.getImage()).into(foodViewHolder.food_image);

                final Food clickItem = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent foodDetail = new Intent(getContext(), FoodDetail.class);
                        foodDetail.putExtra("foodID", adapter.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}