package org.shpstartup.android.yocount.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.shpstartup.android.yocount.AlbumStorageDirFactory;
import org.shpstartup.android.yocount.BaseAlbumDirFactory;
import org.shpstartup.android.yocount.FroyoAlbumDirFactory;
import org.shpstartup.android.yocount.ImageContract;
import org.shpstartup.android.yocount.NumeroContract;
import org.shpstartup.android.yocount.NumeroDialog;
import org.shpstartup.android.yocount.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by harshgupta on 25/10/16.
 */
public class SubjectSingleDetailFragment extends Fragment {

    private int m_id,mposition=0,mtotal=0,_id=0,mcountfind=0;
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    private String category_name;
    private String mCurrentPhotoPath;
    public static final int DIALOG_FRAGMENT = 2;
    public static final int CAMERA_REQUEST_ACCESS=5;
    ImageView imageadd;
    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";
    private String imageFileName;
    public static int idvalue;

    public static SubjectSingleDetailFragment newInstance(int _id){
        idvalue=_id;
        return new SubjectSingleDetailFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode ==5) {
            Log.d("funnyway","hmmm");
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                NumeroDialog dialog = new NumeroDialog();
                dialog.setTargetFragment(SubjectSingleDetailFragment.this, DIALOG_FRAGMENT);
                Bundle args = new Bundle();
                args.putString(NumeroDialog.DIALOG_TYPE, NumeroDialog.IMAGE_SELECTION);
                args.putString(NumeroContract.NumeroColumns.NUMERO_CATEGORY, category_name);
                dialog.setArguments(args);
                dialog.show(getFragmentManager().beginTransaction(), "image_add");
            } else {

            }
            return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_click_mainfragment, container, false);

        ImageView addCount = (ImageView) rootView.findViewById(R.id.addCount);
        ImageView subtractCount = (ImageView) rootView.findViewById(R.id.subtractCount);
        addCount.setVisibility(View.INVISIBLE);
        subtractCount.setVisibility(View.INVISIBLE);

        m_id = idvalue;
        Log.d("mid",String.valueOf(m_id));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        mContentResolver = getActivity().getContentResolver();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot();
            }
        });

        TextView textView = (TextView) rootView.findViewById(R.id.n_name);

        final TextView textView1 = (TextView) rootView.findViewById(R.id.n_number);

        TextView textView2 = (TextView) rootView.findViewById(R.id.n_description);



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
                textView2.setText(description.toUpperCase());

            }
            mCursor.close();
        }

        imageadd = (ImageView) rootView.findViewById(R.id.imageadd);
        imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) || (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED)){
                    requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_ACCESS);
                }else {
                    imageadd.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_one));
                    NumeroDialog dialog = new NumeroDialog();
                    dialog.setTargetFragment(SubjectSingleDetailFragment.this, DIALOG_FRAGMENT);
                    Bundle args = new Bundle();
                    args.putString(NumeroDialog.DIALOG_TYPE, NumeroDialog.IMAGE_SELECTION);
                    args.putString(NumeroContract.NumeroColumns.NUMERO_CATEGORY, category_name);
                    dialog.setArguments(args);
                    dialog.show(getFragmentManager().beginTransaction(), "image_add");
                }
            }
        });

        return rootView;
    }

    public void AddImage(String category_name,int m_id,Uri selectedImage){
        try {
            String realPath;
            long ncreatedtime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            if(category_name!=null && category_name!="" && m_id!=-1) {

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
                File imageFile = new File(mCurrentPhotoPath);
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);
                if (mCurrentPhotoPath != null) {
                    galleryAddPicx();
                    mCurrentPhotoPath = null;
                }

                ContentValues values=new ContentValues();
                values.put(ImageContract.ImageColumns.IMAGECATEGORY_NAME, category_name);
                values.put(ImageContract.ImageColumns.IMAGENAME, imageFileName);
                values.put(ImageContract.ImageColumns.IMAGECATEGORY_CREATED_DATE,ncreatedtime);
                Uri returned= mContentResolver.insert(ImageContract.URI_TABLE,values);
                Toast.makeText(getActivity().getApplication().getBaseContext(), "Your image has been saved!", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getActivity().getApplication().getBaseContext(), "There was an error - please try again", Toast.LENGTH_LONG).show();


        }
    }

    private File getAlbumDir() {
        File storageDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir= mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());
            if (storageDir != null) {
                if (! storageDir.mkdirs()) {
                    if (! storageDir.exists()){
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }
        }else{
            Toast.makeText(getActivity().getApplication().getBaseContext(), "External storage Problem", Toast.LENGTH_SHORT).show();
        }
        return storageDir;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileNamex = JPEG_FILE_PREFIX + timeStamp + "_";
        File albumF = getAlbumDir();
        File imageF = File.createTempFile(imageFileNamex, JPEG_FILE_SUFFIX, albumF);
        imageFileName=imageF.toString().substring(imageF.toString().lastIndexOf("/")+1);
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File f = null;
            try {
                f = setUpPhotoFile();
                mCurrentPhotoPath = f.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                f = null;
                mCurrentPhotoPath = null;
            }

            if(f!=null){
                Uri photoUri= FileProvider.getUriForFile(getContext(),"com.example.android.fileprovider",f);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, actionCode);
            }

        }
    }

    private void handleCameraPhoto(){
        if (mCurrentPhotoPath != null) {
            galleryAddPic();
            mCurrentPhotoPath = null;
        }
    }

    private void galleryAddPic() {
        Log.d("gallerycheck","yesitis");
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);

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
            FileOutputStream outputStream = new FileOutputStream(f);
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);

            long ncreatedtime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            ContentValues values=new ContentValues();
            values.put(ImageContract.ImageColumns.IMAGECATEGORY_NAME, category_name);
            values.put(ImageContract.ImageColumns.IMAGENAME, imageFileName);
            values.put(ImageContract.ImageColumns.IMAGECATEGORY_CREATED_DATE,ncreatedtime);
            Uri returned= mContentResolver.insert(ImageContract.URI_TABLE,values);
            Toast.makeText(getActivity().getApplication().getBaseContext(), "Your image has been saved to folder !", Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void takeScreenshot() {
        long ncreatedtime= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        try {
            File folder = new File(Environment.getExternalStorageDirectory() + "/yocount");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("coming","reply");

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {

            Uri selectedImage = data.getData();

            Log.d("categoryx",category_name);

            AddImage(category_name,m_id,selectedImage);
        }

        else if(requestCode == DIALOG_FRAGMENT && resultCode == Activity.RESULT_OK){
            Intent takePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
            startActivityForResult(takePictureIntent, 1);
        }

        else if(requestCode == 3 && resultCode == Activity.RESULT_OK){
            dispatchTakePictureIntent(4);

        }else if(requestCode == 4 && resultCode == Activity.RESULT_OK){
            handleCameraPhoto();
        }else if(requestCode == 4){
            if(mCurrentPhotoPath!=null){
                File fdelete = new File(mCurrentPhotoPath);
                if(fdelete.exists()){
                    fdelete.delete();
                }
            }
        }

    }

    private void galleryAddPicx() {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.app_name_small);
    }
}
