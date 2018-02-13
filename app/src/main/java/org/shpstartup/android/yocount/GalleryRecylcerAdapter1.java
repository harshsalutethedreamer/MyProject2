package org.shpstartup.android.yocount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

/**
 * Created by harshgupta on 01/10/16.
 */
public class GalleryRecylcerAdapter1 extends RecyclerView.Adapter<GalleryRecylcerViewHolder1>{

    private Context mcontext;
    private String[] mfilepathstrings,mfilepathnames;
    private int mposition,mi;
    private String mcatergory_name;

    public GalleryRecylcerAdapter1(Context context, String[] filepathstrings, String[] filepathnames,String catergory_name,int i){
        mcontext=context;
        mfilepathnames=filepathnames;
        mfilepathstrings=filepathstrings;
        mcatergory_name=catergory_name;
        mi=i;
    }

    @Override
    public GalleryRecylcerViewHolder1 onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(org.shpstartup.android.yocount.R.layout.gallery1, null);
        GalleryRecylcerViewHolder1 galleryRecylcerViewHolder1 = new GalleryRecylcerViewHolder1(view);
        return galleryRecylcerViewHolder1;
    }

    @Override
    public int getItemCount() {
            return mi;
    }

    @Override
    public void onBindViewHolder(GalleryRecylcerViewHolder1 holder, final int position) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mcontext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int targetW = (displaymetrics.widthPixels/3);
        mposition=position;

        /* Get the size of the image */
        Log.d("filepath",mfilepathstrings[position]);
        Picasso.with(mcontext).load("file:///"+mfilepathstrings[position])
                .resize(targetW,targetW)
                .centerCrop()
                .error(org.shpstartup.android.yocount.R.drawable.placeholder)
                .placeholder((org.shpstartup.android.yocount.R.drawable.placeholder))
                .into(holder.photo);


        holder.setClickListener(new GalleryRecylcerViewHolder1.ClickListener(){
            @Override
            public void onClick(View v, int position, boolean isLongCLick) {
                Log.d("click","wow");
                if(isLongCLick){

                }else{
                    Log.d("click",String.valueOf(position));
                    Intent i = new Intent(mcontext,Gallery2Activity.class);
                    i.putExtra("position",position);
                    i.putExtra("category_name",mcatergory_name);
                    i.putExtra("total",mi);
                    mcontext.startActivity(i);
                }
            }
        });
    }
}
