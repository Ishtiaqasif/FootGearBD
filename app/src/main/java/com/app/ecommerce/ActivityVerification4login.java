package com.app.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ecommerce.activities.MainActivity;
import com.app.ecommerce.models.UserAlt;
import com.app.ecommerce.utilities.SharedPref;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ActivityVerification4login extends AppCompatActivity {
    //UserAlt user = new UserAlt();
    Button btnSignIn;
    EditText Phone, Code;
    FirebaseAuth mAuth;
    FirebaseDatabase _db;
    DatabaseReference _myRef;
    SharedPref sharedPref;
    String codeSent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification4login);

        // Write a message to the database

        btnSignIn = findViewById(R.id.buttonSignIn);
        Phone = findViewById(R.id.editTextPhone);
        Code = findViewById(R.id.editTextCode);

        String temp = "+88" + getIntent().getStringExtra("ContactNumber");

        Phone.setText(temp);
        Phone.setEnabled(false);
        btnSignIn.setEnabled(false);

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
                if(validate()) {
                    verifySignIn();
                }


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

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(intent);


                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ActivityVerification4login.this, getString(R.string.wrong_code), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
    }


    private boolean validate(){

        String code = Code.getText().toString();
        boolean flag;
        if (code.isEmpty()){
            Code.setError(getString(R.string.enter_code));
            Code.requestFocus();
            flag = false;
        }
        else
            flag =  true;

        return flag;
    }
    private void sendVerificationCode(){

        String phone = Phone.getText().toString();


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

            Toast.makeText(ActivityVerification4login.this, getString(R.string.check_inbox), Toast.LENGTH_SHORT).show();
            btnSignIn.setBackgroundColor(getResources().getColor(R.color.primaryColor));
            btnSignIn.setEnabled(true);
             //todo fixing setting the saved pref
             //setSavedPreferences();
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {

            Toast.makeText(ActivityVerification4login.this, getString(R.string.failed_varification), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            Toast.makeText(ActivityVerification4login.this, getString(R.string.sent_code), Toast.LENGTH_SHORT).show();
            codeSent =s;
        }

    };

    void setSavedPreferences(){


        _myRef.child(String.valueOf(Phone)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserAlt user = dataSnapshot.getValue(UserAlt.class);

                assert user != null;
                addUserToSavedPref(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            // Failed to read value
              //  Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        }
    private void addUserToSavedPref(UserAlt user) {

        sharedPref.setYourName(user.name);
        sharedPref.setYourShopName(user.shopName);
        sharedPref.setYourShopAddress(user.shopLocation);
        sharedPref.setYourPhone(user.phone);
        sharedPref.setYourNID(user.NID);
        sharedPref.setYourEmail(user.email);
    }

}
