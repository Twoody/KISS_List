package com.beWoody.tanner.KISS_List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
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
    private static final int DATABASE_VERSION      = 1;
    private static final String DATABASE_NAME      = "userDB";
    public static final String TABLE_USER          = "userTable";

    // User Table Columns names
    private static final String KEY_ID             = "id";             //key
    private static final String KEY_FONT           = "font";           //txt
    private static final String KEY_COLORPRIMARY   = "colorPrimary";   //int
    private static final String KEY_COLORSECONDARY = "colorSecondary"; //int
    private static final String KEY_LISTCOLOR      = "listcolor";      //int
    private static final String KEY_FONTCOLOR      = "fontcolor";      //int
    private static final String KEY_FONTSIZE       = "fontsize";       //int
    private static final String KEY_ISAPPENDING    = "isAppending";    //bool as int
    private static final String USERID             = "1"; //This should never change;

    public UserDBHelper(Context context) {
        //constuctor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //reinstateTables();
        //add_catId_to_task();
        //updateTasksTable_catId();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_FONT + " TEXT, "
                + KEY_COLORPRIMARY + " INTEGER, "
                + KEY_COLORSECONDARY + " INTEGER, "
                + KEY_LISTCOLOR + " INTEGER, "
                + KEY_FONTCOLOR + " INTEGER, "
                + KEY_FONTSIZE + " INTEGER, "
                + KEY_ISAPPENDING + " INTEGER "
                + ")";
        db.execSQL(create_sql);
        int entries = getCount(db);
        if (entries == -1 || entries == 0) {
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
        newuser.setFont("roboto");            //Default for android
        newuser.setColorPrimary(Resources.getSystem().getColor(android.R.color.white, null));
        newuser.setColorSecondary(Resources.getSystem().getColor(android.R.color.white, null));
        newuser.setListcolor(Resources.getSystem().getColor(android.R.color.darker_gray, null));
        newuser.setFontcolor(Resources.getSystem().getColor(android.R.color.black, null));
        newuser.setFontsize(14);              //Default for android `small`
        newuser.setIsAppending(1);            //Default is to append lists and items

        ContentValues values = new ContentValues();
        values.put(KEY_FONT, newuser.getFont());
        values.put(KEY_COLORPRIMARY, newuser.getColorPrimary());
        values.put(KEY_COLORSECONDARY, newuser.getColorSecondary());
        values.put(KEY_LISTCOLOR, newuser.getListcolor());
        values.put(KEY_FONTCOLOR, newuser.getFontcolor());
        values.put(KEY_FONTSIZE, newuser.getFontsize());
        values.put(KEY_ISAPPENDING, newuser.getIsAppending());
        long newRowId = db.insert(TABLE_USER, null, values);
        if (newRowId == -1)
            Log.e("UDBH", "USER NOT INSERTED INTO DB");
        else
            newuser.setId((int)newRowId);
    }
    public int getCount(SQLiteDatabase db){
        //Return the number of entries in the table;
        int count;
        String query       = "SELECT count(*) FROM " + TABLE_USER;
        Cursor cursor      = db.rawQuery(query, null);
        if (cursor.moveToNext())
            count = cursor.getInt(0);
        else
            count = -1;
        cursor.close();
        return count;
    }
    public User getUser(){
        User retUser       = new User();
        SQLiteDatabase _db = this.getWritableDatabase();
        String selectAll   = "SELECT * FROM " + TABLE_USER;
        Cursor cursor      = _db.rawQuery(selectAll, null);
        int cnt = 0;
        if (cursor.moveToNext()) {
            do {
                retUser.setId(cursor.getInt(0));
                retUser.setFont(cursor.getString(1));
                retUser.setColorPrimary(cursor.getInt(2));
                retUser.setColorSecondary(cursor.getInt(3));
                retUser.setListcolor(cursor.getInt(4));
                retUser.setFontcolor(cursor.getInt(5));
                retUser.setFontsize(cursor.getInt(6));
                retUser.setIsAppending(cursor.getInt(7));
                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0) {
            Log.w("UDBH", "NO ITEMS FOUND FOR QUERY `" + selectAll + "`");
            retUser = null;
        }
        _db.close();
        return retUser;
    }
    public boolean updateIsAppending(int isappending){
        Boolean ret = false;
        String update            = "UPDATE " + TABLE_USER;
        String set               = " SET " + KEY_ISAPPENDING + "='" + isappending + "'";
        String where             = " WHERE 1=1";
        where += " AND " + KEY_ID + "='" + USERID + "'";
        update += set + where;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(update);
            ret = true;
        }
        catch (SQLException e){
            Log.e("UDBH: updateTaskPlace", "ISSUE WITH QUERY `" + update + "`");
            Log.e("UDBH: updateTaskPlace", e.getMessage());
        }
        db.close();
        return ret;
    }
}
