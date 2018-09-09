package com.beWoody.tanner.KISS_List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Pop_settings_colors extends Activity {
    protected UserDBHelper userdb;
    private String changethis;
    private User user;
    private int fontcolor;
    private int backgroundcolor;
    private int secondarycolor;
    private int listcolor;
    private Drawable _apple;    //1
    private Drawable _aqua;     //2
    private Drawable _barbie;   //3
    private Drawable _black;
    private Drawable _blue;
    private Drawable _brown;
    private Drawable _fuschia;
    private Drawable _gold;
    private Drawable _gray;     //9
    private Drawable _lavender; //10
    private Drawable _orange;
    private Drawable _purple;
    private Drawable _red;
    private Drawable _yellow;   //14

    protected void onCreate(Bundle savedInstanceState) {
        /*
         * Tanner 20180906
         * Activity to choose fontsize and to set the fontsize userdb;
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_settings_colors);
        Resources res = getResources();
        userdb = new UserDBHelper(this);
        Intent parentIntent = getIntent();
        Bundle parentBD = parentIntent.getExtras();
        if (parentBD != null)
            changethis = (String) parentBD.get("changethis");
        else
            changethis = "";


        DisplayMetrics dimensions = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dimensions);
        int width = dimensions.widthPixels;
        int height = dimensions.heightPixels;
        double width2 = width * 0.8;
        double height2 = height * 0.4;
        width = (int) width2;
        height = (int) height2;
        getWindow().setLayout(width, height);
        RelativeLayout foo = findViewById(R.id.layout_popup_settings_color);
        foo.setBackgroundColor(res.getColor(R.color.yellow_300, null));

        int apple = Color.parseColor("#8DB600");
        _apple      = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView applecolorcircle   = findViewById(R.id.color_apple);
        _apple.mutate().setColorFilter(apple, PorterDuff.Mode.SRC);
        applecolorcircle.setBackground(_apple);

        int aqua = Color.parseColor("#00FFFF");
        _aqua       = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView aquacolorcircle   = findViewById(R.id.color_aqua);
        _aqua.mutate().setColorFilter(aqua, PorterDuff.Mode.SRC);
        aquacolorcircle.setBackground(_aqua);

        int barbie = Color.parseColor("#FF007F");
        _barbie     = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());  //3
        TextView barbiecolorcircle   = findViewById(R.id.color_barbie);
        _barbie.mutate().setColorFilter(barbie, PorterDuff.Mode.SRC);
        applecolorcircle.setBackground(_barbie);

        int black = Color.parseColor("#000000");
        _black      = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView blackcolorcircle   = findViewById(R.id.color_black);
        _black.mutate().setColorFilter(black, PorterDuff.Mode.SRC);
        blackcolorcircle.setBackground(_black);

        int blue = Color.parseColor("#654321");
        _blue       = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView bluecolorcircle   = findViewById(R.id.color_blue);
        _blue.mutate().setColorFilter(blue, PorterDuff.Mode.SRC);
        bluecolorcircle.setBackground(_blue);

        int brown = Color.parseColor("#FF00FF");
        _brown      = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView browncolorcircle   = findViewById(R.id.color_brown);
        _brown.mutate().setColorFilter(brown, PorterDuff.Mode.SRC);
        browncolorcircle.setBackground(_brown);

        int fuschia = Color.parseColor("#");
        _fuschia    = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView fuschiacolorcircle   = findViewById(R.id.color_fuschia);
        _fuschia.mutate().setColorFilter(fuschia, PorterDuff.Mode.SRC);
        fuschiacolorcircle.setBackground(_fuschia);

        int gold = Color.parseColor("#FFDF00");
        _gold       = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView goldcolorcircle   = findViewById(R.id.color_gold);
        _gold.mutate().setColorFilter(gold, PorterDuff.Mode.SRC);
        goldcolorcircle.setBackground(_gold);

        int gray = Color.parseColor("#676767");
        _gray       = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView graycolorcircle   = findViewById(R.id.color_gray);
        _gray.mutate().setColorFilter(gray, PorterDuff.Mode.SRC);
        graycolorcircle.setBackground(_gray);

        int lavender = Color.parseColor("#B57EDC");
        _lavender   = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());  //10
        TextView lavendercolorcircle   = findViewById(R.id.color_lavender);
        _lavender.mutate().setColorFilter(lavender, PorterDuff.Mode.SRC);
        lavendercolorcircle.setBackground(_lavender);

        int orange = Color.parseColor("#FF6600");
        _orange     = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView orangecolorcircle   = findViewById(R.id.color_orange);
        _orange.mutate().setColorFilter(orange, PorterDuff.Mode.SRC);
        orangecolorcircle.setBackground(_orange);

        int purple = Color.parseColor("#5946B2");
        _purple     = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView purplecolorcircle   = findViewById(R.id.color_purple);
        _purple.mutate().setColorFilter(purple, PorterDuff.Mode.SRC);
        purplecolorcircle.setBackground(_purple);

        int red = Color.parseColor("#FF0000");
        _red        = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());
        TextView redcolorcircle   = findViewById(R.id.color_red);
        _red.mutate().setColorFilter(red, PorterDuff.Mode.SRC);
        redcolorcircle.setBackground(_red);

        int yellow = Color.parseColor("#FFFF00");
        _yellow     = ResourcesCompat.getDrawable(res, R.drawable.circle, getTheme());  //14
        TextView yellowcolorcircle   = findViewById(R.id.color_yellow);
        _yellow.mutate().setColorFilter(yellow, PorterDuff.Mode.SRC);
        yellowcolorcircle.setBackground(_yellow);


        /*
        large.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fontsize != 22)
                    userdb.updateFontsize(22);
                finish();
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }//end onBackPressed()

    @Override
    public void onDestroy(){
        userdb.close();
        super.onDestroy();
    }
}
