package com.RIDdev.safealert;

import android.content.Intent;
import android.os.Bundle;
import android.Manifest;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



public class MainActivity extends AppCompatActivity {
    public static AlertStarters alert = null;
    public static Constant cnst = new Constant();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        alert = new AlertStarters(this);
        alert.Setup();
        Intent serviceIntent = new Intent(this, Constant.class);
        startService(serviceIntent);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

        SMSMan.sendSMS("Hello!");
    }
    public void Emergency(View v)
    {
        alert.Despair(10000);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 2);
        //alert.call();
        SMSMan.sendSMS("HELP HELP HELP HELP");
    }
}