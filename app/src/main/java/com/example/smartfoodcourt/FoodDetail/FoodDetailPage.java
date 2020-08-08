package com.example.smartfoodcourt.FoodDetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartfoodcourt.Cart;
import com.example.smartfoodcourt.Common;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.Rating;
import com.example.smartfoodcourt.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class FoodDetailPage extends AppCompatActivity implements RatingDialogListener, FoodDetailContract.View{

    TextView txtName, txtPrice, txtDes, txtDiscount, txtQuantity;
    ImageView imgFood, imgAddCart, btnUp, btnDown, imgCart, imgOutOfOrder;
    Button btnBackDetail;
    FloatingActionButton btnStar, btnComment;
    RatingBar ratingBar;

    FoodDetailContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        txtName = findViewById(R.id.txtName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDes = findViewById(R.id.txtDes);
        txtDiscount = findViewById(R.id.txtDiscount);
        txtQuantity = findViewById(R.id.txtQuantity);
        imgFood = findViewById(R.id.imgFood);
        imgOutOfOrder = findViewById(R.id.outOfOrder_image);
        btnBackDetail = findViewById(R.id.btnBack);
        imgAddCart = findViewById(R.id.imgAddCart);
        imgCart = findViewById(R.id.imgCart);
        btnDown = findViewById(R.id.imgDown);
        btnUp = findViewById(R.id.imgUp);
        btnStar = findViewById(R.id.btnStar);
        btnComment = findViewById(R.id.btnComment);
        ratingBar = findViewById(R.id.ratingBar);

        if(getIntent() != null) {
            String foodRef = getIntent().getStringExtra(Common.INTENT_FOOD_REF);
            assert foodRef != null;
            if (!foodRef.isEmpty()) {
                presenter = new FoodPresenter(this, foodRef);
                presenter.loadFood();
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
                Intent cartIntent = new Intent(FoodDetailPage.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = (Integer.parseInt(txtQuantity.getText().toString()) + 1);
                txtQuantity.setText(String.valueOf(a));
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = (Integer.parseInt(txtQuantity.getText().toString()) - 1);
                if(a > 0){
                    txtQuantity.setText(String.valueOf(a));
                }
            }
        });

        btnStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.requestRatingFood();
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.getFoodComment();
            }
        });

        imgAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.addFoodToCart(txtQuantity.getText().toString());
            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {
    }

    @Override
    public void onPositiveButtonClicked(int valueRating, @NotNull String comments) {
        Rating rating = new Rating(String.valueOf(valueRating), comments);
        presenter.saveRating(rating);
    }


    @Override
    public void showFoodDetail(Food food) {
        Picasso.with(getBaseContext()).load(food.getImage()).into(imgFood);
        txtPrice.setText(Common.convertPriceToVND(food.getPrice()));
        txtName.setText(food.getName());
        txtDes.setText(food.getDescription());
        txtDiscount.setText(String.format("%s%%", food.getDiscount()));
        ratingBar.setRating(Float.parseFloat(food.getStar()));
        if(food.getStatus().equals("1")){
            imgOutOfOrder.setImageResource(Common.convertOutOfOrderToImage());
        }
    }

    @Override
    public void showRatingDialog(ArrayList<String> levelList) {
        new AppRatingDialog.Builder().setPositiveButtonText("Comment").setNegativeButtonText("Cancel")
                .setNoteDescriptions(levelList)
                .setDefaultRating(1).setTitle("Rate this Food").setDescription("Please rating food and comment your feedback")
                .setTitleTextColor(R.color.colorPrimary).setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here...").setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark).setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetailPage.this).show();
    }

    @Override
    public void showCommentPage(String foodRef) {
        Intent commentIntent = new Intent(FoodDetailPage.this, CommentPage.class);
        commentIntent.putExtra(Common.INTENT_FOOD_REF, foodRef);
        startActivity(commentIntent);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void closeView() {
        finish();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
