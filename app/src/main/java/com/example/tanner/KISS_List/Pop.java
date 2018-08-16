package com.example.tanner.KISS_List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Pop extends Activity{
    protected TaskerDBHelper db;
    CatAdapter adapt;
    List<Categories> list;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        DisplayMetrics dimensions = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimensions);
        int width      = dimensions.widthPixels;
        int height     = dimensions.heightPixels;
        double width2  = width  * 0.8;
        double height2 = height * 0.4;
        width          = (int) width2;
        height         = (int) height2;
        getWindow().setLayout(width, height);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        db     = new TaskerDBHelper(this);
        list   = db.getAllCategories();
        adapt  = new CatAdapter(this, R.layout.list_categories, list);
        EditText foo = (EditText) findViewById(R.id.editText_categories);

        foo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addCatNow(v);
                    return true;
                }
                return false;
            }
        });
        button = findViewById(R.id.button_addCategoryToDB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCatNow(v);
                finish();
            }
        });
    }//end onCreate()

    public void addCatNow(View v){
        Resources res = getResources();
        boolean debug = res.getBoolean(R.bool.debug);
        EditText t    = findViewById(R.id.editText_categories);
        String s1     = t.getText().toString();
        if (s1.equalsIgnoreCase("")){
            Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG);
        }
        else {
            int categoryCount = db.countCategories();
            int place         = categoryCount + 1; //Always append the added item
            Categories cat    = new Categories(s1, place);
            db.addCat(cat);
            Log.d("cat", s1 + " added");
            t.setText("");
            adapt.add(cat);
            adapt.notifyDataSetChanged();
        }
        finish();
    }//end addTaskNow()
}//end Pop()
