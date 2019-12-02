package com.selimkilicaslan.computer_project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ActionActivity extends AppCompatActivity {

    ImageView imageView;
    ListView listView;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    float xcord;
    float ycord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        imageView=findViewById(R.id.imageView);
        listView = (ListView)findViewById(R.id.actionslistview);
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        startChronometer();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                    pauseChronometer();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(ActionActivity.this, "Match finishes", Toast.LENGTH_SHORT).show();
                }

            }

        });

        String actionsList[] = {"3pt #11k. John", "pt #11k. John", "pt #11k. John", "pt #11k. John", "pt #11k. John", "pt #11k. John"};


        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_listview,R.id.textView,actionsList);

        listView.setAdapter(adapter);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                pauseChronometer();
                final Dialog dialog = new Dialog(ActionActivity.this);
                dialog.setContentView(R.layout.popup_layout);
                dialog.show();

                xcord=event.getX();
                ycord=event.getY();

                Log.i("X", String.valueOf(event.getX()));
                Log.i("X", String.valueOf(event.getY()));

                return false;
            }
        });
    }

    public void addActionBtnOnClick(View view) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.popup_layout);
        dialog.show();
    }

    public void actionPressed(View view) {
        switch (view.getTag().toString()){
            case "1":

                break;
            case "2":

                break;
            case "3":

                break;
            case "4":

                break;
            case "5":

                break;
            case "6":

                break;
            case "7":

                break;
            case "8":

                break;

        }


    }
    public void startChronometer() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }
    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

}
