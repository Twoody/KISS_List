package com.beWoody.tanner.KISS_List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_manage_tasks extends AppCompatActivity {
    /*
    *  Author:  Tanner Woody
    *  Date:    20180816
    *  TODO:
    *       1. Enable activity for changing `place` of items
    *            This should use `getAllTasks` and only have one listview
    *       2.
    *       3.
    */
    protected TaskerDBHelper db;
    MyAdapter adapt1;
    MyAdapter adapt2;
    List<Tasker> list1;
    List<Tasker> list2;
    private FloatingActionButton fab_add_task;
    ListView listTask1;
    ListView listTask2;
    Categories parentCat;
    String catId;
    String category;
    final int NCT_SELECT = 0; //NOT COMPLETED TASKS
    final int NCT_DELETE = 1; //NOT COMPLETED TASKS
    final int CT_SELECT  = 2; //COMPLETED TASKS
    final int CT_DELETE  = 3; //COMPLETED TASKS
    final int CT_RENAME  = 4; //COMPLETED TASKS
    final int NCT_RENAME = 5; //NOT COMPLETED TASKS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tasks);
        db = new TaskerDBHelper(this);
        Intent parentIntent = getIntent();
        Bundle parentBD     = parentIntent.getExtras();

        if (parentBD != null)
            catId   = (String) parentBD.get("catId");
        else
            catId = "";
        Log.d("taskInit", "catid: `" + catId + "`");
        if (catId != "")
            category = db.getCategoryFromId(catId);
        else
            category = "";
Log.d("AMT44", "cat: `"+category+"`");
        list1     = db.getAllNoncompletedTasks(category);
        adapt1    = new MyAdapter(this, R.layout.list_inner_view, list1);
        listTask1 = findViewById(R.id.listView_tasks);
        listTask1.setAdapter(adapt1);

        list2    = db.getAllCompletedTasks(category);
        adapt2    = new MyAdapter(this, R.layout.list_inner_view, list2);
        listTask2 = findViewById(R.id.listView_completedTasks);
        listTask2.setAdapter(adapt2);

        registerForContextMenu(listTask1);
        registerForContextMenu(listTask2);

        fab_add_task = findViewById(R.id.fab_add_task);
        fab_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start Pop_tasks.java activity
                Intent popup = new Intent(activity_manage_tasks.this, Pop_tasks.class);
                popup.putExtra("category", category);
                startActivity(popup);
            }//end onClick()
        });//end setOnClickListener()
    }//end onCreate()

    @Override
    public void onResume(){
        super.onResume();
        db = new TaskerDBHelper(this);
        refreshUIThread();
    }

    public void refreshUIThread(){
        refreshUIThread_notcompleted();
        refreshUIThread_completed();
    }

    public void refreshUIThread_completed(){
        /*
         *  Author: Tanner - 20180806
         *  Data does not dynamically check the data when it is altered;
         *  Functions to update UI if data is altered;
         */
        list2.clear();
        list2.addAll(db.getAllCompletedTasks(category));
        adapt2.notifyDataSetChanged();
        listTask2.invalidateViews();
        listTask2.refreshDrawableState();
    }//end refreshUIThread_notcompleted()

    public void refreshUIThread_notcompleted(){
        /*
         *  Author: Tanner - 20180806
         *  Data does not dynamically check the data when it is altered;
         *  Functions to update UI if data is altered;
         */
        list1.clear();
        list1.addAll(db.getAllNoncompletedTasks(category));
        adapt1.notifyDataSetChanged();
        listTask1.invalidateViews();
        listTask1.refreshDrawableState();
    }//end refreshUIThread_notcompleted()

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }//end onBackPressed()

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String foo = Integer.toString(v.getId());
        Tasker obj;
        String SELECT = "Select";
        String DELETE = "Delete";
        String RENAME = "Edit content";
        if(v.getId() == R.id.listView_tasks){
            ListView catView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            obj = (Tasker) catView.getItemAtPosition(acmi.position);
            menu.add(0, NCT_SELECT, 0, SELECT);
            menu.add(0, NCT_DELETE, 0, DELETE);
            menu.add(0, NCT_RENAME, 0, RENAME);
        }
        else if(v.getId() == R.id.listView_completedTasks) {
            ListView catView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            obj = (Tasker) catView.getItemAtPosition(acmi.position);
            menu.add(0, CT_SELECT, 0, SELECT);
            menu.add(0, CT_DELETE, 0, DELETE);
            menu.add(0, CT_RENAME, 0, RENAME);
        }
        else{
            Toast.makeText(this, "Tough shootin', Tex.", Toast.LENGTH_LONG).show();
        }
        getMenuInflater().inflate(R.menu.task_menu, menu);
    }//end onCreateContextMenu()

    @Override
    public boolean onContextItemSelected(MenuItem item){
        Tasker obj;
        Resources res = getResources();
        boolean debug = res.getBoolean(R.bool.debug);
        int item_id   = item.getItemId();
        ContextMenu.ContextMenuInfo CMI = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) CMI;
        if(item_id == NCT_SELECT || item_id == NCT_DELETE || item_id == NCT_RENAME)
            obj = adapt1.getItem(acmi.position);
        else if(item_id == CT_SELECT || item_id == CT_DELETE || item_id == CT_RENAME)
            obj = adapt2.getItem(acmi.position);
        else
            return false;

        int taskId       = obj.getId();
        String category  = obj.getCategory();
        String content   = obj.getContent();
        String id        = Integer.toString(obj.getId());
        db               = new TaskerDBHelper(this);
        boolean ret      = true;
        String toastText = "";
        if (item_id == CT_SELECT || item_id == NCT_SELECT)
            toastText += "Selected " + content;
        else if(item_id == NCT_DELETE || item_id == CT_DELETE){
            Boolean didDelete = db.deleteTask(id);
            if (didDelete)
                toastText += "Deleted " + content;
            else
                toastText += "\nERROR: DID NOT DELETE `" + content + ";\n\tCONTENT COULD NOT BE FOUND";
        }
        else if(item_id == CT_RENAME || item_id == NCT_RENAME){
            Intent renamePopup = new Intent(activity_manage_tasks.this, Pop_renameContent.class);
            renamePopup.putExtra("taskId", id);
            startActivity(renamePopup);
            toastText += "Edited task";
        }
        else{
            toastText += "ERROR: NOTHING SELECTED";
            ret = super.onContextItemSelected(item);
        }
        Toast.makeText( this, toastText, Toast.LENGTH_SHORT).show();
        refreshUIThread();//BUG: NEED A BOOLEAN CHECK IF DATA WAS CHANGED OR NOT
        return ret;
    }//end onContextItemSelected

    private class MyAdapter extends ArrayAdapter<Tasker> {
        Context context;
        List<Tasker> taskList = new ArrayList<Tasker>();
        int layoutResourceId;
        private MyAdapter(Context context,
                          int layoutResourceId,
                          List<Tasker> objects
                          )
        {
            super(context, layoutResourceId, objects);
            this.layoutResourceId = layoutResourceId;
            this.taskList         = objects;
            this.context          = context;
        }//end constructor

    /**
     * This method will Define what the view inside the list view will
     * finally look like Here we are going to code that the checkbox state
     * is the status of task and check box text is the task name
     */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox chk        = null;
            Tasker current      = taskList.get(position);
            if (convertView == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(
                        R.layout.list_inner_view,
                        parent,
                        false);
                chk = convertView.findViewById(
                        R.id.checkbox_task);
                convertView.setTag(chk);
                chk.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CheckBox cb       = (CheckBox) v;
                        Tasker changeTask = (Tasker) cb.getTag();
                        int isChecked     = cb.isChecked() == true ? 1 : 0;
                        changeTask.setStatus(isChecked); //1 is checked; 0 is not checked
                        db.updateTask(changeTask);
                        refreshUIThread();
                    }//end onClick()
                });//end chk.setOnClickListener()
            }
            else{
                chk = (CheckBox) convertView.getTag();
            }
            String display   = "";
            display += current.getContent();
            chk.setText(display);
            chk.setChecked(current.getStatus() == 1 ? true:false);
            chk.setTag(current);
            Log.d("listener", String.valueOf(current.getId()));
            return convertView;
        }//end getView
    }//end MyAdaper
}//end activity_manage_tasks()
