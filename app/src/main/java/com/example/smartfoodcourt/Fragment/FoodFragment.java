package com.example.smartfoodcourt.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Common;
import com.example.smartfoodcourt.FoodDetail.FoodDetailPage;
import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodFragment extends Fragment {


    RecyclerView recyclerFood;

    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> foodAdapter;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchFoodAdapter;
    Integer supplierID = 0;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_food, container, false);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food/List");

        recyclerFood = (RecyclerView)root.findViewById(R.id.recycler_food);
        recyclerFood.setHasFixedSize(true);
        recyclerFood.setLayoutManager(new LinearLayoutManager(getContext()));

        loadFoodList();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        foodAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(searchFoodAdapter != null){
            searchFoodAdapter.stopListening();
        }
    }

    private void loadFoodList() {
        FirebaseRecyclerOptions<Food> options;
        Bundle bundle = this.getArguments();
        if(bundle != null) {
             supplierID = bundle.getInt(Common.CHOICE_STALL, 0);
        }
        if(supplierID == 0){
            options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodList, Food.class).build();
        }
        else{
             options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodList.orderByChild("supplierID").equalTo(supplierID), Food.class).build();
        }
        foodAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull final Food food) {
                if(food.getStatus() != null && food.getStatus().equals("1"))
                    foodViewHolder.outOfOrder_image.setImageResource(Common.convertOutOfOrderToImage());
                foodViewHolder.food_name.setText(food.getName());
                foodViewHolder.food_price.setText(Common.convertPriceToVND(food.getPrice()));
                foodViewHolder.food_supplier.setText(String.format("Stall %s", food.getSupplierID()));
                Picasso.with(getContext()).load(food.getImage()).into(foodViewHolder.food_image);

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent foodDetail = new Intent(getContext(), FoodDetailPage.class);
                        foodDetail.putExtra(Common.INTENT_FOOD_REF, foodAdapter.getRef(position).getKey());
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

        foodAdapter.notifyDataSetChanged();
        recyclerFood.setAdapter(foodAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
       if(supplierID == null){
           getActivity().getMenuInflater().inflate(R.menu.food_search, menu);
           MenuItem searchItem = menu.findItem(R.id.action_search);
           SearchView searchView = (SearchView)searchItem.getActionView();
           searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
           searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
               @Override
               public boolean onQueryTextSubmit(String s) {
                   return false;
               }

               @Override
               public boolean onQueryTextChange(String s) {
                   if(s.isEmpty()) recyclerFood.setAdapter(foodAdapter);
                   else {
                       showSearchFoodList(s);
                   }
                   return false;
               }
           });
       }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showSearchFoodList(String s) {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodList.orderByChild("name").startAt(s).endAt(s + "\uf8ff"), Food.class).build();
        searchFoodAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull final Food food) {
                foodViewHolder.food_name.setText(food.getName());
                foodViewHolder.food_price.setText(Common.convertPriceToVND(food.getPrice()));
                foodViewHolder.food_supplier.setText(String.format("Stall %s", food.getSupplierID()));
                Picasso.with(getContext()).load(food.getImage()).into(foodViewHolder.food_image);
                if(food.getStatus().equals("1"))
                    foodViewHolder.outOfOrder_image.setImageResource(Common.convertOutOfOrderToImage());

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent foodDetail = new Intent(getContext(), FoodDetailPage.class);
                        foodDetail.putExtra(Common.INTENT_FOOD_REF, searchFoodAdapter.getRef(position).getKey());
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
        searchFoodAdapter.startListening();
        searchFoodAdapter.notifyDataSetChanged();
        recyclerFood.setAdapter(searchFoodAdapter);
    }

}