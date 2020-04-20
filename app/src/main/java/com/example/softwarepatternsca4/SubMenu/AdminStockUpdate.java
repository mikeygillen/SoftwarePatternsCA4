package com.example.softwarepatternsca4.SubMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.softwarepatternsca4.Adapters.ProductAdapter;
import com.example.softwarepatternsca4.Classes.Product;
import com.example.softwarepatternsca4.Interface;
import com.example.softwarepatternsca4.R;
import com.example.softwarepatternsca4.finalViews.AdminPurchaseView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminStockUpdate extends AppCompatActivity implements ProductAdapter.OnProductListener, Interface {
    private static final String TAG = "AdminStockUpdate";

    private DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;

    private Spinner filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_stock_update);

        prodList.clear();

        EditText search = (EditText) findViewById(R.id.searchBar);
        filter = (Spinner) findViewById(R.id.spinner_product);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.product_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter.setAdapter(adapter);

        recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: snapshot = " + snapshot);
                for (DataSnapshot result : snapshot.getChildren()) {
                    String name = result.child("Name").getValue().toString();
                    String manufacturer = result.child("Manufacturer").getValue().toString();
                    String image = result.child("Image").getValue().toString();
                    String category = result.child("Category").getValue().toString();
                    double price = Double.parseDouble(result.child("Price").getValue().toString());
                    int quantity = Integer.parseInt(result.child("Quantity").getValue().toString());

                    Product product = new Product(name, manufacturer, category, price, quantity, image);
                    prodList.add(product);
                }

                productAdapter = new ProductAdapter(AdminStockUpdate.this, prodList);
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                String value = (String) filter.getSelectedItem();
                Toast.makeText(getApplication(), "Item Selected " + value, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }

    /*
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sort(String value) {
        ArrayList<Product> sortedList = new ArrayList<>();
        if (value.equalsIgnoreCase("Name (A-Z)")){


        }else if (value.equalsIgnoreCase("Name (A-Z)")){

        }
    }

     */


    private void filter(String text) {
        ArrayList<Product> filteredList = new ArrayList<>();
        for (Product item : prodList) {
            if(item.getName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        productAdapter.filterlist(filteredList);
    }

    @Override
    public void onProductClick(int position) {
        Toast.makeText(this, "Item clicked" + prodList.get(position).getName(), Toast.LENGTH_LONG).show();
        finish();
        Intent intent = new Intent(AdminStockUpdate.this, AdminPurchaseView.class);
        intent.putExtra("product_position", position);
        startActivity(intent);

    }
}