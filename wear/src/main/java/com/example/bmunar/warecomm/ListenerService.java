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

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Updated by akester on 11/28/15.
 * Job of class is to send/receive message to/from mobile
 */
public class ListenerService extends WearableListenerService {
    private static final String TAG = "ListenerService";
    private GoogleApiClient mApiClient;
    private static final String SEND_MESSAGE_ALL = "/send_message_all";
    private static final String SEND_MESSAGE_DPT = "/send_message_dpt";
    private static final String SEND_MESSAGE_INDV = "/send_message_indv";
    private String features;
    private String code;
    private String dpt;


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
            features = intent.getStringExtra("features");
            Log.d(TAG, features);
            if (Objects.equals(features, "all")) {
                code = intent.getStringExtra("code");
                Log.d(TAG, code);
            } else if (Objects.equals(features, "dpt")) {
                dpt = intent.getStringExtra("dpt");
                Log.d(TAG, dpt);
            }
        }
        createAndStartTimer();
        return START_STICKY;
    }

    private void createAndStartTimer() {
        Log.d(TAG, "createAndStartTimer");

        new Thread(new Runnable() {
            @Override
            public void run() {
                mApiClient.connect();
                //CHANGE THESE FOR SPECIFIC MESSAGES
                if (Objects.equals(features, "all")) {//THIS WONT DO ANYTHING
                    sendMessage(SEND_MESSAGE_ALL, code);
                }else if (Objects.equals(features, "dpt")) {//THIS WONT DO ANYTHING
                    sendMessage(SEND_MESSAGE_DPT, dpt);
                }else {
                    sendMessage(SEND_MESSAGE_INDV, code);//GENERIC MESSAGE WITH NO SPECIFIC SOURCE
                }
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

        if( messageEvent.getPath().equalsIgnoreCase( SEND_MESSAGE_ALL ) ) {
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, message);

            Intent intent = new Intent(this, TestActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("message", message);
            startActivity(intent);

//            if (Objects.equals(message, "stop")) { //THIS IS TO START AN ACTIVITY BASED OFF MESSAGE
//                Intent intent = new Intent(this, CheckIn.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("MESSAGE", message);
//                startActivity(intent);
//            }
//            else if (Objects.equals(message, "photo")){ //THIS IS TO START AN ACTIVITY BASED OFF MESSAGE
//                Intent intent = new Intent(this, CheckIn.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("MESSAGE", message);
//                startActivity(intent);
//            }
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}