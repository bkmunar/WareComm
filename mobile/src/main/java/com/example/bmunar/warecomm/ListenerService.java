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
 * Job of class is to send/receive messages to/from wear
 */
public class ListenerService extends WearableListenerService {
    private static final String TAG = "ListenerService";
    private GoogleApiClient mApiClient;
    private static final String START_ACTIVITY_QUAKE = "/start_activity_quake"; //CHANGE
    private static final String START_ACTIVITY_PHOTO = "/start_activity_photo"; //CHANGE
    private static final String START_ACTIVITY = "/start_activity";
    private String message;
    private String sender;


    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        //initialize the googleAPIClient for message passing
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
            sender = intent.getStringExtra("sender");
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
                mApiClient.connect();
                //CHANGE THESE FOR SPECIFIC MESSAGES
                if (Objects.equals(sender, "LocationService")) {//THIS WONT DO ANYTHING
                    sendMessage(START_ACTIVITY_QUAKE, message);
                }else if (Objects.equals(sender, "PhotoActivity")) {//THIS WONT DO ANYTHING
                    sendMessage(START_ACTIVITY_PHOTO, message);
                }else {
                    sendMessage(START_ACTIVITY, message);//GENERIC MESSAGE WITH NO SPECIFIC SOURCE
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
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived");

        if( messageEvent.getPath().equalsIgnoreCase( START_ACTIVITY ) ) {
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, message);

            Intent intent = new Intent(this, RegisterPage.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("message", message);
            startActivity(intent);

//            if (Objects.equals(message, "stop")) {
//                Intent intent = new Intent(this, HomePage.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("MESSAGE", message);
//                startActivity(intent);
//            }
//            else if (Objects.equals(message, "photo")){
//                Intent intent = new Intent(this, HomePage.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("MESSAGE", message);
//                startActivity(intent);
//            }
        } else {
            super.onMessageReceived( messageEvent );
        }

    }


}
