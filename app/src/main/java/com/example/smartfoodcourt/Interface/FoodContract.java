package com.example.smartfoodcourt.Interface;

import android.content.Context;

import com.example.smartfoodcourt.Model.Food;
import com.example.smartfoodcourt.Model.Rating;

import java.util.ArrayList;

public interface FoodContract {
    interface View{
        void showFoodDetail(Food food);
        void showRatingDialog(ArrayList<String> levelList);
        void showCommentPage(String foodRef);
        void showToast(String message);
        void closeView();
        Context getContext();
    }
    interface Presenter{
        void loadFood();
        void requestRatingFood();
        void getFoodComment();
        void addFoodToCart(String quantity);
        void saveRating(Rating rating);
    }
}
