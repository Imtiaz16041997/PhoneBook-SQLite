package com.example.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private EditText editTextname,editTextphone;
    Button saveButton,showButton;
    public static DatabaseHelper mSQLiteHelper;

    AwesomeValidation awesomeValidation;

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

        //Initialize Validation Style
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        //Add for Name
        awesomeValidation.addValidation(this,R.id.editName, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        //Add for Mobile
        awesomeValidation.addValidation(this,R.id.editPhone,"[0-9]{11}$",R.string.invalid_phone);


        //Creating Database
        mSQLiteHelper = new DatabaseHelper(this,"RECORDDB.sqlite",null,1);

        //Creating Table in Database
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR , phone VARCHAR )");

        //add record to SQLite
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                try{
                    if(awesomeValidation.validate()){
                    Cursor c=mSQLiteHelper.findTask(editTextphone.getText().toString().trim());
                    if(c.moveToFirst())
                    {
                        Toast.makeText(getApplicationContext(), "Already Exist", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        // Inserting record
                        mSQLiteHelper.insertData(editTextname.getText().toString().trim(), editTextphone.getText().toString().trim());
                        Toast.makeText(getApplicationContext(), "Successfully Data Inserted", Toast.LENGTH_LONG).show();

                        //reset views
                        editTextname.setText("");
                        editTextphone.setText("");
                    }

                    }}catch(Exception e){

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