package com.example.tanner.taskapp;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class TaskerDBHelper extends SQLiteOpenHelper {
    /*
    * public SQLiteOpenHelper (Context context,
                String name,
                SQLiteDatabase.CursorFactory factory,
                int version)
    */
    boolean debug = false;
    boolean verbose = false;
    private static final int DATABASE_VERSION    = 1;
    private static final String DATABASE_NAME    = "taskappDB";
    public static final String TABLE_TASKS       = "tasksTable";
    public static final String TABLE_CATEGORIES  = "categoriesTable";
    // tasks Table Columns names
    private static final String KEY_ID           = "id";       //key
    public static final String KEY_CATEGORY      = "category"; //txt
    private static final String KEY_CONTENT      = "content";  //txt
    private static final String KEY_STATUS       = "status";   //int
    private static final String KEY_PLACE        = "place";    //int

    public TaskerDBHelper(Context context) {
        //constuctor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //reinstateTables();
        //reinstateTables();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_CONTENT + " TEXT, "
                + KEY_STATUS + " INTEGER, "
                + KEY_PLACE + " INTEGER "
                + ")";
        String create_sql2 = "CREATE TABLE IF NOT EXISTS " + TABLE_CATEGORIES + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_PLACE + " INTEGER "
                + ")";
        db.execSQL(create_sql);
        db.execSQL(create_sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db); // Create tables again
    }

    /*
    *  This has almost no reason to be here.
    *  It is strictly to wipe out the databases when we are starting new.
    */
    public void reinstateTables(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public void addCat(Categories category) {
        //Add `task` to the database listed in `DATABASE_NAME`
        //Tanner 20180717
        SQLiteDatabase db    = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, category.getCategory());

        // status of task- can be 0 for not done and 1 for done
        values.put(KEY_PLACE, category.getPlace());

        long newRowId = db.insert(TABLE_CATEGORIES, null, values);
        db.close(); // Closing database connection
        if (newRowId == -1)
            Log.e("cat: TBDH", "CATEGORY NOT INSERTED INTO DB");
        else
            category.setId((int)newRowId);
        if (debug == true && verbose == true){
            String msg = "\nDEBUG:\t"+ category;
            msg += "\n\tID:\t\t\t"     + category.getId();
            msg += "\n\tCATEGORY:\t"   + category.getCategory();
            msg += "\n\tPLACE:\t\t"    + category.getPlace();
            Log.d("listener: cat: TDBH", msg);
        }
    }//end addTask()

    public int getCategoryPlace(String category){
        /*
        *  Tanner 20180814
        *  Get the `place` of the provided `category` from category's table;
        *  Return -1 if not found;
        *  Else, return place of category;
        */
        int ret = -1;
        String query = "SELECT * FROM " + TABLE_CATEGORIES;
        String where = " WHERE 1=1";
        where += " AND " + KEY_CATEGORY + " = '"+category+"'";
        query += where;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor     = db.rawQuery(query, null);
        if(cursor.moveToNext())
            ret = cursor.getInt(2);
        cursor.close();
        return ret;
    }

    public Boolean deleteCat(String category){
        /*
         * Author: Tanner Woody
         * Date:   20180805
         * Delete `category` from `categories` table;
         * Remove all instances of rows in tasksTable that have string `category` == column `category`
         * TODO:
         *      Properly hand `place` of all table entries exceeding `this.place`
        */
        SQLiteDatabase db = this.getWritableDatabase();
        //sqlitedatabase.delete returns total number of rows deleted;
        int catPlace = getCategoryPlace(category);
        int suc     = db.delete(TABLE_CATEGORIES, KEY_CATEGORY + " = '" + category +"'", null);
        int suc2    = db.delete(TABLE_TASKS, KEY_CATEGORY + " = '" + category +"'", null);
        //Update all other `place` values where `place` > catPlace
        updateCategoryPlaceOnDelete(catPlace);
        Boolean ret = false;
        if (suc > 0 || suc2 > 0)
            ret = true;
        db.close();
        return ret;
    }//end deleteCat()

    public int updateCategoryPlaceOnDelete(int place){
        /*
        *  Tanner 20180814
        *  Run `upate` statements on all Category items where item.place > place;
        *  Update statement will set item.place to (item.place - 1);
        */
        String query = "SELECT * FROM " + TABLE_CATEGORIES;
        String where = " WHERE 1=1";
        where += " AND " + KEY_PLACE + " > '" + place + "'";
        query += place;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor     = db.rawQuery(query, null);
        int updates       = 0;
        if(cursor.moveToNext()){
            do{
                int id       = cursor.getInt(0);
                int newPlace = cursor.getInt(2)-1;
                if(updateCategoryPlace(cursor, newPlace))
                    updates++;
            } while(cursor.moveToNext());
        }
        else
            Log.e("TDBH: UCPOD", "ISSUE WITH QUERY `" + query + "`");
        cursor.close();
        db.close();
        return updates;
    }//end updateCategoryPlaceOnDelete

    public boolean updateCategoryPlace(Cursor catRow, int newPlace){
        /*
        *  Tanner 20180814
        *  Update item in Category table with `newPlace` via `id`
        */
        boolean ret         = false;
        int itemId          = catRow.getInt(0);
        String itemCategory = catRow.getString(1);
        String update       = "UPDATE " + TABLE_CATEGORIES;
        String set          = " SET " + KEY_PLACE + "='"+newPlace+"'";
        String where        = " WHERE 1=1";
        where += "AND id='" + itemId + "'";
        update += set + where;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(update);
            ret = true;
        }
        catch (SQLException e){
            Log.e("TDBH: UCPOD", "ISSUE WITH QUERY `" + update + "`");
            Log.e("TDBH: UCPOD", e.getMessage());
        }
        db.close();
        return ret;
    }//end updateCategoryPlaceOnDelete()

    public Boolean deleteTask(String id){
        /*
         * Author: Tanner Woody
         * Date:   20180812
         * Delete `task` from `tasksTable`;
         * Remove all instances of rows with `id` provided;
         * Returns false if 0 rows are removed;
         * * TODO:
         *      TEST that properly handling `place` of all table entries exceeding `this.place`
         */
        SQLiteDatabase db = this.getWritableDatabase();
        int suc     = db.delete(TABLE_TASKS, KEY_ID + " = '" + id +"'", null);
        Boolean ret = false;
        if (suc > 0)
            ret = true;
        db.close();
        return ret;
    }//end deleteTask()

    public void addTask(Tasker tasker) {
        //Add `task` to the database listed in `DATABASE_NAME`
        //Tanner 20180717
        SQLiteDatabase db    = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, tasker.getCategory());
        values.put(KEY_CONTENT, tasker.getContent());

        // status of task- can be 0 for not done and 1 for done
        values.put(KEY_STATUS, tasker.getStatus());
        values.put(KEY_PLACE, tasker.getPlace());

        long newRowId = db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection

        if (newRowId == -1)
            Log.e("task:TBDH", "TASK NOT INSERTED INTO DB");
        else
            tasker.setId((int)newRowId);

        if (debug == true && verbose == true){
            String msg = "\nDEBUG:\t"+ tasker;
            msg += "\n\tID:\t\t\t"     + tasker.getId();
            msg += "\n\tCATEGORY:\t"   + tasker.getCategory();
            msg += "\n\tCONTENT:\t"    + tasker.getContent();
            msg += "\n\tSTATUS:\t\t"   + tasker.getStatus();
            msg += "\n\tPLACE:\t\t"    + tasker.getPlace();
            Log.d("listener: task: TDBH", msg);
        }
    }//end addTask()

    public List<Tasker> getAllTasks(String tableName, String category) {
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll      = "SELECT * FROM " + tableName;
        if (category != "") {
            selectAll += " WHERE " + KEY_CATEGORY + " = '" + category + "' ";
        }
        selectAll += " ORDER BY place ";
        SQLiteDatabase db     = this.getWritableDatabase();
        Cursor cursor         = db.rawQuery(selectAll, null);
        int cnt = 0;
        if (cursor.moveToNext()) {
            do {
                Tasker tasker = new Tasker();
                tasker.setId(cursor.getInt(0));
                tasker.setCategory(cursor.getString(1));
                tasker.setContent(cursor.getString(2));
                tasker.setStatus(cursor.getInt(3));
                tasker.setPlace(cursor.getInt(4));
                taskList.add(tasker);
                Log.d("LOOP", "Loop " + Integer.toString(cnt) + ": " + tasker.getContent());
                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0)
            Log.e("TBDH: getAllTasks", "NO ITEMS FOUND FOR QUERY `"+selectAll+"`");
        else if(debug == true)
            Log.d("TBDH: getAllTasks", "QUERY `"+selectAll+"` RETURNED "+ cnt +" RESULTS");
        return taskList;
    }//end getAllTasks()

    public List<Tasker> getAllCompletedTasks(String tableName, String category) {
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll      = "SELECT * FROM " + tableName;
        String whereclause    = "";
        whereclause += " WHERE 1=1 ";
        if (category != "") {
            whereclause += " AND "+ KEY_CATEGORY +" = '" + category + "' ";
        }
        whereclause += " AND status = '1' ";
        selectAll += whereclause;
        selectAll += " ORDER BY place ";
        SQLiteDatabase db     = this.getWritableDatabase();
        Cursor cursor         = db.rawQuery(selectAll, null);
        int cnt = 0;
        if (cursor.moveToNext()) {
            do {
                Tasker tasker = new Tasker();
                tasker.setId(cursor.getInt(0));
                tasker.setCategory(cursor.getString(1));
                tasker.setContent(cursor.getString(2));
                tasker.setStatus(cursor.getInt(3));
                tasker.setPlace(cursor.getInt(4));
                taskList.add(tasker);
                Log.d("LOOP", "Loop " + Integer.toString(cnt) + ": " + tasker.getContent());
                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0)
            Log.e("TBDH: getAllTasks", "NO ITEMS FOUND FOR QUERY `"+selectAll+"`");
        else if(debug == true)
            Log.d("TBDH: getAllTasks", "QUERY `"+selectAll+"` RETURNED "+ cnt +" RESULTS");
        return taskList;
    }//end getAllCompletedTasks()

    public List<Tasker> getAllnoncompletedTasks(String tableName, String category) {
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll      = "SELECT * FROM " + tableName;
        String whereclause    = "";
        whereclause += " WHERE 1=1 ";
        if (category != "") {
            whereclause += " AND "+ KEY_CATEGORY +" = '" + category + "' ";
        }
        whereclause += " AND status = '0' ";
        selectAll += whereclause;
        selectAll += " ORDER BY place ";
        SQLiteDatabase db     = this.getWritableDatabase();
        Cursor cursor         = db.rawQuery(selectAll, null);
        int cnt = 0;
        if (cursor.moveToNext()) {
            do {
                Tasker tasker = new Tasker();
                tasker.setId(cursor.getInt(0));
                tasker.setCategory(cursor.getString(1));
                tasker.setContent(cursor.getString(2));
                tasker.setStatus(cursor.getInt(3));
                tasker.setPlace(cursor.getInt(4));
                taskList.add(tasker);
                Log.d("LOOP", "Loop " + Integer.toString(cnt) + ": " + tasker.getContent());
                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0)
            Log.e("TBDH: getAllTasks", "NO ITEMS FOUND FOR QUERY `"+selectAll+"`");
        else if(debug == true)
            Log.d("TBDH: getAllTasks", "QUERY `"+selectAll+"` RETURNED "+ cnt +" RESULTS");
        return taskList;
    }//end getAllNoncompletedTasks()

    public int countCategories(){
        /*
         *  Tanner 20180814
         *  Return count of all categories;
         *  Return -1 if Error
         */
        int count;
        String query      = "SELECT count(*) FROM " + TABLE_CATEGORIES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor     = db.rawQuery(query, null);
        if (cursor.moveToNext())
            count = cursor.getInt(0);
        else
            count = -1;
        cursor.close();
        return count;
    }//end countCategories

    public String getCategoryFromId(String id){
        String cat = "";
        String query      = "SELECT * FROM " + TABLE_CATEGORIES + " WHERE id = '"+id+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor     = db.rawQuery(query, null);
        if (cursor.moveToNext())
            cat = cursor.getString(1);
        cursor.close();
        return cat;
    }

    public Integer getCategoryCount(String category){
        //BUG: Do not include where clause if category is empty string
        int numOfCategories = -1;
        String table          = TABLE_CATEGORIES;
        String select         = "SELECT count(*) from " + table + " WHERE " +KEY_CATEGORY+ "='" + category +"'";
        SQLiteDatabase db     = this.getReadableDatabase();
        Cursor cursor         = db.rawQuery(select, null);
        if (cursor.moveToNext())
            numOfCategories = cursor.getInt(0);
        cursor.close();
        return numOfCategories;
    }//end getCategoryCount()

    public List<Categories> getAllCategories() {
        List<Categories> catList = new ArrayList<Categories>();
        String selectAll         = "SELECT * FROM " + TABLE_CATEGORIES + " ORDER BY place ";
        SQLiteDatabase db        = this.getReadableDatabase();
        Cursor cursor            = db.rawQuery(selectAll, null);
        if (cursor.moveToNext()) {
            do {
                Categories category = new Categories();
                category.setId(cursor.getInt(0));
                category.setCategory(cursor.getString(1));
                category.setPlace(cursor.getInt(2));
                catList.add(category);
            } while (cursor.moveToNext());
        }
        return catList;
    }//end getAllTasks()

    public void updateTask(Tasker tasker) {
        SQLiteDatabase db    = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] _ids        = new String[]{String.valueOf(tasker.getId())};
        String whereclause   = KEY_ID + " = ?";
        values.put(KEY_CATEGORY, tasker.getCategory());
        values.put(KEY_CONTENT, tasker.getContent());
        values.put(KEY_STATUS, tasker.getStatus());
        values.put(KEY_PLACE, tasker.getPlace());

        db.update(TABLE_TASKS, values, whereclause, _ids);
        db.close();
    }//end updateTask()
}