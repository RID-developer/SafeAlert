package com.RIDdev.safealert;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Handler;

public class AlertStarters extends BroadcastReceiver {
    private static int count = 0;
    private static long end = 0;
    private Context context;

    public AlertStarters(Context context) {
        this.context = context;
    }

    public void Setup() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);  // Detect when the screen is turned off
        filter.addAction(Intent.ACTION_SCREEN_ON);   // Optional: detect when the screen is turned on
        context.registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)||intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            long start = System.currentTimeMillis();
            if (safe(start - end) < 3000) {
                count++;
            } else {
                count = 1;
            }
            end = start;

            if (count == 3) {
                SMSMan.sendSMS("HELP ME, I AM IN DANGER!");
                count = 0;
            }
        }

    }

    public long safe(long x) {
        return (x < 1) ? 1 : x;
    }
    public void gyroscope() {
        SensorManager sensor = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
        Sensor gyro = sensor.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    if (Math.abs(event.values[0]) < 0.1f && Math.abs(event.values[1]) < 0.1f && Math.abs(event.values[2]) < 0.1f) {
                        Despair(10000);
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };

        sensor.registerListener(listener, gyro, SensorManager.SENSOR_DELAY_UI);
    }

    public void Despair(long dur) {

        Handler handler = new Handler();
        Intent intent = new Intent(context, Alert.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        Alert.followup = true;
        Runnable callRunnable = new Runnable() {
            @Override
            public void run() {
                if(Alert.followup)
                call();
            }
        };
        handler.postDelayed(callRunnable, dur);
    }
    public void call()
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:+69 420 666 888 1234"));
        context.startActivity(intent);
    }


    public void onDestroy() {
        context.unregisterReceiver(this);
    }
}
