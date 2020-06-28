package com.example.smartfoodcourt.Model;

import java.util.List;

public class CartStallItem {
    private String supplierID;
    private List<CartItem> cartItemList;
    private Integer total = 0;

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

    public CartStallItem() {
    }


    public Integer getTotal() {
        return total;
    }

    public CartStallItem(String supplierID, List<CartItem> cartItemList) {
        this.supplierID = supplierID;
        this.cartItemList = cartItemList;
        for(CartItem t: cartItemList){
            total += (int) (Float.parseFloat(t.getPrice())*(1 - Float.parseFloat(t.getDiscount())/100)*Float.parseFloat(t.getQuantity()));
        }
    }
}
