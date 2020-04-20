package com.example.softwarepatternsca4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminActivity  extends AppCompatActivity implements ProductAdapter.OnProductListener, View.OnClickListener, Interface {
    private static final String TAG = "AdminActivity";

    private RecyclerView recyclerView, userRecyclerView;
    private ProductAdapter productAdapter, userAdapter;
    private DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");

    RelativeLayout relativeLayout, relativeLayout1, relativeLayout2;
    Button viewmoreUpdate, viewmoreCust, viewmore2;
    int height, height1, height2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        recyclerView = findViewById(R.id.updateStockRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        relativeLayout = (RelativeLayout) findViewById(R.id.expandable);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.expandable1);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.expandable2);

        viewmoreUpdate = (Button) findViewById(R.id.viewmoreStock);
        viewmoreCust = (Button) findViewById(R.id.viewmoreCustomers);
        viewmore2 = (Button) findViewById(R.id.viewmore2);

        viewmoreUpdate.setOnClickListener(this);
        viewmoreCust.setOnClickListener(this);
        viewmore2.setOnClickListener(this);


        relativeLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        relativeLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        relativeLayout.setVisibility(View.GONE);
                        relativeLayout1.setVisibility(View.GONE);
                        relativeLayout2.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        relativeLayout.measure(widthSpec, heightSpec);
                        height = relativeLayout.getMeasuredHeight();
                        relativeLayout1.measure(widthSpec, heightSpec);
                        height1 = relativeLayout.getMeasuredHeight();
                        relativeLayout2.measure(widthSpec, heightSpec);
                        height2 = relativeLayout.getMeasuredHeight();
                        return true;
                    }
                });

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: snapshot = " + snapshot);
                for (DataSnapshot result : snapshot.getChildren()) {
                    String name = result.child("Name").getValue().toString();
                    String manufacturer = result.child("Manufacturer").getValue().toString();
                    String category = result.child("Category").getValue().toString();
                    double price = Double.parseDouble(result.child("Price").getValue().toString());
                    int quantity = Integer.parseInt(result.child("Quantity").getValue().toString());
                    String image = result.child("Image").getValue().toString();

                    Product prod1 = new Product(name, manufacturer, category, price, quantity, image);
                    prodList.add(prod1);
                }


                productAdapter = new ProductAdapter(AdminActivity.this, prodList);
                recyclerView.setAdapter(productAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange: snapshot = " + snapshot);
                for (DataSnapshot result : snapshot.getChildren()) {
                    String email = result.child("Email").getValue().toString();
                    String address = result.child("Address").getValue().toString();
                    String payment = result.child("Payment").getValue().toString();

                    User user = new User(email, address, payment);
                    userList.add(user);
                }

                userAdapter = new UserAdapter(AdminActivity.this, userList);
                userRecyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void expand(RelativeLayout layout, int layoutHeight) {
        layout.setVisibility(View.VISIBLE);
        ValueAnimator animator = slideAnimator(layout, 0, layoutHeight);
        animator.start();
    }

    private void collapse(final RelativeLayout layout) {
        int finalHeight = layout.getHeight();
        ValueAnimator mAnimator = slideAnimator(layout, finalHeight, 0);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }


    private ValueAnimator slideAnimator(final RelativeLayout layout, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
                layoutParams.height = value;
                layout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewmoreStock:
                if (relativeLayout.getVisibility() == View.GONE) {
                    expand(relativeLayout, height);
                } else {
                    collapse(relativeLayout);
                }
                break;

            case R.id.viewCustomers:
                if (relativeLayout1.getVisibility() == View.GONE) {
                    expand(relativeLayout1, height1);
                } else {
                    collapse(relativeLayout1);
                }
                break;

            case R.id.viewmore2:
                if (relativeLayout2.getVisibility() == View.GONE) {
                    expand(relativeLayout2, height2);
                } else {
                    collapse(relativeLayout2);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_heading, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.updateStock:
                Toast.makeText(this, "Update Stock Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.viewCustomers:
                Toast.makeText(this, "View Customers selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.adminLogout:
                Toast.makeText(this, "Logout selected", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(this, MainActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onProductClick(int position) {
        Toast.makeText(this, "Item clicked" + prodList.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.e(TAG, "onSwiped swiped to view");
            int position = viewHolder.getPosition();

            final String product = prodList.get(position).getName();

            Toast.makeText(getApplication(), product + " Swiped", Toast.LENGTH_LONG).show();
        }
    };
}
