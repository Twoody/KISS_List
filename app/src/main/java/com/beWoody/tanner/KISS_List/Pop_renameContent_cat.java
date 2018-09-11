package com.beWoody.tanner.KISS_List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Pop_renameContent_cat extends Activity{
    /*
    *  Tanner 201808??
    *  Rename Category;
    *  Opened via AMC.java and contextmenu;
    */
    protected TaskerDBHelper db;
    Button button;
    int catId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rename_cat);

        //Get `taskId` from activity_manage_tasks back;
        Intent parentIntent = getIntent();
        Bundle parentBD = parentIntent.getExtras();
        if (parentBD != null)
            catId = (int) parentBD.get("catId");
        else
            catId = 0;

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

        db                 = new TaskerDBHelper(this);
        Categories thisCat = db.getCategories(catId);
        button             = findViewById(R.id.button_updateCatContent);
        EditText input     = (EditText) findViewById(R.id.editText_renameCat);

        input.setText(thisCat.getCategory());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                renameCategoryNow(v);
                finish();
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    renameCategoryNow(v);
                    return true;
                }
                return false;
            }
        });
    }//end onCreate()

    public void renameCategoryNow(View v){
        EditText t         = findViewById(R.id.editText_renameCat);
        String newCategory = t.getText().toString();
        if (newCategory.equalsIgnoreCase("") )
            Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG);
        else{
            db.updateCatCategory(catId, newCategory);
            t.setText("");
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

}