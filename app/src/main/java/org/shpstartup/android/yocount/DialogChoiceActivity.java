package org.shpstartup.android.yocount;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshgupta on 05/10/16.
 */
public class DialogChoiceActivity extends Activity {
    public static String RESULT_POSITION = "position";
    public static String RESULT_ID = "_id";
    public String[] countrynames;
    private TypedArray imgs;
    private List<CategoryChoice> countryList;
    private ListView listView;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.shpstartup.android.yocount.R.layout.dialog_layout2);
        populateCountryList();
        listView=(ListView) findViewById(org.shpstartup.android.yocount.R.id.lista);
        name=(TextView) findViewById(org.shpstartup.android.yocount.R.id.name);
        final String namex = getIntent().getExtras().getString("name");
        name.setText(namex.toUpperCase());
        ArrayAdapter<CategoryChoice> adapter = new DialogChoiceListArrayAdapter(this, countryList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int _id = getIntent().getExtras().getInt("_id");
                CategoryChoice c = countryList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_POSITION, position);
                returnIntent.putExtra(RESULT_ID,_id);
                returnIntent.putExtra(NumeroContract.NumeroColumns.NUMERO_CATEGORY,namex);
                setResult(RESULT_OK, returnIntent);
                imgs.recycle(); //recycle images
                finish();
            }
        });
    }

    private void populateCountryList() {
        countryList = new ArrayList<CategoryChoice>();
        countrynames = getResources().getStringArray(org.shpstartup.android.yocount.R.array.category_choice);
        String ab= countrynames[0];
        imgs = getResources().obtainTypedArray(org.shpstartup.android.yocount.R.array.category_flags);
        Drawable drawable = imgs.getDrawable(0);
        for(int i = 0; i < countrynames.length; i++){
            countryList.add(new CategoryChoice(countrynames[i], imgs.getDrawable(i)));
        }
    }

    public class CategoryChoice {
        private String name;
        private Drawable flag;
        public CategoryChoice(String name, Drawable flag){
            this.name = name;
            this.flag = flag;
        }
        public String getName() {
            return name;
        }
        public Drawable getFlag() {
            return flag;
        }
    }
}
