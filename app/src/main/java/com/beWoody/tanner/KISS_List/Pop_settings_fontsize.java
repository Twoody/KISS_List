package com.beWoody.tanner.KISS_List;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Pop_settings_fontsize extends Activity{
    protected UserDBHelper userdb;
    private User user;
    private int fontsize;
    private Button small;
    private Button med;
    private Button large;

    protected void onCreate(Bundle savedInstanceState) {
        /*
        * Tanner 20180906
        * Activity to choose fontsize and to set the fontsize userdb;
        */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_settings_fontsize);
        Resources res = getResources();
        userdb = new UserDBHelper(this);
        fontsize = userdb.getFontsize();

        DisplayMetrics dimensions = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimensions);
        int width = dimensions.widthPixels;
        int height = dimensions.heightPixels;
        double width2 = width * 0.8;
        double height2 = height * 0.25;
        width = (int) width2;
        height = (int) height2;
        getWindow().setLayout(width, height);
        RelativeLayout foo = findViewById(R.id.layout_popup_settings_fontsize);
        foo.setBackgroundColor(res.getColor(R.color.yellow_300, null));

        small = findViewById(R.id.button_fontSmall);
        med   = findViewById(R.id.button_fontMedium);
        large = findViewById(R.id.button_fontLarge);

        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontsize != 14)
                    userdb.updateFontsize(14);
                finish();
            }
        });
        med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontsize != 18)
                    userdb.updateFontsize(18);
                finish();
            }
        });
        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontsize != 22)
                    userdb.updateFontsize(22);
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
