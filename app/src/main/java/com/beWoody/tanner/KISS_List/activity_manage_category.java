package com.beWoody.tanner.KISS_List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class activity_manage_category extends AppCompatActivity{
    /*
     *  Author:  Tanner Woody
     *  Date:    20180816
     *  TODO:
     *       1. Enable activity for changing `place` of items
     *            This should use `getAllCategories` and only have one listview
     *       2. Set the character limit for the editText
     *            Make the editText attribute responsive to characters being filled in;
     *            i.e. "Make toast" has 10 characters; Tell user they have used 10 out of 100
     *                  characters;
     *       3. Test renaming of lists
     */
    RVCatAdapter adapter;
    protected TaskerDBHelper db;
    List<Categories> list2;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category2);

        // data to populate the RecyclerView with
        db       = new TaskerDBHelper(this);
        list2    = db.getAllCategories();

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        registerForContextMenu(recyclerView);
        adapter = new RVCatAdapter(
                           this,
                           list2,
                           new ClickListener() {
                               @Override
                               public void onPositionClicked(int position) {
                                   // callback performed on click
                               }

                               @Override
                               public void onLongClicked(int position) {
                                   // callback performed on click
                               }

                               @Override
                               public void OnCreateContextMenuListener(int position) {
                                   // callback performed on click
                               }
                           });
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.fab_add_category);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(activity_manage_category.this, Pop.class));
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        refreshUIThread();
    }
    public void refreshUIThread(){
        /*
         *  Author: Tanner - 20180806
         *  Data does not dynamically check the data when it is altered;
         *  Functions to update UI if data is altered;
         */
        list2.clear();
        list2.addAll(db.getAllCategories());
        for (int i=0; i<list2.size(); i++){
            Categories foo = list2.get(i);
            Log.d("MEAT22", foo.getCategory());
        }
        adapter.notifyDataSetChanged();
    }//end refreshUIThread

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int item_id   = item.getItemId();
        ContextMenu.ContextMenuInfo CMI = item.getMenuInfo();
        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) CMI;
        Categories obj   = adapter.getItem(acmi.position);
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
            Intent renamePopup = new Intent(activity_manage_category2.this, Pop_renameContent_cat.class);
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
}


