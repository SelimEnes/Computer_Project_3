package com.selimkilicaslan.computer_project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class ActionActivity extends AppCompatActivity {

    ImageView imageView;
    ListView actionslistView;
    ListView playersListView;
    private Chronometer chronometer;
    long time;
    ArrayList <MatchAction> team1ActionsArrayList;
    ArrayList <MatchAction> team2ActionsArrayList;

    int TEAM_SELECTED=1;
    private long pauseOffset;
    private boolean running;
    private boolean endOfMatch;
    float xcord;
    float ycord;
    Dialog action_type_dialog;
    Dialog action_players_dialog;
    List actionlist;
    List playersList;
    ArrayAdapter actionsAdapter;
    ArrayAdapter playersArrayAdapter;
    Button team1Btn,team2Btn;
    MatchAction matchAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        team1ActionsArrayList =new ArrayList();
        team2ActionsArrayList =new ArrayList();

        imageView=findViewById(R.id.imageView);
        actionslistView =findViewById(R.id.actionslistview);
        chronometer = findViewById(R.id.chronometer);
        team1Btn=findViewById(R.id.team1Btn);
        team2Btn=findViewById(R.id.team2Btn);
        matchAction = new MatchAction();

        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        startChronometer();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 5000) {
                    endOfMatch=true;
                    pauseChronometer();
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(ActionActivity.this, "Match finishes", Toast.LENGTH_SHORT).show();
                }

            }

        });
        actionlist =new ArrayList();
        playersList=new ArrayList();
        playersList.add("ubey");
        playersList.add("selim");

        actionsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, actionlist);
        playersArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, playersList);


        actionslistView.setAdapter(actionsAdapter);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!endOfMatch) {
                    time=SystemClock.elapsedRealtime() - chronometer.getBase();
                    matchAction.setTime(time);
                    pauseChronometer();
                    action_type_dialog = new Dialog(ActionActivity.this);
                    action_type_dialog.setContentView(R.layout.action_popup_dialog);
                    action_type_dialog.show();
                    action_type_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            startChronometer();
                        }
                    });
                    xcord = event.getX();
                    ycord = event.getY();

                    matchAction.setLocationX(xcord);
                    matchAction.setLocationY(ycord);

                    Log.i("X", String.valueOf(event.getX()));
                    Log.i("y", String.valueOf(event.getY()));
                }else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    try {
                                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(ActionActivity.this.openFileOutput("hello.txt", ActionActivity.this.MODE_PRIVATE));
                                        outputStreamWriter.write("hello");
                                        outputStreamWriter.close();
                                    }
                                    catch (IOException e) {
                                        Log.e("Exception", "File write failed: " + e.toString());
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:

                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActionActivity.this);
                    builder.setMessage("The match ended. Do you want to export match analysis data to a txt file?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    Toast.makeText(ActionActivity.this,"Match end",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }

    public void actionPressed(View view) {
        String actionType=((TextView)view).getText().toString();
        matchAction.setType(actionType);
        action_type_dialog.dismiss();
        players_dialogShow();
    }
    public void players_dialogShow(){
        action_players_dialog = new Dialog(ActionActivity.this);
        action_players_dialog.setContentView(R.layout.action_players_dialog);
        action_players_dialog.show();
        playersListView=action_players_dialog.findViewById(R.id.playersListView);

        playersListView.setAdapter(playersArrayAdapter);

        playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                matchAction.setPerson1(playersList.get(position).toString());
                if(TEAM_SELECTED==1){
                    team1ActionsArrayList.add(matchAction);

                }else if (TEAM_SELECTED==2){
                    team2ActionsArrayList.add(matchAction);
                }
                actionlist.add(matchAction.getType()+"  "+ matchAction.getPerson1()+"  "+ matchAction.getActionTeamName()+"  "+ matchAction.getTime());
                action_players_dialog.dismiss();
                actionsAdapter.notifyDataSetChanged();
                startChronometer();
            }
        });

        action_players_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startChronometer();
            }
        });
    }

    public void startChronometer() {
        if (!running&& !endOfMatch) {
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

    public void teamPressed(View view) {

        if(view.getTag().toString().equals("0")){
            TEAM_SELECTED=1;
            String teamName=team1Btn.getText().toString();
           matchAction.setActionTeamName(teamName);
            team1Btn.setBackgroundColor(getColor(R.color.colorPrimary));
            team1Btn.setTextColor((Color.WHITE));
            team2Btn.setBackgroundColor(Color.WHITE);
            team2Btn.setTextColor((Color.BLACK));

        }else if(view.getTag().toString().equals("1")){
            TEAM_SELECTED=2;
            String teamName=team2Btn.getText().toString();
            matchAction.setActionTeamName(teamName);
            team2Btn.setBackgroundColor(getColor(R.color.colorPrimary));
            team2Btn.setTextColor((Color.WHITE));
            team1Btn.setBackgroundColor(Color.WHITE);
            team1Btn.setTextColor(Color.BLACK);
        }
    }
}
