package org.shpstartup.android.yocount;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by harshgupta on 10/09/16.
 */
public class NumeroRecyclerViewHolder extends RecyclerView.ViewHolder{
    protected TextView nname,ncount,ndescription,ndate,nname2;
    protected ImageView photogallery,star,choice,plus,minus,playbutton;
    protected LinearLayout ncategorybox,insidebox;

    public NumeroRecyclerViewHolder(View view){
        super(view);
        this.nname=(TextView) view.findViewById(R.id.first_alphabet_category);
        this.nname2=(TextView) view.findViewById(R.id.remaining_text_category);
        this.ndate=(TextView) view.findViewById(R.id.ncreatedate);
        this.ncount=(TextView) view.findViewById(R.id.display_count);
        this.star = (ImageView) view.findViewById(R.id.favourite_button);
        this.photogallery = (ImageView) view.findViewById(R.id.play_button);
        this.ncategorybox = (LinearLayout) view.findViewById(R.id.categorybox);
        this.choice = (ImageView) view.findViewById(R.id.more_options);
        this.plus=(ImageView) view.findViewById(R.id.plus_button_add);
        this.minus=(ImageView) view.findViewById(R.id.minus_button);
        this.playbutton=(ImageView) view.findViewById(R.id.play_button);
        this.insidebox=(LinearLayout) view.findViewById(R.id.insidebox);
    }

    public interface gotogallery{
        void gotogallery(String name);
    }
}
