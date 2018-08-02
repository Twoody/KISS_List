package com.example.tanner.taskapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class TaskerDBHelper extends SQLiteOpenHelper {
    /*
    * public SQLiteOpenHelper (Context context,
                String name,
                SQLiteDatabase.CursorFactory factory,
                int version)
    */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "taskappDB";
    private static final String TABLE_TASKS = "tasksTable";
    // tasks Table Columns names
    private static final String KEY_ID = "id";      //key
    private static final String KEY_CATEGORY = "category"; //txt
    private static final String KEY_CONTENT = "content";  //txt
    private static final String KEY_STATUS = "status";   //int
    private static final String KEY_PLACE = "place";    //int

    public TaskerDBHelper(Context context) {
        //constuctor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_CONTENT + " TEXT, "
                + KEY_STATUS + " INTEGER, "
                + KEY_PLACE + " INTEGER "
                + ")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db); // Create tables again
    }

    //Do we want these files here? Where do they live?!
    public void addTask(Tasker tasker) {
        //Add `task` to the database listed in `DATABASE_NAME`
        //Tanner 20180717

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, tasker.getCategory());
        values.put(KEY_CONTENT, tasker.getContent());

        // status of task- can be 0 for not done and 1 for done
        values.put(KEY_STATUS, tasker.getStatus());
        values.put(KEY_PLACE, tasker.getPlace());

        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }//end addTask()

    public List<Tasker> getAllTasks() {
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);
        if (cursor.moveToNext()) {
            do {
                Tasker tasker = new Tasker();
                tasker.setId(cursor.getInt(0));
                tasker.setCategory(cursor.getString(1));
                tasker.setStatus(cursor.getInt(2));
                tasker.setPlace(cursor.getInt(3));
                taskList.add(tasker);
            } while (cursor.moveToNext());
        }
        return taskList;
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
    }//end updateTask()
}