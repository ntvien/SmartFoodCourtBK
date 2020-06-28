package com.example.smartfoodcourt.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.smartfoodcourt.Cart;
import com.example.smartfoodcourt.Model.CartItem;
import com.example.smartfoodcourt.Model.CartStallItem;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Database extends SQLiteAssetHelper {
    static final String DB_NAME = "FoodCourt.db";
    static final int DB_VER = 1;
    public Database(Context context) {
        super(context, DB_NAME,null, DB_VER);
    }

    public List<CartStallItem> getCart() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"SupplierID", "Name", "Price", "Quantity", "Discount"};
        String sqlTable = "CartItem";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        List<CartStallItem> result = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                ListIterator<CartStallItem> it = result.listIterator();
                int flag = 0;

                while(it.hasNext()){
                    CartStallItem temp = it.next();
                    if(temp.getSupplierID().equals(c.getString(c.getColumnIndex("SupplierID")))){
                        temp.addItem(new CartItem(c.getString(c.getColumnIndex("Name")),
                                c.getString(c.getColumnIndex("Price")),
                                c.getString(c.getColumnIndex("Quantity")),
                                c.getString(c.getColumnIndex("Discount"))));
                        it.set(temp);
                        flag = 1;
                        break;
                    }
                }
                if(flag == 0){
                    List<CartItem> t = new ArrayList<>();
                    t.add(new CartItem(c.getString(c.getColumnIndex("Name")),
                                    c.getString(c.getColumnIndex("Price")),
                                    c.getString(c.getColumnIndex("Quantity")),
                                    c.getString(c.getColumnIndex("Discount"))));
                    result.add(new CartStallItem(c.getString(c.getColumnIndex("SupplierID")),t));
                }

            } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart (CartItem cartItem, String supplierID) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO CartItem(SupplierID, Name, Price, Quantity, Discount) VALUES('%s', '%s', '%s', '%s', '%s');",
                supplierID,
                cartItem.getFoodName(),
                cartItem.getPrice(),
                cartItem.getQuantity(),
                cartItem.getDiscount());
        db.execSQL(query);
    }

    public void cleanCart () {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM CartItem");
        db.execSQL(query);
    }
}
