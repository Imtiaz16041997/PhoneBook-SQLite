package com.example.contact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;

    public DatabaseHelper( Context context,  String name,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //Query Data
    public void queryData(String sql){
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(sql);
    }

    //Insert Data

    public void insertData(String name, String phone){
        SQLiteDatabase database = this.getWritableDatabase();

        //Query to insert record in database table
        //Here RECORD is a table name in db we will create in mainactivity
        String sql = "INSERT INTO RECORD VALUES (NULL, ? ,?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1,name);
        statement.bindString(2,phone);

        if(name.length() !=0 && phone.length() !=0){
            statement.executeInsert();
        }



    }

    //Update Data

    public void updateData(String name, String phone, int id){
        SQLiteDatabase database = this.getWritableDatabase();

        //Query to update record

        String sql = "UPDATE RECORD SET name=? , phone=? WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);



        statement.bindString(1,name);
        statement.bindString(2,phone);
        statement.bindDouble(3,(double)id);


        statement.execute();
        database.close();

    }

    //Delete Data

    public void deleteData(int id){

        SQLiteDatabase database = this.getWritableDatabase();

        //query to delete record using id
        String sql = "DELETE FROM RECORD WHERE id=?";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1,(double)id);

        statement.execute();
        database.close();

    }

    //Getting Data

    public Cursor getData(String sql){

        SQLiteDatabase database = this.getReadableDatabase();
            return database.rawQuery(sql,null);

    }

//    public Cursor searchData(String sql2){
//
//        SQLiteDatabase database = this.getReadableDatabase();
//        return database.rawQuery(sql2,null);
//
//    }

}
