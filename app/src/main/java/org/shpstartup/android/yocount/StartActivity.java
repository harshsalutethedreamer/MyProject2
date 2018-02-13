package org.shpstartup.android.yocount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.shpstartup.android.yocount.R.layout.activity_start);
//        setContentView(R.layout.activity_setting);
        //  Declare a new thread to do a preference check
        //  Initialize SharedPreferences


        pref = getSharedPreferences("StartingScreen", MODE_PRIVATE);

        //  Create a new boolean and preference and set it to true
        boolean isFirstStart = pref.getBoolean("firstStart", true);

        //  If the activity has never started before...
        if (isFirstStart) {

            //  Launch app intro
            Intent i = new Intent(StartActivity.this, WelcomeActivity.class);
            startActivity(i);
            finish();

            //  Make a new preferences editor
            SharedPreferences.Editor e = pref.edit();

            //  Edit preference to make it false because we don't want this to run again
            e.putBoolean("firstStart", false);

            //  Apply changes
            e.commit();
        }else{
            Intent i = new Intent(StartActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
