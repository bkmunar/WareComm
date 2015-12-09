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

public class ReplyPing extends Activity {
    private static final String TAG = "ReplyPing";
    private String features;
    private String indv;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_ping);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null) {
            features = intent.getStringExtra("features");
            indv = intent.getStringExtra("indv");
            message = intent.getStringExtra("message");
            Log.d(TAG, features);
            Log.d(TAG, indv);
            Log.d(TAG, message);
        }

        //BRYAN THIS IS THE CODE THAT WILL TELL YOU WHAT IMAGE TO DISPLAY ON THIS ACTIVITY
        if (indv.equals("appliances")) {
            ImageView iv = (ImageView)findViewById(R.id.replyPingImage);
            iv.setImageResource(R.drawable.replyping);
        } else if (indv.equals("bath")) {
            ImageView iv = (ImageView)findViewById(R.id.replyPingImage);
            iv.setImageResource(R.drawable.replyping);
        } else if (indv.equals("electrical")) {
            ImageView iv = (ImageView)findViewById(R.id.replyPingImage);
            iv.setImageResource(R.drawable.replyping);
        } else {
            ImageView iv = (ImageView)findViewById(R.id.replyPingImage);
            iv.setImageResource(R.drawable.replyping);
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
        getMenuInflater().inflate(R.menu.menu_reply_ping, menu);
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

    public void replyPing(View view){
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show ();
        Intent intent = new Intent(this, ListenerService.class);

        Bundle extras = new Bundle();
        extras.putString("features", "indv"); //all dpt indv
        extras.putString("indv", "dana"); //dana, jackson
        extras.putString("message", message);
        intent.putExtras(extras);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void touchCancel(View view){
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, IndividualList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
