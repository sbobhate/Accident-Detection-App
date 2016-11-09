package com.lifeline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PhoneVerificationActivity extends AppCompatActivity {

    private static final String KEY = "com.lifeline.secret";
    private static final String STATE = "com.lifeline.state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        SharedPreferences.Editor editor = getSharedPreferences(KEY, MODE_PRIVATE).edit();
        editor.putString(STATE, "verification");
        editor.commit();
    }

    public void goToDashboard(View view) {
        finish();
        startActivity(new Intent(this, DashboardActivity.class));
    }
}
