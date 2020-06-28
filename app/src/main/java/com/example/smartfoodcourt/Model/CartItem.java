package com.example.smartfoodcourt.Model;

public class CartItem {
    private String Name;
    private String Price;
    private String Quantity;
    private String Discount;

    public CartItem() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
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

    public CartItem(String Name, String price, String quantity, String discount) {
        this.Name = Name;
        Price = price;
        Quantity = quantity;
        Discount = discount;
    }
}
