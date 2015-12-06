package com.example.bmunar.warecomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Updated by akester on 12/6/15.
 */
public class TestActivity extends Activity {
    private static final String TAG = "TestActivity";
    private static final String test = "This is a test message from the Wear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        //RECEIVE
        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        Log.d(TAG, message);

        //SEND
        Intent anotherIntent = new Intent(this, ListenerService.class);
        Bundle extras = new Bundle();
        extras.putString("message", test);
        extras.putString("sender", TAG);
        anotherIntent.putExtras(extras);
        startService(anotherIntent);

    }
}
