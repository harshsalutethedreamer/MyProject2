package org.shpstartup.android.yocount;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by harshgupta on 24/09/16.
 */
public class NumeroDialog extends DialogFragment {
    public static final String DATE_SELECTION = "dateselect";
    public static final String DIALOG_TYPE = "command";
    public static final String DELETE_RECORD = "deleteRecord";
    public static final String IMAGE_SELECTION = "imageselection";
    public static final String OPEN_CHOICE = "choice";
    private LayoutInflater mLayoutInflater;
    public Dialog dialog;
    private ScreenSlideFavourityFragment screenSlideFavourityFragment;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        mLayoutInflater = getActivity().getLayoutInflater();

        String command = getArguments().getString(DIALOG_TYPE);
        final View view;
        if(command.equals(OPEN_CHOICE)){
            view = mLayoutInflater.inflate(org.shpstartup.android.yocount.R.layout.dialog_layout2, null);
        }else{
            view = mLayoutInflater.inflate(org.shpstartup.android.yocount.R.layout.dialog_layout, null);
        }
        if (command.equals(DELETE_RECORD)) {
            final int _id = getArguments().getInt(NumeroContract.NumeroColumns.NUMERO_ID);
            String name= getArguments().getString(NumeroContract.NumeroColumns.NUMERO_CATEGORY);
            TextView popupMessage = (TextView) view.findViewById(org.shpstartup.android.yocount.R.id.popup_message);
            popupMessage.setText("Are you sure you want to delete "+ name + " from your category list?");
            builder.setView(view).setPositiveButton("OK", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    Uri uri = NumeroContract.Numeros.buildFriendUri(String.valueOf(_id));
                    contentResolver.delete(uri,null,null);
                    Intent intent = new Intent(getActivity().getApplicationContext(),MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog,int which){

                        }
                    });
        }else if(command.equals(IMAGE_SELECTION)){
            builder.setTitle(org.shpstartup.android.yocount.R.string.take_photo)
                    .setItems(org.shpstartup.android.yocount.R.array.photo_type, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(which==0){
                                getTargetFragment().onActivityResult(3, Activity.RESULT_OK, getActivity().getIntent());
                            }else if(which==1){
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                            }
                        }
                    });
        }

        return builder.create();
    }


}
