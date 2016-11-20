package com.lifeline;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by shantanubobhate on 11/20/16.
 */

public class ServiceHandler {

    private SensorService mLocalService;
    private boolean isBound = false;

    private Context context;

    public ServiceHandler (Context tContext) {
        this.context = tContext;
    }

    // Function to handle a new Service Connection
    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SensorService.LocalBinder mLocalBinder = (SensorService.LocalBinder) iBinder;
            mLocalService = mLocalBinder.getService();
            isBound = true;
            // Display when connected
            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            // Display when disconnected
            Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
        Toast.makeText(context, "Bound", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, SensorService.class);
        context.bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService() {
        Toast.makeText(context, "Unbound", Toast.LENGTH_SHORT).show();
        context.unbindService(myConnection);
    }
}
