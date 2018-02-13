package org.shpstartup.android.yocount;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

public class FavouriteActivity extends Fragment{

    private static int NUM_PAGES = 0;
    private int[] _id;
    private VerticalViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    private int in=0;
    private String TAG;

    public interface MyFavouriteCall{
        void myfavouritecallaction();
    }

    private MyFavouriteCall myFavouriteCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TAG=FavouriteActivity.class.getSimpleName();

        ViewGroup rootView = (ViewGroup) inflater.inflate(org.shpstartup.android.yocount.R.layout.activity_screen_slide, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(org.shpstartup.android.yocount.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Instantiate a ViewPager and a PagerAdapter.
        Log.d("inworking",String.valueOf(in));
        if(in>0){
            mPagerAdapter.notifyDataSetChanged();
            mPager.invalidate();
        }else{
            in=in+1;
            mPager = (VerticalViewPager) view.findViewById(org.shpstartup.android.yocount.R.id.pager);
            mPagerAdapter = new ScreenSlidePagerAdapter(getChildFragmentManager());
            mPager.setAdapter(mPagerAdapter);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new ScreenSlideFavourityFragment().newInstance(NUM_PAGES,_id,position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            mContentResolver = getActivity().getContentResolver();
            String[] projection = {BaseColumns._ID,
                    NumeroContract.NumeroColumns.NUMERO_SPECIAL};
            String selection = NumeroContract.NumeroColumns.NUMERO_SPECIAL + " == 1";
            String order = NumeroContract.NumeroColumns.NUMERO_SPECIAL+" DESC,"+ NumeroContract.NumeroColumns.NUMERO_UPDATEDDATE+" DESC";
            mCursor = mContentResolver.query(NumeroContract.URI_TABLE, projection,null, null,order);
            NUM_PAGES = mCursor.getCount();
            if(NUM_PAGES !=0){
                if (mCursor != null) {
                    int i =0;
                    _id =new int[mCursor.getCount()];
                    if (mCursor.moveToFirst()) {
                        do {
                            int id = mCursor.getInt(
                                    mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_ID));
                            _id[i]=id;
                            i=i+1;
                        } while (mCursor.moveToNext());
                    }
                    mCursor.close();

                }
            }
            return NUM_PAGES;
        }
    }

    public void takeScreenshot() {
        long ncreatedtime= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        try {
            File folder = new File(Environment.getExternalStorageDirectory() + "/numeros");
            if (!folder.exists()) {
                folder.mkdir();
            }
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = folder + "/" + ncreatedtime + ".jpg";

            // create bitmap screen capture
            View v1 = getActivity().getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            openScreenshot(imageFile);

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        Uri uri = Uri.fromFile(imageFile);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        intent.setDataAndType(uri, "image/*");
        intent.setType("image/jpeg");
        startActivity(intent);
    }

    public void changeFragment(){
        Log.d(TAG,"changeFragment");
        mPagerAdapter.notifyDataSetChanged();
    }

}
