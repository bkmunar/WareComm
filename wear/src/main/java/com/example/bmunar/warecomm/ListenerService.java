package com.example.bmunar.warecomm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Updated by akester on 11/28/15.
 * Job of class is to send/receive message to/from mobile
 */
public class ListenerService extends WearableListenerService {
    private static final String TAG = "ListenerService";
    private String message;
    private static final String START_ACTIVITY = "/start_activity";
    private GoogleApiClient mApiClient;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");

        super.onCreate();
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        Bundle extras = intent.getExtras();
        if (extras!=null) {
            message = intent.getStringExtra("message");
            Log.d(TAG, message);
        }
        createAndStartTimer();
        return START_STICKY;
    }

    private void createAndStartTimer() {
        Log.d(TAG, "createAndStartTimer");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, message);
                mApiClient.connect();
                sendMessage(START_ACTIVITY, message);

            }
        }).start();
    }

    private void sendMessage( final String path, final String text ) {
        Log.d(TAG, "sendMessage");

        Log.d("GOOGLECLIENT", String.valueOf(mApiClient.isConnected()));

        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mApiClient ).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived");

//        if( messageEvent.getPath().equalsIgnoreCase( START_ACTIVITY ) ) {
//            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
//            Log.d(TAG, message);
//            if (Objects.equals(message, "stop")) {//EDIT THIS FOR CORRECT MESSAGE
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("MESSAGE", message);
//                startActivity(intent);
//            }
//            else if (Objects.equals(message, "photo")){//EDIT THIS FOR CORRECT MESSAGE
//                Intent intent = new Intent(this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("MESSAGE", message);
//                startActivity(intent);
//            }
//        } else {
            super.onMessageReceived( messageEvent );
//        }

    }
}