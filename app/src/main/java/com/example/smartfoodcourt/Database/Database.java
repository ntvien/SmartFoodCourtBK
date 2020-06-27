package com.example.smartfoodcourt.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.smartfoodcourt.Model.CartItem;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    static final String DB_NAME = "SmartFoodCourt.db";
    static final int DB_VER = 1;
    public Database(Context context) {
        super(context, DB_NAME,null, DB_VER);
    }

    public List<CartItem> getCart() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"FoodID", "FoodName", "Price", "Quantity", "Discount"};
        String sqlTable = "CartItem";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<CartItem> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new CartItem(c.getString(c.getColumnIndex("FoodID")),
                        c.getString(c.getColumnIndex("FoodName")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Discount"))
                        ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart (CartItem cartItem) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO CartItem(FoodID, FoodName, Price, Quantity, Discount) VALUES('%s', '%s', '%s', '%s', '%s');",
                cartItem.getFoodID(),
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
