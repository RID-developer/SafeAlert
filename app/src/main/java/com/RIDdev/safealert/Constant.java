package com.RIDdev.safealert;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
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
        while (true) {
            long beginning = System.currentTimeMillis();

            long end = System.currentTimeMillis() - beginning;
            pause(safety(5000 - end));
        }
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
