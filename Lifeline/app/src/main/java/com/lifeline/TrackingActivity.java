package com.lifeline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TrackingActivity extends AppCompatActivity {

    private ServiceHandler mServiceHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        mServiceHandler = new ServiceHandler(this);
        mServiceHandler.doBindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destroyed", Toast.LENGTH_SHORT).show();
        mServiceHandler.doUnbindService();
    }

    public void stopTracking(View view) {
        finish();
    }
}
