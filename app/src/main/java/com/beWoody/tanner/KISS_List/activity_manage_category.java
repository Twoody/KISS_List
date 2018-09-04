package com.beWoody.tanner.KISS_List;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_manage_category extends AppCompatActivity {
    /*
     *  Author:  Tanner Woody
     *  Date:    20180816
     *  TODO:
     *       1. Enable activity for changing `place` of items
     *            This should use `getAllCategories` and only have one listview
     *       2. Make the editText attribute responsive to characters being filled in;
     *            i.e. "Make toast" has 10 characters; Tell user they have used 10 out of 100
     *                  characters;
     *       3. Make `select` in contextmenu a `copy to clipboard option instead;
     */
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        //myToolbar.setTitle(mTitle.getText());

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(this, "Clicked on Settings", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_organize:
                // User chose the "Organize" action;
                // Start new activity designed to organize tasks;
                Toast.makeText(this, "Clicked on Organize", Toast.LENGTH_LONG).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v.getId() == R.id.listView_categories) {
            ListView catView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        }
        else{
            Toast.makeText(this, "Tough shootin', Tex.", Toast.LENGTH_LONG).show();
        }
        getMenuInflater().inflate(R.menu.category_menu, menu);
    }//end onCreateContextMenu()

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int item_id   = item.getItemId();
        ContextMenu.ContextMenuInfo CMI = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) CMI;
        Categories obj   = adapt.getItem(acmi.position);
        String category  = obj.getCategory();
        String catId     = Integer.toString(obj.getId());
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
        }
        else if(item_id == R.id.rename_cat){
                //Open a popup window;
                //autofill editTest with exiting `content`
                //update table with new `content`
                //make toast
            Intent renamePopup = new Intent(activity_manage_category.this, Pop_renameContent_cat.class);
            renamePopup.putExtra("catId", catId);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
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
                        String catId              = Integer.toString(changeActivity.getId());
                        String changeCategory     = changeActivity.getCategory();
                        Toast toast = Toast.makeText(
                                getApplicationContext(),
                                "Opening " + changeCategory,
                                Toast.LENGTH_LONG
                        );
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        openTasksActivity(catId); //ListView activity of each task per category selected.
                    }//end onClick()
                });//end chk.setOnClickListener()
            }
            else{
                catbut = (Button) convertView.getTag();
            }
            Categories current    = catList.get(position);
            String display        = current.getCategory();
            List listOfTasks      = db.getAllTasks(current.getCategory());
            List completedTasks   = db.getAllCompletedTasks(current.getCategory());
            int numberOfTasks     = listOfTasks.size();
            int numberOfCompleted = completedTasks.size();
            display += " (" + Integer.toString(numberOfCompleted);
            display += "/";
            display += Integer.toString(numberOfTasks) + ")";
            catbut.setText(display);
            catbut.setTag(current);
            return convertView;
        }//end getView

        public void openTasksActivity(String catId){
            Intent intent;
            Class foo = activity_manage_tasks.class;
            intent    = new Intent(activity_manage_category.this, foo);
            intent.putExtra("catId", catId);
            startActivity(intent);
        }//end openTasksActivity
    }//end MyAdaper
}//end activity_manage_category