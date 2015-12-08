package com.example.bmunar.warecomm;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
        postToServer();
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

        //createAndStartEarthquakeService();
        return START_STICKY;
    }

    private void createAndStartEarthquakeService() {
        Log.d(TAG, "Get Quakes");

        CountDownTimer timer = new CountDownTimer(INTERVAL, SECOND) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                Log.d(TAG, "onFinish");
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject result = null;
                    try {
                        Log.e("TAG", "Starting Earthquake URL construction");
                         //Construct URL to query Earthquake data

                        URL url = new URL("http://162.243.156.197:3002/get");
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        try {
                            Log.e("asdf", "got in");

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            // What if JSON object is null? The InputStream is null?
                            result = new JSONObject(readStream(in));
                            Log.e("result in thread", result.toString());

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
                        if (result != null)
                            onPostExecute(result.toString());
                    } catch (Exception e){
                        // Do nothing.
                    }
            }
        }).start();
                // Start the timer again
                createAndStartEarthquakeService();
            }
        };

        timer.start();
    }

    //Refactor sloppy JSON
    protected void onPostExecute(String result) throws JSONException {
        Log.d(TAG, "onPostExecute");
        JSONObject mainObject = new JSONObject(result);
        Iterator<String> iter = mainObject.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            String dst = key;
            String msg = mainObject.getJSONObject(key).getString("msg");
            String senderId = mainObject.getJSONObject(key).getString("senderId");
            System.out.println(dst + " " + msg + " " + senderId);


            //CODE TO SEND DATA
            Intent intent = new Intent(this, ListenerService.class);
            Bundle extras = new Bundle();
            extras.putString("features", features); //all dpt indv

            if (key.equals("all")) {
                extras.putString("message", msg);
                extras.putString("sender", senderId);
                code = intent.getStringExtra("code"); //adam black blue brown
                Log.d(TAG, code);
            } else if (key.equals("dpt")) {
                extras.putString("message", msg);
                extras.putString("sender", senderId);
                dpt = intent.getStringExtra("dpt"); //appliances, bath, electrical, flooring
                Log.d(TAG, dpt);
            } else if (key.equals("indv")) {
                extras.putString("message", msg);
                extras.putString("sender", senderId);
                indv = intent.getStringExtra("indv"); //dana, jackson
                Log.d(TAG, indv);
            }

            intent.putExtras(extras);
//            startService(intent);
        }




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

    public void postToServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL urlToRequest = new URL("http://162.243.156.197:3002/post?dst=1&msg=derp");
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
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        Log.d("in get post Data", "");
        for (Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        Log.d("post result", result.toString());
        return result.toString();
    }

    public void deleteFromServer(String dst){

    }
}
