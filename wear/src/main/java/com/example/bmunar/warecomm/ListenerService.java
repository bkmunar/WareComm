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
    private String indv;
    private String message1;


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
            } else if (Objects.equals(features, "indv")){
                indv = intent.getStringExtra("indv");
                message1 = intent.getStringExtra("message");
                Log.d(TAG, indv);
                Log.d(TAG, message1);
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
                if (Objects.equals(features, "all")) {
                    sendMessage(SEND_MESSAGE_ALL, code);
                }else if (Objects.equals(features, "dpt")) {
                    sendMessage(SEND_MESSAGE_DPT, dpt);
                }else {
                    final String doubleInfo = indv.concat(" ").concat(message1);
                    sendMessage(SEND_MESSAGE_INDV, doubleInfo);
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
            Intent intent = new Intent(this, BroadcastNotification.class);
            Bundle extras = new Bundle();
            extras.putString("features", "all"); //all dpt indv
            extras.putString("code", message); //adam black blue brown
            intent.putExtras(extras);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if ( messageEvent.getPath().equalsIgnoreCase( SEND_MESSAGE_DPT ) ) {
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, message);
            Intent intent = new Intent(this, requestNotification.class);
            Bundle extras = new Bundle();
            extras.putString("features", "dpt"); //all dpt indv

            final String[] splitStringArray = message.split(" ");
            String a = splitStringArray[0];
            String b = splitStringArray[1];

            extras.putString("dpt", a); //dana, jackson
            extras.putString("message", b);

            intent.putExtras(extras);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else if ( messageEvent.getPath().equalsIgnoreCase( SEND_MESSAGE_INDV ) ){
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, message);
            Intent intent = new Intent(this, IndividualRequest.class);
            Bundle extras = new Bundle();
            extras.putString("features", "indv"); //all dpt indv

            final String[] splitStringArray = message.split(" "); ///THIS WILL NOT WORK FOR SENTENCES
            String a = splitStringArray[0];
            String b = splitStringArray[1];

            extras.putString("indv", a); //dana, jackson
            extras.putString("message", b);

            intent.putExtras(extras);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}