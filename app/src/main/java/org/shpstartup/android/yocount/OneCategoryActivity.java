package org.shpstartup.android.yocount;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by harshgupta on 23/09/16.
 */
public class OneCategoryActivity extends AppCompatActivity{

    private int m_id,mposition=0,mtotal=0,_id=0,mcountfind=0;
    public final static String ID="_id";
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    ImageView imageadd;
    private String category_name;
    private static final String CATEGORY="category_name";
    private String mCurrentPhotoPath;
    private ImageView iv;
    SharedPreferences pref;

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onecategory);

        Bundle bundle=getIntent().getExtras();
        m_id=bundle.getInt(ID);

        mContentResolver = getContentResolver();

        FloatingActionButton fab = (FloatingActionButton) findViewById(org.shpstartup.android.yocount.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
            }
        });

        pref = getSharedPreferences("BasicUserDetail", MODE_PRIVATE);
        String nickname= pref.getString("nickname", "");
        if(nickname != null && nickname != ""){
            TextView tusername = (TextView)findViewById(org.shpstartup.android.yocount.R.id.nickname);
            tusername.setText(nickname.toUpperCase());
        }

        TextView textView = (TextView) findViewById(R.id.n_name);

        final TextView textView1 = (TextView) findViewById(R.id.n_number);

        TextView textView2 = (TextView) findViewById(R.id.n_description);

        ImageView addCount = (ImageView) findViewById(R.id.addCount);
        ImageView subtractCount = (ImageView) findViewById(R.id.subtractCount);

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

        String selection = NumeroContract.NumeroColumns.NUMERO_ID + " == "+m_id;
        mCursor = mContentResolver.query(NumeroContract.URI_TABLE, null,selection, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                String category = mCursor.getString(
                        mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_CATEGORY));
                textView.setText(category.toUpperCase());
                category_name=category.toLowerCase();

                int count = mCursor.getInt(
                        mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_COUNT));
                textView1.setText(String.valueOf(count));
                mcountfind=count;

                int id = mCursor.getInt(
                        mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_ID));
                textView.setTag(id);
                _id=id;

                String description = mCursor.getString(
                        mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_DESCRIPTION));
                textView2.setText(description);

            }
            mCursor.close();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            Log.d("categoryx",category_name);

            AddImage(category_name,m_id,selectedImage);


        } else if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            Log.d("working","yeshereitis");
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, 1);
        }

        else if(requestCode == 3 && resultCode == Activity.RESULT_OK){
            dispatchTakePictureIntent(4);

        }else if(requestCode == 4 && resultCode == Activity.RESULT_OK){
            handleCameraPhoto();
        }

    }

    public void AddImage(String category_name,int m_id,Uri selectedImage){
        try {
            String realPath;
            if(category_name!=null && category_name!="" && m_id!=-1) {
                long ncreatedtime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                File folder = new File(Environment.getExternalStorageDirectory() + "/numeros/" + category_name);
                if (!folder.exists()) {
                    folder.mkdir();
                }

                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds=true;
                o.inSampleSize=6;

                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = mContentResolver.query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                realPath = cursor.getString(columnIndex);
                cursor.close();

                FileInputStream inputStream = new FileInputStream(realPath);
                BitmapFactory.decodeStream(inputStream,null,o);
                inputStream.close();

                //The new size we want to scale to
                final int REQUIRED_SIZE=75;

                //The new size we want to scale to
                int scale =1;
                while (o.outHeight/scale/2>=REQUIRED_SIZE && o.outWidth/scale/2>=REQUIRED_SIZE){
                    scale *=2;
                }
                Log.d("screensliderfavourityxx","aaa");

                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize=scale;
                inputStream = new FileInputStream(realPath);

                Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream,null,o2);

                String mPath = folder + "/" + ncreatedtime + ".jpg";
                File imageFile = new File(mPath);
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

                Toast.makeText(getApplication().getBaseContext(), "Your image has been saved to folder !" + category_name, Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplication().getBaseContext(), "There was an error - please try again", Toast.LENGTH_LONG).show();


        }
    }

    private File getAlbumDir() {
        File folder = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            folder = new File(Environment.getExternalStorageDirectory() + "/numeros/" + category_name);
            if (!folder.exists()) {
                folder.mkdir();
            }
        }else{
            Toast.makeText(getApplication().getBaseContext(), "External storage Problem", Toast.LENGTH_SHORT).show();
        }
        return folder;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        long ncreatedtime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        String imageFileName = String.valueOf(ncreatedtime);
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
        return imageF;
    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();
        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void dispatchTakePictureIntent(int actionCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File f = null;
            try {
                f = setUpPhotoFile();
                mCurrentPhotoPath = f.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            } catch (IOException e) {
                e.printStackTrace();
                f = null;
                mCurrentPhotoPath = null;
            }
            startActivityForResult(takePictureIntent, actionCode);
        }
    }

    private void handleCameraPhoto(){
        if (mCurrentPhotoPath != null) {
            galleryAddPic();
            mCurrentPhotoPath = null;
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

        try {

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;

            FileInputStream inputStream = new FileInputStream(f);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            //The new size we want to scale to
            final int REQUIRED_SIZE = 75;

            //The new size we want to scale to
            int scale = 1;
            while (o.outHeight / scale / 2 >= REQUIRED_SIZE && o.outWidth / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            inputStream = new FileInputStream(f);
            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream,null,o2);

            String mPath = mCurrentPhotoPath;
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            Toast.makeText(getApplication().getBaseContext(), "Your image has been saved to folder !" + category_name, Toast.LENGTH_LONG).show();
        }catch (IOException e){
            e.printStackTrace();
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
            View v1 = getWindow().getDecorView().getRootView();
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
}
