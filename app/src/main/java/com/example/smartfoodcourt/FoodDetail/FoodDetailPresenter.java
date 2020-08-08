package com.example.smartfoodcourt.FoodDetail;

import androidx.annotation.NonNull;

import com.example.smartfoodcourt.Common;
import com.example.smartfoodcourt.Database.Database;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.Rating;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodDetailPresenter implements FoodDetailContract.Presenter {
    DatabaseReference foodReference, ratingReference;
    FoodDetailContract.View foodView;
    String foodRef;
    Food food;
 public FoodDetailPresenter(FoodDetailContract.View foodView, String foodRef){
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
                    food.getDiscount()), food.getSupplierID());
            foodView.showToast("Món ăn đã được thêm vào giỏ hàng");
        }
        else foodView.showToast("Món ăn đã hết hàng");
    }

    @Override
    public void saveRating(Rating rating) {
        ratingReference.child(Common.user.getName()).setValue(rating);
        foodView.showToast("Đánh giá của bạn đã được lưu lại. Cảm ơn bạn rất nhiều.");
    }

    @Override
    public void loadFood() {
        foodReference.child(foodRef).addListenerForSingleValueEvent(new ValueEventListener() {
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
        levelList.add("Rất tệ");
        levelList.add("Không ngon");
        levelList.add("Khá OK");
        levelList.add("Rất ngon");
        levelList.add("Xuất sắc");
        foodView.showRatingDialog(levelList);
    }
}
