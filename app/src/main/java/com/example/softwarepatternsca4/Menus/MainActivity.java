package com.example.softwarepatternsca4.Menus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.softwarepatternsca4.Interface;
import com.example.softwarepatternsca4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements Interface {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText email_login, password_login, email_signup, password_signup, address_signup, payment_signup;
    private Button button_login, button_signup;

    private FirebaseAuth firebaseAuth;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    final DatabaseReference[] newUser = new DatabaseReference[1];
    final FirebaseUser[] mCurrentUser = new FirebaseUser[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_login = (EditText) findViewById(R.id.login_email);
        password_login = (EditText) findViewById(R.id.login_password);
        email_signup = (EditText) findViewById(R.id.signup_email);
        password_signup = (EditText) findViewById(R.id.signup_password);
        address_signup = (EditText) findViewById(R.id.signup_address);
        payment_signup = (EditText) findViewById(R.id.signup_payment);

        button_login = (Button) findViewById(R.id.btn_login);
        button_signup = (Button) findViewById(R.id.btn_signup);

        firebaseAuth = FirebaseAuth.getInstance();

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), StoreActivity.class));
        }
    }

    private void userLogin() {
        Log.d(TAG, "Login");
        button_login.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = email_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();

        if (email.equals("Admin11") && password.equals("admin")) {
            this.finish();
            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
        }
        else {
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                email_login.setError("Enter a valid email address");
            } else if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
                password_login.setError("Password must be between 4 and 10 characters");
            } else {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    onLoginSuccess();
                                } else {
                                    Toast.makeText(MainActivity.this, "Login Unsuccessful, Please try again", Toast.LENGTH_SHORT).show();
                                    onLoginFailed();
                                }
                            }
                        });

                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                // On complete call either onLoginSuccess or onLoginFailed
                                onLoginSuccess();
                                progressDialog.dismiss();
                            }
                        }, 3000);
            }
        }
    }

    private void signup() {
        Log.d(TAG, "Signup");

        button_signup.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String email = email_signup.getText().toString();
        final String password = password_signup.getText().toString();
        final String address = address_signup.getText().toString();
        final String payment = payment_signup.getText().toString();

        // TODO: Implement your own signup logic here.
        progressDialog.setMessage("Registering new Account...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "createUserWithEmail:success");
                    Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    mCurrentUser[0] = task.getResult().getUser();
                    newUser[0] = mDatabase.child(mCurrentUser[0].getUid());

                    newUser[0].child("Email").setValue(email);
                    newUser[0].child("Address").setValue(address);
                    newUser[0].child("Payment").setValue(payment);

                    onLoginSuccess();
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Registration Unsuccessful, Please try again" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    onLoginFailed();
                }
            }
        });
        progressDialog.dismiss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                // TODO: Implement successful login logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
                startActivity(new Intent(getApplicationContext(), StoreActivity.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        button_login.setEnabled(true);
        finish();
        startActivity(new Intent(getApplicationContext(), StoreActivity.class));
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        button_login.setEnabled(true);
    }

}
