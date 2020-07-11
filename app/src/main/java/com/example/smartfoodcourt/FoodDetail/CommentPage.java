package com.example.smartfoodcourt.FoodDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartfoodcourt.Common;
import com.example.smartfoodcourt.Model.Rating;
import com.example.smartfoodcourt.R;
import com.example.smartfoodcourt.ViewHolder.CommentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommentPage extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference ratingReference;
    FirebaseRecyclerAdapter<Rating, CommentViewHolder> adapter;
    String foodRef;

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null) adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        recyclerView = findViewById(R.id.recyclerComment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent() != null)
            foodRef = getIntent().getStringExtra(Common.INTENT_FOOD_REF);
        if(foodRef != null && !foodRef.isEmpty())
        {
            ratingReference = FirebaseDatabase.getInstance().getReference("Rating/" + foodRef + "/List");
            FirebaseRecyclerOptions<Rating> options = new FirebaseRecyclerOptions.Builder<Rating>().setQuery(ratingReference, Rating.class).build();
            adapter = new FirebaseRecyclerAdapter<Rating, CommentViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull CommentViewHolder commentViewHolder, final int position, @NonNull Rating rating) {
                    commentViewHolder.ratingBarDetail.setRating(Float.parseFloat(rating.getRateValue()));
                    commentViewHolder.txtUserName.setText(adapter.getRef(position).getKey());
                    commentViewHolder.txtComment.setText(rating.getComment());
                }

                @NonNull
                @Override
                public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
                    return new CommentViewHolder(view);
                }
            };
            recyclerView.setAdapter(adapter);
        }
    }
}