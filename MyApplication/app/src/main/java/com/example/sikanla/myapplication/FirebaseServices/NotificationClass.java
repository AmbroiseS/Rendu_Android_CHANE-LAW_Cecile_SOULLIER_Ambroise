package com.example.sikanla.myapplication.FirebaseServices;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Sikanla on 23/05/2017.
 */

public class NotificationClass {
    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    public OkHttpClient mClient;

    public void sendMessage(final JSONArray recipients, final String title, final String body, final String icon, final String message) {
        mClient = new OkHttpClient();

        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);

                    JSONObject data = new JSONObject();
                    data.put("title", title);
                    data.put("subtitle",body);
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    Log.e("ee",result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {

        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + "AIzaSyDmNEdqSeXZvf61KYUva4pM40Mh-tT0QhM")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}
