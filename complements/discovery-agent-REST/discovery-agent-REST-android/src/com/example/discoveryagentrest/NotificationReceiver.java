package com.example.discoveryagentrest;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NotificationReceiver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_result);
        TextView tv=(TextView)findViewById(R.id.textView1);
        tv.setText("After notification is clicked" );*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

}
