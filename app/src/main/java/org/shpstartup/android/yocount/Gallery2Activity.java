package org.shpstartup.android.yocount;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class Gallery2Activity extends AppCompatActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static int NUM_PAGES = 0;
    private String category_name;
    private File[] listFile=null;
    private String[] FilePathStrings;
    private int initialposition=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.shpstartup.android.yocount.R.layout.activity_gallery2);

        Toolbar toolbar = (Toolbar)findViewById(org.shpstartup.android.yocount.R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        actionBar.setDisplayShowTitleEnabled(false);

        final ImageView imageView=(ImageView)findViewById(org.shpstartup.android.yocount.R.id.action_settings);
        imageView.setVisibility(View.INVISIBLE);

        ImageView backbutton=(ImageView) findViewById(org.shpstartup.android.yocount.R.id.action_homereturnbutton);
        backbutton.setVisibility(View.VISIBLE);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle bundle=getIntent().getExtras();
        category_name=bundle.getString("category_name");
        initialposition=bundle.getInt("position");

        Log.d("categorys",category_name);
        File folder = new File(Environment.getExternalStorageDirectory() + "/numeros/" + category_name);
        if (folder.exists()) {
            listFile = folder.listFiles();
            if(listFile!=null){
                if(folder.isDirectory()) {
                    // Instantiate a ViewPager and a PagerAdapter.
                    NUM_PAGES=listFile.length;
                    mPager = (ViewPager) findViewById(org.shpstartup.android.yocount.R.id.gallerypager);
                    mPagerAdapter = new GalleryPager2Adapter(getSupportFragmentManager());
                    mPager.setAdapter(mPagerAdapter);
                    mPager.setCurrentItem(initialposition);

                }
            }
            else{
                Intent i = new Intent(Gallery2Activity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }else{
            Intent i = new Intent(Gallery2Activity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private class GalleryPager2Adapter extends FragmentStatePagerAdapter {
        public GalleryPager2Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new GalleryPager2Fragment().newInstance(NUM_PAGES,position,category_name);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
