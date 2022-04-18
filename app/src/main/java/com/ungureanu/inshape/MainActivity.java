package com.ungureanu.inshape;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    //vars
    private ArrayList<String> Names = new ArrayList<>();
    private ArrayList<String> Kgs = new ArrayList<>();
    private ArrayList<String> Sets = new ArrayList<>();
    private ArrayList<String> Reps = new ArrayList<>();
    private FloatingActionButton floatingActionButton;
    private EditText editName;
    private EditText editKgs;
    private EditText editSets;
    private EditText editReps;
    private Context context;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private DatabaseHelper myDb;
    private Cursor cursor;
    private String useCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Log.d(TAG, "onCreate: called");

        Intent intent = getIntent();
        this.useCase = intent.getStringExtra("message");
        //Toast.makeText(MainActivity.this, useCase, Toast.LENGTH_SHORT).show();

        context = this;
        myDb = new DatabaseHelper(this.context);
        this.cursor = myDb.getAllData(useCase);

        init();
        setFloatingActionButtonGUI();
        setEditNameGUI();
        setEditKgsGUI();
        setEditSetsGUI();
        setEditRepsGUI();
        setFloatingActionButtonListener();
    }

    private void setEditNameGUI() {
        //Log.d(TAG, "setEditNameGUI: called");
        this.editName = (EditText) findViewById(R.id.idEditName);
    }

    private void setEditKgsGUI() {
        //Log.d(TAG, "setEditKgsGUI: called");
        this.editKgs = (EditText) findViewById(R.id.idEditKilos);
    }

    private void setEditSetsGUI() {
        //Log.d(TAG, "setEditSetsGUI: called");
        this.editSets = (EditText) findViewById(R.id.idEditSets);
    }

    private void setEditRepsGUI() {
        //Log.d(TAG, "setEditRepsGUI: called");
        this.editReps = (EditText) findViewById(R.id.idEditReps);
    }

    private void setFloatingActionButtonGUI() {
        //Log.d(TAG, "setFloatingActionButton: called");
        this.floatingActionButton = (FloatingActionButton) findViewById(R.id.idFloatingActionButton);
    }

    private void setFloatingActionButtonListener() {
        //Log.d(TAG, "setFloatingActionButtonListener: called");
        this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: called");

                if(editName.getText().toString().length() > 0 &&
                    editKgs.getText().toString().length() > 0 &&
                    editSets.getText().toString().length() > 0 &&
                    editReps.getText().toString().length() > 0) {


                    Names.add(editName.getText().toString());
                    Kgs.add(editKgs.getText().toString());
                    Sets.add(editSets.getText().toString());
                    Reps.add(editReps.getText().toString());
//
//                initRecyclerView();

                    boolean isInserted = myDb.insertData(editName.getText().toString(),
                            editKgs.getText().toString(),
                            editSets.getText().toString(),
                            editReps.getText().toString(),
                            useCase);

                    if (isInserted == true) {
                        //Toast.makeText(context, "Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Not added to list", Toast.LENGTH_SHORT).show();
                    }

                    adapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(MainActivity.this, "Fill empty fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void init() {

        initRecyclerView();

        StringBuffer stringBuffer = new StringBuffer();
        while (cursor.moveToNext()){
            Names.add(cursor.getString(1));
            Kgs.add(cursor.getString(2));
            Sets.add(cursor.getString(3));
            Reps.add(cursor.getString(4));
        }

    }

    private void initRecyclerView() {
        //Log.d(TAG, "initRecyclerView: called");

        recyclerView = findViewById(R.id.idRecyclerView);

        adapter = new RecyclerViewAdapter(context, Names, Kgs, Sets, Reps);

        ItemTouchHelper.Callback callback = new MyItemTouchHelper(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);

        adapter.setItemTouchHelperAdapter(itemTouchHelper);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,
                DividerItemDecoration.VERTICAL));        //adapter.notifyItemRangeInserted(0, Names.size());
        adapter.notifyDataSetChanged();
    }
//
//    ItemTouchHelper.SimpleCallback itemTOuchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//            Names.remove(viewHolder.getAdapterPosition());
//            Kgs.remove(viewHolder.getAdapterPosition());
//            Sets.remove(viewHolder.getAdapterPosition());
//            Reps.remove(viewHolder.getAdapterPosition());
//            adapter.notifyDataSetChanged(); //Not the best
//        }
//    };
//
//    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack = new ItemTouchHelper.Callback();

}