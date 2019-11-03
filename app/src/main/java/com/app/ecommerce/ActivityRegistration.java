package com.app.ecommerce;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.SimpleCustomValidation;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class ActivityRegistration extends AppCompatActivity {



    private FirebaseAuth mAuth;
    EditText Name, ShopName, ShopLocation , ContactNumber, NID, Email;
    Button register;
    private AwesomeValidation awesomeValidation;


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

        awesomeValidation.addValidation(ActivityRegistration.this, R.id.emailOptional, new SimpleCustomValidation() {
            @Override
            public boolean compare(String editTextEmail  ) {
                // check if the email is null

                if (editTextEmail.length() == 0 )  {
                    return true;
                }
                else return EMAIL_ADDRESS_PATTERN.matcher(editTextEmail).matches();

            }
        }, R.string.email_error);


       // awesomeValidation.addValidation(ActivityRegistration.this, R.id.emailOptional, android.util.Patterns.EMAIL_ADDRESS, R.string.email_error);





        register.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                if (awesomeValidation.validate()){
                    Toast.makeText(ActivityRegistration.this, "Everything is Correct", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ActivityRegistration.this, ActivityVerification.class);
                    intent.putExtra ( "Name", Name.getText().toString() );
                    intent.putExtra ( "ShopName", ShopName.getText().toString() );
                    intent.putExtra ( "ShopLocation", ShopLocation.getText().toString() );
                    intent.putExtra ( "ContactNumber", ContactNumber.getText().toString() );
                    intent.putExtra ( "NID", NID.getText().toString() );
                    intent.putExtra ( "Email", Email.getText().toString() );

                    startActivity(intent);
                }
                else {
                    Toast.makeText(ActivityRegistration.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(

            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
}
