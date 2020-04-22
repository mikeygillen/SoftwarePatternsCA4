package com.example.softwarepatternsca4.AdminSubMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.softwarepatternsca4.Adapters.OrderAdapter;
import com.example.softwarepatternsca4.Classes.Order;
import com.example.softwarepatternsca4.Interface;
import com.example.softwarepatternsca4.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminViewCustomerActivity extends AppCompatActivity implements Interface {
    private static final String TAG = "AdminViewCustomerActivity";

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_customer);

        recyclerView = findViewById(R.id.orderRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: snapshot = " + snapshot);
                for (DataSnapshot result : snapshot.getChildren()) {
                    String name = result.child("user").getValue().toString();
                    String address = result.child("shipping").getValue().toString();
                    String payment = result.child("payment").getValue().toString();
                    String products = result.child("products").getValue().toString();
                    double cost = Double.parseDouble(result.child("cost").getValue().toString());

                    Order order = new Order(name, address, payment, products, cost);
                    orderList.add(order);
                }

                orderAdapter = new OrderAdapter(AdminViewCustomerActivity.this, orderList);
                recyclerView.setAdapter(orderAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
