package com.beWoody.tanner.KISS_List;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class activity_manage_category2 extends AppCompatActivity implements RVCatAdapter.ItemClickListener {

    RVCatAdapter adapter;
    protected TaskerDBHelper db;
    List<Categories> list2;

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
        adapter = new RVCatAdapter(this, list2);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

}
