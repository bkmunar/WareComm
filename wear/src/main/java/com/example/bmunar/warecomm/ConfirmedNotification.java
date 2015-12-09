package com.example.bmunar.warecomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class ConfirmedNotification extends Activity {
    private static final String TAG = "ConfirmedNotification";
    private String features;
    private String dpt;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_notification);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null) {
            features = intent.getStringExtra("features"); //all dpt indv
            dpt = intent.getStringExtra("dpt"); //appliances, bath, electrical, flooring
            message = intent.getStringExtra("message");
            Log.d(TAG, features);
            Log.d(TAG, dpt);
        }

        //BRYAN THIS IS THE CODE THAT WILL TELL YOU WHAT IMAGE TO DISPLAY ON THIS ACTIVITY
        if (message.equals("Cancel")) {
            ImageView iv = (ImageView)findViewById(R.id.confirmedNotificationImage);
            iv.setImageResource(R.drawable.confirmednotification);
        } else {
            ImageView iv = (ImageView)findViewById(R.id.confirmedNotificationImage);
            iv.setImageResource(R.drawable.bryanisonhisway);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirmed_notification, menu);
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

    public void returnBack(View view){
        Intent intent = new Intent(this, DepartmentList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
