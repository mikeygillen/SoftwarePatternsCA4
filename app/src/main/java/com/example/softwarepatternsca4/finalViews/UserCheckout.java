package com.example.softwarepatternsca4.finalViews;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.softwarepatternsca4.Classes.Order;
import com.example.softwarepatternsca4.Classes.Product;
import com.example.softwarepatternsca4.Interfaces.Interface;
import com.example.softwarepatternsca4.Menus.StoreActivity;
import com.example.softwarepatternsca4.R;
import com.example.softwarepatternsca4.Templatemethod.RevolutValidation;
import com.example.softwarepatternsca4.Templatemethod.CardValidator;
import com.example.softwarepatternsca4.Templatemethod.MasterCardValidation;
import com.example.softwarepatternsca4.Templatemethod.VisaValidation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserCheckout extends AppCompatActivity implements Interface {
    private static final String TAG = "UserCheckout";

    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private String userid = mUser.getUid();
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userid);

    private EditText cardname, cardnumber, pin;
    private Spinner cardType;
    private Button pay;

    private String name, address, paymentType, key;
    private double cost;
    private int stock;
    private ProgressDialog progressDialog;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentcheckout);

        cardname = findViewById(R.id.cardname);
        cardnumber = findViewById(R.id.checkout_cardnum);
        pin = findViewById(R.id.pin_editText);
        cardType = findViewById(R.id.cardtype);
        pay = findViewById(R.id.confirm_order_button);

        ArrayAdapter<CharSequence> cardAdapter = ArrayAdapter.createFromResource(this, R.array.card_type, android.R.layout.simple_spinner_item);
        cardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardType.setAdapter(cardAdapter);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("Email").getValue().toString();
                address = snapshot.child("Address").getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent intent = getIntent();
        cost = intent.getDoubleExtra("total", 0);

        cardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentType = cardType.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardName = cardname.getText().toString();
                String cardNumber = cardnumber.getText().toString();
                String pinNo = pin.getText().toString();

                CardValidator validator = null;
                Log.d(TAG, "onClick: paymentType = " + paymentType);

                if (paymentType.equals("Visa")) {
                    validator = new VisaValidation(cardName, cardNumber, pinNo);

                } else if (paymentType.equals("MasterCard")) {
                    validator = new MasterCardValidation(cardName, cardNumber, pinNo);

                } else if (paymentType.equals("Revolut")) {
                    validator = new RevolutValidation(cardName, cardNumber, pinNo);

                }

                assert validator != null;
                if (validator.validate()) {
                    progressDialog = new ProgressDialog(UserCheckout.this, R.style.AppTheme);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Purchasing...");
                    progressDialog.show();
                    updateStock();
                    createOrder();

                    shoppingList.clear();
                    progressDialog.dismiss();
                    Toast.makeText(getApplication(), "Purchase Complete ", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(UserCheckout.this, StoreActivity.class));
                } else {
                    Toast.makeText(getApplication(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void updateStock () {
        Log.d(TAG, "Minus Stock");
        pay.setEnabled(false);

        for (final Product item : shoppingList) {
            stock = item.getQuantity() - 1;

            final DatabaseReference currentProdRef = FirebaseDatabase.getInstance().getReference().child("Products");
            currentProdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot result : snapshot.getChildren()) {
                        if (result.child("name").getValue().toString().equalsIgnoreCase(item.getName())) {
                            key = result.getKey();
                        }
                    }
                    currentProdRef.child(key).child("quantity").setValue(stock);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }


    private void createOrder() {
        Log.d(TAG, "Creating Order");
        DatabaseReference newOrder = FirebaseDatabase.getInstance().getReference().child("Orders");
        String orderKey = newOrder.push().getKey();

        String products = null;
        for (Product item : shoppingList) {
            products = item.getName() + ", " + products;
        }
        final String finalProducts = products;
        final Order order = new Order(name, address, paymentType, finalProducts, cost);

        newOrder.child(orderKey).setValue(order, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                Log.d("ssd", "onComplete: " + error);
            }
        });
    }
}