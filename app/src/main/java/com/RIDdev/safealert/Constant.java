package com.RIDdev.safealert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class Constant extends Service {
    static Context ctx;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void Initiate() {
        ctx = getApplicationContext();
        Thread everlasting = new Thread(new Runnable() {
            @Override
            public void run() {
                forever();
            }
        });
        everlasting.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Initiate();
        return START_STICKY;
    }

    public void forever() {
        float[] location = new float[1];
        Alert a = new Alert();
          AlertStarters alert = new AlertStarters();
        while (true) {
            long beginning = System.currentTimeMillis();
            location = a.locate();
            if(location != null){
            sendSMS("lat "+location[1]+";long "+location[2]);
            //if(location[0]> && location[1]> && location[0] < && location[1] <)
            alert.Despair(10000);
            }
            long end = System.currentTimeMillis() - beginning;
            pause(safety(3600000 - end));
        }
    }
    private void sendSMS(final String msg) {
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                SMSMan.sendSMS(msg);
            }
        });
    }

    public long safety(long x) {
        return (x < 1) ? 1 : x;
    }

    public void pause(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
