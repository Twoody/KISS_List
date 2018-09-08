package com.beWoody.tanner.KISS_List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Pop_settings_font extends Activity{
    protected UserDBHelper userdb;
    private String font;
    private Button casual;
    private Button cursive;
    private Button monospace;
    private Button roboto;
    private Button sansserif;

    protected void onCreate(Bundle savedInstanceState) {
        /*
         * Tanner 20180906
         * Activity to choose font and to set the font in userdb;
         * TODO:
         *      Go through available typefaces and programitatically make a list
         *          instead of using popup_settings_font template;
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_settings_font);
        Resources res = getResources();
        userdb = new UserDBHelper(this);
        font = userdb.getFont();

        DisplayMetrics dimensions = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimensions);
        int width = dimensions.widthPixels;
        int height = dimensions.heightPixels;
        double width2 = width * 0.8;
        double height2 = height * 0.4;
        width = (int) width2;
        height = (int) height2;
        getWindow().setLayout(width, height);
        RelativeLayout foo = findViewById(R.id.layout_popup_settings_font);
        foo.setBackgroundColor(res.getColor(R.color.yellow_300, null));

        casual    = findViewById(R.id.button_font_casual);
        cursive   = findViewById(R.id.button_font_cursive);
        monospace = findViewById(R.id.button_font_monospace);
        roboto    = findViewById(R.id.button_font_roboto);
        sansserif = findViewById(R.id.button_font_sans_serif);

        casual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdb.updateFont("casual");
                finish();
            }
        });
        cursive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdb.updateFont("cursive");
                finish();
            }
        });
        monospace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdb.updateFont("monospace");
                finish();
            }
        });
        roboto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdb.updateFont("roboto");
                finish();
            }
        });
        sansserif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdb.updateFont("sans-serif");
                finish();
            }
        });
    }

    @Override
    public void onDestroy(){
        userdb.close();
        super.onDestroy();
    }
}
