package org.shpstartup.android.yocount;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class GalleryActivity extends AppCompatActivity {

    private String category_name;
    private int _id;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile=null;
    private RecyclerView mRecyclerView;
    private GalleryRecylcerAdapter1 galleryRecylcerAdapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(org.shpstartup.android.yocount.R.layout.activity_gallery);

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

        mRecyclerView=(RecyclerView) findViewById(org.shpstartup.android.yocount.R.id.galleryall_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));

        Bundle bundle=getIntent().getExtras();
        category_name=bundle.getString("category_name");
        _id=bundle.getInt("_id");
        File folder = new File(Environment.getExternalStorageDirectory() + "/numeros/" + category_name);
        if (folder.exists()) {
            listFile = folder.listFiles();
            if(listFile!=null){
                if(folder.isDirectory()) {
                    // Create a String array for FilePathStrings
                    FilePathStrings = new String[listFile.length];
                    // Create a String array for FileNameStrings
                    FileNameStrings = new String[listFile.length];
                    for (int i = 0; i < listFile.length; i++) {
                        // Get the path of the image file
                        FilePathStrings[i] = listFile[i].getAbsolutePath();
                        // Get the name image file
                        FileNameStrings[i] = listFile[i].getName();
                    }

                    galleryRecylcerAdapter1 = new GalleryRecylcerAdapter1(GalleryActivity.this,FilePathStrings,FileNameStrings,category_name);
                    mRecyclerView.setAdapter(galleryRecylcerAdapter1);
                }
                }
            else{
                Intent i = new Intent(GalleryActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }else{
            Intent i = new Intent(GalleryActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }
}
