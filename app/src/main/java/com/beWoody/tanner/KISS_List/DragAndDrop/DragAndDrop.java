package com.beWoody.tanner.KISS_List.DragAndDrop;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.beWoody.tanner.KISS_List.R;

public class DragAndDrop extends AppCompatActivity implements MainFragment.OnListItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.drag_and_drop);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_drag_and_drop));

        if (savedInstanceState == null) {
        MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction()
        .add(R.id.content_drag_and_drop, fragment)
        .commit();
        }
    }

    @Override
    public void onListItemClick(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
            fragment = new RecyclerListFragment();
            break;

        case 1:
            fragment = new RecyclerGridFragment();
            break;
        }

        getSupportFragmentManager().beginTransaction()
            .replace(R.id.content_drag_and_drop, fragment)
            .addToBackStack(null)
            .commit();
    }
}
