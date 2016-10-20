package org.shpstartup.android.yocount;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by harsh on 2/16/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    String string1;
    static final int DIALOG_ID=0;
    public static final String[] MONTHS = {"JANUARY", "FEBUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULLY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(),this, year, month, day);

    }



    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear=monthOfYear;
        String month_x=MONTHS[monthOfYear];
        if(dayOfMonth<10){
            string1="0"+dayOfMonth + "-" + month_x + "-" + year;
        }else{
            string1=dayOfMonth + "-" + month_x + "-" + year;
        }

        AddActivity activity = (AddActivity) getActivity();
        activity.addDate(string1);
//        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }



}
