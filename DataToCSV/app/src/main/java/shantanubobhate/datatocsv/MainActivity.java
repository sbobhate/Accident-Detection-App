package shantanubobhate.datatocsv;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity  {


    /*
     *
     * This project uses opencsv
     * The binary files can be found here: https://sourceforge.net/projects/opencsv/files/latest/download
     *
     * Instructions to compile binary:
     *  - Download the library.jar file and copy it to your /libs/ folder inside your application project.
     *  - Press "Sync Project with Gradle Files" button. (Between AVD Manager and Project Structure icon in the tool bar.
     *
     * NOTE: When you install the app, make sure you go to Applications and allow the permissions.
     *
     */

    private EditText editTextFilename, editTextDescription;
    private Button buttonTracking;
    private Boolean isTracking = false;

    private SensorService mLocalService;
    private boolean isBound = false;

    private boolean hasPermission = false;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE= 1;

    // Function to handle a new Service Connection
    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SensorService.LocalBinder mLocalBinder = (SensorService.LocalBinder) iBinder;
            mLocalService = mLocalBinder.getService();
            isBound = true;
            // Display when connected
            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            // Display when disconnected
            Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            isTracking = savedInstanceState.getBoolean("IsTracking");
            isBound = savedInstanceState.getBoolean("IsBound");
            hasPermission = savedInstanceState.getBoolean("HasPermission");
        } else {
            // Probably initialize members with default values for a new instance
        }

        // Initialize Views
        editTextFilename = (EditText) findViewById(R.id.editTextFilename);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        buttonTracking = (Button) findViewById(R.id.buttonTracking);

        // Check for permissions and request if not enabled
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(this, new String[]
                {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            hasPermission = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isTracking) buttonTracking.setText("Stop Tracking");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        outState.putBoolean("IsTracking", isTracking);
        outState.putBoolean("IsBound", isBound);
        outState.putBoolean("HasPermission", hasPermission);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        isTracking = savedInstanceState.getBoolean("IsTracking");
        isBound = savedInstanceState.getBoolean("IsBound");
        hasPermission = savedInstanceState.getBoolean("HasPermission");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    Toast.makeText(this, "Permission given", Toast.LENGTH_SHORT).show();
                    hasPermission = true;
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

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void toggleTracking(View view) {
        if (isTracking) {
            isTracking = false;
            doUnbindService();
            buttonTracking.setText("Start Tracking");
        } else {
            if (hasPermission) {
                String fileName = "";
                fileName = editTextFilename.getText().toString() + ".csv";
                if (fileName == "") {
                    fileName = "data.csv";
                }
                String description = editTextDescription.getText().toString();
                isTracking = true;
                buttonTracking.setText("Stop Tracking");
                doBindService(fileName, description);
            }
        }
    }

    void doBindService(String filename, String description) {
        Intent intent = new Intent(this, SensorService.class);
        intent.putExtra("filename", filename);
        intent.putExtra("description", description);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    void doUnbindService()
    {
        unbindService(myConnection);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}