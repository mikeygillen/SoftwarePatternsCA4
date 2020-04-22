package com.example.softwarepatternsca4.StoreSubMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.softwarepatternsca4.Adapters.ReviewAdapter;
import com.example.softwarepatternsca4.Classes.Review;
import com.example.softwarepatternsca4.Interfaces.Interface;
import com.example.softwarepatternsca4.Classes.Product;
import com.example.softwarepatternsca4.Menus.StoreActivity;
import com.example.softwarepatternsca4.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProductReview extends AppCompatActivity implements Interface {
    private static final String TAG = "UserProductReview";

    private DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");
    private RecyclerView recyclerView;
    private ReviewAdapter adapter;

    private TextView name, manufacturer, price, category;
    private EditText ratingComment;
    private RatingBar ratingBar;
    private ImageView picture;
    private Button addReview, addCart;
    private Product product = null;
    private int product_position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_product_review);

        Intent intent = getIntent();
        product_position = intent.getIntExtra("product_position", 0);

        product = prodList.get(product_position);

        name = findViewById(R.id.name_cr);
        manufacturer = findViewById(R.id.manufacturer_cr);
        price = findViewById(R.id.price_cr);
        category = findViewById(R.id.category_cr);
        picture = findViewById(R.id.image_cr);
        ratingBar = (RatingBar) findViewById(R.id.users_rating);
        ratingComment = (EditText) findViewById(R.id.users_comment);
        addReview = (Button) findViewById(R.id.button_add_comment);
        addCart = (Button) findViewById(R.id.button_add_cart);

        name.setText(product.getName());
        manufacturer.setText(product.getManufacturer());
        category.setText(product.getCategory());
        price.setText("$" + product.getPrice());
        Picasso.get().load(product.getImage()).fit().centerInside().into(picture);


        recyclerView = findViewById(R.id.commentRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                reviews.clear();
                Log.d(TAG, "onDataChange: snapshot = " + snapshot);
                for (DataSnapshot result : snapshot.getChildren()) {
                    String prodName = result.child("productName").getValue().toString();
                    String comment = result.child("comment").getValue().toString();
                    double rating = Double.parseDouble(result.child("rating").getValue().toString());

                    if (prodName.equalsIgnoreCase(product.getName())) {
                        Review review = new Review(comment, rating);
                        reviews.add(review);
                    }
                }
                adapter = new ReviewAdapter(UserProductReview.this, reviews);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateReview();
            }
        });
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCart();
            }
        });
    }

    private void updateCart() {
        shoppingList.add(product);
        startActivity(new Intent(UserProductReview.this, StoreActivity.class));
    }

    private void updateReview() {
        Log.d(TAG, "Updating Review");
        addReview.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(UserProductReview.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting...");
        progressDialog.show();

        DatabaseReference newReview = FirebaseDatabase.getInstance().getReference().child("Comments");
        String reviewKey = newReview.push().getKey();
        String comment = ratingComment.getText().toString().trim();
        double rating = ratingBar.getRating();
        final Review review = new Review(product.getName(), comment, rating);

        newReview.child(reviewKey).setValue(review, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                Log.d("ssd", "onComplete: " + error);
            }
        });

        progressDialog.dismiss();
        Toast.makeText(getApplication(), "Comment added - " + comment + ", Rating - " + rating, Toast.LENGTH_SHORT).show();
    }
}
