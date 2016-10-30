package com.example.xianglinzhou.accelerometer;

import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.widget.TextView;
import android.hardware.Sensor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.math.BigDecimal;
import android.app.Activity;

import static android.content.Context.SENSOR_SERVICE;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    Sensor accelerometer;
    SensorManager sm;
    TextView accerlation;
    Double tmpMaxX = 0.00;
    Double tmpMaxY = 0.00;
    Double tmpMaxZ = 0.00;


    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm=(SensorManager)getSystemService(SENSOR_SERVICE);

        accelerometer=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        accerlation=(TextView)findViewById(R.id.acceleration);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Double ValueX = (double)(Math.round(event.values[0]*100)/100.0);
        Double ValueY = (double)(Math.round(event.values[1]*100)/100.0);
        Double ValueZ = (double)(Math.round(event.values[1]*100)/100.0);
        if(ValueX > tmpMaxX)
        {
            tmpMaxX = ValueX;
        }
        if(ValueY > tmpMaxY)
        {
            tmpMaxY = ValueY;
        }
        if(ValueZ > tmpMaxZ)
        {
            tmpMaxZ = ValueZ;
        }
        accerlation.setText("X:"+ValueX+
        "\nY:"+ValueY+
        "\nZ:"+ValueZ+
        "\nMaxX:"+tmpMaxX+
        "\nMaxY:"+tmpMaxY+
        "\nMaxZ:"+tmpMaxZ);
    }

}
