package com.example.bmunar.warecomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class BroadcastNotification extends Activity {
    private static final String TAG = "BroadcastNotification";
    private String features;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_notification);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null) {
            features = intent.getStringExtra("features");
            code = intent.getStringExtra("code"); //adam black blue brown
            Log.d(TAG, features);
            Log.d(TAG, code);
        }

        // BRYAN THIS IS THE CODE THAT WILL TELL YOU WHAT IMAGE TO DISPLAY ON THIS ACTIVITY
        if (code.equals("Adam")) {
            ImageView iv = (ImageView) findViewById(R.id.broadcastNotificationImage);
            iv.setImageResource(R.drawable.broadcastnotification);
        } else if (code.equals("Black")) {
            ImageView iv = (ImageView) findViewById(R.id.broadcastNotificationImage);
            iv.setImageResource(R.drawable.broadcastnotificationpingblack);
        } else if (code.equals("Blue")) {
            ImageView iv = (ImageView) findViewById(R.id.broadcastNotificationImage);
            iv.setImageResource(R.drawable.broadcastnotificationpingblue);
        } else {
            ImageView iv = (ImageView) findViewById(R.id.broadcastNotificationImage);
            iv.setImageResource(R.drawable.broadcastnotificationpingbrown);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_broadcast_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // ADD ON TOUCH EVENT TO RETURN TO MAIN SCREEN
    public void backToHome(View view){
        Log.d(TAG, "selectIndividual");
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BroadcastCodes.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
