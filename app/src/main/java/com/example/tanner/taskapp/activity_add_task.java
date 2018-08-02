package com.example.tanner.taskapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_add_task extends AppCompatActivity {
    protected TaskerDBHelper db;
    MyAdapter adapt;
    List<Tasker> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        db = new TaskerDBHelper(this);
        list = db.getAllTasks();
        adapt = new MyAdapter(this, R.layout.list_inner_view, list);
        ListView listTask = findViewById(R.id.listView1);
        listTask.setAdapter(adapt);
    }

    public void addTaskNow(View v){
        EditText t = findViewById(R.id.editText1);
        String s1 = t.getText().toString();
        String s2 = t.getText().toString();
        if (s1.equalsIgnoreCase("") && s2.equalsIgnoreCase("") ){
            Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG);
        }
        else {
            Tasker task = new Tasker(s1, s2, 0, 1);
            db.addTask(task);
            Log.d("tasker", "adda added");
            t.setText("");
            adapt.add(task);
            adapt.notifyDataSetChanged();
        }
    }//end addTaskNow()

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu; This adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.activity_view_task, menu);
        return true;
    }//end onCreateOptionsMenu()
*/
    private class MyAdapter extends ArrayAdapter<Tasker>{
        Context context;
        List<Tasker> taskList = new ArrayList<Tasker>();
        int layoutResourceId;
        public MyAdapter(Context context, int layoutResourceId,
                         List<Tasker> objects) {
            super(context, layoutResourceId, objects);
            this.layoutResourceId = layoutResourceId;
            this.taskList = objects;
            this.context = context;
        }//end constructor

        /**
         * This method will DEFINe what the view inside the list view will
         * finally look like Here we are going to code that the checkbox state
         * is the status of task and check box text is the task name
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CheckBox chk = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                                           Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(
                                 R.layout.list_inner_view,
                                 parent,
                                 false);
                chk = convertView.findViewById(
                         R.id.checkBox1); //Not same as example...
                convertView.setTag(chk);
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
                            "Clicked on Checkbox: "+ cb.getTag() +": " + cb.getText() + " is " + cb.isChecked() + ": " +Integer.toString(isChecked),
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
    }//end MyAdaper
}//end activity_add_task
