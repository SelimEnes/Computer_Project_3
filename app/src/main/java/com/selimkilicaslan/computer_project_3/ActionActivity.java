package com.selimkilicaslan.computer_project_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ActionActivity extends AppCompatActivity {


    String firstTeam, secondTeam;
    String firstTeamID, secondTeamID;

    ImageView imageView;
    ListView actionslistView;
    private Chronometer chronometer;
    long time;
    List<MatchAction> matchActions;

    int TEAM_SELECTED=1;
    private long pauseOffset;
    private boolean running;
    private boolean endOfMatch;
    float xcord;
    float ycord;
    Dialog action_type_dialog;
    List actionlist;

    BasketballTeam team1, team2;
    BasketballMatch basketballMatch = new BasketballMatch();

    List<Player> team1Players = new ArrayList<>();
    List<Player> team2Players = new ArrayList<>();
    List team1Names = new ArrayList<>();
    List team2Names = new ArrayList<>();
    List<Player> team1ActivePlayers = new ArrayList<>();
    List<Player> team2ActivePlayers = new ArrayList<>();
    List team1ActiveNames = new ArrayList<>();
    List team2ActiveNames = new ArrayList<>();
    List<Player> team1Subs = new ArrayList<>();
    List<Player> team2Subs = new ArrayList<>();
    List team1SubNames = new ArrayList<>();
    List team2SubNames = new ArrayList<>();
    static int selectedPlayerCount = 0;

    ArrayAdapter actionsAdapter;
    Button team1Btn,team2Btn;
    MatchAction matchAction;

    int team1Score = 0;
    int team2Score = 0;


    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        matchActions = new ArrayList<>();

        firstTeam = getIntent().getStringExtra("FirstTeam");
        secondTeam = getIntent().getStringExtra("SecondTeam");
        firstTeamID = getIntent().getStringExtra("FirstID");
        secondTeamID = getIntent().getStringExtra("SecondID");

        basketballMatch.setHome(firstTeamID);
        basketballMatch.setAway(secondTeamID);
        basketballMatch.setMatchID(UUID.randomUUID().toString());
        basketballMatch.setSeason(Year.now().toString());


        getPlayerList();

        imageView=findViewById(R.id.imageView);
        actionslistView =findViewById(R.id.actionslistview);
        chronometer = findViewById(R.id.chronometer);
        team1Btn=findViewById(R.id.team1Btn);
        team2Btn=findViewById(R.id.team2Btn);
        team1Btn.setText(firstTeam);
        team2Btn.setText(secondTeam);
        matchAction = new MatchAction();

        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());
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

        actionsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, actionlist);


        actionslistView.setAdapter(actionsAdapter);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!endOfMatch) {
                    time=SystemClock.elapsedRealtime() - chronometer.getBase();
                    matchAction = new MatchAction();
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
                                        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                                        }
                                        else {
                                            String path = Environment.getExternalStorageDirectory() + "/" + File.separator
                                                    + firstTeam + "-" + secondTeam + "-" + Calendar.getInstance().getTime().toString() + ".txt";
                                            File file = new File(path);
                                            file.createNewFile();
                                            HashMap<String, MatchAction> actionHashMap = new HashMap<>();
                                            for (MatchAction action : matchActions) {
                                                String uuid = UUID.randomUUID().toString();
                                                actionHashMap.put(uuid, action);
                                            }
                                            basketballMatch.setActions(actionHashMap);
                                            basketballMatch.setScoreHome(team1Score);
                                            basketballMatch.setScoreAway(team2Score);

                                            String toWrite = basketballMatch.toString();

                                            if (file.exists()) {
                                                OutputStream fo = new FileOutputStream(file);
                                                fo.write(toWrite.getBytes());
                                                fo.close();
                                                System.out.println("file created: " + file);
                                            }

                                            saveMatchToDatabase();
                                        }
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
                    builder.setMessage("The match ended. Do you want to export match analysis data to a txt file?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    Toast.makeText(ActionActivity.this,"Match end",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
    }

    private void getPlayerList() {
        final DatabaseReference teamRef = database.getReference("/Teams/");

        teamRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot teamSnap : dataSnapshot.getChildren()) {
                    BasketballTeam team = teamSnap.getValue(BasketballTeam.class);
                    String teamID = team.getTeamID();
                    if (teamID.equals(firstTeamID)) {
                        team1 = team;
                        for (DataSnapshot snap : teamSnap.child("players").getChildren()){
                            try {
                                Player player = snap.getValue(Player.class);
                                team1Players.add(player);
                                team1Names.add(player.getName());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }

                    } else if (teamID.equals(secondTeamID)) {
                        team2 = team;
                        for (DataSnapshot snap : teamSnap.child("players").getChildren()){
                            try {
                                Player player = snap.getValue(Player.class);
                                team2Players.add(player);
                                team2Names.add(player.getName());
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
                selectPlayersDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void actionPressed(View view) {
        String actionType=((TextView)view).getText().toString();
        matchAction.setType(actionType);
        action_type_dialog.dismiss();
        List<List<Player>> playerLists = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        boolean isSub = false;
        switch (actionType){
            case "Foul":
                if(TEAM_SELECTED == 1){
                    playerLists.add(team1ActivePlayers);
                    playerLists.add(team2ActivePlayers);
                    matchAction.setActionTeamName(firstTeam);
                } else {
                    playerLists.add(team2ActivePlayers);
                    playerLists.add(team1ActivePlayers);
                    matchAction.setActionTeamName(secondTeam);
                }
                titleList.add("Foul - Select first player");
                titleList.add("Foul - Select second player");
                break;
            case "Sub":
                if(TEAM_SELECTED == 1){
                    playerLists.add(team1ActivePlayers);
                    playerLists.add(team1Subs);
                    matchAction.setActionTeamName(firstTeam);
                } else {
                    playerLists.add(team2ActivePlayers);
                    playerLists.add(team2Subs);
                    matchAction.setActionTeamName(secondTeam);
                }
                titleList.add("Sub - Select first player");
                titleList.add("Sub - Select second player");
                isSub = true;
                break;
            case "Asst":
                if(TEAM_SELECTED == 1){
                    playerLists.add(team1ActivePlayers);
                    playerLists.add(team1ActivePlayers);
                    matchAction.setActionTeamName(firstTeam);
                } else {
                    playerLists.add(team2ActivePlayers);
                    playerLists.add(team2ActivePlayers);
                    matchAction.setActionTeamName(secondTeam);
                }
                titleList.add("Assist - Select first player");
                titleList.add("Assist - Select second player");
                break;
            case "to":
                if(TEAM_SELECTED == 1){
                    playerLists.add(team1ActivePlayers);
                    playerLists.add(team1ActivePlayers);
                    matchAction.setActionTeamName(firstTeam);
                } else {
                    playerLists.add(team2ActivePlayers);
                    playerLists.add(team2ActivePlayers);
                    matchAction.setActionTeamName(secondTeam);
                }
                titleList.add("Pass - Select first player");
                titleList.add("Pass - Select second player");
                break;
            case "1pt":
                if(TEAM_SELECTED == 1){
                    playerLists.add(team1ActivePlayers);
                    team1Score += 1;
                    matchAction.setActionTeamName(firstTeam);
                } else {
                    playerLists.add(team2ActivePlayers);
                    team2Score += 1;
                    matchAction.setActionTeamName(secondTeam);
                }
                titleList.add("Score - Select player");
                break;
            case "2pt":
                if(TEAM_SELECTED == 1){
                    playerLists.add(team1ActivePlayers);
                    team1Score += 2;
                    matchAction.setActionTeamName(firstTeam);
                } else {
                    playerLists.add(team2ActivePlayers);
                    team2Score += 2;
                    matchAction.setActionTeamName(secondTeam);
                }
                titleList.add("Score - Select player");
                break;
            case "3pt":
                if(TEAM_SELECTED == 1){
                    playerLists.add(team1ActivePlayers);
                    team1Score += 3;
                    matchAction.setActionTeamName(firstTeam);
                } else {
                    playerLists.add(team2ActivePlayers);
                    team2Score += 3;
                    matchAction.setActionTeamName(secondTeam);
                }
                titleList.add("Score - Select player");
                break;
            default:
                if(TEAM_SELECTED == 1){
                    playerLists.add(team1ActivePlayers);
                    matchAction.setActionTeamName(firstTeam);
                } else {
                    playerLists.add(team2ActivePlayers);
                    matchAction.setActionTeamName(secondTeam);
                }
                titleList.add("Missed Shot - Select player");
                break;
        }
        actionPlayersSelection(playerLists, titleList, isSub);
        //startChronometer();

    }
    public void actionPlayersSelection(List<List<Player>> playerLists, List<String> titleList, final boolean isSub){

        //for(int i = 0; i < playerLists.size(); i++){
        //    getActionPlayer(playerLists.get(i), titleList.get(i));
        //    matchAction.setPerson1(toReturn);
        //}
        if(playerLists.size() == 1) {
            final List<Player> playerList = playerLists.get(0);
            String title = titleList.get(0);
            String[] players = new String[playerList.size()];
            for(int i = 0; i < playerList.size(); i++){
                players[i] = "#" + playerList.get(i).getNumber() + " - " + playerList.get(i).getName();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(ActionActivity.this);
            builder.setTitle(title);
            builder.setItems(players, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    matchAction.setPerson1(playerList.get(which));
                    matchActions.add(matchAction);

                    actionlist.add(matchAction.getType()+"  "+ matchAction.getPerson1().getName() + "  "
                            + matchAction.getPerson1().getTeam() + "  "+ matchAction.getTime());
                    //action_players_dialog.dismiss();
                    actionsAdapter.notifyDataSetChanged();
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    startChronometer();
                }
            });

// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if(playerLists.size() == 2) {
            final List<Player> playerList1 = playerLists.get(0);
            final List<Player> playerList2 = playerLists.get(1);
            final String title1 = titleList.get(0);
            final String title2 = titleList.get(1);
            final String[] players1 = new String[playerList1.size()];
            final String[] players2 = new String[playerList2.size()];
            for(int i = 0; i < playerList1.size(); i++){
                players1[i] = "#" + playerList1.get(i).getNumber() + " - " + playerList1.get(i).getName();
            }
            for(int i = 0; i < playerList2.size(); i++){
                players2[i] = "#" + playerList2.get(i).getNumber() + " - " + playerList2.get(i).getName();
            }
            AlertDialog.Builder builder1 = new AlertDialog.Builder(ActionActivity.this);
            builder1.setTitle(title1);
            builder1.setItems(players1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    matchAction.setPerson1(playerList1.get(which));
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(ActionActivity.this);
                    builder2.setTitle(title2);
                    builder2.setItems(players2, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            matchAction.setPerson2(playerList2.get(which));
                            matchActions.add(matchAction);
                            if(isSub){
                                int outIndex = playerList1.indexOf(matchAction.getPerson1());
                                int inIndex = playerList2.indexOf(matchAction.getPerson2());
                                Player temp = playerList1.get(outIndex);
                                playerList1.set(outIndex, playerList2.get(inIndex));
                                playerList2.set(inIndex, temp);
                                updatePlayerLists();
                            }

                            actionlist.add(matchAction.getType() + "  " + matchAction.getPerson1().getName() + " - " + matchAction.getPerson2().getName()
                                    + "  " + matchAction.getPerson1().getTeam() + "  " + matchAction.getTime());
                            actionsAdapter.notifyDataSetChanged();
                        }
                    });
                    builder2.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            startChronometer();
                        }
                    });

                    AlertDialog dialog2 = builder2.create();
                    dialog2.show();
                }
            });
            builder1.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    startChronometer();
                }

            });

            AlertDialog dialog1 = builder1.create();
            dialog1.show();
        }
        //action_players_dialog = new Dialog(ActionActivity.this);
        //action_players_dialog.setContentView(R.layout.action_players_dialog);
        //action_players_dialog.show();
        //playersListView=action_players_dialog.findViewById(R.id.playersListView);
