package org.shpstartup.android.yocount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class KnowAboutApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_about_app);

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
}
