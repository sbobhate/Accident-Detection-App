package shantanubobhate.datatocsv;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
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

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SensorService.LocalBinder mLocalBinder = (SensorService.LocalBinder) iBinder;
            mLocalService = mLocalBinder.getService();
            isBound = true;
            Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
            Toast.makeText(MainActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        editTextFilename = (EditText) findViewById(R.id.editTextFilename);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        buttonTracking = (Button) findViewById(R.id.buttonTracking);
    }

    public void toggleTracking(View view) {
        if (isTracking) {
            isTracking = false;
            doUnbindService();
            buttonTracking.setText("Start Tracking");
        } else {
            String fileName = editTextFilename.getText().toString() + ".csv";
            String description = editTextDescription.getText().toString();
            isTracking = true;
            doBindService(fileName, description);
            buttonTracking.setText("Stop Tracking");
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