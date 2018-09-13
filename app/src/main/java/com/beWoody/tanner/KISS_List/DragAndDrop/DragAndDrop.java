package com.beWoody.tanner.KISS_List.DragAndDrop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.beWoody.tanner.KISS_List.DragHelper.OnStartDragListener;
import com.beWoody.tanner.KISS_List.DragHelper.SimpleItemTouchHelperCallback;
import com.beWoody.tanner.KISS_List.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DragAndDrop extends AppCompatActivity implements OnStartDragListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ItemTouchHelper mItemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drag_and_drop);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_drag_and_drop));

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        final String[] items = getResources().getStringArray(R.array.main_items);
        List<String> mItems = new ArrayList<>();
        mItems.addAll(Arrays.asList(getApplicationContext().getResources().getStringArray(R.array.dummy_items)));

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        DAD_Adapter mAdapter = new DAD_Adapter(getApplicationContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

}
