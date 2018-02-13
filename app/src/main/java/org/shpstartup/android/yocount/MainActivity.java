package org.shpstartup.android.yocount;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ViewPager mViewPager;
    private PagerAdapter mPager;
    Bitmap bitmap1;
    private AsyncTask task;
    private String username,nickname;
    SharedPreferences pref;
    private Activity activity;
    private Fragment fragment;
    private MainFragment mainFragment;
    private FavouriteActivity favouriteActivity;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    int NUM_FILES=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mainFragment=new MainFragment();
        this.favouriteActivity=new FavouriteActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        activity=MainActivity.this;

        Toolbar toolbar = (Toolbar)findViewById(org.shpstartup.android.yocount.R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        actionBar.setDisplayShowTitleEnabled(false);

        ImageView toolbarsetting=(ImageView)findViewById(org.shpstartup.android.yocount.R.id.action_settings);
        toolbarsetting.setVisibility(View.VISIBLE);

        LinearLayout startlinearLayout=(LinearLayout)findViewById(org.shpstartup.android.yocount.R.id.linearstart);
        startlinearLayout.setOnClickListener(this);

        pref = getSharedPreferences("BasicUserDetail", MODE_PRIVATE);
        nickname= pref.getString("nickname", "");
        username= pref.getString("username", "");
        if(nickname != null && nickname != ""){
            TextView tusername = (TextView)findViewById(org.shpstartup.android.yocount.R.id.nickname);
            tusername.setText(nickname.toUpperCase());
        }

        mViewPager=(ViewPager)findViewById(org.shpstartup.android.yocount.R.id.pager);
        mPager=new SliderAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPager);
        mViewPager.setCurrentItem(1);

        final ImageView imageView=(ImageView)findViewById(org.shpstartup.android.yocount.R.id.action_settings);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.animate().rotation(360F).setDuration(1000);
                Intent i = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    public class SliderAdapter extends FragmentStatePagerAdapter{

        public SliderAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("working","check");
            if(position==0){
                return favouriteActivity;
                //return new FavouriteActivity();
            }else{
                return mainFragment;
//                return new MainFragment();
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    public void setAdapter(int position) {
        Log.d("mainsetadapter",String.valueOf(position));
        mainFragment.changeFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            int countryCode = data.getExtras().getInt(DialogChoiceActivity.RESULT_POSITION);
            int _id = data.getExtras().getInt(DialogChoiceActivity.RESULT_ID);
            String name = data.getExtras().getString(NumeroContract.NumeroColumns.NUMERO_CATEGORY);
            if(countryCode==0){
                Intent i = new Intent(MainActivity.this,EditActivity.class);
                i.putExtra("_id",_id);
                startActivity(i);
            }else if(countryCode==1){
                NumeroDialog dialog = new NumeroDialog();
                Bundle args = new Bundle();
                args.putString(NumeroDialog.DIALOG_TYPE, NumeroDialog.DELETE_RECORD);
                args.putInt(NumeroContract.NumeroColumns.NUMERO_ID,_id);
                args.putString(NumeroContract.NumeroColumns.NUMERO_CATEGORY,name.toUpperCase());
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(),"delete-record");
            }else if(countryCode==2){
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
                    String category_name = name.toLowerCase();
                    File folder = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
                    if (folder.exists()) {
                        mContentResolver = getContentResolver();
                        String selection = ImageContract.ImageColumns.IMAGECATEGORY_NAME + " == '"+category_name+"'";
                        mCursor = mContentResolver.query(ImageContract.URI_TABLE, null,selection,null,null);
                        NUM_FILES=mCursor.getCount();
                        if(NUM_FILES!=0){
                            Intent i = new Intent(MainActivity.this,GalleryActivity.class);
                            i.putExtra("category_name",category_name);
                            i.putExtra("_id",_id);
                            startActivity(i);
                        }else{
                            Toast.makeText(MainActivity.this, "No Image Exist", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "No Image Exist", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "External storage is not mounted READ/WRITE", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()== org.shpstartup.android.yocount.R.id.linearstart){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void onChangeFavouirteFragment(){
        Log.d("working","yesworking");
        favouriteActivity.changeFragment();
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.app_name_small);
    }

    public void playbutton(String name,int _id){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            String category_name = name.toLowerCase();
            File folder = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (folder.exists()) {
                mContentResolver = getContentResolver();
                String selection = ImageContract.ImageColumns.IMAGECATEGORY_NAME + " == '"+category_name+"'";
                mCursor = mContentResolver.query(ImageContract.URI_TABLE, null,selection,null,null);
                NUM_FILES=mCursor.getCount();
                if(NUM_FILES!=0){
                    Intent i = new Intent(MainActivity.this,GalleryActivity.class);
                    i.putExtra("category_name",category_name);
                    i.putExtra("_id",_id);
                    startActivity(i);
                }else{
                    Toast.makeText(MainActivity.this, "No Image Exist", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(MainActivity.this, "No Image Exist", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(MainActivity.this, "External storage is not mounted READ/WRITE", Toast.LENGTH_LONG).show();
        }
    }

}
