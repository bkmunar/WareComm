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

public class CreateReply extends Activity {
    private static final String TAG = "CreateReply";
    private String features;
    private String indv;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reply);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null) {
            features = intent.getStringExtra("features");
            indv = intent.getStringExtra("indv");
            Log.d(TAG, features);
            Log.d(TAG, indv);
        }

        //BRYAN THIS IS THE CODE THAT WILL TELL YOU WHAT IMAGE TO DISPLAY ON THIS ACTIVITY

        ImageView iv = (ImageView)findViewById(R.id.createReplyImage);
        iv.setImageResource(R.drawable.createreply);
        Button button1 = (Button)findViewById(R.id.Adam);
        button1.setX(35);
        button1.setY(55);
//        button1.setBackgroundColor(Color.TRANSPARENT);

        Button button2 = (Button)findViewById(R.id.Black);
        button2.setX(135);
        button2.setY(55);
//        button2.setBackgroundColor(Color.TRANSPARENT);

        Button button3 = (Button)findViewById(R.id.Blue);
        button3.setX(35);
        button3.setY(155);
//        button3.setBackgroundColor(Color.TRANSPARENT);

        Button button4 = (Button)findViewById(R.id.Brown);
        button4.setX(135);
        button4.setY(155);
//        button4.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_reply, menu);
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

    public void createReply(View view){
        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show ();
        Intent intent = new Intent(this, ReplyPing.class);

        Bundle extras = new Bundle();
        extras.putString("features", "indv"); //all dpt indv
        extras.putString("indv", "dana"); //dana, jackson

        message = "TEST";//REMOVE
        extras.putString("message", message); //////////////////////////////MAKE MESSAGE EQUAL TO BUTTON
        intent.putExtras(extras);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
