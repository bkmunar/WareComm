package com.example.bmunar.warecomm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class DepartmentList extends Activity {
    private static final String TAG = "DepartmentList";

    private float x1,x2;
    static final int MIN_DISTANCE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deparment_list);

        ImageView iv = (ImageView)findViewById(R.id.departmentListImage);
        iv.setImageResource(R.drawable.departmentlist);

        Button button1 = (Button)findViewById(R.id.Appliances);
        button1.setX(35);
        button1.setY(55);
//        button1.setBackgroundColor(Color.TRANSPARENT);

        Button button2 = (Button)findViewById(R.id.Bath);
        button2.setX(135);
        button2.setY(55);
//        button2.setBackgroundColor(Color.TRANSPARENT);

        Button button3 = (Button)findViewById(R.id.Electrical);
        button3.setX(35);
        button3.setY(155);
//        button3.setBackgroundColor(Color.TRANSPARENT);

        Button button4 = (Button)findViewById(R.id.Flooring);
        button4.setX(135);
        button4.setY(155);
//        button4.setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_deparment_list, menu);
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

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;

                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // Left to Right swipe action
                    if (x2 < x1)
                    {
                        Toast.makeText(this, "Right to Left", Toast.LENGTH_SHORT).show ();
                        Intent intent = new Intent(this, IndividualList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }

                    // Right to left swipe action
                    else
                    {
                        Toast.makeText(this, "Left to Right", Toast.LENGTH_SHORT).show ();
                        Intent intent = new Intent(this, BroadcastCodes.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void selectDepartment(View view){
        Log.d(TAG, "selectDepartment");

        Toast.makeText(this, "Tap", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DepartmentPing.class);

        Bundle extras = new Bundle();
        extras.putString("features", "dpt"); //all dpt indv
        extras.putString("dpt", ((Button) view).getText().toString());
        intent.putExtras(extras);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
