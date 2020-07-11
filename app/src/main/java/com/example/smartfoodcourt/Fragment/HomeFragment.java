package com.example.smartfoodcourt.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Common;
import com.example.smartfoodcourt.FoodDetail.FoodPage;
import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.Stall;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.GreatFoodViewHolder;
import com.example.smartfoodcourt.ViewHolder.StallViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    RecyclerView greatFoodRecycler, stallRecycler;
    FirebaseDatabase database;
    DatabaseReference foodList, supplierList;

    FirebaseRecyclerAdapter<Food, GreatFoodViewHolder> adapterNewFood;
    FirebaseRecyclerAdapter<Stall, StallViewHolder> adapterStall;

    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food/List");
        supplierList = database.getReference("Supplier/List");
        greatFoodRecycler = (RecyclerView)root.findViewById(R.id.great_food_recycler);
        stallRecycler = root.findViewById(R.id.stall_recycler);

        greatFoodRecycler.setHasFixedSize(true);
        greatFoodRecycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        stallRecycler.setHasFixedSize(true);
        stallRecycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        loadGreatFoodList();
        loadStallList();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapterNewFood.startListening();
        adapterStall.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterNewFood.stopListening();
        adapterStall.stopListening();
    }

    private void loadStallList() {
        FirebaseRecyclerOptions<Stall> options = new FirebaseRecyclerOptions.Builder<Stall>().setQuery(supplierList, Stall.class).build();
        adapterStall = new FirebaseRecyclerAdapter<Stall, StallViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StallViewHolder stallViewHolder, int i, final Stall stall) {
                stallViewHolder.txtStall.setText(stall.getName());
                if(!stall.getImage().isEmpty()) Picasso.with(getContext()).load(stall.getImage()).into(stallViewHolder.imgStall);
                stallViewHolder.txtNumber.setText(stall.getSupplierID());
                stallViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        FoodFragment foodFragment = new FoodFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(Common.CHOICE_STALL, stall.getSupplierID());
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
        stallRecycler.setAdapter(adapterStall);
    }

    private void loadGreatFoodList() {

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodList.orderByChild("star").limitToLast(5), Food.class).build();

        adapterNewFood = new FirebaseRecyclerAdapter<Food, GreatFoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull GreatFoodViewHolder greatFoodViewHolder, int i, @NonNull Food food) {
                if(food.getStatus().equals("1"))
                    greatFoodViewHolder.outOfOrder_image.setImageResource(Common.convertOutOfOrderToImage());
                greatFoodViewHolder.ratingBar.setRating(Float.parseFloat(food.getStar()));
                greatFoodViewHolder.discount_image.setImageResource(Common.convertDiscountToImage(food.getDiscount()));
                greatFoodViewHolder.food_name.setText(food.getName());
                greatFoodViewHolder.food_price.setText(Common.convertPricetoVND(food.getPrice()));
                Picasso.with(getContext()).load(food.getImage()).into(greatFoodViewHolder.food_image);

                greatFoodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent foodDetail = new Intent(getContext(), FoodPage.class);
                        foodDetail.putExtra(Common.INTENT_FOOD_REF, adapterNewFood.getRef(position).getKey());
                        startActivity(foodDetail);
                    }
                });
            }

            @NonNull
            @Override
            public GreatFoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                greatFoodRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(getContext(), R.anim.layout_item_from_left));
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.great_food_item, parent, false);
                return new GreatFoodViewHolder(itemView);
            }
        };
        adapterNewFood.notifyDataSetChanged();
        greatFoodRecycler.setAdapter(adapterNewFood);
    }

}