package com.example.bmunar.warecomm;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * Updated by akester on 11/28/15.
 */
public class PostServerService extends Service {
    private static final String TAG = "GetServerService";
    public static final String PREFS_NAME = "MyApp_Settings";
    private HttpURLConnection urlConnection;

    private String features;
    private String code;
    private String dpt;
    private String indv;
    private String sender = "jackson";
    private String newFeatures = "all"; //HARD CODED FOR TESTING


    private static final int INTERVAL = 3000;
    private static final int SECOND = 1000;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        Bundle extras = intent.getExtras();
        if (extras!=null) {
            features = intent.getStringExtra("features"); //all dpt indv
            Log.d(TAG, features);
            if (Objects.equals(features, "all")) {
                code = intent.getStringExtra("code"); //adam black blue brown
                Log.d(TAG, code);
            } else if (Objects.equals(features, "dpt")) {
                dpt = intent.getStringExtra("dpt"); //appliances, bath, electrical, flooring
                Log.d(TAG, dpt);
            }else if (Objects.equals(features, "indv")){
                indv = intent.getStringExtra("indv"); //dana, jackson
                Log.d(TAG, indv);
            }
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    // Util function for Services and Async Tasks
    public static String readStream(InputStream is) {
        // Pulled implementation from Stack Overflow
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            Log.e("IOException", e.toString());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                Log.e("IOException", e.toString());
            }
        }
        return sb.toString();
    }


    public void postToServer(String dst){
        final String DEST = dst;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL urlToRequest = new URL("http://162.243.156.197:3002/post?dst=" + DEST);
                    HttpURLConnection urlConnection = (HttpURLConnection) urlToRequest.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    String postParameters = "dst=1&msg=merp";
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print("dst=1&msg=merp");
                    out.close();

                    int statusCode = urlConnection.getResponseCode();
                    if (statusCode != HttpURLConnection.HTTP_OK) {
                        Log.d("NOT WORKING", "???????");
                    }else {
                        Log.d("WORKING", "???????");
                    }
                } catch (Exception e) {

                }finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }
        ).start();
    }
}
