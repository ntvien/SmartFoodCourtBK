package com.example.smartfoodcourt.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.FoodDetail;
import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.Stall;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.FoodViewHolder;
import com.example.smartfoodcourt.ViewHolder.StallViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

public class HomeFragment extends Fragment {



    RecyclerView popularRecycler, stallRecycler;
    View root;


    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList, supplierList;


    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapterPopularFood;
    FirebaseRecyclerAdapter<Stall, StallViewHolder> adapterStall;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
          root = inflater.inflate(R.layout.fragment_home, container, false);




        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");
        supplierList = database.getReference("Supplier");

        popularRecycler = (RecyclerView)root.findViewById(R.id.popular_recycler);
        stallRecycler = root.findViewById(R.id.stall_recycler);

        popularRecycler.setHasFixedSize(true);

         layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);

        layoutManager = new LinearLayoutManager(getContext());
        //layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        popularRecycler.setLayoutManager(layoutManager);


        popularRecycler.setLayoutManager(layoutManager);
        stallRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        loadPopularFoodList();
        loadMenuStall();

        return root;
    }

    private void loadMenuStall() {

        FirebaseRecyclerOptions<Stall> options = new FirebaseRecyclerOptions.Builder<Stall>()
                .setQuery(supplierList, Stall.class).build();
        adapterStall = new FirebaseRecyclerAdapter<Stall, StallViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StallViewHolder stallViewHolder, int i, final Stall stall) {
                stallViewHolder.txtStall.setText(stall.getName());
                if(!stall.getImage().isEmpty())Picasso.with(getContext()).load(stall.getImage()).into(stallViewHolder.imgStall);

                stallViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        FoodFragment foodFragment = new FoodFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("supplierID", stall.getSupplierID());
                        foodFragment.setArguments(bundle);
                        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.nav_host_fragment, foodFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }
                });
            }

            @NonNull
            @Override
            public StallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.stall_item, parent, false);
                return new StallViewHolder(itemView);
            }
        };

        adapterStall.notifyDataSetChanged();
        adapterStall.startListening();
        stallRecycler.setAdapter(adapterStall);
    }

    private void loadPopularFoodList() {

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodList, Food.class).build();
        adapterPopularFood = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
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
                        foodDetail.putExtra("foodID", adapterPopularFood.getRef(position).getKey());
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

        adapterPopularFood.notifyDataSetChanged();
        adapterPopularFood.startListening();
        popularRecycler.setAdapter(adapterPopularFood);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}