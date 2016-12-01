package com.lifeline;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class SensorService extends Service implements SensorEventListener {

    // TAG to identify notification
    private static final int NOTIFICATION = 007;

    // IBinder object to allow Activity to connect
    private final IBinder mBinder = new LocalBinder();

    // Sensor Objects
    private Sensor accelerometer;
    private SensorManager mSensorManager;

    private double accelerationX, accelerationY, accelerationZ;

    private int threshold = 20;
    private ShakeDetector mShakeDetector;
    private CountDownTimer timer;
    private MediaPlayer mediaPlayeralarm;

    // Notification Manager
    private NotificationManager mNotificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    public class LocalBinder extends Binder {
        public SensorService getService() {
            return SensorService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayeralarm = MediaPlayer.create(this, R.raw.rising_swoops);
        timer = new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                mediaPlayeralarm.start();
            }

            public void onFinish() {
                mediaPlayeralarm.stop();
            }
        };

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        /*
         * The following shake detector code does not work
         * Instead I have added code in the onSensorChanged method below to trigger the alarm
         */
        /*
        mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake() {
                // Do stuff! This is where we call sendSMS?
                Toast.makeText(SensorService.this, "Shake Detected", Toast.LENGTH_SHORT).show();
                timer.start();
            }
        });
        */

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        showNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSensorManager.unregisterListener(this);                            // Unregister sensor when not in use

        mNotificationManager.cancel(NOTIFICATION);
        stopSelf();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        accelerationX = (Math.round(sensorEvent.values[0]*1000)/1000.0);
        accelerationY = (Math.round(sensorEvent.values[1]*1000)/1000.0);
        accelerationZ = (Math.round(sensorEvent.values[2]*1000)/1000.0);

        /*** Detect Accident ***/
        if (accelerationX > threshold || accelerationY > threshold || accelerationZ > threshold) {
            timer.start();  // Sound the alarm
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void showNotification() {
        Log.d("SERVICE DEBUG", "Notification Shown");
        CharSequence text = "Started Data Collection";

        // PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        // PendingIntent deleteIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            Notification mNotification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.bell)
                    .setTicker(text)
                    .setContentTitle("Hello there!")
                    .setContentText(text)
                    .setAutoCancel(false)
                    .build();

            mNotificationManager.notify(NOTIFICATION, mNotification);
        }
    }
}
