package com.example.softwarepatternsca4.finalViews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.softwarepatternsca4.Adapters.ProductAdapter;
import com.example.softwarepatternsca4.Classes.Order;
import com.example.softwarepatternsca4.Classes.Product;
import com.example.softwarepatternsca4.Interface;
import com.example.softwarepatternsca4.Menus.StoreActivity;
import com.example.softwarepatternsca4.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShoppingCart extends AppCompatActivity implements ProductAdapter.OnItemClickListener, Interface {
    private static final String TAG = "ShoppingCart";

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userid = mUser.getUid();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);

    private String name, address, payment, discountCode, key;
    private double netCost, discountValue, grossCost;
    private int stock;
    private TextView subTotal, discount, total;
    private Button purchase;

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(ShoppingCart.this, shoppingList);
        recyclerView.setAdapter(productAdapter);

        subTotal = findViewById(R.id.cost_sc);
        discount = findViewById(R.id.discount_sc);
        total = findViewById(R.id.purchasePrice_sc);
        purchase = findViewById(R.id.button_purchase_sc);


        for (Product item : shoppingList){
            netCost = netCost + item.getPrice();
        }
        subTotal.setText("$" + netCost);

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStock();
                createOrder();
            }
        });

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("Email").getValue().toString();
                address = snapshot.child("Address").getValue().toString();
                payment = snapshot.child("Payment").getValue().toString();
                discountCode = snapshot.child("Discount").getValue().toString();
                Log.d(TAG, "onDataChange: discountCode = " + discountCode);
                if (discountCode.equals("premium")){
                    discountValue = 0.75;
                }else if (discountCode.equals("standard")){
                    discountValue = 0.90;
                }else {
                    discountValue = 1;
                }

                calculateCost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void calculateCost() {
        grossCost = netCost * discountValue;
        String discountText = String.valueOf(grossCost-netCost);

        subTotal.setText("$" + netCost);
        discount.setText("$" + discountText);
        total.setText("$" + grossCost);
    }


    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Item clicked" + shoppingList.get(position).getName(), Toast.LENGTH_LONG).show();
        //finish();
        //Intent intent = new Intent(AdminStockUpdate.this, AdminPurchaseView.class);
        //intent.putExtra("product_position", position);
        //startActivity(intent);
    }



    private void createOrder() {
        Log.d(TAG, "Creating Order");
        DatabaseReference newOrder = FirebaseDatabase.getInstance().getReference().child("Orders");
        String orderKey = newOrder.push().getKey();

        String products = null;
        for(Product item : shoppingList) {
            products = item.getName() + ", " + products;
        }
        final String finalProducts = products;
        final Order order = new Order(name, address, payment, products);

        newOrder.child(orderKey).setValue(order, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                Log.d("ssd", "onComplete: " + error);
            }
        });
        startActivity(new Intent(ShoppingCart.this, StoreActivity.class));
    }

    private void updateStock() {
        Log.d(TAG, "Minus Stock");
        purchase.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ShoppingCart.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Purchasing...");
        progressDialog.show();

        for(final Product item : shoppingList){
            stock = item.getQuantity() - 1;

            final DatabaseReference currentProdRef = FirebaseDatabase.getInstance().getReference().child("Products");
            currentProdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot result : snapshot.getChildren()) {
                        if(result.child("name").getValue().toString().equalsIgnoreCase(item.getName())) {
                            key = result.getKey();
                        }
                    }
                    currentProdRef.child(key).child("Quantity").setValue(stock);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            Log.d(TAG, "updateStock: new quantity = " + stock);
        }
        progressDialog.dismiss();
        Toast.makeText(getApplication(), "Purchase Complete ", Toast.LENGTH_LONG).show();
    }
}
