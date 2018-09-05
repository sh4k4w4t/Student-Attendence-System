package com.delaroystudios.teacherassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivitySubject extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> Ids;
    ArrayList<String> Names;
    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_sub);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent launchIntent = new Intent(getBaseContext(), ActivityAddSubject.class);
                startActivity(launchIntent);
            }
        });

        Ids = new ArrayList<>();
        Names = new ArrayList<>();
        listView = (ListView)findViewById(R.id.subjectList);
        loadSchedules();
        listView.setOnItemLongClickListener(this);
    }

    private void loadSchedules() {
        Ids.clear();
        Names.clear();
        String qu = "SELECT * FROM SUBJECT ORDER BY SUBNAME";
        Cursor cursor = AppBase.handler.execQuery(qu);
        if(cursor==null||cursor.getCount()==0)
        {
            Toast.makeText(getBaseContext(),"No Subjests Available",Toast.LENGTH_LONG).show();
        }else
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                Names.add(cursor.getString(1) + " (" + cursor.getString(0) + ')');
                Ids.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(),android.R.layout.simple_list_item_1,Names);
        listView.setAdapter(adapter);
    }


    public void refresh(MenuItem item) {
        loadSchedules();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scheduler_menu, menu);
        return true;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle("Delete Subject?");
        alert.setMessage("Do you want to delete this schedule ?");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String qu = "DELETE FROM SUBJECT WHERE SUBID = '" + Ids.get(position) + "' ";
                if (AppBase.handler.execAction(qu)) {
                    Toast.makeText(getBaseContext(), "Deleted", Toast.LENGTH_LONG).show();
                    loadSchedules();
                } else {
                    Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();
                    loadSchedules();
                }
                dialog.dismiss();
            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
        return true;
    }
}
