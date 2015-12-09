package com.example.bmunar.warecomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Objects;

/**
 * Created by akester on 12/6/15.
 */
public class TestActivity extends Activity {
    private static final String TAG = "TestActivity";
    private String features = "indv";
    private String code = "adam";
    private String dpt = "tools";
    private String indv = "dana";
    private String message = "TEST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        //SEND
        Intent intent = new Intent(this, ListenerService.class);
        Bundle extras = new Bundle();
        extras.putString("features", features); //all dpt indv
        if (Objects.equals(features, "all")) {
            extras.putString("code", code); //adam black blue brown
            Log.d(TAG, code);
        } else if (Objects.equals(features, "dpt")) {
            extras.putString("dpt", dpt); //appliances, bath, electrical, flooring
            extras.putString("message", message);
            Log.d(TAG, dpt);
        } else if (Objects.equals(features, "indv")) {
            extras.putString("indv", indv); //dana, jackson
            extras.putString("message", message);
            Log.d(TAG, indv);
        }
        intent.putExtras(extras);
        startService(intent);
    }
}
