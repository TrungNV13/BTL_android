package com.example.sqlitedemo.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.sqlitedemo.model.Item;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ChiTieu.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE items(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "category TEXT," +
                "price TEXT," +
                "date TEXT)";
            db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    // Get ALL with DATE DESCENDING
    public List<Item> getAll() {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String order = "date ASC";
        Cursor rs = st.query("items", null, null,
                null, null, null, order);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String title = rs.getString(1);
            String category = rs.getString(2);
            String price = rs.getString(3);
            String date = rs.getString(4);
            list.add(new Item(id, title, category, price, date));
        }
        return list;
    }

    // Adding new Item
    public void addItem(Item item) {
        String sql = "INSERT INTO items(title, category, price, date)" +
                "VALUES (?, ?, ?, ?)";
        String[] arg = {item.getTitle(), item.getCategory(), item.getPrice(), item.getDate()};
        SQLiteDatabase st = getWritableDatabase();
        st.execSQL(sql, arg);
    }

    // Get By Date
    public List<Item> getByDate(String date) {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String whereClause = "date like ?";
        String[] whereArg = {date};
        Cursor rs = st.query("items", null, whereClause, whereArg,
                null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String title = rs.getString(1);
            String category = rs.getString(2);
            String price = rs.getString(3);
            list.add(new Item(id, title, category, price, date));
        }
        return list;
    }

    // Update an Item
    public void updateItem(Item item) {
        String sql = "UPDATE items " +
                "SET title = ?, category = ?, price = ?, date = ? " +
                "WHERE id = ?";
        String[] arg = {item.getTitle(), item.getCategory(), item.getPrice(), item.getDate(), String.valueOf(item.getId())};
        SQLiteDatabase st = getWritableDatabase();
        st.execSQL(sql, arg);
    }

    // Delete an Item
    public void deleteItem(int id) {
        String sql = "DELETE FROM items " +
                "WHERE id = ?";
        String[] arg = {String.valueOf(id)};
        SQLiteDatabase st = getWritableDatabase();
        st.execSQL(sql, arg);
    }

    // Search by Title
    public List<Item> searchByTitle(String key) {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String whereClause = "title like ?";
        String[] whereArg = {"%" + key + "%"};
        Cursor rs = st.query("items", null, whereClause, whereArg,
                null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String title = rs.getString(1);
            String category = rs.getString(2);
            String price = rs.getString(3);
            String date = rs.getString(4);
            list.add(new Item(id, title, category, price, date));
        }
        return list;
    }

    // Search by Category
    public List<Item> searchByCategory(String category) {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String whereClause = "category like ?";
        String[] whereArg = {category};
        Cursor rs = st.query("items", null, whereClause, whereArg,
                null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String title = rs.getString(1);
            String price = rs.getString(3);
            String date = rs.getString(4);
            list.add(new Item(id, title, category, price, date));
        }
        return list;
    }

    // Search by Date
    public List<Item> searchByDateFromTo(String from, String to) {
        List<Item> list = new ArrayList<>();
        SQLiteDatabase st = getReadableDatabase();
        String whereClause = "date BETWEEN ? AND ?";
        String[] whereArg = {from.trim(), to.trim()};
        Cursor rs = st.query("items", null, whereClause, whereArg,
                null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String title = rs.getString(1);
            String category = rs.getString(2);
            String price = rs.getString(3);
            String date = rs.getString(4);
            list.add(new Item(id, title, category, price, date));
        }
        return list;
    }
}
