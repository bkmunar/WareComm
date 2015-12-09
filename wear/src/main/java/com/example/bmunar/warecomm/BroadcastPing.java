package com.example.bmunar.warecomm;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class BroadcastPing extends Activity {
    private static final String TAG = "BroadcastPing";
    private String features;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_ping);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null) {
            features = intent.getStringExtra("features");
            code = intent.getStringExtra("code"); //adam black blue brown
            Log.d(TAG, features);
            Log.d(TAG, code);
        }

        //BRYAN THIS IS THE CODE THAT WILL TELL YOU WHAT IMAGE TO DISPLAY ON THIS ACTIVITY
        if (code.equals("Adam")) {
            ImageView iv = (ImageView)findViewById(R.id.broadcastPingImage);
            iv.setImageResource(R.drawable.broadcastping);
        } else if (code.equals("Black")) {
            ImageView iv = (ImageView)findViewById(R.id.broadcastPingImage);
            iv.setImageResource(R.drawable.codeblackping);
        } else if (code.equals("Blue")) {
            ImageView iv = (ImageView)findViewById(R.id.broadcastPingImage);
            iv.setImageResource(R.drawable.codeblueping);
        } else {
            ImageView iv = (ImageView)findViewById(R.id.broadcastPingImage);
            iv.setImageResource(R.drawable.codebrownping);
        }

        Button button1 = (Button)findViewById(R.id.Cancel);
        button1.setX(73);
        button1.setY(170);
        button1.setBackgroundColor(Color.TRANSPARENT);
        button1.setTextColor(Color.TRANSPARENT);

        Button button2 = (Button)findViewById(R.id.Ping);
        button2.setX(145);
        button2.setY(170);
        button2.setBackgroundColor(Color.TRANSPARENT);
        button2.setTextColor(Color.TRANSPARENT);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_broadcast_ping, menu);
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

    public void broadcastPing(View view){
        Log.d(TAG, "broadcastPing");
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ListenerService.class);
        Bundle extras = new Bundle();
        extras.putString("features", features); //all dpt indv
        extras.putString("code", code); //adam black blue brown
        intent.putExtras(extras);
        Log.d(TAG, features);
        Log.d(TAG, code);

        startService(intent);

        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();
        Intent intent2 = new Intent(this, BroadcastCodes.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent2);

    }

    public void touchCancel(View view){
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BroadcastCodes.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
