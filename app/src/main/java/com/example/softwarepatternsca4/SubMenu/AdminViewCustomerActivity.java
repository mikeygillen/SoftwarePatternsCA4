package com.example.softwarepatternsca4.SubMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.softwarepatternsca4.Adapters.UserAdapter;
import com.example.softwarepatternsca4.Classes.User;
import com.example.softwarepatternsca4.Interface;
import com.example.softwarepatternsca4.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminViewCustomerActivity extends AppCompatActivity implements UserAdapter.OnUserListener, Interface {
    private static final String TAG = "AdminViewCustomerActivity";

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_customer);

        recyclerView = findViewById(R.id.customerRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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

                userAdapter = new UserAdapter(AdminViewCustomerActivity.this, userList);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onUserClick(int position) {
        Toast.makeText(this, "User clicked" + userList.get(position).getEmail(), Toast.LENGTH_LONG).show();
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
            final String user = userList.get(position).getEmail();
            Toast.makeText(getApplication(), user + " Swiped", Toast.LENGTH_LONG).show();
        }
    };
}
