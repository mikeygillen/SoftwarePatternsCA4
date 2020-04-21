package com.example.softwarepatternsca4.Menus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.softwarepatternsca4.AdminSubMenu.AdminStockUpdate;
import com.example.softwarepatternsca4.AdminSubMenu.AdminViewCustomerActivity;
import com.example.softwarepatternsca4.R;

public class AdminActivity  extends AppCompatActivity{
    private static final String TAG = "AdminActivity";

    Button toProducts, toCustomers, magic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toProducts = (Button) findViewById(R.id.buttonUpdateStock);
        toCustomers = (Button) findViewById(R.id.buttonViewCustomer);
        magic = (Button) findViewById(R.id.buttonMagic);

        toProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, AdminStockUpdate.class);
                finish();
                startActivity(i);
            }
        });

        toCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, AdminViewCustomerActivity.class);
                finish();
                startActivity(i);
            }
        });

        magic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AdminActivity.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        });
    }
}
