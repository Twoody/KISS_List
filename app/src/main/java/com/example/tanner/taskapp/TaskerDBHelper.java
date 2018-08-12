package com.example.tanner.taskapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class TaskerDBHelper extends SQLiteOpenHelper {
    /*
    * public SQLiteOpenHelper (Context context,
                String name,
                SQLiteDatabase.CursorFactory factory,
                int version)
    */
    boolean debug = false;
    private static final int DATABASE_VERSION    = 1;
    private static final String DATABASE_NAME    = "taskappDB";
    public static final String TABLE_TASKS      = "tasksTable";
    private static final String TABLE_CATEGORIES = "categoriesTable";
    // tasks Table Columns names
    private static final String KEY_ID           = "id";       //key
    public static final String KEY_CATEGORY     = "category"; //txt
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
    *  It is strictly yo wipe out the databases when we are starting new.
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
        if (debug == true){
            String msg = "\nDEBUG:\t"+ category;
            msg += "\n\tID:\t\t\t"     + category.getId();
            msg += "\n\tCATEGORY:\t"   + category.getCategory();
            msg += "\n\tPLACE:\t\t"    + category.getPlace();
            Log.d("listener: cat: TDBH", msg);
        }
    }//end addTask()

    public Boolean deleteCat(String category){
        /*
         * Author: Tanner Woody
         * Date:   20180805
         * Delete `category` from `categories` table;
         * Remove all instances of rows in tasksTable that have string `category` == column `category`
        */
        SQLiteDatabase db = this.getWritableDatabase();
        //sqlitedatabase.delete returns total number of rows deleted;
        int suc     = db.delete(TABLE_CATEGORIES, KEY_CATEGORY + " = '" + category +"'", null);
        int suc2    = db.delete(TABLE_TASKS, KEY_CATEGORY + " = '" + category +"'", null);
        Boolean ret = false;
        if (suc > 0 || suc2 > 0)
            ret = true;
        db.close();
        return ret;
    }//end deleteCat()

    public Boolean deleteTask(String id){
        /*
         * Author: Tanner Woody
         * Date:   20180812
         * Delete `task` from `tasksTable`;
         * Remove all instances of rows with `id` provided;
         * Returns false if 0 rows are removed;
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

        if (debug == true){
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
            selectAll += " WHERE category = '" + category + "' ";
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
        return taskList;
    }//end getAllTasks()

    public Integer getCategoryCount(String category){
        //BUG: Do not include where clause if category is empty string
        String table          = TABLE_CATEGORIES;
        String select         = "SELECT count(*) from " + table + " WHERE category='"+ category +"'";
        SQLiteDatabase db     = this.getReadableDatabase();
        Cursor cursor         = db.rawQuery(select, null);
        cursor.moveToNext();
        int numOfCategories = cursor.getInt(0);
        cursor.close();
        return numOfCategories;
    }//end getCategoryCount()
/*    public Integer getUniqueCategories(){

    }//end getUniqueCategories
*/
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