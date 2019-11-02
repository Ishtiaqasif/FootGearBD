package com.app.ecommerce;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.ButterKnife;
import butterknife.BindView;

public class ActivityLogin extends AppCompatActivity  {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;



    @BindView(R.id.phone_login) EditText _phoneText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signUpLink;


    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        ButterKnife.bind(this);

        _phoneText = findViewById(R.id.phone_login);
        _loginButton = findViewById(R.id.btn_login);
        _signUpLink = findViewById(R.id.link_signup);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signUpLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), ActivityRegistration.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "ActivityLogin");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(ActivityLogin.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String phone = _phoneText.getText().toString();
        //TODO: Implement your own authentication logic here.

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();;
        databaseReference.child("users").orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(
                new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //Log.i(Constants.TAG, "dataSnapshot value = " + dataSnapshot.getValue());

                        if (dataSnapshot.exists()) {
                            Intent intent = new Intent(ActivityLogin.this, ActivityVerification4login.class);

                            intent.putExtra ( "ContactNumber", phone );

                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "You look familiar to us.", Toast.LENGTH_LONG).show();

                        } else {

                            Toast.makeText(getApplicationContext(), "You don't have a account! Sign Up First!", Toast.LENGTH_LONG).show();
                            // User Not Yet Exists
                            // Do your stuff here if user not yet exists
                        }
                    }
                    @Override
                    public void onCancelled (@NonNull DatabaseError databaseError){

                    }
                }

        );


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);



        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String phone = _phoneText.getText().toString();

        if (phone.isEmpty() || phone.length() < 10) {
            _phoneText.setError("Enter a valid phone number");
            valid = false;
        } else {
            _phoneText.setError(null);
        }



        return valid;
    }
}