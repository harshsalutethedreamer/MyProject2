package org.shpstartup.android.yocount;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
    private static int NUM_PAGES = 0,NUM_TOTAL=0;;
    private String category_name;
    private File[] listFile=null;
    private int initialposition=0;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    private String[] FilePathStrings;
    private String[] FileNameStrings;
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
        NUM_TOTAL=bundle.getInt("total");
        Log.d("countingu",String.valueOf(NUM_TOTAL));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        mContentResolver = getContentResolver();
        File folder = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
        if (folder.exists()) {
            String selection = ImageContract.ImageColumns.IMAGECATEGORY_NAME + " == '"+category_name+"'";
            mCursor = mContentResolver.query(ImageContract.URI_TABLE, null,selection,null,null);
            NUM_PAGES=mCursor.getCount();

            if(NUM_PAGES>0 || mCursor!=null){
                if(folder.isDirectory()) {
                        int i =0;
                        FileNameStrings = new String[NUM_TOTAL];
                        FilePathStrings = new String[NUM_TOTAL];
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
            return new GalleryPager2Fragment().newInstance(NUM_TOTAL,position,category_name,FilePathStrings);
        }

        @Override
        public int getCount() {
            return NUM_TOTAL;
        }
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.app_name_small);
    }

}
