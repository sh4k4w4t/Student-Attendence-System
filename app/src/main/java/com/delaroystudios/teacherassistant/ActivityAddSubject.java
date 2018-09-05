package com.delaroystudios.teacherassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityAddSubject extends AppCompatActivity {

    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);


        Button btn = (Button) findViewById(R.id.buttonADD);
        assert btn != null;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase(v);
            }
        });
    }


    public void saveToDatabase(View view) {
        EditText Ids = (EditText)findViewById(R.id.Sub_Id);
        EditText Names = (EditText)findViewById(R.id.Sub_Name);

        if(Ids.getText().length()==0||Names.getText().length()==0)
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle("Invalid");
            alert.setMessage("Insufficient Data");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }

        String qu = "INSERT INTO SUBJECT VALUES('" +Ids.getText().toString()+ "'," +
                "'" + Names.getText().toString().toUpperCase() +"');";
        Log.d("Subject Add" , qu);
        AppBase.handler.execAction(qu);
        qu = "SELECT * FROM SUBJECT WHERE SUBID = '" + Ids.getText().toString() +  "';";
        Log.d("Subject Add" , qu);
        if(AppBase.handler.execQuery(qu)!=null)
        {
            Toast.makeText(getBaseContext(),"Subject Added", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }
}
