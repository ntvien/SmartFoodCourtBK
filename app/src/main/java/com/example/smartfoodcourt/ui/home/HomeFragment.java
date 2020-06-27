package com.example.smartfoodcourt.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfoodcourt.Adapter.AsiaFoodAdapter;
import com.example.smartfoodcourt.Adapter.PopularFoodAdapter;
import com.example.smartfoodcourt.FoodDetail;
import com.example.smartfoodcourt.Interface.ItemClickListener;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.PopularFood;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.FoodViewHolder;
import com.example.smartfoodcourt.ui.food.FoodFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {


    private HomeViewModel homeViewModel;

    RecyclerView popularRecycler, asiaRecycler;
    PopularFoodAdapter popularFoodAdapter;
    AsiaFoodAdapter asiaFoodAdapter;
    Button btnMenu;
    View root;

    private HomeViewModel galleryViewModel;

    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;

    String categoryID="";

    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;


    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
       root = inflater.inflate(R.layout.fragment_home, container, false);

        btnMenu = (Button)root.findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodFragment foodFragment = new FoodFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment, foodFragment);
                fragmentTransaction.commit();
            }
        });


        //Firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Food");

        popularRecycler = (RecyclerView)root.findViewById(R.id.popular_recycler);
        popularRecycler.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        popularRecycler.setLayoutManager(layoutManager);

        loadFoodList();




        List<PopularFood> popularFoodList = new ArrayList<>();




//        List<AsiaFood> asiaFoodList = new ArrayList<>();
//        asiaFoodList.add(new AsiaFood("Chicago Pizza", "$20", R.drawable.banhbaotrungmuoi, "4.5", "Briand Restaurant"));
//        asiaFoodList.add(new AsiaFood("Straberry Cake", "$25", R.drawable.banhbaotrungmuoi, "4.2", "Friends Restaurant"));
//        asiaFoodList.add(new AsiaFood("Chicago Pizza", "$20", R.drawable.banhbaotrungmuoi, "4.5", "Briand Restaurant"));
//        asiaFoodList.add(new AsiaFood("Straberry Cake", "$25", R.drawable.banhbaotrungmuoi, "4.2", "Friends Restaurant"));
//        asiaFoodList.add(new AsiaFood("Chicago Pizza", "$20", R.drawable.banhbaotrungmuoi, "4.5", "Briand Restaurant"));
//        asiaFoodList.add(new AsiaFood("Straberry Cake", "$25", R.drawable.banhbaotrungmuoi, "4.2", "Friends Restaurant"));
//
//       setAsiaRecycler(asiaFoodList);

        return root;
    }
//    private void setAsiaRecycler(List<AsiaFood> asiaFoodList) {
//        asiaRecycler = root.findViewById(R.id.popular_recycler);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
//        asiaRecycler.setLayoutManager(layoutManager);
//        asiaFoodAdapter = new AsiaFoodAdapter(getContext(), asiaFoodList);
//        asiaRecycler.setAdapter(asiaFoodAdapter);
//
//    }

    private void setPopularRecycler(List<PopularFood> popularFoodList) {
        popularRecycler = root.findViewById(R.id.popular_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        popularRecycler.setLayoutManager(layoutManager);
        popularFoodAdapter = new PopularFoodAdapter(getContext(), popularFoodList);
        popularRecycler.setAdapter(popularFoodAdapter);
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
        popularRecycler.setAdapter(adapter);
    }

}