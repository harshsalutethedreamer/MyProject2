package org.shpstartup.android.yocount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by harshgupta on 10/09/16.
 */
public class NumeroDatabase extends SQLiteOpenHelper {
    private static final String TAG = NumeroDatabase.class.getSimpleName();
    private static final String DATABASE_NAME="yocount.db";
    private static final int DATABASE_VERSION = 3;
    private final Context mContext;

    interface Tables{
        String NUMEROS = "yocount";
        String IMAGECOUNT = "imagecount";
    }

    public NumeroDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext=context;
        Log.d("ruworking","checkingx");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("ruworking","checkingy");

        sqLiteDatabase.execSQL("CREATE TABLE "+ Tables.NUMEROS + " ("
                +BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
        + NumeroContract.NumeroColumns.NUMERO_CATEGORY+" TEXT NOT NULL,"
        + NumeroContract.NumeroColumns.NUMERO_DESCRIPTION+ " TEXT NOT NULL,"
        + NumeroContract.NumeroColumns.NUMERO_COUNT+ " INTEGER NOT NULL,"
        + NumeroContract.NumeroColumns.NUMERO_SPECIAL+ " INTEGER DEFAULT 0,"
        + NumeroContract.NumeroColumns.NUMERO_CREATEDDATE+ " INTEGER DEFAULT 0,"
        + NumeroContract.NumeroColumns.NUMERO_UPDATEDDATE+ " INTEGER DEFAULT 0,"
        + NumeroContract.NumeroColumns.NUMERO_DATE+ " TEXT NOT NULL)");

        sqLiteDatabase.execSQL("CREATE TABLE "+ Tables.IMAGECOUNT + " ("
                +BaseColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ImageContract.ImageColumns.IMAGECATEGORY_NAME+" TEXT NOT NULL,"
                + ImageContract.ImageColumns.IMAGENAME+ " TEXT NOT NULL,"
                + ImageContract.ImageColumns.IMAGECATEGORY_CREATED_DATE+ " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        int version=oldVersion;
        Log.d("findversion",String.valueOf(version));
//        if(version != DATABASE_VERSION){
//            Log.d("Hello","whatcontentprovider_xupgrade");
//            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ Tables.NUMEROS);
//            onCreate(sqLiteDatabase);
//        }
    }

    public static void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
