package com.example.contact;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.contact.model.Model;
import java.util.ArrayList;

public class RecordListActivity extends AppCompatActivity {

    ListView mListView;
    ArrayList<Model> mList;
    RecordListAdapter mAdapter = null;

//    Button mButton;

    //Check Permission





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Record List");


        mListView=findViewById(R.id.listViewId);
        mList = new ArrayList<>();
        mAdapter = new RecordListAdapter(this,R.layout.row,mList);
        mListView.setAdapter(mAdapter);

//        mButton = findViewById(R.id.phone_call);


        if(ContextCompat.checkSelfPermission(RecordListActivity.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED){
            //When Permission is not granted
            // i Request for permission
            ActivityCompat.requestPermissions(RecordListActivity.this,new String[]{Manifest.permission.CALL_PHONE},100);
        }


        //get all data from sqlite with cursor

        Cursor cursor  = MainActivity.mSQLiteHelper.getData("SELECT * FROM RECORD");
        mList.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);

            mList.add(new Model(id,name,phone));

        }

        mAdapter.notifyDataSetChanged();
        if(mList.size() == 0){

            Toast.makeText(getApplicationContext(), "No Data is Available", Toast.LENGTH_SHORT).show();
        }



        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {

                //Alert dialog to display options of update and delete
                final CharSequence [] items = {"Update","Delete","Call"};
//                final CharSequence [] items = {"Update","Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(RecordListActivity.this);

                dialog.setTitle("Choose an Action");

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            //update
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while(c.moveToNext() ){
                                arrID.add(c.getInt(0));
                            }
                            //Show update Dialog
                            showDialogUpdate(RecordListActivity.this,arrID.get(position));
                        }
                        if(i==1){
                                //delete
                            Cursor c = MainActivity.mSQLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();

                            while(c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }

//                        if(i==2){
//
//                            String phone = view.findViewById(R.id.textphone).toString();
//                            Intent intent = new Intent(Intent.ACTION_DIAL);
//                            intent.setData(Uri.parse("tel:" + phone));
//                            getBaseContext().startActivity(intent);
//                        }
                                //2nd try
                        if(i==2){

                            TextView tvPhone = view.findViewById(R.id.textphone);
                            String phone = tvPhone.getText().toString();
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phone));
                            getBaseContext().startActivity(intent);
                        }

                    }
                });
                dialog.show();
                return true;
            }
        });

        //call

    }
    //check permission



    //DeleteDialog
    private void showDialogDelete(final int idRecord) {

        AlertDialog.Builder dialogDelete =new AlertDialog.Builder(RecordListActivity.this);
        dialogDelete.setTitle("Warning!!");
        dialogDelete.setMessage("Are you sure you want to delete this?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener(){


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                try{
                    MainActivity.mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(getApplicationContext(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                }catch (Exception error){
                    Log.e("Delete error",error.getMessage());
                }


            }
        });

        dialogDelete.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        dialogDelete.show();
    }


                //UpdateDialog
    private void showDialogUpdate(Activity activity, int position){

        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Update");

       final EditText updateNameId = dialog.findViewById(R.id.updateNameId);
       final EditText updatePhoneId = dialog.findViewById(R.id.updatePhoneId);
       final Button updatebuttonId = dialog.findViewById(R.id.updatebuttonId);

        //get Data Row Clicked  from SQLite

        Cursor cursor  = MainActivity.mSQLiteHelper.getData("SELECT * FROM RECORD WHERE id="+position);
        mList.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            updateNameId.setText(name);
            String phone = cursor.getString(2);
            updatePhoneId.setText(phone);
            mList.add(new Model(id,name,phone));

        }


        //set width of dialog
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        //set height of dialog
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width,height);
        dialog.show();



        updatebuttonId.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                try{

                    MainActivity.mSQLiteHelper.updateData(
                            updateNameId.getText().toString().trim(),
                            updatePhoneId.getText().toString().trim(),
                            position);

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                }catch(Exception error){
                    Log.e("Update error",error.getMessage());
                }
                updateRecorderList();

            }
        });

    }

    private void updateRecorderList() {

        //get all data from SQLite

        Cursor cursor = MainActivity.mSQLiteHelper.getData("SELECT * FROM RECORD");
        mList.clear();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);

            mList.add(new Model(id,name,phone));
        }
        mAdapter.notifyDataSetChanged();

    }


}