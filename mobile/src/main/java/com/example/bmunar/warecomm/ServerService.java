package com.example.bmunar.warecomm;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by akester on 11/28/15.
 */
public class ServerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();
        if (extras!=null) {
            String message = intent.getStringExtra("message");
            Log.d("INTENT", message);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
