package com.example.clinica_universitaria;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MiFireBaseInstanceIdService extends FirebaseMessagingService {
    public static final String TAG = "NOTICIAS";
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        String token = FirebaseInstanceId.getInstance().getId();
        Log.d(TAG, "Token: " + token);
    }
}
