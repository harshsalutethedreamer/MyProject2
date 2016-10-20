package org.shpstartup.android.yocount;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by harshgupta on 10/09/16.
 */
public class NumeroRecyclerAdapter extends RecyclerView.Adapter<NumeroRecyclerViewHolder>{

    private Context ncontext;
    private List<InformationActivity> mnumeros;
    private static FragmentManager sFragmentManager;
    private ContentResolver mContentResolver;
    private Cursor mCursor;
    private int NUM_PAGES;
    private RecyclerView mRecyclerView;
    private MainActivity mainActivity;

    public NumeroRecyclerAdapter(MainActivity activity,Context ncontext, List<InformationActivity> mnumeros,FragmentManager fragmentManager,RecyclerView mRecyclerView) {
        this.ncontext = ncontext;
        this.mnumeros = mnumeros;
        sFragmentManager=fragmentManager;
        this.mRecyclerView=mRecyclerView;
        mainActivity=activity;
    }

    @Override
    public void onBindViewHolder(final NumeroRecyclerViewHolder holder, final int position) {

        Log.d("bindview",String.valueOf(mnumeros.get(position).getTotalcount()));
        String ab=mnumeros.get(position).getTopicname().substring(0,1).toUpperCase();
        holder.nname.setText(ab);
        holder.nname2.setText(mnumeros.get(position).getTopicname().toLowerCase().substring(1));
        holder.ndate.setText(mnumeros.get(position).getNdate());
        holder.ncount.setText(String.valueOf(mnumeros.get(position).getTotalcount()));
        holder.ncategorybox.setTag(String.valueOf(mnumeros.get(position).getId()));
        if(mnumeros.get(position).getNspecial()>0){
            holder.star.setImageResource(org.shpstartup.android.yocount.R.drawable.ic_star_black_24dp);
            holder.star.setTag("famous");
        }else{
            holder.star.setImageResource(R.drawable.ic_star_border_black_24dp);
            holder.star.setTag("notfamous");
        }

//        holder.ndeletebutton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                NumeroDialog dialog = new NumeroDialog();
//                Bundle args = new Bundle();
//                args.putString(NumeroDialog.DIALOG_TYPE, NumeroDialog.DELETE_RECORD);
//                args.putInt(NumeroContract.NumeroColumns.NUMERO_ID,mnumeros.get(position).getId());
//                args.putString(NumeroContract.NumeroColumns.NUMERO_CATEGORY,mnumeros.get(position).getTopicname().toUpperCase());
//                dialog.setArguments(args);
//                dialog.show(sFragmentManager,"delete-record");
//            }
//        });

//        holder.ncategorybox.OnLongClickListener

        final ImageView sta=holder.star;
        final TextView textView=holder.ncount;

        holder.choice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NumeroDialog dialog = new NumeroDialog();
//                Bundle args = new Bundle();
//                args.putString(NumeroDialog.DIALOG_TYPE, NumeroDialog.OPEN_CHOICE);
//                args.putInt(NumeroContract.NumeroColumns.NUMERO_ID,mnumeros.get(position).getId());
//                args.putString(NumeroContract.NumeroColumns.NUMERO_CATEGORY,mnumeros.get(position).getTopicname().toUpperCase());
//                dialog.setArguments(args);
//                dialog.show(sFragmentManager,"choice-by-user");
                final Intent intent = new Intent(ncontext, DialogChoiceActivity.class);
                intent.putExtra("_id",mnumeros.get(position).getId());
                intent.putExtra("name",mnumeros.get(position).getTopicname());
                ((Activity)ncontext).startActivityForResult(intent, 1);
            }
        });

        holder.star.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String fam =sta.getTag().toString();
                    if(fam=="famous"){
                        Integer _id = mnumeros.get(position).getId();
                        long ncreatedtime= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                        ContentValues values = new ContentValues();
                        values.put(NumeroContract.NumeroColumns.NUMERO_SPECIAL, 0);
                        values.put(NumeroContract.NumeroColumns.NUMERO_UPDATEDDATE, ncreatedtime);
                        Uri uri = NumeroContract.Numeros.buildFriendUri(String.valueOf(_id));
                        int recordsUpdated = mContentResolver.update(uri, values,null,null);
                        sta.setImageResource(org.shpstartup.android.yocount.R.drawable.ic_star_black_24dp);
                        sta.setTag("notfamous");
                        mainActivity.setAdapter(0);
                    }else if(fam=="notfamous"){
                        String[] projection = {BaseColumns._ID,
                                NumeroContract.NumeroColumns.NUMERO_SPECIAL};
                        String selection = NumeroContract.NumeroColumns.NUMERO_SPECIAL + " == 1";
                        String sortby = NumeroContract.NumeroColumns.NUMERO_UPDATEDDATE + " ASC";
                        mCursor = mContentResolver.query(NumeroContract.URI_TABLE, projection,selection, null, sortby);
                        NUM_PAGES = mCursor.getCount();
                        Log.d("CHECK",String.valueOf(NUM_PAGES));
                        if (mCursor != null) {
                            if (mCursor.getCount() > 4){
                                if (mCursor.moveToFirst()) {
                                        Integer _id = mCursor.getInt(
                                                mCursor.getColumnIndex(NumeroContract.NumeroColumns.NUMERO_ID));
                                        long ncreatedtime= TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
                                        ContentValues values = new ContentValues();
                                        values.put(NumeroContract.NumeroColumns.NUMERO_SPECIAL, 0);
                                        values.put(NumeroContract.NumeroColumns.NUMERO_UPDATEDDATE, ncreatedtime);
                                        Uri uri = NumeroContract.Numeros.buildFriendUri(String.valueOf(_id));
                                        int recordsUpdated = mContentResolver.update(uri, values, null, null);

                                }
                            }
                        }
                        Log.d("CHECK","DONE");
                        Integer _id = mnumeros.get(position).getId();
                        ContentValues values = new ContentValues();
                        values.put(NumeroContract.NumeroColumns.NUMERO_SPECIAL, 1);
                        Uri uri = NumeroContract.Numeros.buildFriendUri(String.valueOf(_id));
                        int recordsUpdated = mContentResolver.update(uri, values,null,null);
                        sta.setImageResource(org.shpstartup.android.yocount.R.drawable.ic_star_border_black_24dp);
                        sta.setTag("famous");
                        mainActivity.setAdapter(0);
                    }else{
                        Log.d("WRONG","something goes wrong");
                    }
            }
            });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int _id = mnumeros.get(position).getId();
                int mcountfind;
                mcountfind= Integer.valueOf(textView.getText().toString());
                Log.d("IDFIND",String.valueOf(_id));
                ContentValues values = new ContentValues();
                values.put(NumeroContract.NumeroColumns.NUMERO_COUNT, mcountfind+1);
                textView.setText(String.valueOf(mcountfind+1));
                Uri uri = NumeroContract.Numeros.buildFriendUri(String.valueOf(_id));
                int recordsUpdated = mContentResolver.update(uri, values, null, null);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int _id = mnumeros.get(position).getId();
                int mcountfind;
                mcountfind= Integer.valueOf(textView.getText().toString());
                if(_id!=0 && mcountfind>0) {
                    ContentValues values = new ContentValues();
                    values.put(NumeroContract.NumeroColumns.NUMERO_COUNT, mcountfind - 1);
                    textView.setText(String.valueOf(mcountfind - 1));
                    Uri uri = NumeroContract.Numeros.buildFriendUri(String.valueOf(_id));
                    int recordsUpdated = mContentResolver.update(uri, values, null, null);
                }
            }
        });

        holder.insidebox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("insidebox","coming");
                int _id = mnumeros.get(position).getId();
                Intent intent = new Intent(ncontext, OneCategoryActivity.class);
                intent.putExtra(OneCategoryActivity.ID, _id);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        // the context of the activity
                        mainActivity,
                        new Pair<View, String>(holder.insidebox,
                                ncontext.getString(R.string.transition_name))
                );
                ActivityCompat.startActivity(mainActivity, intent, options.toBundle());
            }
        });

    }

    @Override
    public NumeroRecyclerViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(org.shpstartup.android.yocount.R.layout.n_one,null);
        NumeroRecyclerViewHolder numeroRecyclerViewHolder=new NumeroRecyclerViewHolder(view);
        mContentResolver=ncontext.getContentResolver();

        return numeroRecyclerViewHolder;
    }

    @Override
    public int getItemCount() {
        return mnumeros.size();
    }

}
