package org.shpstartup.android.yocount;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    private static final String JPEG_FILE_SUFFIX = ".jpg";
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        Bundle bundle=getIntent().getExtras();

        mContentResolver = getContentResolver();
        category_name=bundle.getString("category_name");
        _id=bundle.getInt("_id");
        File folder = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
        if (folder.exists()) {
            String selection = ImageContract.ImageColumns.IMAGECATEGORY_NAME + " == '"+category_name+"'";
            mCursor = mContentResolver.query(ImageContract.URI_TABLE, null,selection,null,null);
            int NUM_FILES=mCursor.getCount();
            Log.d("imagecount",String.valueOf(NUM_FILES));
            if(folder.isDirectory()) {
                    if (mCursor != null) {
                        int i =0;
                        FileNameStrings = new String[NUM_FILES];
                        FilePathStrings = new String[NUM_FILES];
                        if (mCursor.moveToFirst()) {
                            do {
                                String  filename = mCursor.getString(
                                        mCursor.getColumnIndex(ImageContract.ImageColumns.IMAGENAME));

                                File f = new File(folder.toString()+"/"+filename);
                                Log.d("deleted",folder.toString()+"/"+filename);
                                if(f.exists()){
                                    FileNameStrings[i]=filename;
                                    FilePathStrings[i]=folder.toString()+"/"+FileNameStrings[i];
                                    Log.d("filepx",FilePathStrings[i]);
                                    i=i+1;
                                }else{
                                    /*Delete detail of Image*/
                                    Log.d("deleted","yesiamdoingmywork");
                                    int currentname_id=mCursor.getInt(
                                            mCursor.getColumnIndex(ImageContract.ImageColumns.IMAGECATEGORY_ID));
                                    Log.d("deleted",String.valueOf(currentname_id));
                                    Uri uri = ImageContract.ImageCategory.buildFriendUri(String.valueOf(currentname_id));
                                    mContentResolver.delete(uri,null,null);
                                }
                            } while (mCursor.moveToNext());
                        }
                        mCursor.close();

                        galleryRecylcerAdapter1 = new GalleryRecylcerAdapter1(GalleryActivity.this,FilePathStrings,FileNameStrings,category_name,i);
                        mRecyclerView.setAdapter(galleryRecylcerAdapter1);
                    }else{
                        Intent i = new Intent(GalleryActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();
                    }


                }
        }else{
            Intent i = new Intent(GalleryActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.app_name_small);
    }
}
