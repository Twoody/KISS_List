package com.beWoody.tanner.KISS_List;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RVCatAdapter extends RecyclerView.Adapter<RVCatAdapter.MyViewHolder>{
    private LayoutInflater mInflater;
    private List<Categories> catList = new ArrayList<Categories>();
    protected TaskerDBHelper db;
    private final ClickListener listener;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    // data is passed into the constructor
    RVCatAdapter(Context context,  List<Categories> objects, ClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;
        this.catList = objects;
        db = new TaskerDBHelper(context);

    }

    public RVCatAdapter(List<Categories> catList, ClickListener listener) {
        this.listener = listener;
        this.catList = catList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View itemView = LayoutInflater.from(parent.getContext())
          //      .inflate(R.layout.recyclerview_categories, parent, false, listener);

        //return new MyViewHolder(itemView);
        //From stackoverflow.com
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_categories, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Categories category = catList.get(position);
        holder.catView.setText(category.getCategory());

        Categories current    = catList.get(position);
        String display        = current.getCategory();
        List listOfTasks      = db.getAllTasks(current.getCategory()); //BUG: Needs to be using catid;
        List completedTasks   = db.getAllCompletedTasks(current.getCategory());
        int numberOfTasks     = listOfTasks.size();
        int numberOfCompleted = completedTasks.size();
        display += " (" + Integer.toString(numberOfCompleted);
        display += "/";
        display += Integer.toString(numberOfTasks) + ")";
        holder.catView.setText(display);
        holder.catView.setTag(current);
        holder.catView.setOnClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                setPosition(holder.viewHolder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener,
            View.OnCreateContextMenuListener{
        private Button catView;
        private WeakReference<ClickListener> listenerRef;


        public MyViewHolder(View view, ClickListener listener) {
            super(view);

            listenerRef = new WeakReference<>(listener);
            catView = (Button) view.findViewById(R.id.RVcatButton);
            catView.setOnClickListener(this);
            catView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menuInfo is null
            menu.add(Menu.NONE, R.id.delete_cat,
                    Menu.NONE, "Delete");
            menu.add(Menu.NONE, R.id.rename_cat,
                    Menu.NONE, "Edit Text");
        }//end onCreateContextMenu()



        // onClick Listener for view
        @Override
        public void onClick(View v) {

            if (v.getId() == catView.getId()) {
                Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(v.getContext(), "ROW PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
            listenerRef.get().onPositionClicked(getAdapterPosition());
        }

        //onLongClickListener for view
        @Override
        public boolean onLongClick(View v) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Hello Dialog")
                    .setMessage("LONG CLICK DIALOG WINDOW FOR ICON " + String.valueOf(getAdapterPosition()))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            builder.create().show();
            listenerRef.get().onLongClicked(getAdapterPosition());
            return true;
        }
    }
}