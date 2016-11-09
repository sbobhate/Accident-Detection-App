package com.lifeline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private String state;
    private static final String KEY = "com.lifeline.secret";
    private static final String STATE = "com.lifeline.state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences mSharedPreferences = getSharedPreferences(KEY, MODE_PRIVATE);
        state = mSharedPreferences.getString(STATE, null);

        if (state == "signUp") {
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        } else if (state == "personalInfo") {
            finish();
            startActivity(new Intent(this, PersonalInfoActivity.class));
        } else if (state == "verification") {
            finish();
            startActivity(new Intent(this, PhoneVerificationActivity.class));
        } else if (state == "login") {
            finish();
            startActivity(new Intent(this, LoginScreenActivity.class));
        } else {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(STATE, "signUp");
            editor.commit();
            finish();
            startActivity(new Intent(this, SignUpActivity.class));
        }
    }
}
