package com.app.ecommerce.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.app.ecommerce.Config;
import com.app.ecommerce.ActivityLogin;
import com.app.ecommerce.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ActivitySplash extends AppCompatActivity {

    Boolean isCancelled = false;
    private ProgressBar progressBar;
    long id = 0;
    String url = "";

    FirebaseUser currentUser;

    FirebaseAuth mAuth;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mAuth = FirebaseAuth.getInstance();


        if (Config.ENABLE_RTL_MODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }
        }



        progressBar = findViewById(R.id.progressBar);

        if (getIntent().hasExtra("nid")) {
            id = getIntent().getLongExtra("nid", 0);
            url = getIntent().getStringExtra("external_link");
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);

                if (!isCancelled) {
                    if (id == 0) {
                        if (url.equals("") || url.equals("no_url")) {


                            currentUser = mAuth.getCurrentUser();


                            if (currentUser != null ){

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{

                                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                                startActivity(intent);
                                finish();
                            }

                        } else {

                            Intent a = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(a);

                            Intent b = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(b);

                            finish();
                        }
                    } else if (id == 1010101010) {

                        Intent a = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(a);

                        Intent b = new Intent(getApplicationContext(), ActivityHistory.class);
                        startActivity(b);

                        finish();

                    } else {

                        Intent a = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(a);

                        Intent b = new Intent(getApplicationContext(), ActivityNotificationDetail.class);
                        b.putExtra("product_id", id);
                        startActivity(b);

                        finish();

                    }
                }
            }
        }, Config.SPLASH_TIME);

    }

}