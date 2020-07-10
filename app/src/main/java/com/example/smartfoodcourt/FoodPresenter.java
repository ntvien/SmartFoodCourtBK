package com.example.smartfoodcourt;

import androidx.annotation.NonNull;

import com.example.smartfoodcourt.Database.Database;
import com.example.smartfoodcourt.Interface.FoodContract;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodPresenter implements FoodContract.Presenter {
    DatabaseReference foodReference, ratingReference;
    FoodContract.View foodView;
    String foodRef;
    Food food;
    public FoodPresenter(FoodContract.View foodView, String foodRef){
        this.foodView = foodView;
        this.foodRef = foodRef;
        foodReference = FirebaseDatabase.getInstance().getReference("Food/List");
        ratingReference = FirebaseDatabase.getInstance().getReference("Rating/" + foodRef + "/List");

    }

    @Override
    public void getFoodComment() {
        foodView.showCommentPage(foodRef);
    }

    @Override
    public void addFoodToCart(String quantity) {
        if(food.getStatus().equals("0")) {
            new Database(foodView.getContext()).addToCart(new CartItem(food.getName(),
                    food.getPrice(), quantity,
                    food.getDiscount(), food.getFoodID()), food.getSupplierID());
            foodView.showToast("Food is added to your cart");
        }
        else foodView.showToast("Food is out of order");
    }

    @Override
    public void saveRating(Rating rating) {
        ratingReference.child(Common.user.getName()).setValue(rating);
        foodView.showToast("Your rating saved. Thank you very much");
    }

    @Override
    public void loadFood() {
        foodReference.child(foodRef).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                food = snapshot.getValue(Food.class);
                if(food != null) foodView.showFoodDetail(food);
                else foodView.closeView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void requestRatingFood() {
        ArrayList<String> levelList = new ArrayList<>();
        levelList.add("Very Bad");
        levelList.add("Not Good");
        levelList.add("Quite OK");
        levelList.add("Very Good");
        levelList.add("Excellent");
        foodView.showRatingDialog(levelList);
    }
}
