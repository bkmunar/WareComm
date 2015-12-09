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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Objects;

/**
 * Updated by akester on 11/28/15.
 */
public class GetServerService extends Service {
    private static final String TAG = "GetServerService";
    public static final String PREFS_NAME = "MyApp_Settings";
    private HttpURLConnection urlConnection;

    private String features;
    private String code;
    private String dpt;
    private String indv;

    final String receiverId = "bryan";


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

        createAndStartGetServerService();
        return START_STICKY;
    }

    private void createAndStartGetServerService() {
        Log.d(TAG, "Get Messages");

        CountDownTimer timer = new CountDownTimer(INTERVAL, SECOND) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                Log.d(TAG, "onFinish");
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject result = null;
                    try {
                        Log.d(TAG, "Starting URL construction");

                        URL url = new URL("http://162.243.156.197:3002/get");
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        try {
                            Log.e(TAG, "got in");

                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            // What if JSON object is null? The InputStream is null?
                            result = new JSONObject(readStream(in));
                            Log.e(TAG, result.toString());

                        } finally {
                            urlConnection.disconnect();
                            Log.e(TAG, "got out");
                        }
                    } catch (Exception e) {
                        // do nothing for now
                        Log.e(TAG, "some exception");
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
                createAndStartGetServerService();
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
            Log.d(TAG, key);
            String senderId = mainObject.getJSONObject(key).getString("senderId");
            Log.d(TAG, senderId);

            if (!Objects.equals(receiverId, senderId)) {

                //CODE TO SEND DATA TO LISTENER
                Intent intent = new Intent(this, ListenerService.class);
                Bundle extras = new Bundle();

                if (key.equals("all")) {
                    extras.putString("features", key); //all dpt indv

                    String msg1 = mainObject.getJSONObject(key).getString("msg1");
                    Log.d(TAG, msg1);
                    extras.putString("code", msg1); //adam black blue brown
                    //extras.putString("sender", senderId);
                    deleteFromServer(key);

                } else if (key.equals("dpt")) {
                    extras.putString("features", key); //all dpt indv

                    String msg1 = mainObject.getJSONObject(key).getString("msg1");
                    Log.d(TAG, msg1);
                    extras.putString("dpt", msg1);//appliances, bath, electrical, flooring

                    String msg2 = mainObject.getJSONObject(key).getString("msg2");
                    Log.d(TAG, msg2);
                    extras.putString("dpt", msg2);

                    //extras.putString("sender", senderId);
                    deleteFromServer(key);

                } else if (key.equals("indv")) {
                    extras.putString("features", key); //all dpt indv

                    String msg1 = mainObject.getJSONObject(key).getString("msg1");
                    Log.d(TAG, msg1);
                    extras.putString("indv", msg1); //dana, jackson

                    String msg2 = mainObject.getJSONObject(key).getString("msg2");
                    Log.d(TAG, msg2);
                    extras.putString("message", msg2);

                    //extras.putString("sender", senderId);
                    deleteFromServer(key);

                }

                intent.putExtras(extras);
                startService(intent);
            }
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

    public void deleteFromServer(String dst){
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
