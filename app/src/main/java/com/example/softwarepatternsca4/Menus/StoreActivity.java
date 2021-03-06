package com.example.softwarepatternsca4.Menus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.softwarepatternsca4.Interfaces.Interface;
import com.example.softwarepatternsca4.Classes.Product;
import com.example.softwarepatternsca4.Adapters.ProductAdapter;
import com.example.softwarepatternsca4.R;
import com.example.softwarepatternsca4.StoreSubMenu.ShoppingCart;
import com.example.softwarepatternsca4.StoreSubMenu.UserProductReview;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class StoreActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener, Interface {
    private static final String TAG = "StoreActivity";

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userid = mUser.getUid();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);
    private DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products");

    private Spinner filter;
    private String orderBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        prodList.clear();

        EditText search = (EditText) findViewById(R.id.searchBar);
        filter = (Spinner) findViewById(R.id.spinner_product);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.product_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(adapter);

        recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String sortRequest = (String) filter.getSelectedItem();
                Toast.makeText(getApplication(), "Item Selected " + sortRequest, Toast.LENGTH_SHORT).show();
                if (sortRequest.equals("Name (A-Z)")){
                    orderBy = "name";
                } else if (sortRequest.equals("Name (Z-A)")) {
                    orderBy = "name";
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(StoreActivity.this);
                    mLayoutManager.setStackFromEnd(true);
                    GridLayoutManager layout = new GridLayoutManager(StoreActivity.this, 1);
                    layout.setReverseLayout(true);
                    recyclerView.setLayoutManager(layout);
                }else if (sortRequest.equals("Manufacturer")) {
                    orderBy = "manufacturer";
                }else if (sortRequest.equals("Category")) {
                    orderBy = "category";
                }else if (sortRequest.equals("Price (High-Low)")) {
                    orderBy = "price";
                }
                else if (sortRequest.equals("Price (Low-High)")) {
                    orderBy = "price";
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(StoreActivity.this);
                    mLayoutManager.setStackFromEnd(true);
                    GridLayoutManager layout = new GridLayoutManager(StoreActivity.this, 1);
                    layout.setReverseLayout(true);
                    recyclerView.setLayoutManager(layout);
                }
                Query queryRef = productRef.orderByChild(orderBy).limitToLast(10);
                queryRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        prodList.clear();
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                        {
                            Product productItem = dataSnapshot1.getValue(Product.class);
                            prodList.add(productItem);
                        }
                        productAdapter = new ProductAdapter(StoreActivity.this, prodList);
                        recyclerView.setAdapter(productAdapter);
                        productAdapter.setOnItemClickListener(StoreActivity.this);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: snapshot = " + snapshot);
                for (DataSnapshot result : snapshot.getChildren()) {
                    Product prod1 = result.getValue(Product.class);
                    prodList.add(prod1);
                }

                productAdapter = new ProductAdapter(StoreActivity.this, prodList);
                recyclerView.setAdapter(productAdapter);
                productAdapter.setOnItemClickListener(StoreActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The Retrieve User Failed: ");
            }
        });
    }

    private void filter(String text) {
        for (Product item : prodList) {
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        productAdapter.filterlist(filteredList);
    }

    /*private void retrieveProducts() {
        Log.d(TAG, "retrieveUsers: beginning");
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: snapshot = " + snapshot);
                for (DataSnapshot result : snapshot.getChildren()) {
                    String name = result.child("name").getValue().toString();
                    String manufacturer = result.child("manufacturer").getValue().toString();
                    String category = result.child("category").getValue().toString();
                    double price = Double.parseDouble(result.child("price").getValue().toString());
                    int quantity = Integer.parseInt(result.child("quantity").getValue().toString());
                    String image = result.child("image").getValue().toString();

                    Product prod1 = new Product(name, manufacturer, category, price, quantity, image);
                    prodList.add(prod1);
                }

                productAdapter = new ProductAdapter(StoreActivity.this, prodList);
                recyclerView.setAdapter(productAdapter);
                productAdapter.setOnItemClickListener(StoreActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The Retrieve User Failed: ");
            }
        });
        Log.d(TAG, "retrieveUsers: ending");
    }

     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.store_heading, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.shoppingBasketIcon:
                Toast.makeText(this, "Shopping Basket Selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(StoreActivity.this, ShoppingCart.class));
                return true;
            case R.id.checkout:
                Toast.makeText(this, "Checkout selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                firebaseAuth.signOut();
                Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "Item clicked" + prodList.get(position).getName(), Toast.LENGTH_LONG).show();
        //shoppingList.add(prodList.get(position));
        Intent intent = new Intent(StoreActivity.this, UserProductReview.class);
        intent.putExtra("product_position", position);
        startActivity(intent);
    }
}
