package org.shpstartup.android.yocount;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshgupta on 17/09/16.
 */
public class NumeroListLoader extends AsyncTaskLoader<List<InformationActivity>> {
    private static final String LOG_TAG=NumeroListLoader.class.getSimpleName();
    private List<InformationActivity> mNumeros;
    private ContentResolver mContentResolver;
    private Cursor mCursor;

    public NumeroListLoader(Context context, Uri uri, ContentResolver contentResolver){
        super(context);
        mContentResolver=contentResolver;
    }

    @Override
    public List<InformationActivity> loadInBackground() {
        String[] projection = {BaseColumns._ID,
                NumeroContract.NumeroColumns.NUMERO_CATEGORY,
                NumeroContract.NumeroColumns.NUMERO_DATE,
                NumeroContract.NumeroColumns.NUMERO_COUNT,
                NumeroContract.NumeroColumns.NUMERO_DESCRIPTION,
                NumeroContract.NumeroColumns.NUMERO_UPDATEDDATE,
                NumeroContract.NumeroColumns.NUMERO_SPECIAL};
        List<InformationActivity> entries = new ArrayList<InformationActivity>();

        //String order = NumeroContract.NumeroColumns.NUMERO_UPDATEDDATE+" DESC";
        mCursor = mContentResolver.query(NumeroContract.URI_TABLE, projection, null, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    int _id = mCursor.getInt(mCursor.getColumnIndex(BaseColumns._ID));
                    String category = mCursor.getString(
                            mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_CATEGORY));
                    String description = mCursor.getString(
                            mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_DESCRIPTION));
                    int count = mCursor.getInt(
                            mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_COUNT));
                    String date = mCursor.getString(
                            mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_DATE));
                    int special = mCursor.getInt(
                            mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_SPECIAL));
                    InformationActivity friend = new InformationActivity(_id,category,date,count,description,special);
                    entries.add(friend);
                } while (mCursor.moveToNext());
            }
        }
        return entries;
    }

    @Override
    public void deliverResult(List<InformationActivity> numeros) {
        if(isReset()){
            if(numeros != null){
                mCursor.close();
            }
        }

        List<InformationActivity> oldFriendList = mNumeros;
        if(mNumeros == null || mNumeros.size() == 0){
            Log.d(LOG_TAG, "++++++ No Data Returned");
        }
        mNumeros=numeros;
        if(isStarted()){
            super.deliverResult(numeros);
        }
        if(oldFriendList != null && oldFriendList != numeros){
            mCursor.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if(mNumeros!=null){
            deliverResult(mNumeros);
        }

        if(takeContentChanged() || mNumeros == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(mCursor != null){
            mCursor.close();
        }
        mNumeros = null;
    }

    @Override
    public void onCanceled(List<InformationActivity> data) {
        super.onCanceled(data);
        if(mCursor!= null){
            mCursor.close();
        }
    }

    @Override
    public void forceLoad() {
        super.forceLoad();
    }
}
