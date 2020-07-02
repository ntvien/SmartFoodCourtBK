package com.example.smartfoodcourt.Model;

public class Stall {
    String image;
    String name;
    String supplierID;

    public Stall() {
    }

    public Stall(String image, String name, String supplierID) {
        this.image = image;
        this.name = name;
        this.supplierID = supplierID;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
