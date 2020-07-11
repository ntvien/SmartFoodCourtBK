package com.example.smartfoodcourt.Model;

import java.util.List;

public class CartGroupItem {
    private String supplierID;
    private List<CartItem> cartItemList;
    private Integer total = 0;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }
    public void addItem(CartItem t){
        cartItemList.add(t);
        total += (int) (Float.parseFloat(t.getPrice())*(1 - Float.parseFloat(t.getDiscount())/100)*Float.parseFloat(t.getQuantity()));
    }
    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public CartGroupItem() {
    }

    public Integer getTotal() {
        return total;
    }

    public CartGroupItem(String supplierID, List<CartItem> cartItemList) {
        this.supplierID = supplierID;
        this.cartItemList = cartItemList;
        this.type = "0";
        for(CartItem t: cartItemList){
            total += (int) (Float.parseFloat(t.getPrice())*(1 - Float.parseFloat(t.getDiscount())/100)*Float.parseFloat(t.getQuantity()));
        }
    }
}