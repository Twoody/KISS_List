package com.beWoody.tanner.KISS_List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    /*
     *  Author:  Tanner Woody
     *  Date:    20180904
     *  Functionality:
     *      Change the font size
     *          SUPPORTED and TESTED.
     *          Open new activity to change font size.
     *          Display selected fontsize on activity#finish()
     *      Change the font typeface
     *          SUPPORTED AND 1/2 TESTED
     *              TEST on text throughout cat and task adapters;
     *          Open new activity to choose 1 of 5 fonts;
     *          Display selected font on activity#finish();
     *      Change the font color
     *      *          NOT SUPPORTED
     *      Change the background color
     *      *          NOT SUPPORTED
     *      Change the secondary colors
     *      *          NOT SUPPORTED
     *      change the list colors
     *      *          NOT SUPPORTED
     *      change the font colors
     *      *          NOT SUPPORTED
     *
     *  TODO:
     *      1. Colors activity
     *          One activity that will use `putextra` for which color to change;
     *          New activity should not allow already used colors in the list;
     */
    protected UserDBHelper userdb;
    private Resources res;

    private User user;
    private int fontsize;
    private String font;
    private int fontcolor;
    private int backgroundcolor;
    private int secondarycolor;
    private int listcolor;
    private int isAppending;            //bool as int;
    private TextView dFontsize;         //Font size display;
    private TextView dFont;             //Font face display;
    private Typeface face;              //Font typeface;

    private Drawable _primary;
    private Drawable _secondary;
    private Drawable _fontcolor;
    private Drawable _listcolor;

    private TextView primarycolorcircle;
    private TextView secondarycolorcircle;
    private TextView listcolorcircle;
    private TextView fontcolorcircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        res    = getResources();
        userdb = new UserDBHelper(this);

        Toolbar taskToolbar = (Toolbar) findViewById(R.id.toolbar_settings);
        setSupportActionBar(taskToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Text to be displayed in toolbar
        TextView toolbarTitle = (TextView) taskToolbar.findViewById(R.id.toolbar_settingsTitle);
        toolbarTitle.setText("Settings");

        setInterface(); //Set all of the colors and types as currently defined;

        final Switch switch_isappending = (Switch) findViewById(R.id.switch_isAppending);
        if (isAppending==1)
            switch_isappending.setChecked(true);
        switch_isappending.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String toast = "";
                switch_isappending.setChecked(b);
                if(b) {
                    toast += "Appending items to the end";
                    isAppending = 1;
                }
                else {
                    toast += "New items will be shown at the top -- COMING SOON!";
                    isAppending = 0;
                }
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
                userdb.updateIsAppending(isAppending);
            }
        });

        Button changefont = findViewById(R.id.button_chooseFont);
        changefont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Font Options", Toast.LENGTH_LONG).show();
                Intent popup = new Intent(Settings.this, Pop_settings_font.class);
                startActivity(popup);
            }
        });
        Button changefontsize = findViewById(R.id.button_fontsize);
        changefontsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Font Size Options", Toast.LENGTH_LONG).show();
                Intent popup = new Intent(Settings.this, Pop_settings_fontsize.class);
                startActivity(popup);
                //Should call onResume() next;
            }
        });

        primarycolorcircle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Open colorpicking activity;
                //Save instance of selected color there in userdb
                //close activity;
                Toast.makeText(getApplicationContext(), "BACKGROUND COLORS COMING SOON!", Toast.LENGTH_LONG).show();
                //Intent popup = new Intent(activity_manage_tasks.this, Pop_tasks.class);
                //popup.putExtra("category", category);
                //popup.putExtra("catId", catId);
                //startActivity(popup);
            }
        });
        secondarycolorcircle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Open colorpicking activity;
                //Save instance of selected color there in userdb
                //close activity;
                Toast.makeText(getApplicationContext(), "SECONDARY COLORS COMING SOON!", Toast.LENGTH_LONG).show();
                //Intent popup = new Intent(activity_manage_tasks.this, Pop_tasks.class);
                //popup.putExtra("category", category);
                //popup.putExtra("catId", catId);
                //startActivity(popup);
            }
        });
        fontcolorcircle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Open colorpicking activity;
                //Save instance of selected color there in userdb
                //close activity;
                Toast.makeText(getApplicationContext(), "FONT COLORS COMING SOON!", Toast.LENGTH_LONG).show();
                //Intent popup = new Intent(activity_manage_tasks.this, Pop_tasks.class);
                //popup.putExtra("category", category);
                //popup.putExtra("catId", catId);
                //startActivity(popup);
            }
        });
        listcolorcircle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Open colorpicking activity;
                //Save instance of selected color there in userdb
                //close activity;
                Toast.makeText(getApplicationContext(), "LIST COLORS COMING SOON!", Toast.LENGTH_LONG).show();
                //Intent popup = new Intent(activity_manage_tasks.this, Pop_tasks.class);
                //popup.putExtra("category", category);
                //popup.putExtra("catId", catId);
                //startActivity(popup);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        userdb.close();
        this.finish();
    }//end onBackPressed()

    @Override
    public void onDestroy(){
        userdb.close();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //To go to the previous activity in the stack
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume(){
        super.onResume();
        res = getResources();
        setInterface();
    }

    public void setInterface(){
        //Get updated user obj and fill user variables;
        user            = userdb.getUser();
        font            = user.getFont();
        fontsize        = user.getFontsize();
        fontcolor       = user.getFontcolor();
        secondarycolor  = user.getColorSecondary();
        backgroundcolor = user.getColorPrimary();
        listcolor       = user.getListcolor();
        isAppending     = user.getIsAppending();

        //Current typeface
        face  = Typeface.create(font, Typeface.NORMAL);
        dFont = findViewById(R.id.textView_currentFont);
        dFont.setText(font);
        dFont.setTypeface(face);

        //Current font size
        String curText = "Font size is ";
        if (fontsize == 14)
            curText += "small";
        else if (fontsize == 18)
            curText += "medium";
        else if (fontsize == 22)
            curText += "large";
        dFontsize = findViewById(R.id.textview_fontsize);
        dFontsize.setText(curText);
        dFontsize.setTypeface(Typeface.create(font, Typeface.NORMAL));
        dFontsize.setTextSize(fontsize);

        _primary   = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        _secondary = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        _fontcolor = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        _listcolor = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());

        primarycolorcircle   = findViewById(R.id.backgroundColorCircle);
        secondarycolorcircle = findViewById(R.id.secondaryColorCircle);
        listcolorcircle      = findViewById(R.id.listColorCircle);
        fontcolorcircle      = findViewById(R.id.fontColorCircle);


        //Current font color
        _fontcolor.mutate().setColorFilter(fontcolor, PorterDuff.Mode.SRC);
        fontcolorcircle.setBackground(_fontcolor);

        //Current background color
        _primary.mutate().setColorFilter(backgroundcolor, PorterDuff.Mode.SRC);
        primarycolorcircle.setBackground(_primary);

        //Current list color
        _listcolor.mutate().setColorFilter(listcolor, PorterDuff.Mode.SRC);
        listcolorcircle.setBackground(_listcolor);

        //Current secondary color
        _secondary.mutate().setColorFilter(secondarycolor, PorterDuff.Mode.SRC);
        secondarycolorcircle.setBackground(_secondary);
    }
}
