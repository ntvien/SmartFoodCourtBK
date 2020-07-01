package com.example.smartfoodcourt.Model;

public class Food {
    private String name, image, description, price, discount, supplierID;

    public Food() {
    }

    public Food(String name, String image, String description, String price, String discount, String menuID) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.discount = discount;
        supplierID = menuID;
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
