package shantanubobhate.datatocsv;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


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

    private FileWriter mFileWriter;
    private EditText editTextFilename, editTextDescription;
    private Button buttonTracking;
    private Boolean isTracking = false;
    private File dir;
    private CSVWriter writer;
    private String[] dataContainer = {"Acceleration X", "Acceleration Y", "Acceleration Z", "..."};
    private Sensor accelerometer;
    private SensorManager mSensorManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Views
        editTextFilename = (EditText) findViewById(R.id.editTextFilename);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        buttonTracking = (Button) findViewById(R.id.buttonTracking);

        // Get the root directory
        // .. This is the Device Storage
        File root = android.os.Environment.getExternalStorageDirectory();
        dir = new File(root.getAbsolutePath() + "/DataToCSV");              // Get the path to the new directory to create
        if (!dir.exists()) {                                                // Check if this directory exists
            dir.mkdirs();                                                   // Create a new directory called DataToCSV
        }

        mSensorManger = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManger.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManger.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void toggleTracking(View view) {
        if (isTracking) {
            isTracking = false;
            buttonTracking.setText("Start Tracking");
            try {
                writer.close();                                                 // Close writer stream
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            isTracking = true;
            buttonTracking.setText("Stop Tracking");

            String fileName = editTextFilename.getText().toString() + ".csv";   // Get the filename
            String description = editTextDescription.getText().toString();

            File f = new File(dir, fileName);                                   // Create a new file
            String filePath = f.getAbsolutePath();                              // Get the absolute path to the file
            writer = null;                                                      // Create the CSVWrite object which is from the opencsv library
            if (f.exists() && !f.isDirectory()) {
                try {
                    mFileWriter = new FileWriter(filePath, true);              // Initialize file writer
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writer = new CSVWriter(mFileWriter);                            // Initialize opencsv writer
            } else {
                try {
                    writer = new CSVWriter(new FileWriter(filePath));           // Initialize opencsv writer
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Write Headers
            writer.writeNext(dataContainer);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Only write to csv if app isTracking
        if (isTracking) {

            // THIS IS WHERE THE DATA IS WRITTEN //
            double ValueX = (Math.round(event.values[0]*1000)/1000.0);
            double ValueY = (Math.round(event.values[1]*1000)/1000.0);
            double ValueZ = (Math.round(event.values[2]*1000)/1000.0);

            dataContainer[0] = "" + ValueX;
            dataContainer[1] = "" + ValueY;
            dataContainer[2] = "" + ValueZ;
            dataContainer[3] = "...";

            writer.writeNext(dataContainer);
            ///////////////////////////////////////
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            writer.close();                                                 // Close writer stream
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

