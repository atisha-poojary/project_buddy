package com.njit.buddy.app;

import android.app.Activity;
import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/** Created by Indraneel on 2/19/2016. **/

public class ConditionsActivity extends Activity {

    private Button B1;
    private Button B2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        // Set actions for the Accept and Reject Buttons
        B1 = (Button) findViewById(R.id.accept);
        B2 = (Button) findViewById(R.id.reject);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRegisterPage();
            }
        });

        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoLoginActivity();
            }
        });}

    public void gotoRegisterPage() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}