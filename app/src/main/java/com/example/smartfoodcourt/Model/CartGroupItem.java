package com.example.smartfoodcourt.Model;

import java.util.List;

public class CartGroupItem {
    private Integer supplierID;
    private Integer total = 0;
    private String type;
    private List<CartItem> cartItemList;

    public CartGroupItem() {
    }

    public CartGroupItem(Integer supplierID, List<CartItem> cartItemList) {
        this.supplierID = supplierID;
        this.cartItemList = cartItemList;
        this.type = "0";
        for(CartItem t: cartItemList){
            total += (int) (Float.parseFloat(t.getPrice())*(1 - Float.parseFloat(t.getDiscount())/100)*Float.parseFloat(t.getQuantity()));
        }
    }

    public Integer getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(Integer supplierID) {
        this.supplierID = supplierID;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void addItem(CartItem t){
        cartItemList.add(t);
        total += (int) (Float.parseFloat(t.getPrice())*(1 - Float.parseFloat(t.getDiscount())/100)*Float.parseFloat(t.getQuantity()));
    }

}
