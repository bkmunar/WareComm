package com.example.bmunar.warecomm;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Updated by akester on 11/28/15.
 */
public class ServerService extends Service {
    private static final String TAG = "ServerService";
    public static final String PREFS_NAME = "MyApp_Settings";
    private HttpURLConnection urlConnection;

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
        getData();
        return START_STICKY;
    }

    //Listen for quakes on interval
    protected void getData() {
        Log.d(TAG, "getData");

        CountDownTimer timer = new CountDownTimer(INTERVAL, SECOND) {
            public void onTick(long millisUntilFinished) { }
            public void onFinish() {
                Log.d(TAG, "onFinish");
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        StringBuilder result = new StringBuilder();
                        try {
                            String quakeURL = "http://162.243.156.197:3001/get";
                            Log.d(TAG, quakeURL);

                            URL url = new URL(quakeURL);
                            urlConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                result.append(line);
                            }

                        }catch( Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                            urlConnection.disconnect();
                        }
                        try {
                            onPostExecute(result.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                // Start the timer again
                getData();
            }
        };

        timer.start();
    }

    //Refactor sloppy JSON
    protected void onPostExecute(String result) throws JSONException {
        Log.d(TAG, "onPostExecute");
        Log.d(TAG, result);
        JSONObject mainObject = new JSONObject(result);

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
