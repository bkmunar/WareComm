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

public class CreateMessage extends Activity {
    private static final String TAG = "CreateMessage";
    private String features;
    private String indv;
    private int message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);

        ImageView iv = (ImageView)findViewById(R.id.createMessageImage);
        iv.setImageResource(R.drawable.createmessage);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras!=null) {
            features = intent.getStringExtra("features");
            indv = intent.getStringExtra("indv");
            Log.d(TAG, features);
            Log.d(TAG, indv);
        }

        Button button1 = (Button)findViewById(R.id.Adam);
        button1.setX(35);
        button1.setY(55);
        button1.setBackgroundColor(Color.TRANSPARENT);
        button1.setTextColor(Color.TRANSPARENT);

        Button button2 = (Button)findViewById(R.id.Black);
        button2.setX(135);
        button2.setY(55);
        button2.setBackgroundColor(Color.TRANSPARENT);
        button2.setTextColor(Color.TRANSPARENT);

        Button button3 = (Button)findViewById(R.id.Blue);
        button3.setX(35);
        button3.setY(155);
        button3.setBackgroundColor(Color.TRANSPARENT);
        button3.setTextColor(Color.TRANSPARENT);

        Button button4 = (Button)findViewById(R.id.Brown);
        button4.setX(135);
        button4.setY(155);
        button4.setBackgroundColor(Color.TRANSPARENT);
        button4.setTextColor(Color.TRANSPARENT);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_message, menu);
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

    public void createMessage(View view){
        Log.d(TAG, "createMessage");
        Intent intent = new Intent(this, IndividualPing.class);

        Bundle extras = new Bundle();
        extras.putString("features", "indv"); //all dpt indv
        extras.putString("indv", "dana"); //dana, jackson

        switch (((Button) view).getText().toString()) {
            case "call me": message = 1;
                break;
            case "needed at cs": message = 2;
                break;
            case "need you at tools": message = 3;
                break;
            default: message = 4;
        }
        extras.putString("message", Integer.toString(message));
        intent.putExtras(extras);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
