package com.beWoody.tanner.KISS_List;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class Pop_settings_colors extends Activity {
    protected UserDBHelper userdb;
    private String changethis;
    private User user;

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
        double width2 = width * 0.4;
        double height2 = height * 0.4;
        width = (int) width2;
        height = (int) height2;
        getWindow().setLayout(width, height);

        ScrollView scrollView = findViewById(R.id.scroll_colors);
        scrollView.setBackgroundColor(res.getColor(R.color.yellow_300, getTheme()));

        LinearLayout mLayout = findViewById(R.id.colorContainer);
        mLayout.setBackgroundColor(res.getColor(R.color.yellow_300, getTheme()));

        ArrayList<AvailableColors> colors = new ArrayList<AvailableColors>();
        colors.add(new AvailableColors("Apple",     "#8DB600"));
        colors.add(new AvailableColors("Aqua",      "#00FFFF"));
        colors.add(new AvailableColors("Barbie",    "#FF007F"));
        colors.add(new AvailableColors("Black",     "#000000"));
        colors.add(new AvailableColors("Blue",      "#0247FE"));
        colors.add(new AvailableColors("Bone",      "#E3DAC9"));
        colors.add(new AvailableColors("Brown",     "#654321"));
        colors.add(new AvailableColors("Fuschia",   "#FF00FF"));
        colors.add(new AvailableColors("Gold",      "#FFDF00"));
        colors.add(new AvailableColors("Gray",      "#676767"));
        colors.add(new AvailableColors("Lavender",  "#B57EDC"));
        colors.add(new AvailableColors("Orange",    "#FF6600"));
        colors.add(new AvailableColors("Purple",    "#5946B2"));
        colors.add(new AvailableColors("Red",       "#FF0000"));
        colors.add(new AvailableColors("Yellow",    "#FFFF00"));
        colors.add(new AvailableColors("White",     "#FFFFFF"));
        //colors.add(new AvailableColors("", "#"));

        for (int i=0; i<colors.size(); i++){
            AvailableColors __color = colors.get(i);
            String colorname        = __color.getColorname();
            final String colorhex   = __color.getHex();
            final int color         = Color.parseColor(colorhex);
            GradientDrawable circle = (GradientDrawable) res.getDrawable(R.drawable.circle, getTheme());
            TextView insertThis = new TextView(this);

            circle.setColor(color);
            circle.setStroke(2, Color.BLACK);

            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params1.setMargins(0, 8, 0, 8);
            params1.gravity = Gravity.CENTER;
            insertThis.setLayoutParams(params1);
            insertThis.setBackground(circle);
            insertThis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userdb.updateFontcolor(changethis, color);
                    finish();
                }
            });

            mLayout.addView(insertThis);
        }
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
