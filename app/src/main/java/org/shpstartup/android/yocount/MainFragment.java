package org.shpstartup.android.yocount;


import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<InformationActivity>> {

    private RecyclerView mRecyclerView;
    private NumeroRecyclerAdapter numeroRecyclerAdapter;
    private ContentResolver mContentResolver;
    private List<InformationActivity> mnumeros;
    private static int LOADER_ID=1;
    private static int LOADER_IDA = 2;
    private String matchText="";
    private EditText mSearchEditText;
    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    FloatingActionButton fab;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView= (ViewGroup) inflater.inflate(org.shpstartup.android.yocount.R.layout.activity_main2,container,false);

        relativeLayout=(RelativeLayout) rootView.findViewById(R.id.relativeLayout_introduction);

        mSearchEditText = (EditText) rootView.findViewById(org.shpstartup.android.yocount.R.id.search);
        mSearchEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    matchText=mSearchEditText.getText().toString();
                    getActivity().getSupportLoaderManager().initLoader(LOADER_ID++, null,MainFragment.this);
                    return true;
                }
                return false;
            }
        });

        mRecyclerView=(RecyclerView) rootView.findViewById(org.shpstartup.android.yocount.R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        fab = (FloatingActionButton) rootView.findViewById(org.shpstartup.android.yocount.R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),AddActivity.class);
                startActivity(i);
            }
        });

        linearLayout = (LinearLayout) rootView.findViewById(R.id.linearlayout);

        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null,this);
        return rootView;
    }

    @Override
    public Loader<List<InformationActivity>> onCreateLoader(int id, Bundle args) {
        mContentResolver=getActivity().getContentResolver();
        Log.d("valuex",String.valueOf(id));
        if(id==1){
            Log.d("working","changeFragmentohyear");
            return new NumeroListLoader(getActivity(),NumeroContract.URI_TABLE, mContentResolver);
        }else{
            return new NumerosSearchListLoader(getActivity(),NumeroContract.URI_TABLE,mContentResolver,matchText);
        }

    }

    @Override
    public void onLoadFinished(Loader<List<InformationActivity>> loader, List<InformationActivity> numeros) {
        mnumeros = numeros;
        Log.d("mumeros",String.valueOf(numeros.size()));
        if(numeros.size()>0) {
            relativeLayout.setVisibility(View.GONE);
            numeroRecyclerAdapter = new NumeroRecyclerAdapter((MainActivity) getActivity(),getActivity(), mnumeros,getActivity().getSupportFragmentManager(),mRecyclerView,(MainFragment) this);
            mRecyclerView.setAdapter(numeroRecyclerAdapter);
        }else{
         relativeLayout.setVisibility(View.VISIBLE);
        }

        Log.d("finding",String.valueOf(mnumeros.size()));
    }

    @Override
    public void onLoaderReset(Loader<List<InformationActivity>> loader) {

    }

    public void changeFragment(){
        Log.d("working","changeFragmentx");
        getActivity().getSupportLoaderManager().initLoader(LOADER_IDA++, null,MainFragment.this);
    }

//    public void onChangeFabVisiblity(){
//        fab.setVisibility(View.INVISIBLE);
//    }


}
