package com.example.tanner.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{
    private Button button;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        button = findViewById(R.id.floatingActionButton2);
        button.setOnClickListener(openCategoryMenu(););
    }//end onCreate()

    public void openCategoryMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        /*
        * Open a menu with EditText and `submit` button
        */
        //Intent intent = new Intent(this, addCategoryMenu);
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Add a New Checklist");
        getMenuInflater().inflate(R.menu.category_menu, menu);
        Toast.makeText(getApplicationContext(), "Peroggative: Open a menu; Incomplete;", Toast.LENGTH_LONG);

    }//end addCategoryToDatabase

}//end MainActivity
