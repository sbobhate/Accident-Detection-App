package com.lifeline;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String state;
    private static final String KEY = "com.lifeline.secret";
    private static final String STATE = "com.lifeline.state";
    private static final int MY_PERMISSION_REQUEST_INTERNET = 1;
    private static final int MY_PERMISSION_REQUEST_READ_PHONE_STATE = 2;
    private static final int MY_PERMISSION_REQUEST_ACCESS_COARSE_LOCATION = 3;
    private static final int MY_PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 4;
    private static final int MY_PERMISSION_REQUEST_SEND_SMS = 5;

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    Toast.makeText(this, "Permission given", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("This application needs the permission to save data\n" +
                            "Go to Applications > Application Manager\n" +
                            "to give permission.")
                            .setTitle("Files will not be Saved")
                            .setNeutralButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return;
            }
        }
    }
}
