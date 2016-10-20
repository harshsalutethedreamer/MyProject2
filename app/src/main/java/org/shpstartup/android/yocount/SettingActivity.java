package org.shpstartup.android.yocount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    private TextView knowaboutapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.shpstartup.android.yocount.R.layout.activity_setting);

        Toolbar toolbar = (Toolbar)findViewById(org.shpstartup.android.yocount.R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        actionBar.setDisplayShowTitleEnabled(false);



        Button save = (Button) findViewById(org.shpstartup.android.yocount.R.id.save);
        final EditText nickname = (EditText) findViewById(org.shpstartup.android.yocount.R.id.nickname);

        knowaboutapp=(TextView) findViewById(R.id.knowaboutapp);
        knowaboutapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingActivity.this,KnowAboutApp.class);
                startActivity(i);
            }
        });

        final SharedPreferences pref=getSharedPreferences("BasicUserDetail", Context.MODE_PRIVATE);
        String nicknamex= pref.getString("nickname", "");
        nickname.setText(nicknamex);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reply="";
                if(nickname.getText().toString()!=null){
                    SharedPreferences.Editor edit=pref.edit();
                    edit.putString("nickname", nickname.getText().toString());
                    edit.commit();
                    reply ="Nickname:" + nickname.getText().toString();

                }
                Toast.makeText(SettingActivity.this,reply+" added successfully",Toast.LENGTH_SHORT).show();
            }
        });

        ImageView backbutton=(ImageView) findViewById(org.shpstartup.android.yocount.R.id.action_homereturnbutton);
        backbutton.setVisibility(View.VISIBLE);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final ImageView imageView=(ImageView)findViewById(org.shpstartup.android.yocount.R.id.action_settings);
        imageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
