package com.example.sikanla.myapplication.FirebaseServices;

import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static android.content.ContentValues.TAG;

/**
 * Created by Sikanla on 22/05/2017.
 */

public class FirebaseToken extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        SharedPreferences prefs = getSharedPreferences("id", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("APIKEY", refreshedToken);
        editor.commit();
    }
}
