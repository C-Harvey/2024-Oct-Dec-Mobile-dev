package com.example.coreyharveyproject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app_database.db";
    private static final int DATABASE_VERSION = 1;

    public static final String INVENTORY_TABLE = "inventory";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_QUANTITY = "quantity";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Creating inventory table...");
        String CREATE_INVENTORY_TABLE = "CREATE TABLE IF NOT EXISTS " + INVENTORY_TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ITEM_NAME + " TEXT NOT NULL,"
                + COLUMN_QUANTITY + " INTEGER NOT NULL"
                + ")";
        db.execSQL(CREATE_INVENTORY_TABLE);
        Log.d("DatabaseHelper", "Table created successfully.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DatabaseHelper", "Upgrading database...");
        db.execSQL("DROP TABLE IF EXISTS " + INVENTORY_TABLE);
        onCreate(db);
    }

    // Update item quantity
    public void updateItemQuantity(int id, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery = "UPDATE " + INVENTORY_TABLE + " SET " + COLUMN_QUANTITY + " = " + newQuantity
                + " WHERE " + COLUMN_ID + " = " + id;
        db.execSQL(updateQuery);
        db.close();
    }

    // Delete item
    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INVENTORY_TABLE, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Retrieve all items
    public Cursor getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + INVENTORY_TABLE;
        return db.rawQuery(query, null);
    }

    // Add new item to the inventory
    public void addItem(String itemName, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery = "INSERT INTO " + INVENTORY_TABLE + "(" + COLUMN_ITEM_NAME + ", " + COLUMN_QUANTITY + ") VALUES (?, ?)";
        db.execSQL(insertQuery, new Object[]{itemName, quantity});
        db.close();
    }
}