package com.RIDdev.safealert;

import android.content.Context;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMSMan {
    public static void sendSMS(String msg) {
        SmsManager smsMan = SmsManager.getDefault();
        String[] cont = {
                "0753880342",
                "0072931115",
                "0010020030",
                "0510290321",
                "1001010121"
        };

        for (String contact : cont) {
            try {
                smsMan.sendTextMessage(contact, null, msg, null, null);
                Log.d("SMS",msg);
            } catch (Exception e) {
                Log.d("Error",""+e);
            }
        }
    }
}
