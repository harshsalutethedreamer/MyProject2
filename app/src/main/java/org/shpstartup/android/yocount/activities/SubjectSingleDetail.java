package org.shpstartup.android.yocount.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.shpstartup.android.yocount.fragments.SubjectSingleDetailFragment;

/**
 * Created by harshgupta on 10/02/18.
 */

public class SubjectSingleDetail extends BaseFragmentActivity{
    @Override
    protected Fragment createFragment() {
        Bundle bundle=getIntent().getExtras();
        int _id=bundle.getInt("_id");
        return SubjectSingleDetailFragment.newInstance(_id);
    }
}
