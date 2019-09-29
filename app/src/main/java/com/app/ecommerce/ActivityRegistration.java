package com.app.ecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.ecommerce.activities.MainActivity;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivityRegistration extends AppCompatActivity {



    private FirebaseAuth mAuth;
    EditText Name, ShopName, ShopLocation , ContactNumber, NID, Email;
    Button register;
    private AwesomeValidation awesomeValidation;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        UpdateUI();
    }

    private void UpdateUI() {
        Name = findViewById(R.id.name);
        ShopName = findViewById(R.id.shopName);
        ShopLocation = findViewById(R.id.shopLocation);
        ContactNumber = findViewById(R.id.contactNumber);
        NID = findViewById(R.id.NID);
        Email = findViewById(R.id.emailOptional);
        register = findViewById(R.id.register);


        awesomeValidation.addValidation(ActivityRegistration.this, R.id.name, "[a-zA-Z\\s]+", R.string.name_error);
        awesomeValidation.addValidation(ActivityRegistration.this, R.id.shopName, "[a-zA-Z\\s]+", R.string.shop_name_error);
        awesomeValidation.addValidation(ActivityRegistration.this, R.id.shopLocation, "[\\w\\s,-.]+", R.string.shop_location_error);
        awesomeValidation.addValidation(ActivityRegistration.this, R.id.contactNumber,new SimpleCustomValidation(){
            @Override
            public boolean compare(String editTextContactNumber  ) {
                // check if the contact number is 11 digits

                if ((editTextContactNumber.length() == 11) && (editTextContactNumber.matches("[0-9]+")) ) {
                    return true;
                }

                return false;
            }
                },R.string.contact_number_error);
        awesomeValidation.addValidation(ActivityRegistration.this, R.id.NID, new SimpleCustomValidation() {
            @Override
            public boolean compare(String editTextNID  ) {
                // check if the NID is 13 digits

                    if (editTextNID.length() == 13) {
                        return true;
                    }

                return false;
            }
        }, R.string.nid_error);

        awesomeValidation.addValidation(ActivityRegistration.this, R.id.emailOptional, android.util.Patterns.EMAIL_ADDRESS, R.string.email_error);


        register.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()){
                    Toast.makeText(ActivityRegistration.this, "Everything is Correct", Toast.LENGTH_SHORT).show();


                    //mAuth.

                    Intent intent = new Intent(ActivityRegistration.this, Verification.class);
                    intent.putExtra ( "TextBox", ContactNumber.getText().toString() );
                    startActivity(intent);
                }
                else {
                    Toast.makeText(ActivityRegistration.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
