package com.beWoody.tanner.KISS_List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class RVCatAdapter extends RecyclerView.Adapter<RVCatAdapter.ViewHolder>{
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<Categories> catList = new ArrayList<Categories>();

    // data is passed into the constructor
    RVCatAdapter(Context context,  List<Categories> objects) {
        this.mInflater = LayoutInflater.from(context);
        this.catList = objects;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_categories, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Categories current = catList.get(position);
        String cat = current.getCategory();
        holder.myButton.setText(cat);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return catList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button myButton;

        ViewHolder(View itemView) {
            super(itemView);
            myButton = itemView.findViewById(R.id.RVcatButton);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Categories getItem(int id) {
        return catList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
