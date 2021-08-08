package com.example.contact;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editTextname,editTextphone;
    Button saveButton,showButton;
    public static DatabaseHelper mSQLiteHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("New Record");


        //EditText
        editTextname = findViewById(R.id.editName);
        editTextphone = findViewById(R.id.editPhone);
        //Button
        saveButton = findViewById(R.id.savebuttonId);
        showButton = findViewById(R.id.showbuttonId);

        //Creating Database
        mSQLiteHelper = new DatabaseHelper(this,"RECORDDB.sqlite",null,1);

        //Creating Table in Database
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , phone VARCHAR )");


        //add record to SQLite
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                try{

                    mSQLiteHelper.insertData(editTextname.getText().toString().trim(),
                                              editTextphone.getText().toString().trim());

                    Toast.makeText(getApplicationContext(), "Successfully Data Inserted", Toast.LENGTH_LONG).show();

                    //reset views
                    editTextname.setText("");
                    editTextphone.setText("");
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Show recordList to SQLite
        showButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),RecordListActivity.class));

            }
        });




    }
}