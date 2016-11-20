package com.lifeline;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user == null) {
            finish();
            startActivity(new Intent(this, LoginScreenActivity.class));
        }
    }

    public void startTracking(View view) {
        Intent intent = new Intent(this, TrackingActivity.class);
        startActivity(intent);
    }

    public void goToContacts(View view) {
        Intent intent = new Intent(this, MyEmerContActivity.class);
        startActivity(intent);
    }

    public void goToPolicyInfo(View view) {

    }

    public void goToDrivingInfo(View view) {

    }

    public void logout(View view)
    {
        try {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginScreenActivity.class));
        } catch (Exception e) {
            Toast.makeText(this, "Unsuccessful", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
