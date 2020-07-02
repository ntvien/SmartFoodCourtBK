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

import com.example.smartfoodcourt.FoodDetail;
import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class FoodFragment extends Fragment {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryID="";

    String param;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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
        FirebaseRecyclerOptions<Food> options;
        Bundle bundle = this.getArguments();
        if(bundle != null) {
             param = bundle.getString("supplierID",null);
        }
        if(param == null){
            options = new FirebaseRecyclerOptions.Builder<Food>()
                    .setQuery(foodList, Food.class).build();

        }
        else{
             options = new FirebaseRecyclerOptions.Builder<Food>()
                    .setQuery(foodList.orderByChild("supplierID").equalTo(param), Food.class).build();
        }
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull Food food) {
                Locale locale = new Locale("vi", "VN");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                if(Integer.parseInt(food.getDiscount()) > 10) foodViewHolder.discount_image.setImageResource(R.drawable.bigdiscount);
                else if(Integer.parseInt(food.getDiscount()) > 0) foodViewHolder.discount_image.setImageResource(R.drawable.smalldiscount);
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

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(itemView);
            }
        };
        adapter.notifyDataSetChanged();
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
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
                if(s.isEmpty()) recyclerView.setAdapter(adapter);
                else {
                    showSearchFoodList(s);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showSearchFoodList(String s) {
        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodList.orderByChild("name").startAt(s).endAt(s + "\uf8ff"), Food.class).build();
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull Food food) {
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
                        foodDetail.putExtra("foodID", searchAdapter.getRef(position).getKey());
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

        searchAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(searchAdapter);
    }


}