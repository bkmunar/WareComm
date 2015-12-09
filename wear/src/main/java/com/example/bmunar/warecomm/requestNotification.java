package com.example.bmunar.warecomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class requestNotification extends Activity {
    private static final String TAG = "requestNotification";
    private String features;
    private String dpt;
    private String message1;
    private String message2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_notification);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null) {
            features = intent.getStringExtra("features"); //all dpt indv
            dpt = intent.getStringExtra("dpt"); //appliances, bath, electrical, flooring
            message1 = intent.getStringExtra("message");
            Log.d(TAG, features);
            Log.d(TAG, dpt);
        }

        //BRYAN THIS IS THE CODE THAT WILL TELL YOU WHAT IMAGE TO DISPLAY ON THIS ACTIVITY
        if (dpt.equals("appliances")) {
            ImageView iv = (ImageView)findViewById(R.id.requestNotificationImage);
            iv.setImageResource(R.drawable.requestnotification);
        } else if (dpt.equals("bath")) {
            ImageView iv = (ImageView)findViewById(R.id.requestNotificationImage);
            iv.setImageResource(R.drawable.requestnotificationbath);
        } else if (dpt.equals("electrical")) {
            ImageView iv = (ImageView)findViewById(R.id.requestNotificationImage);
            iv.setImageResource(R.drawable.requestnotificationelectrical);
        } else {
            ImageView iv = (ImageView)findViewById(R.id.requestNotificationImage);
            iv.setImageResource(R.drawable.requestnotificationflooring);
        }

        Button button1 = (Button)findViewById(R.id.Cancel);
        button1.setX(73);
        button1.setY(170);
//        button1.setBackgroundColor(Color.TRANSPARENT);

        Button button2 = (Button)findViewById(R.id.Ping);
        button2.setX(145);
        button2.setY(170);
//        button2.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_request_notification, menu);
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

    public void confirmRequest(View view){
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ConfirmedNotification.class);
        Bundle extras = new Bundle();
        extras.putString("features", features); //all dpt indv
        extras.putString("dpt", dpt); //appliances, bath, electrical, flooring
        extras.putString("message", message2); //////////////////////////////MAKE MESSAGE EQUAL TO BUTTON
        intent.putExtras(extras);
        Log.d(TAG, features);
        Log.d(TAG, dpt);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void touchCancel(View view){
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DepartmentList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
