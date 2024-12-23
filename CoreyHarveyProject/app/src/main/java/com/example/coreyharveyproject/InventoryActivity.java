package com.example.coreyharveyproject;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private GridView inventoryGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Initialize GridView and DatabaseHelper
        inventoryGrid = findViewById(R.id.inventoryGrid);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Reference UI elements
        Button addButton = findViewById(R.id.addItemButton);
        EditText itemNameInput = findViewById(R.id.itemNameInput);
        EditText itemQuantityInput = findViewById(R.id.itemQuantityInput);

        // Add new item functionality
        addButton.setOnClickListener(v -> {
            String itemName = itemNameInput.getText().toString();
            String quantityStr = itemQuantityInput.getText().toString();
            int quantity = quantityStr.isEmpty() ? 0 : Integer.parseInt(quantityStr);

            if (!itemName.isEmpty()) {
                ContentValues values = new ContentValues();
                values.put(DatabaseHelper.COLUMN_ITEM_NAME, itemName);
                values.put(DatabaseHelper.COLUMN_QUANTITY, quantity);

                long result = db.insert(DatabaseHelper.INVENTORY_TABLE, null, values);
                if (result != -1) {
                    Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show();
                    loadInventory(); // Refresh GridView
                } else {
                    Toast.makeText(this, "Failed to add item.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter an item name.", Toast.LENGTH_SHORT).show();
            }
        });

        loadInventory(); // Load inventory data when the activity is created
    }

    private void loadInventory() {
        try {
            // Query the inventory table
            Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.INVENTORY_TABLE, null);
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
            } else {
                Toast.makeText(this, "No inventory data available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading inventory: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
            itemQuantityTextView.setText("Qty: " + item.getQuantity());

            return convertView;
        }
    }
}
