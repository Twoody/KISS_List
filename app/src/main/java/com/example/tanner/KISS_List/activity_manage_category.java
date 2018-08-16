package com.example.tanner.KISS_List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

public class activity_manage_category extends AppCompatActivity {
    protected TaskerDBHelper db;
    CatAdapter adapt;
    List<Categories> list2;
    private FloatingActionButton fab;
    ListView listTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        db       = new TaskerDBHelper(this);
        list2    = db.getAllCategories();
        adapt    = new CatAdapter(this, R.layout.list_categories, list2);
        listTask = findViewById(R.id.listView_categories);
        listTask.setAdapter(adapt);

        registerForContextMenu(listTask);

        fab = findViewById(R.id.fab_add_category);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(activity_manage_category.this, Pop.class));
            }
        });
    }//end onCreate()

    @Override
    public void onResume(){
        super.onResume();
        refreshUIThread();
    }

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
        getMenuInflater().inflate(R.menu.category_menu, menu);
    }//end onCreateContextMenu()

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Resources res = getResources();
        boolean debug = res.getBoolean(R.bool.debug);
        int item_id   = item.getItemId();
        ContextMenu.ContextMenuInfo CMI = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) CMI;
        Categories obj   = adapt.getItem(acmi.position);
        String category  = obj.getCategory();
        db               = new TaskerDBHelper(this);
        boolean ret      = true;
        String toastText = "";
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
        refreshUIThread();//BUG: NEED A BOOLEAN CHECK IF DATA WAS CHANGED OR NOT
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
        Intent intent = new Intent(this, activity_manage_category.class);
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
            Resources res = getResources();
            boolean debug = res.getBoolean(R.bool.debug);
            Button catbut = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE
                );
                convertView = inflater.inflate(
                        R.layout.list_categories,
                        parent,
                        false
                );
                catbut = convertView.findViewById(
                        R.id.catButton
                );
                convertView.setTag(catbut);
                catbut.setOnClickListener( new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Button but                = (Button) v;
                        Categories changeActivity = (Categories) but.getTag();
                        Toast toast = Toast.makeText(
                                getApplicationContext(),
                                "Clicked on Button: " + but.getTag() +"\nTEXT:" + but.getText(),
                                Toast.LENGTH_LONG
                        );
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        String category = changeActivity.getCategory();
                        openTasksActivity(category); //ListView activity of each task per category selected.
                    }//end onClick()
                });//end chk.setOnClickListener()
            }
            else{
                catbut = (Button) convertView.getTag();
            }
            Categories current    = catList.get(position);
            int place             = current.getPlace();
            String display        = current.getCategory();
            List listOfTasks      = db.getAllTasks(db.TABLE_TASKS, current.getCategory());
            List completedTasks   = db.getAllCompletedTasks(db.TABLE_TASKS, current.getCategory());
            int numberOfTasks     = listOfTasks.size();
            int numberOfCompleted = completedTasks.size();
            display += " (" + Integer.toString(numberOfCompleted);
            display += "/";
            display += Integer.toString(numberOfTasks) + ")";
            catbut.setText(display);
            catbut.setTag(current);
            Log.d("listener", String.valueOf(current.getId()));
            return convertView;
        }//end getView

        public void openTasksActivity(String category){
            Intent intent;
            Class foo = activity_manage_tasks.class;
            intent = new Intent(activity_manage_category.this, foo);
            intent.putExtra("category", category);
            startActivity(intent);
        }//end openTasksActivity
    }//end MyAdaper
}//end activity_manage_category