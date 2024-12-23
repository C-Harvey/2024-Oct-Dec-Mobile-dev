package com.example.coreyharveyproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InventoryActivity extends AppCompatActivity {

    GridView inventoryGrid;
    Button addItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        inventoryGrid = findViewById(R.id.inventoryGrid);
        addItemButton = findViewById(R.id.addItemButton);

        // Set up the grid view

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Logic to add an item to inventory
                Toast.makeText(InventoryActivity.this, "Item added.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}