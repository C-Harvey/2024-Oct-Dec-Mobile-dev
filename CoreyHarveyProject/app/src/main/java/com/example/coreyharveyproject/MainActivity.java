package com.example.coreyharveyproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private GridView inventoryGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the GridView
        inventoryGrid = findViewById(R.id.inventoryGrid);

        // Initialize DatabaseHelper and get writable database
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        loadInventory(); // Load inventory data when the activity is created
    }

    private void loadInventory() {
        try {
            // Query the inventory table
            Cursor cursor = db.rawQuery("SELECT * FROM inventory", null);
            ArrayList<InventoryItem> items = new ArrayList<>();

            if (cursor != null && cursor.moveToFirst()) {
                // Fetch data from the cursor
                do {
                    int itemNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM_NAME);
                    int quantityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_QUANTITY);

                    if (itemNameIndex != -1 && quantityIndex != -1) {
                        String itemName = cursor.getString(itemNameIndex);
                        int quantity = cursor.getInt(quantityIndex);
                        items.add(new InventoryItem(itemName, quantity));
                    }
                } while (cursor.moveToNext());
                cursor.close();

                // Set the adapter to populate GridView
                InventoryAdapter adapter = new InventoryAdapter(items);
                inventoryGrid.setAdapter(adapter);
                adapter.notifyDataSetChanged(); // Refresh GridView
            } else {
                Toast.makeText(this, "No inventory data available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading inventory: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Update item quantity
    public void updateItem(View view) {
        int itemId = 1; // Set the item ID for demonstration
        int newQuantity = 10; // Set the new quantity for demonstration
        dbHelper.updateItemQuantity(itemId, newQuantity);
        loadInventory();  // Reload inventory to reflect changes
    }

    // Delete item
    public void deleteItem(View view) {
        int itemId = 1; // Set the item ID for demonstration
        dbHelper.deleteItem(itemId);
        loadInventory();  // Reload inventory to reflect changes
    }

    // Custom adapter class for GridView
    private class InventoryAdapter extends BaseAdapter {
        private ArrayList<InventoryItem> inventoryItems;

        public InventoryAdapter(ArrayList<InventoryItem> items) {
            inventoryItems = items;
        }

        @Override
        public int getCount() {
            return inventoryItems.size();
        }

        @Override
        public Object getItem(int position) {
            return inventoryItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext())
                        .inflate(R.layout.inventory_item, parent, false);
            }

            InventoryItem item = inventoryItems.get(position);

            TextView itemNameTextView = convertView.findViewById(R.id.itemNameTextView);
            TextView itemQuantityTextView = convertView.findViewById(R.id.itemQuantityTextView);

            itemNameTextView.setText(item.getItemName());
            itemQuantityTextView.setText(String.valueOf(item.getQuantity()));

            return convertView;
        }
    }

    // SMS Permissions Button Click Handler
    public void openSMS(View view) {
        Intent intent = new Intent(this, SmsPermissionActivity.class);
        startActivity(intent);
    }

    // Inventory Management Button Click Handler
    public void openInventory(View view) {
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            loadInventory();  // Reload inventory after modifications
        }
    }
}
