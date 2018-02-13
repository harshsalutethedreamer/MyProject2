package org.shpstartup.android.yocount;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by harshgupta on 10/09/16.
 */
public class NumerosProvider extends ContentProvider {
    private NumeroDatabase mOpenHelper;

    private static String TAG = NumerosProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int NUMEROS = 100;
    private static final int NUMEROS_ID = 101;

    private static final int IMAGECATEGORIES = 200;
    private static final int IMAGECATEGORIES_ID = 201;


    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NumeroContract.CONTENT_AUTHORITY;
        final String authority_image = ImageContract.CONTENT_AUTHORITY;
        matcher.addURI(authority,"yocount",NUMEROS);
        matcher.addURI(authority,"yocount/*",NUMEROS_ID);
        matcher.addURI(authority_image,"imagecount",IMAGECATEGORIES);
        matcher.addURI(authority_image,"imagecount/*",IMAGECATEGORIES_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        Log.d("ruworking","checking");
        mOpenHelper = new NumeroDatabase(getContext());
        return true;
    }

    private void deleteDatabase(){
        mOpenHelper.close();
        NumeroDatabase.deleteDatabase(getContext());
        mOpenHelper = new NumeroDatabase(getContext());
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case NUMEROS:
                return NumeroContract.Numeros.CONTENT_TYPE;
            case NUMEROS_ID:
                return NumeroContract.Numeros.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: "+uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);
        Log.d("imagecategories",String.valueOf(match));
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        SQLiteQueryBuilder queryBuilderx = new SQLiteQueryBuilder();
        queryBuilder.setTables(NumeroDatabase.Tables.NUMEROS);
        queryBuilderx.setTables(NumeroDatabase.Tables.IMAGECOUNT);
        switch (match){
            case NUMEROS:
                break;
            case NUMEROS_ID:
                String id = NumeroContract.Numeros.getFriendId(uri);
                queryBuilder.appendWhere(BaseColumns._ID+"="+id);
                break;
            case IMAGECATEGORIES:
                break;
            case IMAGECATEGORIES_ID:
                Log.d("imagecategories","isitcoming");
                String ida = ImageContract.ImageCategory.getFriendId(uri);
                queryBuilderx.appendWhere(BaseColumns._ID+"="+ida);
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        Cursor cursor;
        if(match==100 || match==101) {
            cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        }else{
            cursor = queryBuilderx.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG,"insert(uri=" + uri + ", values=" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match){
            case NUMEROS:
                long recordId = db.insertOrThrow(NumeroDatabase.Tables.NUMEROS, null , values);
                return NumeroContract.Numeros.buildFriendUri(String.valueOf(recordId));
            case IMAGECATEGORIES:
                long recordIdx = db.insertOrThrow(NumeroDatabase.Tables.IMAGECOUNT, null , values);
                return ImageContract.ImageCategory.buildFriendUri(String.valueOf(recordIdx));
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        Log.v(TAG,"update(uri=" + uri + ", values=" + contentValues.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final  int match = sUriMatcher.match(uri);
        String selectionCriteria= s;
        switch(match){
            case NUMEROS:
                //
                break;
            case NUMEROS_ID:
                String id= NumeroContract.Numeros.getFriendId(uri);
                selectionCriteria=BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(s)?" AND(" + s + ")" : "");
                        break;
            case IMAGECATEGORIES_ID:
                String mid= ImageContract.ImageCategory.getFriendId(uri);
                selectionCriteria=BaseColumns._ID + "=" + mid
                        + (!TextUtils.isEmpty(s)?" AND(" + s + ")" : "");
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }

        return db.update(NumeroDatabase.Tables.NUMEROS, contentValues, selectionCriteria, strings);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        Log.v(TAG,"delete(uri="+ uri);
        if(uri.equals(NumeroContract.URI_TABLE)){
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final  int match = sUriMatcher.match(uri);
        Log.d("DELETE",String.valueOf(match));

        switch(match){
            case NUMEROS_ID:
                String id= NumeroContract.Numeros.getFriendId(uri);
                String selectionCriteria = BaseColumns._ID+ "=" + id
                    + (!TextUtils.isEmpty(s)?" AND(" + s + ")" : "");
                return db.delete(NumeroDatabase.Tables.NUMEROS,selectionCriteria,strings);

            case IMAGECATEGORIES_ID:
                String mid= ImageContract.ImageCategory.getFriendId(uri);
                String mselectionCriteria = BaseColumns._ID+ "=" + mid
                        + (!TextUtils.isEmpty(s)?" AND(" + s + ")" : "");
                return db.delete(NumeroDatabase.Tables.IMAGECOUNT,mselectionCriteria,strings);

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }


}
