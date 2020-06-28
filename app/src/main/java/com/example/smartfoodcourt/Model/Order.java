package com.example.smartfoodcourt.Model;


import java.util.List;

public class Order {
    private String phone;
     private String total;
    private String status;
    private String comment;
    private List<CartItem> foods;

    public Order() {
    }

    public Order(String phone, String total, String status, List<CartItem> foods) {
        this.phone = phone;
        this.total = total;
        this.status = status;
        this.foods = foods;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getComment(){
        return comment;
    }
    public void setComment(String comment){
        this.comment = comment;
    }

    public List<CartItem> getFoods() {
        return foods;
    }

    public void setFoods(List<CartItem> foods) {
        this.foods = foods;
    }
}