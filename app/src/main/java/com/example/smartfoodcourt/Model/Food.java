package com.example.smartfoodcourt.Model;

public class Food {
    private String description;
    private  String discount;
    private  String foodID;
    private String image;
    private String name;
    private String price;
    private String star;
    private String supplierID;
    public Food() {
    }

    public String getFoodID() {
        return foodID;
    }

    public void setFoodID(String foodID) {
        this.foodID = foodID;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public Food(String description, String discount, String foodID, String image, String name, String price, String star, String supplierID) {
        this.description = description;
        this.discount = discount;
        this.foodID = foodID;
        this.image = image;
        this.name = name;
        this.price = price;
        this.star = star;
        this.supplierID = supplierID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }
}
