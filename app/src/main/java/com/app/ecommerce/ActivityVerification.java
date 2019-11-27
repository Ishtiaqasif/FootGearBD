package com.app.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ecommerce.activities.MainActivity;
import com.app.ecommerce.models.UserAlt;
import com.app.ecommerce.utilities.SharedPref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class ActivityVerification extends AppCompatActivity {

    Button btnSignIn;
    EditText Phone, Code;
    FirebaseAuth mAuth;
    FirebaseDatabase _db;
    DatabaseReference _myRef;
    Context context;
    SharedPref sharedPref;


    String codeSent, name, shopName, shopLocation, contactNumber, NID, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varification);

        // Write a message to the database

        btnSignIn = findViewById(R.id.buttonSignIn);
        Phone = findViewById(R.id.editTextPhone);
        Code = findViewById(R.id.editTextCode);

        String temp = "+88" + getIntent().getStringExtra("ContactNumber");

        Phone.setText(temp);
        Phone.setEnabled(false);
        btnSignIn.setEnabled(false);

        context =  getApplicationContext();
        sharedPref = new SharedPref(context);

         name = getIntent().getStringExtra("Name");
         shopName = getIntent().getStringExtra("ShopName");
         shopLocation = getIntent().getStringExtra("ShopLocation");
         contactNumber = getIntent().getStringExtra("ContactNumber");
         NID = getIntent().getStringExtra("NID");
         email = getIntent().getStringExtra("Email");

        mAuth = FirebaseAuth.getInstance();
        _db = FirebaseDatabase.getInstance();
        _myRef = _db.getReference("users");



        findViewById(R.id.buttonGetVerificationCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendVerificationCode();
            }
        });


        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignIn();

            }
        });
    }

    private void verifySignIn(){
        String code = Code.getText().toString();

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            addUserToDatabase();
                            addUserToSavedPref();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(intent);

                            //FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ActivityVerification.this, "Wrong ActivityVerification Code", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }

    private void addUserToSavedPref() {

        sharedPref.setYourName(name);
        sharedPref.setYourShopName(shopName);
        sharedPref.setYourShopAddress(shopLocation);
        sharedPref.setYourPhone(contactNumber);
        sharedPref.setYourNID(NID);
        sharedPref.setYourEmail(email);
    }

    private void addUserToDatabase(){
        UserAlt user = new UserAlt(
                name,
                shopName,
                shopLocation,
                contactNumber,
                NID,
                email
        );

        _myRef.child(contactNumber).setValue(user);

    }

    private void sendVerificationCode(){

        String phone = Phone.getText().toString();

        if (phone.isEmpty()){
            Phone.setError("Phone Number is Required");
            Phone.requestFocus();

            return;
        }

        if (phone.length() < 10) {
            Phone.setError("Please Provide Valid Phone Number");
            Phone.requestFocus();

            return;
        }


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            Toast.makeText(ActivityVerification.this, "Check your inbox", Toast.LENGTH_SHORT).show();
            btnSignIn.setBackgroundColor(getResources().getColor(R.color.primaryColor));
            btnSignIn.setEnabled(true);

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(ActivityVerification.this, "Verification Failed", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);



            Toast.makeText(ActivityVerification.this, "Code Sent", Toast.LENGTH_SHORT).show();
            codeSent =s;

        }

    };
}
