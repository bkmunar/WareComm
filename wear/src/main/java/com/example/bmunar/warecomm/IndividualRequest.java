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

public class IndividualRequest extends Activity {
    private static final String TAG = "IndividualRequest";
    private String features;
    private String indv;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_request);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null) {
            features = intent.getStringExtra("features");
            indv = intent.getStringExtra("indv");
            message = intent.getStringExtra("message");
            Log.d(TAG, features);
            Log.d(TAG, indv);
        }

        //BRYAN THIS IS THE CODE THAT WILL TELL YOU WHAT IMAGE TO DISPLAY ON THIS ACTIVITY
        if (message.equals("call me")) {
            ImageView iv = (ImageView)findViewById(R.id.individualRequestImage);
            iv.setImageResource(R.drawable.individualrequest);
        } else if (message.equals("needed at cs")) {
            ImageView iv = (ImageView)findViewById(R.id.individualRequestImage);
            iv.setImageResource(R.drawable.individualrequestcs);
        } else if (message.equals("needed at tools")) {
            ImageView iv = (ImageView)findViewById(R.id.individualRequestImage);
            iv.setImageResource(R.drawable.individualrequesttools);
        } else {
            ImageView iv = (ImageView)findViewById(R.id.individualRequestImage);
            iv.setImageResource(R.drawable.individualrequestregister);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_individual_request, menu);
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

    public void replyToRequest(View view){
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show ();
        Intent intent = new Intent(this, CreateReply.class);

        Bundle extras = new Bundle();
        extras.putString("features", "indv"); //all dpt indv
        extras.putString("indv", "dana"); //dana, jackson
        extras.putString("message", "message"); //dana, jackson
        intent.putExtras(extras);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
