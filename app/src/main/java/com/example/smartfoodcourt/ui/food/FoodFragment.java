package com.example.smartfoodcourt.ui.food;

import android.content.Intent;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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

    private FoodViewModel foodViewModel;

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
        foodViewModel =
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
        Bundle bundle = this.getArguments();
        if(bundle != null) {
             param = bundle.getString("supplierID",null);
        }
        if(param == null){
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
        }
        else{
            adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item,
                    FoodViewHolder.class, foodList.orderByChild("supplierID").equalTo(param)) {
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

        }
        adapter.notifyDataSetChanged();
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
                    searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item,
                            FoodViewHolder.class, foodList.orderByChild("name").startAt(s).endAt(s + "\uf8ff")) {
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
                                    foodDetail.putExtra("foodID", searchAdapter.getRef(position).getKey());
                                    startActivity(foodDetail);
                                }
                            });

                        }
                    };
                    searchAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(searchAdapter);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }
}