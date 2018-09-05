package com.beWoody.tanner.KISS_List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDBHelper extends SQLiteOpenHelper {
    /*
    * public SQLiteOpenHelper (Context context,
                String name,
                SQLiteDatabase.CursorFactory factory,
                int version)
    *
    * TODO:
    *   1. TEST TEST TEST
    *       I just created this and have not up
    */
    private static final int DATABASE_VERSION    = 1;
    private static final String DATABASE_NAME    = "userDB";
    public static final String TABLE_USER       = "userTable";

    // User Table Columns names
    private static final String KEY_ID             = "id";             //key
    private static final String KEY_FONT           = "font";           //txt
    private static final String KEY_COLORPRIMARY   = "colorPrimary";   //txt
    private static final String KEY_COLORSECONDARY = "colorSecondary"; //txt
    private static final String KEY_FONTCOLOR      = "fontcolor";      //txt
    private static final String KEY_FONTSIZE       = "fontsize";       //int


    public UserDBHelper(Context context) {
        //constuctor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //reinstateTables();
        //add_catId_to_task();
        //updateTasksTable_catId();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_FONT + " TEXT, "
                + KEY_COLORPRIMARY + " TEXT, "
                + KEY_FONTCOLOR + " TEXT, "
                + KEY_COLORSECONDARY + " INTEGER, "
                + KEY_FONTSIZE + " INTEGER "
                + ")";
        db.execSQL(create_sql);
        int entries = getCount();
        if (entries == -1) {
            //We need a default user to add to
            addDefaultUser(db);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {

    }

    public void addDefaultUser(SQLiteDatabase db){
        //Add a default for settings
        //Should only be called once;
        User newuser = new User();
        newuser.setColorPrimary("#3F51B5");   //Default for KISS_List v1.0.3
        newuser.setColorSecondary("#228B22"); //Popup default
        newuser.setFont("roboto");            //Default for android
        newuser.setFontcolor("808080");       //Default for textViews
        newuser.setFontsize(14);              //Default for android `small`

        ContentValues values = new ContentValues();
        values.put(KEY_COLORPRIMARY, newuser.getColorPrimary());
        values.put(KEY_COLORSECONDARY, newuser.getColorSecondary());
        values.put(KEY_FONT, newuser.getFont());
        values.put(KEY_FONTCOLOR, newuser.getFontcolor());
        values.put(KEY_FONTSIZE, newuser.getFontsize());
        long newRowId = db.insert(TABLE_USER, null, values);
        if (newRowId == -1)
            Log.e("cat: TBDH", "CATEGORY NOT INSERTED INTO DB");
        else
            newuser.setId((int)newRowId);
    }
    public int getCount(){
        //Return the number of entries in the table;
        int count;
        String query      = "SELECT count(*) FROM " + TABLE_USER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor     = db.rawQuery(query, null);
        if (cursor.moveToNext())
            count = cursor.getInt(0);
        else
            count = -1;
        cursor.close();
        //db.close();
        return count;
    }
}