//
        //if(TEAM_SELECTED==1){
        //    playersArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, team1Names);
//
        //}else if (TEAM_SELECTED==2){
        //    playersArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, team2Names);
        //}
        //playersListView.setAdapter(playersArrayAdapter);
//
        //playersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        if(TEAM_SELECTED==1){
        //            team1ActionsArrayList.add(matchAction);
        //            matchAction.setPerson1(team1Players.get(position).getPlayerID());
//
        //        }else if (TEAM_SELECTED==2){
        //            team2ActionsArrayList.add(matchAction);
        //            matchAction.setPerson1(team2Players.get(position).getPlayerID());
        //        }
        //        actionlist.add(matchAction.getType()+"  "+ matchAction.getPerson1()+"  "+ matchAction.getActionTeamName()+"  "+ matchAction.getTime());
        //        action_players_dialog.dismiss();
        //        actionsAdapter.notifyDataSetChanged();
        //        startChronometer();
        //    }
        //});
//
        //action_players_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
        //    @Override
        //    public void onCancel(DialogInterface dialog) {
        //        startChronometer();
        //    }
        //});

    }

    public void updatePlayerLists(){
        team1ActiveNames.clear();
        for (Player player : team1ActivePlayers) {
            team1ActiveNames.add(player.getName());
        }
        team2ActiveNames.clear();
        for (Player player : team2ActivePlayers) {
            team2ActiveNames.add(player.getName());
        }
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

    public void selectPlayersDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(ActionActivity.this);
        builder.setTitle("Select first teams players");
        final boolean[] checkedPlayers = new boolean[team1Names.size()];
        Arrays.fill(checkedPlayers, false);

        final String[] strNames1 = new String[team1Names.size()];
        final String[] strNames2 = new String[team2Names.size()];
        for(int i = 0; i < team1Names.size(); i++){
            strNames1[i] = team1Names.get(i).toString();
        }
        for(int i = 0; i < team2Names.size(); i++){
            strNames2[i] = team2Names.get(i).toString();
        }
        selectedPlayerCount = 0;
        builder.setMultiChoiceItems(strNames1, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                ListView listView = ((AlertDialog)dialog).getListView();
                Button okButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                if (isChecked && selectedPlayerCount == 5) {
                    listView.setItemChecked(which, false);
                } else if(isChecked && selectedPlayerCount < 5){
                    selectedPlayerCount++;
                    checkedPlayers[which] = true;
                } else if(!isChecked) {
                    selectedPlayerCount--;
                    checkedPlayers[which] = false;
                }

                if(selectedPlayerCount == 5) {
                    okButton.setClickable(true);
                } else {
                    okButton.setClickable(false);
                }

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < team1Names.size(); i++) {
                    if (checkedPlayers[i]) {
                        team1ActiveNames.add(team1Names.get(i));
                        team1ActivePlayers.add(team1Players.get(i));
                    } else {
                        team1Subs.add(team1Players.get(i));
                        team1SubNames.add(team1Names.get(i));
                    }

                }

                selectedPlayerCount = 0;
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(ActionActivity.this);
                builder2.setTitle("Select second teams players");
                final boolean[] checkedPlayers2 = new boolean[team2Names.size()];
                Arrays.fill(checkedPlayers2, false);
                builder2.setMultiChoiceItems(strNames2, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        ListView listView = ((AlertDialog) dialog).getListView();
                        Button okButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        if (isChecked && selectedPlayerCount == 5) {
                            listView.setItemChecked(which, false);
                        } else if (isChecked && selectedPlayerCount < 5) {
                            selectedPlayerCount++;
                            checkedPlayers2[which] = true;
                        } else if (!isChecked) {
                            selectedPlayerCount--;
                            checkedPlayers2[which] = false;
                        }

                        if (selectedPlayerCount == 5) {
                            okButton.setClickable(true);
                        } else {
                            okButton.setClickable(false);
                        }

                    }
                });
                builder2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < team2Names.size(); i++) {
                            if (checkedPlayers2[i]) {
                                team2ActiveNames.add(team2Names.get(i));
                                team2ActivePlayers.add(team2Players.get(i));
                            } else {
                                team2Subs.add(team2Players.get(i));
                                team2SubNames.add(team2Names.get(i));
                            }
                        }
                        startChronometer();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                dialog2.show();


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveMatchToDatabase(){
        DatabaseReference ref = database.getReference("/");
        DatabaseReference matchesReference = ref.child("Matches");
        matchesReference.child(basketballMatch.getMatchID()).setValue(basketballMatch);
        DatabaseReference leaguesReference = ref.child("Leagues");
        String uuid = UUID.randomUUID().toString();
        leaguesReference.child(team1.getLeagueID())
                .child("Seasons")
                .child(Year.now().toString())
                .child("Matches")
                .child(uuid).setValue(basketballMatch.getMatchID());
    }



}
