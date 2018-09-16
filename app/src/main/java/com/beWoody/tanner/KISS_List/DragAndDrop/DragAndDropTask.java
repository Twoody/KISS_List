package com.beWoody.tanner.KISS_List.DragAndDrop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beWoody.tanner.KISS_List.DragHelper.OnStartDragListener;
import com.beWoody.tanner.KISS_List.DragHelper.SimpleItemTouchHelperCallback;
import com.beWoody.tanner.KISS_List.R;
import com.beWoody.tanner.KISS_List.Tasker;
import com.beWoody.tanner.KISS_List.TaskerDBHelper;

import java.util.List;

public class DragAndDropTask extends AppCompatActivity implements OnStartDragListener {
    protected TaskerDBHelper db;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;
    private Button submit;
    private Button cancel;
    private List<Tasker> tasklist;
    private int catId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_and_drop_task);
        db                  = new TaskerDBHelper(this);
        Intent parentIntent = getIntent();
        Bundle parentBD     = parentIntent.getExtras();

        if (parentBD != null)
            catId   = (int) parentBD.get("catId");
        else
            catId = 0;
        String cat = db.getCategoryFromId(catId);
        Toolbar taskToolbar = (Toolbar) findViewById(R.id.toolbar_task_drag_and_drop);
        setSupportActionBar(taskToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Text to be displayed in toolbar
        TextView toolbarTitle = (TextView) taskToolbar.findViewById(R.id.toolbar_title_task_drag_and_drop);
        toolbarTitle.setText("Simplify " + cat);

        tasklist = db.getAllTasks(catId);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        final DAD_AdapterTask mAdapter = new DAD_AdapterTask(getApplicationContext(), this, catId);
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
                    Tasker task = mAdapter.getTasker(i);
                    int taskid  = task.getId();
                    db.updateTaskPlace(taskid, i+1);
                }//end i-for

                for(int i=0; i<tasklist.size(); i++){
                    //Delete items that were swiped out of view;
                    Boolean didfinditem = false;
                    Tasker cur          = tasklist.get(i);
                    int curTaskId       = cur.getId();
                    for (int j=0; j<mAdapter.getItemCount(); j++){
                        Tasker listedItem = mAdapter.getTasker(j);
                        int listedItemTaskid   = listedItem.getId();
                        if (curTaskId == listedItemTaskid) {
                            didfinditem = true;
                            break;
                        }
                    }//end j-for
                    if (didfinditem == false)
                        db.deleteTask(curTaskId);
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
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }//end onBackPressed()
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //To go to the previous activity in the stack
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}