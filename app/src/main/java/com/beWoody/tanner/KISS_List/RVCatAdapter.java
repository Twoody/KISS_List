package com.beWoody.tanner.KISS_List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class RVCatAdapter extends RecyclerView.Adapter<RVCatAdapter.MyViewHolder>{
    private LayoutInflater mInflater;
    private List<Categories> catList = new ArrayList<Categories>();

    // data is passed into the constructor
    RVCatAdapter(Context context,  List<Categories> objects) {
        this.mInflater = LayoutInflater.from(context);
        this.catList = objects;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button cat;

        public MyViewHolder(View view) {
            super(view);
            cat = (Button) view.findViewById(R.id.RVcatButton);
        }
    }

    public RVCatAdapter(List<Categories> catList) {
        this.catList = catList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_categories, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Categories category = catList.get(position);
        holder.cat.setText(category.getCategory());
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }
}
