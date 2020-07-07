package com.example.smartfoodcourt.Common;

import com.example.smartfoodcourt.Model.User;
import com.example.smartfoodcourt.R;

import java.text.NumberFormat;
import java.util.Locale;

public class Common {
    public static User user;
    public static String userName;
    public static final String INTENT_FOOD_REF = "FoodRef";
    public static final String USER_KEY = "User";
    public static final String PASSWORD_KEY = "Password";
    public static String convertCodeToStatus(String status) {
        //0: preparing, 1: ready, 2: received
        if (status.equals("0")) return "Preparing";
        else return "Completed";
    }
    public static String convertPricetoVND(String price) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        return fmt.format(Integer.parseInt(price));
    }
    public static String convertPricetoVND(Float price) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        return fmt.format(price);
    }

    public static int convertDiscountToImage(String discount){
        if(Integer.parseInt(discount) > 10) return R.drawable.bigdiscount;
        else if(Integer.parseInt(discount) > 0) return R.drawable.smalldiscount;
        return 0;
    }
}
