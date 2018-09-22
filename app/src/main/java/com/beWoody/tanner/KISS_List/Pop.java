package com.beWoody.tanner.KISS_List;

import android.app.Activity;
import android.content.Intent;
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
    //Add Category from ACM.java and ACM.xml template;
    //Original function, so only named Pop...
    protected TaskerDBHelper db;
    CatAdapter adapt;
    List<Categories> list;
    Button button;
    int copyTasksFrom; //Flag passed from amt.java

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);

        Intent parentIntent = getIntent();
        Bundle parentBD     = parentIntent.getExtras();
        if (parentBD != null)
            copyTasksFrom = (int) parentBD.get("copyTasksOverFrom");
        else
            copyTasksFrom = -1;



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
                if(copyTasksFrom != -1){
                    //We were passed a `catId` to copy tasks over from
                    //This will copy incomplete tasks
                    int toId = db.countCategories(); //just added; TODO: isAppending check...
                    int fromId = copyTasksFrom;
                    Log.d("MEAT22", "COPYING FROM " + Integer.toString(fromId) +" to " + Integer.toString(toId));
                    db.copyIncompleteTasks(toId, fromId);
                    List <Tasker> foo = db.getAllNoncompletedTasks(toId);
                    for (int i=0; i<foo.size(); i++){
                        Log.d("MEAT44", "ITEM: `"+foo.get(i).getContent()+"`");
                    }
                }
                finish();
            }
        });
    }//end onCreate()

    public void addCatNow(View v){
        EditText t = findViewById(R.id.editText_categories);
        String s1  = t.getText().toString();
        if (s1.equalsIgnoreCase("")){
            Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG);
        }
        else {
            int categoryCount = db.countCategories();
            int place         = categoryCount + 1; //Always append the added item
            Categories cat    = new Categories(s1, place); //TODO: isAppending check...
            db.addCat(cat);
            t.setText("");
            adapt.add(cat);
            adapt.notifyDataSetChanged();
        }
        finish();
    }//end addTaskNow()

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }//end onBackPressed()

    @Override
    public void onDestroy(){
        db.close();
        super.onDestroy();
    }

}//end Pop()
