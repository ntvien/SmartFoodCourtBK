package com.example.smartfoodcourt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.telecom.CallRedirectionService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.Rating;
import com.example.smartfoodcourt.ViewHolder.FoodViewHolder;
import com.example.smartfoodcourt.ViewHolder.ShowCommentViewHolder;
//import com.firebase.ui.database.FirebaseListOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowComment extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference ratingFood;

    SwipeRefreshLayout swipeRefreshLayout;
    FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder> adapter;

    String foodID="";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/cf.otf").setFontAttrId(R.attr.fontPath).build());
        setContentView(R.layout.activity_show_comment);


        database = FirebaseDatabase.getInstance();
        ratingFood = database.getReference("Rating");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerComment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                if(getIntent() != null)
//                    foodID = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
//                if(!foodID.isEmpty() && foodID != null)
//                {
//                    Query query = ratingFood.orderByChild("foodID").equalTo(foodID);
//                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>().setQuery(query, Rating.class).build();
//
//                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(Food.class,
//                            R.layout.food_item,
//                            FoodViewHolder.class,
//                            foodList.orderByChild("menuID").equalTo(categoryID)) {
//                        @Override
//                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder showCommentViewHolder, int i, @NonNull Rating rating) {
//
//                            showCommentViewHolder.ratingBarDetail.setRating(Float.parseFloat(rating.getRateValue()));
//                            showCommentViewHolder.txtUserPhone.setText(rating.getPhone());
//                            showCommentViewHolder.txtComment.setText(rating.getComment());
//                        }
//
//                        @NonNull
//                        @Override
//                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_comment_layout, parent, false);
//                            return new ShowCommentViewHolder(view);
//                        }
//                    };
//
//                    loadComment(foodID);
//
//                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);

//                if(getIntent() != null)
//                    foodID = getIntent().getStringExtra(Common.INTENT_FOOD_ID);
//                if(!foodID.isEmpty() && foodID != null)
//                {
//                    Query query = ratingFood.orderByChild("foodID").equalTo(foodID);
//                    FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>().setQuery(query, Rating.class).build();
//
//                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
//                        @Override
//                        protected void onBindViewHolder(@NonNull ShowCommentViewHolder showCommentViewHolder, int i, @NonNull Rating rating) {
//
//                            showCommentViewHolder.ratingBarDetail.setRating(Float.parseFloat(rating.getRateValue()));
//                            showCommentViewHolder.txtUserPhone.setText(rating.getPhone());
//                            showCommentViewHolder.txtComment.setText(rating.getComment());
//                        }
//
//                        @NonNull
//                        @Override
//                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_comment_layout, parent, false);
//                            return new ShowCommentViewHolder(view);
//                        }
//                    };
//
//                    loadComment(foodID);
//
//                }
            }
        });
    }

    private void loadComment(String foodID) {

        //adapter.startListening();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }
}