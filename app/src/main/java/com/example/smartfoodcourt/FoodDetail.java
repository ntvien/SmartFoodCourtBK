package com.example.smartfoodcourt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {

    TextView txtName, txtPrice, txtDes, txtDiscount, txtQuantity;
    ImageView imgFood, imgAddCart, btnUp, btnDown, imgDiscount, imgCart, imgOutOfOrder;
    Button btnBackDetail, btnOrderBy;
    AlertDialog orderDialog;

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
        imgOutOfOrder = (ImageView)findViewById(R.id.outOfOrder_image);
        btnBackDetail = (Button)findViewById(R.id.btnBack);
        imgAddCart = (ImageView) findViewById(R.id.imgAddCart);
        imgCart = (ImageView)findViewById(R.id.imgCart);
        btnDown = (ImageView)findViewById(R.id.imgDown);
        btnUp = (ImageView)findViewById(R.id.imgUp);
        btnStar = (FloatingActionButton)findViewById(R.id.btnStar);
        btnComment = (FloatingActionButton)findViewById(R.id.btnComment);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        btnOrderBy = (Button)findViewById(R.id.btnOrderBy);

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

        btnOrderBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert();
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
                if(food.getStatus().equals("0")){
                    new Database(getBaseContext()).addToCart(new CartItem(food.getName(),
                            food.getPrice(), txtQuantity.getText().toString(),
                            food.getDiscount(), food.getFoodID()), food.getSupplierID());
                    Toast.makeText(FoodDetail.this, "Food is added to your cart", Toast.LENGTH_SHORT).show();
                }
                else if(food.getStatus().equals("1")){
                    Toast.makeText(FoodDetail.this, "Food is out of order", Toast.LENGTH_SHORT).show();
                }
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

                        if(food.getStatus().equals("1")){
                            imgOutOfOrder.setImageResource(Common.convertOutOfOrderToImage(food.getStatus()));
                        }

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

    private void showAlert(){


//        String[] list;
//        list = new String[]{"Eat in", "Take away"};
//        AlertDialog.Builder mBuilder = new AlertDialog.Builder(FoodDetail.this);
//        mBuilder.setTitle("Order By ???");
//        //mBuilder.setIcon();
//        mBuilder.setSingleChoiceItems(list, -1, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        mBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//        AlertDialog mDialog = mBuilder.create();
//        mDialog.show();


        AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
        final CharSequence[] orderBy = {"Eat it", "Take away"};
        final ArrayList selectedItems = new ArrayList();
        myBuilder.setTitle("Order By ???").setMultiChoiceItems(orderBy, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                if(isChecked){
                    selectedItems.add(orderBy[position]);
                }
                else if(selectedItems.contains(position)){
                    selectedItems.remove(Integer.valueOf(position));
                }
            }
        });

        myBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                StringBuilder stringBuilder = new StringBuilder();
//                for(Object orderBy:selectedItems){
//                    stringBuilder.append(orderBy.toString());
//                }
//                Toast.makeText(FoodDetail.this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        orderDialog = myBuilder.create();
        orderDialog.show();
    }
}
