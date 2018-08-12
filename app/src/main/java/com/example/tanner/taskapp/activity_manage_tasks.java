package com.example.tanner.taskapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
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
    protected TaskerDBHelper db;
    MyAdapter adapt;
    List<Tasker> list1;
    private FloatingActionButton fab_add_task;
    ListView listTask;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_tasks);

        Intent parentIntent = getIntent();
        Bundle parentBD     = parentIntent.getExtras();
        if (parentBD != null)
            category = (String) parentBD.get("category");
        else
            category = "";

        db       = new TaskerDBHelper(this);
        list1    = db.getAllTasks("tasksTable", category);
        adapt    = new MyAdapter(this, R.layout.list_inner_view, list1);
        listTask = findViewById(R.id.listView_tasks);
        listTask.setAdapter(adapt);
        registerForContextMenu(listTask);

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
        /*
         *  Author: Tanner - 20180806
         *  Data does not dynamically check the data when it is altered;
         *  Functions to update UI if data is altered;
         */
        list1.clear();
        list1.addAll(db.getAllTasks(db.TABLE_TASKS, category));
        adapt.notifyDataSetChanged();
        listTask.invalidateViews();
        listTask.refreshDrawableState();
    }//end refreshUIThread

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String foo = Integer.toString(v.getId());

        if(v.getId() == R.id.listView_categories) {
            ListView catView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Categories obj = (Categories) catView.getItemAtPosition(acmi.position);
        }
        else{
            Toast.makeText(this, "Tough shootin', Tex.", Toast.LENGTH_LONG).show();
        }
        menu.setHeaderTitle("Editing Tools:");
        getMenuInflater().inflate(R.menu.task_menu, menu);
    }//end onCreateContextMenu()

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        ContextMenu.ContextMenuInfo CMI = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) CMI;
        Tasker obj       = adapt.getItem(acmi.position);
        String category  = obj.getCategory();
        String content   = obj.getContent();
        String id        = Integer.toString(obj.getId());
        db               = new TaskerDBHelper(this);
        boolean ret      = true;
        String toastText = "";
        if (item_id == R.id.select_task)
            toastText += "Selected " + category;
        else if(item_id == R.id.delete_task){
            Boolean didDelete = db.deleteTask(id);
            if (didDelete)
                toastText += "Deleted " + content;
            else
                toastText += "\nERROR: DID NOT DELETE `" + content + ";\n\tCONTENT COULD NOT BE FOUND";
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
            /*
            *  BUG: cb.getText() is retruning the categroy instead of the content
            */
            Resources res = getResources();
            boolean debug = res.getBoolean(R.bool.debug);
            CheckBox chk  = null;
            if (convertView == null) {
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
                        Toast toast = Toast.makeText(
                                getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getTag() +":\nTEXT: " + cb.getText()
                                        + " is " + cb.isChecked() + ":\n BOOL: "
                                        + Integer.toString(isChecked),
                                Toast.LENGTH_LONG
                        );
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }//end onClick()
                });//end chk.setOnClickListener()
            }
            else{
                chk = (CheckBox) convertView.getTag();
            }
            Tasker current = taskList.get(position);
            if (debug == true) {
                String msg = "\nDEBUG:\t"+ current;
                msg += "\n\tID:\t\t\t"     + current.getId();
                msg += "\n\tCATEGORY:\t"   + current.getCategory();
                msg += "\n\tCONTENT:\t"    + current.getContent();
                msg += "\n\tSTATUS:\t\t"   + current.getStatus();
                msg += "\n\tPLACE:\t\t"    + current.getPlace();
                Log.d("listener: AMT", msg);
            }
            chk.setText(current.getContent());
            chk.setChecked(current.getStatus() == 1 ? true:false);
            chk.setTag(current);
            Log.d("listener", String.valueOf(current.getId()));
            return convertView;
        }//end getView
    }//end MyAdaper

}//end activity_manage_tasks()
