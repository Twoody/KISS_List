package com.beWoody.tanner.KISS_List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<Tasker> {
    Context context;
    List<Tasker> taskList = new ArrayList<Tasker>();
    int layoutResourceId;
    public TaskAdapter(Context context, int layoutResourceId, List<Tasker> objects){
        super(context, layoutResourceId, objects);
        this.layoutResourceId = layoutResourceId;
        this.taskList = objects;
        this.context = context;
    }//end constructor

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                                   Context.LAYOUT_INFLATER_SERVICE
        );
        View rowView = inflater.inflate(
                          R.layout.list_inner_view,
                          parent,
                          false
        );
        CheckBox chk = (CheckBox)rowView.findViewById(R.id.checkbox_task);
        Tasker current = taskList.get(position);
        chk.setText(current.getContent());
        chk.setChecked(current.getStatus()==1?true:false);
        return rowView;
    }
    public void updateView(){
        this.notifyDataSetChanged();
    }
}
