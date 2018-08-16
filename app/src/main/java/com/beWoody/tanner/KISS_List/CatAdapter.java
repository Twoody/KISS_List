package com.beWoody.tanner.KISS_List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class CatAdapter extends ArrayAdapter<Categories> {
    Context context;
    List<Categories> catList = new ArrayList<Categories>();
    int layoutResourceId;
    public CatAdapter(Context context, int layoutResourceId, List<Categories> objects){
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.catList = objects;
        this.context = context;
    }//end constructor

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(
                R.layout.list_categories,
                parent,
                false);
        Button catbut = (Button)rowView.findViewById(R.id.catButton);
        Categories current = catList.get(position);
        catbut.setText(current.getCategory());
        rowView.setTag(catbut);
        return rowView;
    }
}
