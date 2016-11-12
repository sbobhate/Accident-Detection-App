package shantanubobhate.datatocsv;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SensorService extends Service implements SensorEventListener {

    private FileWriter mFileWriter;
    private File dir;
    private CSVWriter writer;
    private String[] dataContainer = {"Acceleration X", "Acceleration Y", "Acceleration Z", "..."};

    // TAG to identify notification
    private static final int NOTIFICATION = 007;

    // IBinder object to allow Activity to connect
    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        String fileName = intent.getStringExtra("filename");
        String description = intent.getStringExtra("description");

        // Get the root directory
        // .. This is the Device Storage
//        if (ContextCompat.checkSelfPermission(getApplicationContext(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(MainActivity.ACTIVITY_SERVICE, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    1);
//        }
        File root = android.os.Environment.getExternalStorageDirectory();
        dir = new File(root.getAbsolutePath() + "/DataToCSV");              // Get the path to the new directory to create
        if (!dir.exists()) {                                                // Check if this directory exists
            dir.mkdirs();                                                   // Create a new directory called DataToCSV
        }

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

        dataContainer[3] = description;
        writer.writeNext(dataContainer);

        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        try {
            writer.close();                                                 // Close writer stream
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public SensorService getService() {
            return SensorService.this;
        }
    }

    private Sensor accelerometer;
    private SensorManager mSensorManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);                            // Unregister sensor when not in use

        mNotificationManager.cancel(NOTIFICATION);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Get acceleration values
        dataContainer[0] = "" + (Math.round(sensorEvent.values[0]*1000)/1000.0);
        dataContainer[1] = "" + (Math.round(sensorEvent.values[1]*1000)/1000.0);
        dataContainer[1] = "" + (Math.round(sensorEvent.values[2]*1000)/1000.0);
        writer.writeNext(dataContainer);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    private NotificationManager mNotificationManager;

    private void showNotification() {
        CharSequence text = "Sensor Service Started";

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Notification mNotification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.bell)
                    .setTicker(text)
                    .setContentTitle("Hello there!")
                    .setContentText(text)
                    .setContentIntent(contentIntent)
                    .build();

            mNotificationManager.notify(NOTIFICATION, mNotification);
        }
    }
}