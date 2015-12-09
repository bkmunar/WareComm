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

    private static final String SEND_MESSAGE_ALL = "/send_message_all";
    private static final String SEND_MESSAGE_DPT = "/send_message_dpt";
    private static final String SEND_MESSAGE_DPT2 = "/send_message_dpt2";
    private static final String SEND_MESSAGE_INDV = "/send_message_indv";
    private static final String SEND_MESSAGE_INDV2 = "/send_message_indv2";
    private String features;
    private String code;
    private String dpt;
    private String indv;
    private String message1;
    private String message2;


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
            features = intent.getStringExtra("features");
            Log.d(TAG, features);
            if (Objects.equals(features, "all")) {
                code = intent.getStringExtra("code");
                Log.d(TAG, code);
            } else if (Objects.equals(features, "dpt")) {
                dpt = intent.getStringExtra("dpt");
                message2 = intent.getStringExtra("message");
                Log.d(TAG, dpt);
            }else if (Objects.equals(features, "dpt2")){
                dpt = intent.getStringExtra("dpt");
                message2 = intent.getStringExtra("message");
                Log.d(TAG, dpt);
            } else if (Objects.equals(features, "indv")){
                indv = intent.getStringExtra("indv");
                message1 = intent.getStringExtra("message");
                Log.d(TAG, indv);
            } else if (Objects.equals(features, "indv2")){
                indv = intent.getStringExtra("indv");
                message1 = intent.getStringExtra("message");
                Log.d(TAG, indv);
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
                    final String doubleInfo = dpt.concat("X").concat(message2);
                    sendMessage(SEND_MESSAGE_DPT, doubleInfo);
                }else if (Objects.equals(features, "dpt2")) {
                    final String doubleInfo = dpt.concat("X").concat(message2);
                    sendMessage(SEND_MESSAGE_DPT2, doubleInfo);
                }else if (Objects.equals(features, "indv")){
                    final String doubleInfo = indv.concat("X").concat(message1);
                    sendMessage(SEND_MESSAGE_INDV, doubleInfo);
                }else if (Objects.equals(features, "indv2")){
                    final String doubleInfo = indv.concat("X").concat(message1);
                    sendMessage(SEND_MESSAGE_INDV2, doubleInfo);
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

        if( messageEvent.getPath().equalsIgnoreCase( SEND_MESSAGE_ALL ) ) {
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, message);
            Intent intent = new Intent(this, PostServerService.class);
            Bundle extras = new Bundle();
            extras.putString("features", "all"); //all dpt indv
            extras.putString("code", message); //adam black blue brown
            intent.putExtras(extras);
            startService(intent);
        }else if ( messageEvent.getPath().equalsIgnoreCase( SEND_MESSAGE_DPT ) ) {
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, message);
            Intent intent = new Intent(this, PostServerService.class);
            Bundle extras = new Bundle();
            extras.putString("features", "dpt"); //all dpt indv
            extras.putString("dpt", message); //appliances, bath, electrical, flooring

            final String[] splitStringArray = message.split("X");
            String a = splitStringArray[0];
            String b = splitStringArray[1];

            extras.putString("dpt", a); //dana, jackson
            extras.putString("message", b);

            intent.putExtras(extras);
            startService(intent);
        }else if ( messageEvent.getPath().equalsIgnoreCase( SEND_MESSAGE_INDV ) ){
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, message);
            Intent intent = new Intent(this, PostServerService.class);
            Bundle extras = new Bundle();
            extras.putString("features", "indv"); //all dpt indv

            final String[] splitStringArray = message.split("X"); ///THIS WILL NOT WORK FOR SENTENCES
            String a = splitStringArray[0];
            String b = splitStringArray[1];

            extras.putString("indv", a); //dana, jackson
            extras.putString("message", b);

            intent.putExtras(extras);
            startService(intent);
        } else if ( messageEvent.getPath().equalsIgnoreCase( SEND_MESSAGE_INDV2 ) ) {
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, message);
            Intent intent = new Intent(this, PostServerService.class);
            Bundle extras = new Bundle();
            extras.putString("features", "indv2"); //all dpt indv

            final String[] splitStringArray = message.split("X"); ///THIS WILL NOT WORK FOR SENTENCES
            String a = splitStringArray[0];
            String b = splitStringArray[1];

            extras.putString("indv", a); //dana, jackson
            extras.putString("message", b);

            intent.putExtras(extras);
            startService(intent);

        }else if ( messageEvent.getPath().equalsIgnoreCase( SEND_MESSAGE_DPT2 ) ) {
            String message = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Log.d(TAG, message);
            Intent intent = new Intent(this, PostServerService.class);
            Bundle extras = new Bundle();
            extras.putString("features", "dpt2"); //all dpt indv

            final String[] splitStringArray = message.split("X");
            String a = splitStringArray[0];
            String b = splitStringArray[1];

            extras.putString("dpt", a); //dana, jackson
            extras.putString("message", b);

            intent.putExtras(extras);
            startService(intent);


        }else {
            super.onMessageReceived(messageEvent);

        }

    }

}
