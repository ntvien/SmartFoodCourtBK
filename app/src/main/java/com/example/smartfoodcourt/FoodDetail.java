package com.example.smartfoodcourt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Database.Database;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.Rating;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {

    TextView txtName, txtPrice, txtDes, txtDiscount, txtQuantity;
    ImageView imgFood, imgAddCart, btnUp, btnDown, imgDiscount, imgCart;
    Button btnBackDetail;

    String foodRef = "";
    Food food;

    DatabaseReference foodList, ratingFood;

    RatingBar ratingBar;
    FloatingActionButton btnStar, btnComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        txtName = (TextView)findViewById(R.id.txtName);
        txtPrice = (TextView)findViewById(R.id.txtPrice);
        txtDes = (TextView)findViewById(R.id.txtDes);
        txtDiscount = (TextView)findViewById(R.id.txtDiscount);
        txtQuantity = (TextView)findViewById(R.id.txtQuantity);
        imgFood = (ImageView)findViewById(R.id.imgFood);
        imgDiscount = (ImageView)findViewById(R.id.imgDiscount);
        btnBackDetail = (Button)findViewById(R.id.btnBack);
        imgAddCart = (ImageView) findViewById(R.id.imgAddCart);
        imgCart = (ImageView)findViewById(R.id.imgCart);
        btnDown = (ImageView)findViewById(R.id.imgDown);
        btnUp = (ImageView)findViewById(R.id.imgUp);
        btnStar = (FloatingActionButton)findViewById(R.id.btnStar);
        btnComment = (FloatingActionButton)findViewById(R.id.btnComment);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        if(getIntent() != null) {
            foodRef = getIntent().getStringExtra(Common.INTENT_FOOD_REF);
            if (!foodRef.isEmpty()) {
                foodList = FirebaseDatabase.getInstance().getReference("Food/List");
                ratingFood = FirebaseDatabase.getInstance().getReference("Rating/" + foodRef + "/List");
                loadFood();
            }
        }

        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(FoodDetail.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer a = (Integer.parseInt(txtQuantity.getText().toString()) + 1);
                txtQuantity.setText(a.toString());
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer a = (Integer.parseInt(txtQuantity.getText().toString()) - 1);
                if(a > 0){
                    txtQuantity.setText(a.toString());
                }
            }
        });

        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRating();
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(FoodDetail.this, ShowComment.class);
                commentIntent.putExtra(Common.INTENT_FOOD_REF, foodRef);
                startActivity(commentIntent);
            }
        });

        imgAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new CartItem(food.getName(),
                        food.getPrice(), txtQuantity.getText().toString(),
                        food.getDiscount(), food.getFoodID()), food.getSupplierID());
                Toast.makeText(FoodDetail.this, "Food is added to your cart", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showDialogRating() {
        new AppRatingDialog.Builder().setPositiveButtonText("Comment").setNegativeButtonText("Cancel")
                .setNoteDescriptions((Arrays.asList("Very Bad", "Not Good", "Quite OK", "Very Good", "Excellent")))
                .setDefaultRating(1).setTitle("Rate this Food").setDescription("Please rating food and comment your feedback")
                .setTitleTextColor(R.color.colorPrimary).setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...").setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark).setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetail.this).show();
    }

    private void loadFood() {
        foodList.child(foodRef).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    food = snapshot.getValue(Food.class);
                    if(food != null){
                       Picasso.with(getBaseContext()).load(food.getImage()).into(imgFood);
                       txtPrice.setText(Common.convertPricetoVND(food.getPrice()));
                       txtName.setText(food.getName());
                       txtDes.setText(food.getDescription());
                       txtDiscount.setText(food.getDiscount() + "%");
                       ratingBar.setRating(Float.parseFloat(food.getStar()));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int valueRating, @NotNull String comments) {

        final Rating rating = new Rating(String.valueOf(valueRating), comments);
        ratingFood.child(Common.userName).setValue(rating);
    }
}
