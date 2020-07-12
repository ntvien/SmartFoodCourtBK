package com.example.smartfoodcourt.Model;

public class CartItem {
    private String name;
    private String price;
    private String quantity;
    private String discount;
    private String foodID;

    public CartItem() {
    }

    public CartItem(String name, String price, String quantity, String discount, String foodID) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
        this.foodID = foodID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

}
