package com.beWoody.tanner.KISS_List;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
     *  TODO:
     *       0. Everything
     */
    protected UserDBHelper userdb;
    private int fontsize;
    private String font;
    private int fontcolor;
    private int backgroundcolor;
    private int secondarycolor;
    private int listcolor;
    private int isAppending;           //bool as int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        userdb          = new UserDBHelper(this);
        User user       = userdb.getUser();
        font            = user.getFont();
        fontsize        = user.getFontsize();
        fontcolor       = user.getFontcolor();
        secondarycolor  = user.getColorSecondary();
        backgroundcolor = user.getColorPrimary();
        listcolor       = user.getListcolor();
        isAppending     = user.getIsAppending();

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
                    toast += "New items will be shown at the top";
                    isAppending = 0;
                }
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
                userdb.updateIsAppending(isAppending);
            }
        });
        Typeface face = Typeface.create(font, Typeface.NORMAL);
        TextView currentfont = findViewById(R.id.textView_currentFont);
        currentfont.setText(font);
        currentfont.setTypeface(face);

        Button changefont = findViewById(R.id.button_chooseFont);
        changefont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "COMING SOON!", Toast.LENGTH_LONG).show();
            }
        });

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }
}
