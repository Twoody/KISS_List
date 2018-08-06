package com.example.tanner.taskapp;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_add_category extends AppCompatActivity {
    protected TaskerDBHelper db;
    CatAdapter adapt;
    List<Tasker> list1;
    List<Categories> list2;
    private FloatingActionButton fab;
    ListView listTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        db    = new TaskerDBHelper(this);
        //list1 = db.getAllTasks("tasksTable"); //TODO: Add category parameter to getAllTasks()
        list2 = db.getAllCategories();
        adapt = new CatAdapter(this, R.layout.list_categories, list2);
        listTask = findViewById(R.id.listView1);
        listTask.setAdapter(adapt);

        registerForContextMenu(listTask);

        fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(activity_add_category.this, Pop.class));
                //TODO:
                //  adapt.notifyDataSetChanged(); //Should we have this instead of a new intent in Pop?
            }
        });
    }//end onCreate()

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String foo = Integer.toString(v.getId());

        if(v.getId() == R.id.listView1) {
            ListView catView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Categories obj = (Categories) catView.getItemAtPosition(acmi.position);
        }
        else{
            Toast.makeText(this, "Tough shootin', Tex.", Toast.LENGTH_LONG).show();
        }
        menu.setHeaderTitle("Editing Tools:");
        getMenuInflater().inflate(R.menu.category_menu, menu);
        //adapt.notifyDataSetChanged();
    }//end onCreateContextMenu()

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        ContextMenu.ContextMenuInfo CMI = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) CMI;
        Categories obj   = adapt.getItem(acmi.position);
        String category  = obj.getCategory();
        db               = new TaskerDBHelper(this);
        boolean ret      = true;
        String toastText = "";
        toastText += "ITEM_ID: `" + item_id + "`:\n";
        if (item_id == R.id.select_cat)
            toastText += "Selected " + category;
        else if(item_id == R.id.delete_cat){
            Boolean didDelete = db.deleteCat(category);
            if (didDelete)
                toastText += "Deleted " + category;
            else
                toastText += "\nERROR: DID NOT DELETE `" + category + ";\n\tCATEGORY COULD NOT BE FOUND";
            //openMainActivity(); // BUG: Need to refresh the list, not open the whole activity again...
        }
        else{
            toastText += "ERROR: NOTHING SELECTED";
            ret = super.onContextItemSelected(item);
        }
        Toast.makeText( this, toastText, Toast.LENGTH_SHORT).show();
        refreshUIThread();
        return ret;
    }//end onContextItemSelected

    public void refreshUIThread(){
        /*
        *  Author: Tanner - 20180806
        *  Data does not dynamically check the data when it is altered;
        *  Functions to update UI if data is altered;
        */
        list2.clear();
        list2.addAll(db.getAllCategories());
        adapt.notifyDataSetChanged();
        listTask.invalidateViews();
        listTask.refreshDrawableState();
    }//end refreshUIThread

    public void openMainActivity(){
        Intent intent = new Intent(this, activity_add_category.class);
        startActivity(intent);
    }//end openMainActivity()

    private class CatAdapter extends ArrayAdapter<Categories> {
        Context context;
        List<Categories> catList = new ArrayList<Categories>();
        int layoutResourceId;
        private CatAdapter(Context context,
                          int layoutResourceId,
                          List<Categories> objects)
        {
            super(context, layoutResourceId, objects);
            this.layoutResourceId = layoutResourceId;
            this.catList = objects;
            this.context = context;
        }//end constructor

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Button catbut = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(
                        R.layout.list_categories,
                        parent,
                        false);
                catbut = convertView.findViewById(
                        R.id.catButton);
                convertView.setTag(catbut);
                catbut.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Button but                = (Button) v;
                        Categories changeActivity = (Categories) but.getTag();
                        Toast toast = Toast.makeText(
                                getApplicationContext(),
                                "Clicked on Checkbox: " + but.getTag() +": " + but.getText(),
                                Toast.LENGTH_LONG
                        );
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }//end onClick()
                });//end chk.setOnClickListener()
            }
            else{
                catbut = (Button) convertView.getTag();
            }
            Categories current = catList.get(position);
            catbut.setText(current.getCategory());
            catbut.setTag(current);
            Log.d("listener", String.valueOf(current.getId()));
            return convertView;
        }//end getView

/*
        private class MyAdapter extends ArrayAdapter<Tasker> {
            Context context;
            List<Tasker> taskList = new ArrayList<Tasker>();
            int layoutResourceId;
            private MyAdapter(Context context,
                              int layoutResourceId,
                              List<Tasker> objects)
            {
                super(context, layoutResourceId, objects);
                this.layoutResourceId = layoutResourceId;
                this.taskList = objects;
                this.context = context;
            }//end constructor
*/
            /**
             * This method will Definee what the view inside the list view will
             * finally look like Here we are going to code that the checkbox state
             * is the status of task and check box text is the task name
             */
        /*
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox chk = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(
                        R.layout.list_categories,
                        parent,
                        false);
                catbut = convertView.findViewById(
                        R.id.catButton);
                convertView.setTag(catbut);
                chk.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        CheckBox cb       = (CheckBox) v;
                        Tasker changeTask = (Tasker) cb.getTag();
                        int isChecked     = cb.isChecked() == true ? 1 : 0;
                        changeTask.setStatus(isChecked);
                        //db.updateTask(changeTask);
                        Toast toast = Toast.makeText(
                                getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getTag() +": " + cb.getText()
                                        + " is " + cb.isChecked() + ": "
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
            chk.setText(current.getCategory());
            chk.setChecked(current.getStatus() == 1 ? true:false);
            chk.setTag(current);
            Log.d("listener", String.valueOf(current.getId()));
            return convertView;
        }//end getView
        */
    }//end MyAdaper
}//end activity_add_category
