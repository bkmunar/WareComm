package com.example.bmunar.warecomm;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * Updated by akester on 11/28/15.
 */
public class ServerService extends Service {
    private static final String TAG = "ServerService";
    public static final String PREFS_NAME = "MyApp_Settings";
    private HttpURLConnection urlConnection;

    private String features;
    private String code;
    private String dpt;
    private String indv;
    private String sender = "jackson";
    private String newFeatures = "all"; //HARD CODED FOR TESTING


    private static final int INTERVAL = 10000;
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

        createAndStartEarthquakeService();
        return START_STICKY;
    }

    private void createAndStartEarthquakeService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.e("TAG", "Starting Earthquake URL construction");
                        // Construct URL to query Earthquake data
                        URL url = new URL("http://162.243.156.197:3002/get");
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        try {
                            Log.e("asdf", "got in");

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            // What if JSON object is null? The InputStream is null?
                            JSONObject currentQuery = new JSONObject(readStream(in));
                            Log.e("current query", currentQuery.toString());

                        } finally {
                            urlConnection.disconnect();
                            Log.e("asdf", "got out");
                        }
                    } catch (Exception e) {
                        // do nothing for now
                        Log.e("asdf", "some exception");
                    }

                    try {
                        // Let the Thread sleep for 15 seconds so that the Service doesn't do unnecessary work
                        Thread.sleep(15000);
                    } catch (Exception e){
                        // Do nothing.
                    }
                }
            }
        }).start();
    }

    //Refactor sloppy JSON
    protected void onPostExecute(String result) throws JSONException {
        Log.d(TAG, "onPostExecute");
        Log.d(TAG, result);
        JSONObject mainObject = new JSONObject(result);


        //CODE TO SEND DATA
        Intent intent = new Intent(this, ListenerService.class);
        Bundle extras = new Bundle();
        extras.putString("features", features); //all dpt indv

        if (Objects.equals(newFeatures, "all")) {
            code = intent.getStringExtra("code"); //adam black blue brown
            Log.d(TAG, code);
        } else if (Objects.equals(newFeatures, "dpt")) {
            dpt = intent.getStringExtra("dpt"); //appliances, bath, electrical, flooring
            Log.d(TAG, dpt);
        }else if (Objects.equals(newFeatures, "indv")){
            indv = intent.getStringExtra("indv"); //dana, jackson
            Log.d(TAG, indv);
        }

        intent.putExtras(extras);
        startService(intent);




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
}
