package com.beWoody.tanner.KISS_List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
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

    }

    /**************************** RETIRED FUNCTIONS ****************************/
    public void add_catId_to_task(){
        /*
         *  Tanner 20180819
         *  SHOULD HAVE ONLY BE CALLED ONCE!
         *  ONLY EXISTS AS AN EXAMPLE!
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
        db.close();
    }

    /**************************** HELPER FUNCTIONS ****************************/
    public static List<String> splitEqually(String text, int size) {
        // Helper function to split large portions of text into chunks;
        // Give the list the right capacity to start with.

        List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

        for (int start = 0; start < text.length(); start += size) {
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        }
        return ret;
    }
    public String processInput(String content){
        /*
         * TODO: Make charsBeforeBreak modifiable on textsiz;
         *           This will require userdb call;
         *  String builder to inject a newline at last space found before breaking 80 characters;
         *  If word is more than 80 characters, break word;
         */
        String[] _words       = content.split("\\s+");
        List<String> words    = Arrays.asList(_words);
        String customcontent  = "";
        int charsBeforeBreak  = 20; //Max length of word with no breaks; Max we inject \n at;
        int currCount         = 0;

        for(int i = 0; i < words.size(); i++){
            String word = words.get(i);
            currCount += 1 + word.length();
            if (currCount > charsBeforeBreak) {
                if (currCount == 1 + word.length()) {
                    //Word is way to big for our purpose;
                    List<String> splitwords = splitEqually(word, charsBeforeBreak);
                    String moddedWord = String.join("\n", splitwords);
                    customcontent += moddedWord;
                } else {
                    //We will exceed our limited amount of characters on this run;
                    //Insert newline to start new count
                    customcontent += '\n' + word;
                }
                currCount = 0;
            }
            else{
                //We still have room before newline is needed
                if (i != 0) //Do Not initialize Prior string with a space
                    customcontent += " ";
                customcontent += word;
            }
        }//end i-for
        return customcontent;
    }//end processInput

    /**************************** ADD FUNCTIONS ****************************/
    public int addCat(Categories category) {
        //Add `task` to the database listed in `DATABASE_NAME`
        //Tanner 20180717
        SQLiteDatabase db    = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, category.getCategory());

        // status of task- can be 0 for not done and 1 for done
        values.put(KEY_PLACE, category.getPlace());

        int newRowId = (int) db.insert(TABLE_CATEGORIES, null, values);
        if (newRowId == -1)
            Log.e("cat: TBDH", "CATEGORY NOT INSERTED INTO DB");
        else
            category.setId((int)newRowId);
        db.close(); // Closing database connection
        return newRowId;
    }//end addCat()

    public int addTask(Tasker tasker) {
        //Add `task` to the database listed in `DATABASE_NAME`
        //Tanner 20180717
        int ret = -1;
        SQLiteDatabase db       = this.getWritableDatabase();
        ContentValues values    = new ContentValues();
        String myContent        = tasker.getContent();
        String formattedContent = processInput(myContent);

        values.put(KEY_CATEGORY, tasker.getCategory());
        values.put(KEY_CONTENT, formattedContent);

        // status of task- can be 0 for not done and 1 for done
        values.put(KEY_STATUS, tasker.getStatus());
        values.put(KEY_PLACE, tasker.getPlace());
        values.put(KEY_CATEGORY_ID, tasker.getCatId());

        ret = (int) db.insert(TABLE_TASKS, null, values);

        if (ret == -1)
            Log.e("task:TBDH", "TASK NOT INSERTED INTO DB");
        else
            tasker.setId(ret);
        db.close();
        return ret;
    }//end addTask()

    /**************************** COPY FUNCTIONS ****************************/
    public void copyIncompleteTasks(int toId, int fromId){
        List <Tasker> toCopy = getAllNoncompletedTasks(fromId);

        for (int i=0; i< toCopy.size(); i++){
            Tasker copyThis = toCopy.get(i);
            int taskCatId   = toId;
            int place       = copyThis.getPlace();
            String cat      = copyThis.getCategory();
            String content  = copyThis.getContent();
            Tasker task     = new Tasker(cat, content, 0, place, toId);
            int suc = addTask(task);
        }
        return;
    }

    /**************************** COUNT FUNCTIONS ****************************/
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
        db.close();
        return count;
    }//end countTasks()

    /**************************** DELETE FUNCTIONS ****************************/
    public Boolean deleteCat(int catid){
        /*
         * Author: Tanner Woody
         * Date:   20180805
         * Delete `category` from `categories` table;
         * Remove all instances of rows in tasksTable that have string `category` == column `category
         *
         *sqlitedatabase.delete returns total number of rows deleted;
         *
         *
         * TODO: MAJOR BUG! NEED TO DELETE ON CATID;
         */
        SQLiteDatabase db = this.getWritableDatabase();
        int suc     = db.delete(TABLE_CATEGORIES, KEY_ID + " = " + catid +"", null);
        int suc2    = db.delete(TABLE_TASKS, KEY_CATEGORY_ID + " = " + catid +"", null);
        //Update all other `place` values where `place` > catPlace
        db.close();
        Boolean ret = false;
        if (suc > 0 || suc2 > 0)
            ret = true;
        return ret;
    }//end deleteCat()
    public Boolean deleteTask(int id){
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
        int suc           = db.delete(TABLE_TASKS, KEY_ID + " = '" + id +"'", null);
        //Update all other `place` values where `place` > catPlace
        Boolean ret = false;
        if (suc > 0)
            ret = true;
        db.close();
        return ret;
    }//end deleteTask()

    /**************************** GET FUNCTIONS ****************************/
    public List<Categories> getAllCategories() {
        //Return list of all entries in Categories table;
        List<Categories> catList = new ArrayList<Categories>();
        SQLiteDatabase db        = this.getReadableDatabase();
        String selectAll         = "SELECT * FROM " + TABLE_CATEGORIES + " ORDER BY place ";
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
        cursor.close();
        db.close();
        return catList;
    }//end getAllCategories()
    public List<Tasker> getAllCompletedTasks(int catid) {
        //Return all tasks with `status` marked complete (i.e. 1);
        SQLiteDatabase db     = this.getWritableDatabase();
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll      = "SELECT * FROM " + TABLE_TASKS;
        String whereclause    = "";
        whereclause += " WHERE 1=1 ";
        if (catid != 0) {
            whereclause += " AND "+ KEY_CATEGORY_ID +" = " + catid + " ";
        }
        whereclause += " AND status = 1 ";
        selectAll += whereclause;
        selectAll += " ORDER BY place ";
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
                tasker.setCatId(cursor.getInt(5));
                taskList.add(tasker);

                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0)
            Log.w("getAllCompletedTasks", "NO ITEMS FOUND FOR QUERY `"+selectAll+"`");
        cursor.close();
        db.close();
        return taskList;
    }//end getAllCompletedTasks()
    public List<Tasker> getAllNoncompletedTasks(int catid){
        //Return all tasks with `status` NOT marked complete (i.e. 0);
        SQLiteDatabase db     = this.getWritableDatabase();
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll      = "SELECT * FROM " + TABLE_TASKS;
        String whereclause    = "";
        whereclause += " WHERE 1=1 ";
        if (catid != 0) {
            whereclause += " AND "+ KEY_CATEGORY_ID +" = " + catid + " ";
        }
        whereclause += " AND status = 0 ";
        selectAll += whereclause;
        selectAll += " ORDER BY place ";
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
                tasker.setCatId(cursor.getInt(5));
                taskList.add(tasker);
                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0)
            Log.w("getAllNoncompletedTasks", "NO ITEMS FOUND FOR QUERY `"+selectAll+"`");
        cursor.close();
        db.close();
        return taskList;
    }//end getAllNoncompletedTasks()
    public List<Tasker> getAllTasks(int catid) {
        //Return List of all tasks from given categories id;
        SQLiteDatabase db     = this.getWritableDatabase();
        List<Tasker> taskList = new ArrayList<Tasker>();
        String selectAll      = "SELECT * FROM " + TABLE_TASKS;
        if (catid != 0) {
            selectAll += " WHERE " + KEY_CATEGORY_ID + " = " + catid + " ";
        }
        selectAll += " ORDER BY place ";
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
                tasker.setCatId(cursor.getInt(5));
                taskList.add(tasker);
                cnt++;
            } while (cursor.moveToNext());
        }
        if (cnt == 0)
            Log.w("TBDH: getAllTasks", "NO ITEMS FOUND FOR QUERY `"+selectAll+"`");
        cursor.close();
        db.close();
        return taskList;
    }//end getAllTasks()
    public Categories getCategories(int catId){
        /*
         * Tanner 20180816
         * Return the Categories obj from provided Cat `id`;
         */
        SQLiteDatabase db = this.getReadableDatabase();
        Categories ret    = null;
        String query = "SELECT * FROM " + TABLE_CATEGORIES;
        String where = " WHERE 1=1";
        where += " AND " + KEY_ID + " = " + Integer.toString(catId) + "";
        query += where;
        Cursor cursor     = db.rawQuery(query, null);
        if(cursor.moveToNext()) {
            String category = cursor.getString(1);
            int place       = cursor.getInt(2);
            ret = new Categories(category, place);
        }
        cursor.close();
        db.close();
        return ret;
    }//end getCategories()
    public int getCategoryCount(int catid){
        //TODO: Refractor this to countCategory;
        //BUG: Do not include where clause if category is empty string
        int numOfCategories = -1;
        SQLiteDatabase db     = this.getReadableDatabase();
        String table          = TABLE_CATEGORIES;
        String select         = "SELECT count(*) from " + table + " WHERE " + KEY_ID+ "=" + catid +"";
        Cursor cursor         = db.rawQuery(select, null);
        if (cursor.moveToNext())
            numOfCategories = cursor.getInt(0);
        cursor.close();
        db.close();
        return numOfCategories;
    }//end getCategoryCount()
    public String getCategoryFromId(int id){
        //Categories id, not Tasker catid;
        String cat = "";
        Categories categories = getCategories(id);
        if (categories != null)
            cat = categories.getCategory();
        return cat;
    }//end getCategoryFromId()
    public int getCategoryPlace(int catid){
        Categories cat = getCategories(catid);
        return cat.getPlace();
    }
    public Tasker getTask(int taskId){
        /*
         * Tanner 20180816
         * Return the task name depending on the table specified;
         */
        Tasker ret          = null;
        SQLiteDatabase db   = this.getReadableDatabase();
        String query        = "SELECT * FROM " + TABLE_TASKS;
        String where        = " WHERE 1=1";
        where += " AND " + KEY_ID + " = " + Integer.toString(taskId) + "";
        query += where;
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToNext()) {
            String category = cursor.getString(1);
            String content  = cursor.getString(2);
            int status      = cursor.getInt(3);
            int place       = cursor.getInt(4);
            int catId       = cursor.getInt(5);
            ret = new Tasker(category, content, status, place, catId);
        }
        cursor.close();
        db.close();
        return ret;
    }//end getTask
    public String getTaskCategory(int id){
        String ret = "";
        Tasker task = getTask(id);
        if(task != null)
            ret = task.getCategory();
        return ret;
    }
    public int getTaskPlace(int id){
        int ret = -1;
        Tasker task = getTask(id);
        if(task != null)
            ret = task.getPlace();
        return ret;
    }//end getTaskPlace()
    public int getTaskStatus(int id){
        int ret = -1;
        Tasker task = getTask(id);
        if(task != null)
            ret = task.getStatus();
        return ret;
    }//end getTaskStatus()

    /**************************** UPDATE FUNCTIONS ****************************/
    public boolean updateCatCategory(int catId, String newCategory){
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
        whereCat += " AND " + KEY_ID + "=" + Integer.toString(catId) + "";
        updateCat += setCat + whereCat;

        String updateTask = "UPDATE " + TABLE_TASKS;
        String setTask    = " Set " + KEY_CATEGORY + " = '" + newCategory + "'";
        String whereTask  = " WHERE 1=1";
        whereTask += " AND " + KEY_CATEGORY_ID + " = " + Integer.toString(catId) + "";
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
        db.close();
        return ret;
    }//end updateCatCategory()
    public boolean updateCategoryPlace(int catid, int newPlace){
        /*
         *  Tanner 20180814
         *  Update item in Category table with `newPlace` via `id`
         */
        boolean ret         = false;
        SQLiteDatabase db   = this.getWritableDatabase();
        String update       = "UPDATE " + TABLE_CATEGORIES;
        String set          = " SET " + KEY_PLACE + "='"+newPlace+"'";
        String where        = " WHERE 1=1";
        where += " AND " + KEY_ID + "=" + Integer.toString(catid) + "";
        update += set + where;
        try {
            db.execSQL(update);
            ret = true;
        }
        catch (SQLException e){
            Log.e("TDBH: updateCategoryPlace", "ISSUE WITH QUERY `" + update + "`");
            Log.e("TDBH: updateCategoryPlace", e.getMessage());
        }
        db.close();
        return ret;
    }//end updateCategoryPlace()
    public int updateCategoryPlaceOnDelete(int place){
        /*
         *  Tanner 20180814
         *  Run `upate` statements on all Category items where item.place > place;
         *  Update statement will set item.place to (item.place - 1);
         */
        SQLiteDatabase db = this.getWritableDatabase();
        String query      = "SELECT * FROM " + TABLE_CATEGORIES;
        String where      = " WHERE 1=1";
        where += " AND " + KEY_PLACE + " > " + place + "";
        query += where;
        Cursor cursor = db.rawQuery(query, null);
        int updates   = 0;
        if(cursor.moveToNext()){
            do{
                int id       = cursor.getInt(0);
                int newPlace = cursor.getInt(2)-1;
                if(updateCategoryPlace(id, newPlace))
                    updates++;
            } while(cursor.moveToNext());
        }
        else
            Log.w("TDBH: UCPOD", "ISSUE WITH QUERY `" + query + "`");
        cursor.close();
        db.close();
        return updates;
    }//end updateCategoryPlaceOnDelete
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

    public boolean updateTaskContent(int taskId, String newContent){
        /*
         *  Tanner 20180816
         *  Update item in Task table with `newContent` via `id`
         */
        boolean ret              = false;
        String prcocessedContent = processInput(newContent);
        String update            = "UPDATE " + TABLE_TASKS;
        String set               = " SET " + KEY_CONTENT + "='" + prcocessedContent + "'";
        String where             = " WHERE 1=1";
        where += " AND " + KEY_ID + "=" + Integer.toString(taskId) + "";
        update += set + where;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(update);
            ret = true;
        }
        catch (SQLException e){
            Log.e("TDBH: updateTaskContent", "ISSUE WITH QUERY `" + update + "`");
            Log.e("TDBH: updateTaskContent", e.getMessage());
        }
        db.close();
        return ret;
    }
    public boolean updateTaskPlace(int taskId, int newPlace){
        /*
         *  Tanner 20180814
         *  Update item in Category table with `newPlace` via `id`
         */
        boolean ret         = false;
        SQLiteDatabase db   = this.getWritableDatabase();
        String update       = "UPDATE " + TABLE_TASKS;
        String set          = " SET " + KEY_PLACE + "='"+newPlace+"'";
        String where        = " WHERE 1=1";
        where += " AND " + KEY_ID + "='" + taskId + "'";
        update += set + where;
        try {
            db.execSQL(update);
            ret = true;
        }
        catch (SQLException e){
            Log.e("TDBH: updateTaskPlace", "ISSUE WITH QUERY `" + update + "`");
            Log.e("TDBH: updateTaskPlace", e.getMessage());
        }
        db.close();
        return ret;
    }//end updateTaskPlace()
    public int updateTaskPlaceOnDelete(int catid, int place){
        /*
         *  Tanner 20180814
         *  Run `update` statements on all Category items where item.place > place;
         *  Update statement will set item.place to (item.place - 1);
         */
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_TASKS;
        String where = " WHERE 1=1";
        where += " AND " + KEY_PLACE + " > " + place + "";
        where += " AND " + KEY_CATEGORY_ID + "='" + catid + "'";
        query += where;
        Cursor cursor = db.rawQuery(query, null);
        int updates   = 0;
        if(cursor.moveToNext()){
            do{
                int id       = cursor.getInt(0);
                int newPlace = cursor.getInt(4)-1;
                if(updateTaskPlace(id, newPlace))
                    updates++;
            } while(cursor.moveToNext());
        }
        else
            Log.e("TDBH: UTPOD", "ISSUE WITH QUERY `" + query + "`");
        cursor.close();
        db.close();
        return updates;
    }//end updateTaskPlaceOnDelete
}