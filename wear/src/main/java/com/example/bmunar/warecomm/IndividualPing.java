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

public class IndividualPing extends Activity {
    private static final String TAG = "CreateMessage";
    private String features;
    private String indv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_ping);

        ImageView iv = (ImageView)findViewById(R.id.individualPingImage);
        iv.setImageResource(R.drawable.individualping);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null) {
            features = intent.getStringExtra("features");
            indv = intent.getStringExtra("indv");
            Log.d(TAG, features);
            Log.d(TAG, indv);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_individual_ping, menu);
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

    public void individualPing(View view){
        Log.d(TAG, "individualPing");
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ListenerService.class);
        Bundle extras = new Bundle();
        extras.putString("features", features); //all dpt indv
        extras.putString("indv", indv); //appliances, bath, electrical, flooring
        intent.putExtras(extras);
        Log.d(TAG, features);
        Log.d(TAG, indv);

        startService(intent);


//        //CODE TO FORCE DEMO FOR NOTIFICATION
//        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(this, IndividualRequest.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//


    }
}
