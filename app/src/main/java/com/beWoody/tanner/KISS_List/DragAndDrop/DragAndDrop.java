package com.beWoody.tanner.KISS_List.DragAndDrop;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beWoody.tanner.KISS_List.Categories;
import com.beWoody.tanner.KISS_List.DragHelper.OnStartDragListener;
import com.beWoody.tanner.KISS_List.DragHelper.SimpleItemTouchHelperCallback;
import com.beWoody.tanner.KISS_List.R;
import com.beWoody.tanner.KISS_List.TaskerDBHelper;

import java.util.List;

public class DragAndDrop extends AppCompatActivity implements OnStartDragListener {
    protected TaskerDBHelper db;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private Button submit;
    private Button cancel;
    private List <Categories> catlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar taskToolbar = (Toolbar) findViewById(R.id.toolbar_drag_and_drop);
        setSupportActionBar(taskToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Text to be displayed in toolbar
        TextView toolbarTitle = (TextView) taskToolbar.findViewById(R.id.toolbar_title_drag_and_drop);
        toolbarTitle.setText("Simplify Categories");

        db      = new TaskerDBHelper(this);
        catlist = db.getAllCategories();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        final DAD_Adapter mAdapter = new DAD_Adapter(getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        submit = findViewById(R.id.button_dad_submit);
        submit.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //Get the current order of items in the adapter;
                //Update the positions of the items based on what is displayed;
                //Remove all items from db between what ListContents minus adapterContents;
                for (int i=0; i<mAdapter.getItemCount(); i++){
                    //Update all items with their new place;
                    Categories cat = mAdapter.getCategories(i);
                    int catid      = cat.getId();
                    db.updateCategoryPlace(catid, i+1);
                }//end i-for

                for(int i=0; i<catlist.size(); i++){
                    //Delete items that were swiped out of view;
                    Boolean didfinditem = false;
                    Categories cur = catlist.get(i);
                    int curCatid   = cur.getId();
                    for (int j=0; j<mAdapter.getItemCount(); j++){
                        Categories listedItem = mAdapter.getCategories(j);
                        int listedItemCatid   = listedItem.getId();
                        if (curCatid == listedItemCatid) {
                            didfinditem = true;
                            break;
                        }
                    }//end j-for
                    if (didfinditem == false)
                        db.deleteCat(curCatid);
                }//end i-for
                finish();
            }
        });
        cancel = findViewById(R.id.button_dad_cancel);
        cancel.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }//end onCreate()

    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onDestroy(){
        db.close();
        super.onDestroy();
    }
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
