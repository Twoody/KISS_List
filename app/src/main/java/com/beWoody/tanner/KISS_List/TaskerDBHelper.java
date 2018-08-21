package com.beWoody.tanner.KISS_List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
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

       TODO: FIND OUT WHEN TO CALL:
                add_catId_to_task()
                updateTasksTable_catID()
             , SUCH THAT USERS DATA WILL WORK CORRECTLY WITH NEW UPDATES
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
    private static final String KEY_CATEGORY_ID  = "catId";    //txt

    public TaskerDBHelper(Context context) {
        //constuctor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //reinstateTables();
        //add_catId_to_task();
        //updateTasksTable_catId();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_sql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_CATEGORY + " TEXT, "
                + KEY_CONTENT + " TEXT, "
                + KEY_STATUS + " INTEGER, "
                + KEY_PLACE + " INTEGER, "
                + KEY_CATEGORY_ID + " INTEGER "
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
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        //onCreate(db); // Create tables again
        //db.close();
        //add_catId_to_task();
        //updateTasksTable_catId();
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
        //db.close();
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
        if (newRowId == -1)
            Log.e("cat: TBDH", "CATEGORY NOT INSERTED INTO DB");
        else
            category.setId((int)newRowId);
        //db.close(); // Closing database connection
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
        //db.close();
        return ret;
    }

    public boolean updateTaskContent(String taskId, String newContent){
        /*
         *  Tanner 20180816
         *  Update item in Task table with `newContent` via `id`
         */
        boolean ret         = false;
        Tasker task         = getTask(taskId);
        String update       = "UPDATE " + TABLE_TASKS;
        String set          = " SET " + KEY_CONTENT + "='" + newContent + "'";
        String where        = " WHERE 1=1";
        where += " AND " + KEY_ID + "='" + taskId + "'";
        update += set + where;
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("updateTaskContent", "SQL: `" + update + "`");
        try {
            db.execSQL(update);
            ret = true;
            //Log.d("updateTaskContent()", "UPDATED `" + taskId + "` content TO `" + newContent + "`");
        }
        catch (SQLException e){
            Log.e("TDBH: updateTaskPlace", "ISSUE WITH QUERY `" + update + "`");
            Log.e("TDBH: updateTaskPlace", e.getMessage());
        }
        //db.close();
        return ret;
    }
    public boolean updateCatCategory(String catId, String newCategory){
        /*
         *  Tanner 20180816
         *  Update item in Categories table with `newCategory` via `catId`
         *  Update items in taskTable with newCategory;
         *  TODO: Break up try/catch into two segments;
         */
        boolean ret       = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String updateCat  = "UPDATE " + TABLE_CATEGORIES;
        String setCat     = " SET " + KEY_CATEGORY + "='" + newCategory + "'";
        String whereCat   = " WHERE 1=1";
        whereCat += " AND " + KEY_ID + "='" + catId + "'";
        updateCat += setCat + whereCat;

        String updateTask = "UPDATE " + TABLE_TASKS;
        String setTask    = " Set " + KEY_CATEGORY + " = '" + newCategory + "'";
        String whereTask  = " WHERE 1=1";
        whereTask += " AND " + KEY_CATEGORY_ID + " = " + catId + "";
        updateTask += setTask + whereTask;
        try {
            db.execSQL(updateCat);
            db.execSQL(updateTask);
            ret = true;
        }
        catch (SQLException e){
            Log.e("TDBH: updateCatCategory", "ISSUE WITH QUERY `" + updateCat + "`");
            Log.e("TDBH: updateCatCategory", e.getMessage());
            Log.e("TDBH: updateCatCategory", "ISSUE WITH QUERY `" + updateTask + "`");
            Log.e("TDBH: updateCatCategory", e.getMessage());
        }
        //db.close();
        return ret;
    }//end updateCatCategory()

    public Tasker getTask(String taskId){
        /*
        * Tanner 20180816
        * Return the task name depending on the table specified;
        */
        Tasker ret   = null;
        String query = "SELECT * FROM " + TABLE_TASKS;
        String where = " WHERE 1=1";
        where += " AND " + KEY_ID + " = '" + taskId + "'";
        query += where;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor     = db.rawQuery(query, null);
        if(cursor.moveToNext()) {
            String category = cursor.getString(1);
            String content  = cursor.getString(2);
            int status      = cursor.getInt(3);
            int place       = cursor.getInt(4);
            int catId       = cursor.getInt(5);
            ret = new Tasker(category, content, status, place, catId);
        }
        cursor.close();
        //Log.d("getTask", "SQL: `" + query + "`");
        return ret;
    }//end getTask

    public Categories getCategories(String catId){
        /*
         * Tanner 20180816
         * Return the Categories obj from provided `id`;
         */
        Categories ret   = null;
        String query = "SELECT * FROM " + TABLE_CATEGORIES;
        String where = " WHERE 1=1";
        where += " AND " + KEY_ID + " = '" + catId + "'";
        query += where;
        Log.d("getCat44", "SQL: `" + query + "`");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor     = db.rawQuery(query, null);
        if(cursor.moveToNext()) {
            String category = cursor.getString(1);
            int place       = cursor.getInt(2);
            ret = new Categories(category, place);
        }
        cursor.close();

        return ret;
    }//end getCategories()

    public int getTaskPlace(String id){
        /*
         *  Tanner 20180814
         *  Get the `place` of the provided `task` from tasks table via ID;
         *  Return -1 if not found;
         *  Else, return `place` of task;
         */
        int ret = -1;
        Tasker task = getTask(id);
        if(task != null)
            ret = task.getPlace();
        //db.close();
        return ret;
    }//end getTaskPlace()

    public int getTaskStatus(String id){
        /*
         *  Tanner 20180814
         *  Get the `status` of the provided `task` from tasks table via ID;
         *  Return -1 if not found;
         *  Else, return `place` of task;
         */
        int ret = -1;
        Tasker task = getTask(id);
        if(task != null)
            ret = task.getStatus();
        //db.close();
        return ret;
    }//end getTaskStatus()

    public String getTaskCategory(String id){
        /*
         *  Tanner 20180814
         *  Get the `category` of the provided `task` from tasks table via ID;
         *  Return "" if not found;
         *  Else, return `category` of task;
         */
        String ret = "";
        Tasker task = getTask(id);
        if(task != null)
            ret = task.getCategory();
        //db.close();
        return ret;
    }

    public Boolean deleteCat(String category){
        /*
         * Author: Tanner Woody
         * Date:   20180805
         * Delete `category` from `categories` table;
         * Remove all instances of rows in tasksTable that have string `category` == column `category
         *
        */
        SQLiteDatabase db = this.getWritableDatabase();
        //sqlitedatabase.delete returns total number of rows deleted;
        int catPlace = getCategoryPlace(category);
        int suc     = db.delete(TABLE_CATEGORIES, KEY_CATEGORY + " = '" + category +"'", null);
        int suc2    = db.delete(TABLE_TASKS, KEY_CATEGORY + " = '" + category +"'", null);
        //Update all other `place` values where `place` > catPlace
        int rowsUpdated = updateCategoryPlaceOnDelete(catPlace);
        if(rowsUpdated == 0)
            Log.w("TDBH: deleteCat", "WARNING: NO ROWS UPDATED WITH NEW `"+KEY_PLACE+"`");
        Boolean ret = false;
        if (suc > 0 || suc2 > 0)
            ret = true;
        //db.close();
        return ret;
    }//end deleteCat()

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
        int taskPlace     = getTaskPlace(id);
        String category   = getTaskCategory(id);
        int suc           = db.delete(TABLE_TASKS, KEY_ID + " = '" + id +"'", null);
        //Update all other `place` values where `place` > catPlace
        int rowsUpdated = updateTaskPlaceOnDelete(category, taskPlace);
        if(rowsUpdated == 0)
            Log.w("TDBH: deleteCat", "WARNING: NO ROWS UPDATED WITH NEW `"+KEY_PLACE+"`");
        Boolean ret = false;
        if (suc > 0)
            ret = true;
        return ret;
    }//end deleteTask()

    public int updateCategoryPlaceOnDelete(int place){
        /*
         *  Tanner 20180814
         *  Run `upate` statements on all Category items where item.place > place;
         *  Update statement will set item.place to (item.place - 1);
         */
        String query = "SELECT * FROM " + TABLE_CATEGORIES;
        String where = " WHERE 1=1";
        where += " AND " + KEY_PLACE + " > " + place + "";
        query += where;
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
            Log.w("TDBH: UCPOD", "ISSUE WITH QUERY `" + query + "`");
        cursor.close();
        //db.close();
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
        where += " AND " + KEY_ID + "='" + itemId + "'";
        update += set + where;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(update);
            ret = true;
            //Log.d("updateCategoryPlace()", "UPDATED `"+itemId+"` FROM `"+catRow.getInt(2)+"` TO `"+newPlace+"`");
        }
        catch (SQLException e){
            Log.e("TDBH: UCPOD", "ISSUE WITH QUERY `" + update + "`");
            Log.e("TDBH: UCPOD", e.getMessage());
        }
        //db.close();
        return ret;
    }//end updateCategoryPlaceOnDelete()

    public int updateTaskPlaceOnDelete(String category, int place){
        /*
         *  Tanner 20180814
         *  Run `update` statements on all Category items where item.place > place;
         *  Update statement will set item.place to (item.place - 1);
         */
        String query = "SELECT * FROM " + TABLE_TASKS;
        String where = " WHERE 1=1";
        where += " AND " + KEY_PLACE + " > " + place + "";
        where += " AND " + KEY_CATEGORY + "='" + category + "'";
        query += where;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor     = db.rawQuery(query, null);
        int updates       = 0;
        if(cursor.moveToNext()){
            do{
                int id       = cursor.getInt(0);
                int newPlace = cursor.getInt(4)-1;
                if(updateTaskPlace(cursor, newPlace))
                    updates++;
            } while(cursor.moveToNext());
        }
        else
            Log.e("TDBH: UTPOD", "ISSUE WITH QUERY `" + query + "`");
        cursor.close();
        //db.close();
        return updates;
    }//end updateTaskPlaceOnDelete

    public boolean updateTaskPlace(Cursor taskRow, int newPlace){
        /*
         *  Tanner 20180814
         *  Update item in Category table with `newPlace` via `id`
         */
        boolean ret         = false;
        int itemId          = taskRow.getInt(0);
        String itemCategory = taskRow.getString(1);
        String update       = "UPDATE " + TABLE_TASKS;
        String set          = " SET " + KEY_PLACE + "='"+newPlace+"'";
        String where        = " WHERE 1=1";
        where += " AND " + KEY_ID + "='" + itemId + "'";
        update += set + where;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(update);
            ret = true;
            //Log.d("updateTaskPlace()", "UPDATED `"+itemId+"` FROM `"+ taskRow.getInt(4) +"` TO `"+newPlace+"`");
        }
        catch (SQLException e){
            Log.e("TDBH: updateTaskPlace", "ISSUE WITH QUERY `" + update + "`");
            Log.e("TDBH: updateTaskPlace", e.getMessage());
        }
        //db.close();
        return ret;
    }//end updateTaskPlace()


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
        values.put(KEY_CATEGORY_ID, tasker.getCatId());

        long newRowId = db.insert(TABLE_TASKS, null, values);

        if (newRowId == -1)
            Log.e("task:TBDH", "TASK NOT INSERTED INTO DB");
        else
            tasker.setId((int)newRowId);
        //db.close(); // Closing database connection
    }//end addTask()

    public List<Tasker> getAllTasks(String category) {
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll      = "SELECT * FROM " + TABLE_TASKS;
        if (category != "") {
            selectAll += " WHERE " + KEY_CATEGORY + " = '" + category + "' ";
        }
        selectAll += " ORDER BY place ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectAll, null);
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
                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0)
            Log.w("TBDH: getAllTasks", "NO ITEMS FOUND FOR QUERY `"+selectAll+"`");
        //db.close();
        return taskList;
    }//end getAllTasks()

    public List<Tasker> getAllCompletedTasks(String category) {
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll      = "SELECT * FROM " + TABLE_TASKS;
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

                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0)
            Log.w("getAllCompletedTasks", "NO ITEMS FOUND FOR QUERY `"+selectAll+"`");
        //db.close();
        return taskList;
    }//end getAllCompletedTasks()

    public List<Tasker> getAllNoncompletedTasks(String category) {
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll      = "SELECT * FROM " + TABLE_TASKS;
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
                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0)
            Log.w("getAllNoncompletedTasks", "NO ITEMS FOUND FOR QUERY `"+selectAll+"`");
        //db.close();
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
        //db.close();
        return count;
    }//end countCategories

    public int countTasks(String category){
        /*
         *  Tanner 20180814
         *  Return count of all task within given category;
         *  Return -1 if Error;
         */
        int count;
        String query = "SELECT count(*) FROM " + TABLE_TASKS;
        String where = " WHERE 1=1";
        where += " AND category = '" + category + "'";
        query += where;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor     = db.rawQuery(query, null);
        if (cursor.moveToNext())
            count = cursor.getInt(0);
        else
            count = -1;
        cursor.close();
        //db.close();
        return count;
    }//end countTasks()

    public String getCategoryFromId(String id){
        String cat = "";
        Categories categories = getCategories(id);
        if (categories != null)
            cat = categories.getCategory();
        Log.d("getCategoryFromId89", "cat: `"+cat+"`");
        return cat;
    }//end getCategoryFromId()

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
        //db.close();
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
        //db.close();
        return catList;
    }//end getAllCategories()

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
        //db.close();
    }//end updateTask()

    public void add_catId_to_task(){
        /*
         *  Tanner 20180819
         *  SHOULD ONLY BE CALLED ONCE!
         *  Alter table tasks and add catId
         */
        SQLiteDatabase db = this.getWritableDatabase();
        String newColumn  = "catId";
        String alter      = "ALTER TABLE " + TABLE_TASKS + " ADD COLUMN " + newColumn;
        db.execSQL(alter);

    }//add_catId_to_task()

    public void updateTasksTable_catId(){
        /*
        *  Tanner 20180819
        *  Set catId to TABLE_CATEGORIES.id;
        *     --Allows multiple lists with the same name;
        */
        SQLiteDatabase db = this.getWritableDatabase();
        String newColumn  = "catId";
        String update     = "UPDATE " + TABLE_TASKS;
        String set        = " SET " + newColumn + " = ";
        set += "(Select " + KEY_ID + " FFOM " + TABLE_CATEGORIES;
        set += "  WHERE " + TABLE_CATEGORIES + "." + KEY_CATEGORY + " = " + TABLE_TASKS + "." + KEY_CATEGORY + ")";
        update += set;
        db.execSQL(update);
    }
}