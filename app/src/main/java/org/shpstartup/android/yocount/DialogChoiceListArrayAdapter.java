package org.shpstartup.android.yocount;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by harshgupta on 05/10/16.
 */
public class DialogChoiceListArrayAdapter extends ArrayAdapter<DialogChoiceActivity.CategoryChoice> {

    private final List<DialogChoiceActivity.CategoryChoice> list;
    private final Activity context;

    static class ViewHolder {
        protected TextView name;
        protected ImageView flag;
    }

    public DialogChoiceListArrayAdapter(Activity context, List<DialogChoiceActivity.CategoryChoice> list) {
        super(context, org.shpstartup.android.yocount.R.layout.activity_countrycode_row, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(org.shpstartup.android.yocount.R.layout.activity_countrycode_row, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(org.shpstartup.android.yocount.R.id.name);
            viewHolder.flag = (ImageView) view.findViewById(org.shpstartup.android.yocount.R.id.flag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.name.setText(list.get(position).getName());
        holder.flag.setImageDrawable(list.get(position).getFlag());
        return view;
    }
}

