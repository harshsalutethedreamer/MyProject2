package org.shpstartup.android.yocount;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
public class ScreenSlideFavourityFragment extends Fragment {

    private int m_id,mposition=0,mtotal=0,_id=0,mcountfind=0;
    private Cursor mCursor;
    private ContentResolver mContentResolver;
    ImageView imageadd;
    private ImageView[] dots;
    private LinearLayout dotsLayout;
    private String category_name;
    private static final String CATEGORY="category_name";
    public static final int DIALOG_FRAGMENT = 2;
    private String mCurrentPhotoPath;
    private ImageView iv;
    SharedPreferences pref;

    // newInstance constructor for creating fragment with arguments
    public static ScreenSlideFavourityFragment newInstance(int total,int[] _gid,int position){
        ScreenSlideFavourityFragment screenSlideFavourityFragment = new ScreenSlideFavourityFragment();
        Bundle args = new Bundle();
        args.putInt("_id",_gid[position]);
        args.putInt("total",total);
        args.putInt("position",position);
        screenSlideFavourityFragment.setArguments(args);
        Log.d("positionv",String.valueOf(_gid[position]));
        return screenSlideFavourityFragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_id = getArguments().getInt("_id",0);
        mtotal = getArguments().getInt("total",0);
        mposition=getArguments().getInt("position",0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.n_two, container, false);


        if (Build.VERSION.SDK_INT >= 21) {
            Log.d("version","21");
        } else {
            Log.d("version","less than 21");
            // Implement this feature without material design
        }

        mContentResolver = getActivity().getContentResolver();

        imageadd = (ImageView) rootView.findViewById(org.shpstartup.android.yocount.R.id.imageadd);
        imageadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageadd.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_one));
                NumeroDialog dialog = new NumeroDialog();
                dialog.setTargetFragment(ScreenSlideFavourityFragment.this,DIALOG_FRAGMENT);
                Bundle args = new Bundle();
                args.putString(NumeroDialog.DIALOG_TYPE, NumeroDialog.IMAGE_SELECTION);
                args.putString(NumeroContract.NumeroColumns.NUMERO_CATEGORY,category_name);
                dialog.setArguments(args);
                dialog.show(getFragmentManager().beginTransaction(),"image_add");
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Log.d("category",category_name);
//                startActivityForResult(i, 1);
            }
        });
        imageadd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().setColorFilter(0 * 77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;

                    }
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        view.getDrawable().clearColorFilter();
                        break;
                    }
                }
                return false;
            }
        });

        TextView textView = (TextView) rootView.findViewById(org.shpstartup.android.yocount.R.id.n_name);

        final TextView textView1 = (TextView) rootView.findViewById(org.shpstartup.android.yocount.R.id.n_number);

        TextView textView2 = (TextView) rootView.findViewById(org.shpstartup.android.yocount.R.id.n_description);

        final ImageView addCount = (ImageView) rootView.findViewById(org.shpstartup.android.yocount.R.id.addCount);

        final ImageView subtractCount = (ImageView) rootView.findViewById(org.shpstartup.android.yocount.R.id.subtractCount);

        String selection = NumeroContract.NumeroColumns.NUMERO_ID + " == "+m_id;
        mCursor = mContentResolver.query(NumeroContract.URI_TABLE, null,selection, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                String category = mCursor.getString(
                        mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_CATEGORY));
                Log.d("valuex",String.valueOf(m_id));
                Log.d("valuex",category);
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



        addCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_id!=0) {
                    ContentValues values = new ContentValues();
                    values.put(NumeroContract.NumeroColumns.NUMERO_COUNT, mcountfind+1);
                    textView1.setText(String.valueOf(mcountfind+1));
                    Uri uri = NumeroContract.Numeros.buildFriendUri(String.valueOf(_id));
                    int recordsUpdated = mContentResolver.update(uri, values, null, null);
                    mcountfind=mcountfind+1;
                    ((MainActivity) getContext()).setAdapter(1);
                }
            }
        });

        subtractCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_id!=0 && mcountfind>0) {
                    ContentValues values = new ContentValues();
                    values.put(NumeroContract.NumeroColumns.NUMERO_COUNT, mcountfind-1);
                    textView1.setText(String.valueOf(mcountfind-1));
                    Uri uri = NumeroContract.Numeros.buildFriendUri(String.valueOf(_id));
                    int recordsUpdated = mContentResolver.update(uri, values, null, null);
                    mcountfind=mcountfind-1;
                    ((MainActivity) getContext()).setAdapter(1);
                }
            }
        });



        return rootView;
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

                Toast.makeText(getActivity().getApplication().getBaseContext(), "Your image has been saved to folder !" + category_name, Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getActivity().getApplication().getBaseContext(), "There was an error - please try again", Toast.LENGTH_LONG).show();


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
            Toast.makeText(getActivity().getApplication().getBaseContext(), "External storage Problem", Toast.LENGTH_SHORT).show();
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
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
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

            Toast.makeText(getActivity().getApplication().getBaseContext(), "Your image has been saved to folder !" + category_name, Toast.LENGTH_LONG).show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
