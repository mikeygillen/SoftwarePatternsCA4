package com.example.softwarepatternsca4.finalViews;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.softwarepatternsca4.SubMenu.AdminStockUpdate;
import com.example.softwarepatternsca4.Interface;
import com.example.softwarepatternsca4.Classes.Product;
import com.example.softwarepatternsca4.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminPurchaseView extends AppCompatActivity implements Interface {

    private static final String TAG = "AdminPurchaseView";
    private TextView name, manufacturer, price, category, quantity, purchasePrice, purchaseQuantity;
    private ImageView picture;
    private Button purchase;
    private Product product = null;
    private int product_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_purchase_view);

        Intent intent = getIntent();
        product_position = intent.getIntExtra("product_position", 0);

        product = prodList.get(product_position);

        name = findViewById(R.id.name_ap);
        manufacturer = findViewById(R.id.manufacturer_ap);
        price = findViewById(R.id.price_ap);
        category = findViewById(R.id.category_ap);
        quantity = findViewById(R.id.quantity_ap);

        purchasePrice = findViewById(R.id.purchasePrice_ap);
        purchaseQuantity = findViewById(R.id.purchaseQuantity_ap);
        picture = findViewById(R.id.image_ap);
        purchase = findViewById(R.id.button_purchase_stock);
        name.setText(product.getName());
        manufacturer.setText(product.getManufacturer());
        category.setText(product.getCategory());
        price.setText("$" + product.getPrice());
        quantity.setText(product.getQuantity() + " in stock");

        Picasso.get().load(product.getImage()).fit().centerInside().into(picture);

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStock();
            }
        });

        purchaseQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "getCost: purchaseQuantity = " + Integer.parseInt(purchaseQuantity.getText().toString().trim()));
                double cost = (product.getPrice() * Integer.parseInt(purchaseQuantity.getText().toString().trim()));
                purchasePrice.setText("$" + cost);
            }
        });
    }

    private void updateStock() {
        Log.d(TAG, "Adding Stock");
        purchase.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(AdminPurchaseView.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Purchasing...");
        progressDialog.show();

        int orderAmount = Integer.parseInt(purchaseQuantity.getText().toString().trim());
        int newStockAmount = orderAmount + product.getQuantity();
        String databaseProdPosition = String.valueOf(product_position + 1);
        Log.d(TAG, "updateStock: newAmount = " + newStockAmount);

        DatabaseReference currentProdRef = FirebaseDatabase.getInstance().getReference().child("Products").child(databaseProdPosition);
        Log.d(TAG, "updateStock: dataRef = " + currentProdRef);
        currentProdRef.child("Quantity").setValue(newStockAmount);

        progressDialog.dismiss();
        Toast.makeText(getApplication(), "Stock LevelUpdated to - " + newStockAmount, Toast.LENGTH_LONG).show();

        startActivity(new Intent(AdminPurchaseView.this, AdminStockUpdate.class));
    }
}
