package com.example.smartfoodcourt.Model;

public class CartItem {
    private String FoodID;
    private String FoodName;
    private String Price;
    private String Quantity;
    private String Discount;

    public CartItem() {
    }

    public String getFoodID() {
        return FoodID;
    }

    public void setFoodID(String foodID) {
        FoodID = foodID;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public CartItem(String foodID, String foodName, String price, String quantity, String discount) {
        FoodID = foodID;
        FoodName = foodName;
        Price = price;
        Quantity = quantity;
        Discount = discount;
    }
}
