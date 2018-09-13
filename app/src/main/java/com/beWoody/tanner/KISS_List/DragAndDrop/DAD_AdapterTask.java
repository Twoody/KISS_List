package com.beWoody.tanner.KISS_List.DragAndDrop;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beWoody.tanner.KISS_List.Categories;
import com.beWoody.tanner.KISS_List.DragHelper.ItemTouchHelperAdapter;
import com.beWoody.tanner.KISS_List.DragHelper.ItemTouchHelperViewHolder;
import com.beWoody.tanner.KISS_List.DragHelper.OnStartDragListener;
import com.beWoody.tanner.KISS_List.R;
import com.beWoody.tanner.KISS_List.Tasker;
import com.beWoody.tanner.KISS_List.TaskerDBHelper;

import java.util.Collections;
import java.util.List;

public class DAD_AdapterTask extends RecyclerView.Adapter<DAD_Adapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    protected TaskerDBHelper db;
    List<Tasker> list2;
    private final OnStartDragListener mDragStartListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DAD_AdapterTask(Context context, OnStartDragListener dragStartListener, int catid) {
        mDragStartListener = dragStartListener;
        db                 = new TaskerDBHelper(context);
        list2              = db.getAllTasks(catid);
        db.close();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DAD_Adapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.drag_and_drop_items,
                parent,
                false);

        DAD_Adapter.ItemViewHolder vh = new DAD_Adapter.ItemViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final DAD_Adapter.ItemViewHolder holder, int position) {
        Tasker task        = list2.get(position);
        String taskContent = task.getContent();
        holder.textView.setText(taskContent);
        holder.textView.setTag(task.getId());

        // Start a drag whenever the handle view is touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        list2.remove(position);
        notifyItemRemoved(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list2.size();
    }

    public List<Tasker> getList2() {
        return list2;
    }
    public Tasker getTasker(int position){
        return list2.get(position);
    }

    @Override
    public Boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(list2, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final ImageView handleView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            handleView = (ImageView) itemView.findViewById(R.id.handle);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
