package org.shpstartup.android.yocount;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by harshgupta on 01/10/16.
 */
public class GalleryRecylcerViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

    protected ImageView photo;
    private ClickListener clickListener;

    public GalleryRecylcerViewHolder1(View view){
        super(view);
        this.photo=(ImageView) view.findViewById(org.shpstartup.android.yocount.R.id.gallery1);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
    }

    public interface ClickListener {
        public void onClick(View v, int position, boolean isLongCLick);
    }

        public void setClickListener(ClickListener clickListener){
            this.clickListener=clickListener;
        }

    @Override
    public void onClick(View view) {
        clickListener.onClick(view,getAdapterPosition(),false);
    }

    @Override
    public boolean onLongClick(View view) {
        clickListener.onClick(view,getAdapterPosition(),true);
        return false;
    }
}