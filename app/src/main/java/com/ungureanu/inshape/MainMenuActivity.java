package com.ungureanu.inshape;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class MainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainMenuActivity.class.getSimpleName();
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private Button drawerButton;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView textViewHello;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        setFragmentManager();
        setToolbarGUI();
        //disableActionBar();
        setDrawerLayout();
        setNavigationViewGUI();
        setNavigationViewItemSelected();
        setDrawerButtonGUI();
        setOnClick_DrawerButton();
        setTextViewHello();
    }

    public void programEx(View view){

        String message = "";

        switch (view.getId()) {
            case R.id.idCardViewArm:
                message = "arm";
                break;
            case R.id.idCardViewChest:
                message = "chest";
                break;
            case R.id.idCardViewLeg:
                message = "leg";
                break;
            case R.id.idCardViewBack:
                message = "back";
                break;
            case R.id.idCardViewShoulder:
                message = "shoulder";
                break;
            case R.id.idCardViewAbs:
                message = "abs";
                break;
            case R.id.idCardViewSoon:
                message = "soon";
                break;
        }

        if(!message.equals("soon")) {
            Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
            intent.putExtra("message", message);
            startActivityForResult(intent, 100);// Activity is started with requestCode 100
        }else {
            Toast.makeText(MainMenuActivity.this, "Working on this...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== 2)
        {
            String message=data.getStringExtra("MESSAGE");
            if(!message.equals("SUCCESS")){
                Log.e(TAG, "onActivityResult: called and something went wrong! :(");
            }
        }
    }

    private void setTextViewHello(){
        //Log.d(TAG, "setTextViewHello: called");
        this.textViewHello = findViewById(R.id.idHello);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HH");

        int temp = Integer.parseInt(sdf.format(calendar.getTimeInMillis()));

        if(temp > 12)
        {
            this.textViewHello.setText("Good Evening");
        }
        else
        {
            this.textViewHello.setText("Good Morning");
        }
    }

    private void setFragmentManager(){
        //Log.d(TAG, "setFragmentManager: called");
        this.fragmentManager=getSupportFragmentManager();
    }

    private void setNavigationViewGUI(){
        //Log.d(TAG, "setNavigationViewGUI: called");
        this.navigationView = findViewById(R.id.idNavView);
    }

    private void setNavigationViewItemSelected(){
        //Log.d(TAG, "setNavigationViewItemSelected: called");
        this.navigationView.setNavigationItemSelectedListener(this);
    }
    
    private void disableActionBar(){
        //Log.d(TAG, "disableActionBar: called");
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void setToolbarGUI(){
        //Log.d(TAG, "setToolbarGUI: called");
        //this.toolbar = (Toolbar) findViewById(R.id.idToolbar);
        //setSupportActionBar(this.toolbar);
        //getSupportActionBar().hide();
    }

    private void setDrawerButtonGUI(){
        //Log.d(TAG, "setDrawerButtonGUI: called");
        this.drawerButton = (Button) findViewById(R.id.idDrawerButton);
    }

    private void setDrawerLayout(){
        //Log.d(TAG, "setDrawerLayout: called");
        this.drawerLayout = (DrawerLayout) findViewById(R.id.idDrawer);
    }

    private void setOnClick_DrawerButton(){
        //Log.d(TAG, "setOnClick_DrawerButton: called");
        this.drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        //Log.d(TAG, "onBackPressed: called");
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)){
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            this.fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = fragmentManager.findFragmentById(R.id.idFragmentContainer);
            if(fragment!=null){
                this.fragmentTransaction.remove(fragment);
                this.fragmentTransaction.commit();
            }else{
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //Log.d(TAG, "onNavigationItemSelected: called");
        switch (item.getItemId()){
            case R.id.nav_weather:
                WeatherFragment weatherFragment = WeatherFragment.newInstance();
                getSupportFragmentManager().beginTransaction().replace(R.id.idFragmentContainer,
                        weatherFragment).commit();
                //weatherFragment.findWeather();
                break;

            case R.id.nav_bmi:
                getSupportFragmentManager().beginTransaction().replace(R.id.idFragmentContainer,
                        new BMIFragment()).commit();
                break;

            case R.id.nav_share:
                Toast.makeText(MainMenuActivity.this, "Not working at the moement", Toast.LENGTH_SHORT).show();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true; //Just so we know that an item was selected
    }
}