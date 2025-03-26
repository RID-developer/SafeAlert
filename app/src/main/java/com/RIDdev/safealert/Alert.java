package com.RIDdev.safealert;

import android.content.Context;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Alert extends AppCompatActivity {
    private MediaRecorder record;
    private Camera cam;

    public static boolean followup = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alert);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public void Cancel(View v)
    {
        Stop();
        followup = false;
    }
    private void Start() {
        cam = Camera.open();
        cam.unlock();
        record = new MediaRecorder();

        record.setCamera(cam);
        record.setAudioSource(MediaRecorder.AudioSource.MIC);
        record.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        record.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        record.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        record.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        record.setOutputFile("/dev/null");

        try {
            record.prepare();
            record.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startActivity(new Intent(this, Alert.class));
    }

    private void Stop() {
        if (record != null) {
            record.stop();
            record.release();
        }
        if (cam != null) {
            cam.release();
        }
    }

    public float[] locate() {
        final float[] spot = new float[2];

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        }

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            spot[0] = (float) location.getLatitude();
            spot[1] = (float) location.getLongitude();
        }
        return spot;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Stop();
    }
}