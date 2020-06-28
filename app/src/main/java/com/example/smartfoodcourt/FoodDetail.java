package com.example.smartfoodcourt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;

import com.example.smartfoodcourt.Common.Common;
import com.example.smartfoodcourt.Database.Database;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.Comment;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.Rating;
import com.example.smartfoodcourt.ui.food.FoodViewModel;
import com.example.smartfoodcourt.ui.orders.OrdersFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {
    TextView txtName, txtPrice, txtDes, txtDiscount;
    ImageView imgFood;
    String foodID = "";
    DatabaseReference foodList;
    DatabaseReference ratingFood;

    Button btnBackDetail;
    ImageView imgAddCart;
    ImageView btnUp, btnDown, imgDiscount;
    TextView txtQuantity;
    Food food;

    RatingBar ratingBar;
    private FoodViewModel foodViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDes = findViewById(R.id.txtDes);
        txtDiscount = findViewById(R.id.txtDiscount);
        imgFood = findViewById(R.id.imgFood);

        imgDiscount = findViewById(R.id.imgDiscount);

        btnBackDetail = (Button)findViewById(R.id.btnBackDetail);

        imgAddCart = (ImageView) findViewById(R.id.imgAddCart);
        btnDown = findViewById(R.id.imgDown);
        btnUp = findViewById(R.id.imgUp);
        txtQuantity = findViewById(R.id.txtQuantity);

        foodList = FirebaseDatabase.getInstance().getReference("Food");
        ratingFood = FirebaseDatabase.getInstance().getReference("Rating");

        if(getIntent() != null) {
            foodID = getIntent().getStringExtra("foodID");
            if(!foodID.isEmpty()){
                loadRatingFood(foodID);
                loadFood();
            }
        }

        btnBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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


        FloatingActionButton btnStar = findViewById(R.id.btnStar);
        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogRating();
            }
        });

        FloatingActionButton btnComment = findViewById(R.id.btnComment);
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentIntent = new Intent(FoodDetail.this, ShowComment.class);
                commentIntent.putExtra(Common.INTENT_FOOD_ID, foodID);
                startActivity(commentIntent);
            }
        });

        ratingBar = (RatingBar)findViewById(R.id.ratingBar);


        imgAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new CartItem(
                        foodID,
                        food.getName(),
                        food.getPrice(),
                        txtQuantity.getText().toString(),
                        food.getDiscount()
                ));

                Toast.makeText(FoodDetail.this, "Added to my cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRatingFood(String foodID) {

        Query foodRating = ratingFood.orderByChild("FoodID").equalTo(foodID);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }

              if(count != 0){
                  float averageStar = sum / count;
                  ratingBar.setRating(averageStar);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showDialogRating() {

        new AppRatingDialog.Builder().setPositiveButtonText("Submit").setNegativeButtonText("Cancel")
                .setNoteDescriptions((Arrays.asList("Very Bad", "Not Good", "Quite OK", "Very Good", "Excellent")))
                .setDefaultRating(1).setTitle("Rate this Food").setDescription("Please rating food and comment your feedback")
                .setTitleTextColor(R.color.colorPrimary).setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...").setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark).setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetail.this).show();
    }

    private void loadFood() {
        foodList.child(foodID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                   food = snapshot.getValue(Food.class);

                   Locale locale = new Locale("vi", "VN");
                   NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                   Picasso.with(getBaseContext()).load(food.getImage()).into(imgFood);
                   txtPrice.setText(fmt.format(Integer.parseInt(food.getPrice())));
                   txtName.setText(food.getName());
                   txtDes.setText(food.getDescription());
                   txtDiscount.setText(food.getDiscount() + "%");

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

        final Rating rating = new Rating(Common.currentUser.getPhone(),foodID, String.valueOf(valueRating), comments);



        ratingFood.push().setValue(rating).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FoodDetail.this,"Thank You for submit rating !!!", Toast.LENGTH_SHORT).show();
            }
        });









//        ratingFood.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.child(Common.currentUser.getPhone()).exists()){
//                    ratingFood.child(Common.currentUser.getPhone()).removeValue();
//                    ratingFood.child(Common.currentUser.getPhone()).setValue(rating);
//                }
//                else {
//                    ratingFood.child(Common.currentUser.getPhone()).setValue(rating);
//                }
//                Toast.makeText(FoodDetail.this,"Thank You for submit rating !!!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}
