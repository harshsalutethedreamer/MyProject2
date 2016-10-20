package org.shpstartup.android.yocount;

import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Created by harshgupta on 17/09/16.
 */
public class EditActivity extends AppCompatActivity {

    private final String LOG_TAG=EditActivity.class.getSimpleName();
    private TextView mNameTextView, mEmailTextView, mPhoneTextView;
    private int _id;
    private Button nButton;
    private ContentResolver mContentResolver;
    static final int DIALOG_ID=0;
    private EditText nname,ncount,ndescription,ndate;
    private String nreply;
    private long nupdatedtime;
    private Cursor mCursor;
    public static final String[] MONTHS = {"JANUARY", "FEBUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULLY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.shpstartup.android.yocount.R.layout.add_edit);

        Toolbar toolbar = (Toolbar)findViewById(org.shpstartup.android.yocount.R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        actionBar.setDisplayShowTitleEnabled(false);

        ImageButton dateselection = (ImageButton) findViewById(org.shpstartup.android.yocount.R.id.imageButton1);
        dateselection.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = new DatePicker2Fragment();
                        newFragment.show(getFragmentManager().beginTransaction(), "datePicker");

                    }
                }
        );

        ImageView backbutton=(ImageView) findViewById(org.shpstartup.android.yocount.R.id.action_homereturnbutton);
        backbutton.setVisibility(View.VISIBLE);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        final ImageView imageView=(ImageView)findViewById(org.shpstartup.android.yocount.R.id.action_settings);
        imageView.setVisibility(View.INVISIBLE);




//        getActionBar().setDisplayHomeAsUpEnabled(true);

        TextView addeditname=(TextView) findViewById(org.shpstartup.android.yocount.R.id.addcategoryname);
        addeditname.setText("Edit Category");

        nname=(EditText) findViewById(org.shpstartup.android.yocount.R.id.categoryname);

        ndescription=(EditText) findViewById(org.shpstartup.android.yocount.R.id.description);

        ncount=(EditText) findViewById(org.shpstartup.android.yocount.R.id.initialcount);
        ncount.setText("0", TextView.BufferType.EDITABLE);

        ndate=(EditText) findViewById(org.shpstartup.android.yocount.R.id.date);

        mContentResolver = EditActivity.this.getContentResolver();

        Bundle bundle=getIntent().getExtras();
        _id=bundle.getInt("_id");
        Log.d("valueid",String.valueOf(_id));
        final String ids=String.valueOf(_id);


        String selection = NumeroContract.NumeroColumns.NUMERO_ID + " == "+_id;
        mCursor = mContentResolver.query(NumeroContract.URI_TABLE, null,selection, null, null);
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                String category = mCursor.getString(
                        mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_CATEGORY));
                nname.setText(category);
                nname.setFilters(new InputFilter[] {
                        new InputFilter() {
                            public CharSequence filter(CharSequence src, int start,
                                                       int end, Spanned dst, int dstart, int dend) {
                                return src.length() < 1 ? dst.subSequence(dstart, dend) : "";
                            }
                        }
                });

                int count = mCursor.getInt(
                        mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_COUNT));
                ncount.setText(String.valueOf(count));

                String ndatec = mCursor.getString(
                        mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_DATE));
                ndate.setText(ndatec);

                String description = mCursor.getString(
                        mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_DESCRIPTION));
                ndescription.setText(description);
                ndescription.requestFocus();

            }
            mCursor.close();
        }

        nreply="Something goes wrong try again";

        nupdatedtime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

        nButton=(Button) findViewById(org.shpstartup.android.yocount.R.id.saveButton);
        nButton.setText("Save!");
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValid()){
                    ContentValues values=new ContentValues();
                    values.put(NumeroContract.NumeroColumns.NUMERO_CATEGORY, nname.getText().toString().toLowerCase());
                    values.put(NumeroContract.NumeroColumns.NUMERO_DESCRIPTION,ndescription.getText().toString());
                    values.put(NumeroContract.NumeroColumns.NUMERO_COUNT,Integer.valueOf(ncount.getText().toString()));
                    values.put(NumeroContract.NumeroColumns.NUMERO_DATE, ndate.getText().toString());
                    values.put(NumeroContract.NumeroColumns.NUMERO_UPDATEDDATE, nupdatedtime);
                    Uri uri =NumeroContract.Numeros.buildFriendUri(ids);
                    int recordupdated= mContentResolver.update(uri,values,null,null);
                    Intent intent = new Intent(EditActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),nreply, Toast.LENGTH_LONG).show();
                }
            }
        });

        LinearLayout linearLayout=(LinearLayout) findViewById(org.shpstartup.android.yocount.R.id.mainLayout);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }






    private boolean isValid(){
        if(nname.getText().toString().length() == 0 ||
                ncount.getText().toString().length() == 0||
                ndescription.getText().toString().length() == 0||
                ndate.getText().toString().length()==0){
            nreply="please ensure you have entered data in All Fields.";
            return false;
        }else{
            String[] projection = {BaseColumns._ID,
                    NumeroContract.NumeroColumns.NUMERO_CATEGORY};
            String selection = NumeroContract.NumeroColumns.NUMERO_CATEGORY + " = '" + nname.getText().toString().toLowerCase() + "'";
            mCursor = mContentResolver.query(NumeroContract.URI_TABLE, projection,selection, null, null);
            if(mCursor!=null){
                if(mCursor.getCount()>0){
                    return true;
                }else{
                    nreply="Nope this category does not exist";
                    return false;
                }
            }else{
                nreply="Some error try again";
                return false;
            }

        }
    }

    private boolean someDataEntered(){
        if(mNameTextView.getText().toString().length()>0 ||
            mPhoneTextView.getText().toString().length()>0||
                mEmailTextView.getText().toString().length()>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(AddActivity.this,MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
    }

    public void addDate(String mdate){
        ndate.setText(mdate);
    }


}
