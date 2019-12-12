package com.selimkilicaslan.computer_project_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 100;

    static String firstTeam = "", secondTeam = "";
    static String firstTeamID = "", secondTeamID = "";
    static int selectedTeamCount = 0;


    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    List<League> Leagues = new ArrayList<>();
    List<BasketballTeam> Teams = new ArrayList<>();
    HashMap<String, BasketballMatch> Matches = new HashMap<>();

    static FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestStoragePermission();

        expListView = findViewById(R.id.listViewExpandable);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    selectedTeamCount = 0;


                    final String[] teamNames = new String[Teams.size()];
                    final String[] teamIds = new String[Teams.size()];
                    for(int i = 0; i < Teams.size(); i++){
                        teamNames[i] = Teams.get(i).getTeamName();
                        teamIds[i] = Teams.get(i).getTeamID();
                    }

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Select 2 Teams");
                    final boolean[] checkedTeams = new boolean[teamNames.length];
                    Arrays.fill(checkedTeams, false);
                    //builder.setMultiChoiceItems()
                    builder.setMultiChoiceItems(teamNames, null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            ListView listView = ((AlertDialog)dialog).getListView();
                            Button okButton = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                            if (isChecked && selectedTeamCount == 2) {
                                listView.setItemChecked(which, false);
                            } else if(isChecked && selectedTeamCount < 2){
                                selectedTeamCount++;
                                checkedTeams[which] = true;
                            } else if(!isChecked) {
                                selectedTeamCount--;
                                checkedTeams[which] = false;
                            }

                            if(selectedTeamCount == 2) {
                                okButton.setClickable(true);
                            } else {
                                okButton.setClickable(false);
                            }

                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(int i = 0; i < teamNames.length; i++){
                                if(checkedTeams[i]){
                                    if(firstTeam == ""){
                                        firstTeam = teamNames[i];
                                        firstTeamID = teamIds[i];
                                    } else {
                                        secondTeam = teamNames[i];
                                        secondTeamID = teamIds[i];
                                    }
                                }
                            }

                            Intent i = new Intent(MainActivity.this,ActionActivity.class);
                            i.putExtra("FirstTeam", firstTeam);
                            i.putExtra("SecondTeam", secondTeam);
                            i.putExtra("FirstID", firstTeamID);
                            i.putExtra("SecondID", secondTeamID);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("Cancel", null);

                    AlertDialog dialog = builder.create();
                    dialog.show();



                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        fab.hide();



        DatabaseReference leagueRef = database.getReference("/Leagues/");

        leagueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Leagues.clear();
                    Teams.clear();
                    Matches.clear();
                    for(DataSnapshot snap: dataSnapshot.getChildren()) {
                        League league = snap.getValue(League.class);
                        Leagues.add(league);
                        System.out.println(league.getName());
                    }

                    DatabaseReference teamRef = database.getReference("/Teams/");

                    teamRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Teams.clear();
                            for(DataSnapshot snap: dataSnapshot.getChildren()) {

                                try {
                                    BasketballTeam team = snap.getValue(BasketballTeam.class);
                                    Teams.add(team);
                                    System.out.println(team.getTeamName());
                                }
                                catch(Exception ex){
                                    ex.printStackTrace();
                                }
                            }

                            prepareListData();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.topbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_add_player:
                intent = new Intent(getApplicationContext(), AddPlayerActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add_team:
                intent = new Intent(getApplicationContext(), AddTeamActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add_league:
                intent = new Intent(getApplicationContext(), AddLeagueActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();


        DatabaseReference reference = database.getReference("/");
        DatabaseReference matchRef = reference.child("Matches");
        matchRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot matchSnapshot : dataSnapshot.getChildren()){
                    try {
                        BasketballMatch match = matchSnapshot.getValue(BasketballMatch.class);
                        Matches.put(match.getMatchID(), match);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                for(final League league : Leagues){
                    listDataHeader.add(league.getName());
                    List<String> matchList = new ArrayList<>();
                    try {
                        for (String matchID : league.getSeasons().get(Year.now().toString()).getMatches().values()) {
                            BasketballMatch match = Matches.get(matchID);
                            if (match == null) continue;
                            BasketballTeam home = new BasketballTeam();
                            BasketballTeam away = new BasketballTeam();
                            for (BasketballTeam team : Teams) {
                                if (team.getTeamID().equals(match.getHome())) {
                                    home = team;
                                } else if (team.getTeamID().equals(match.getAway())) {
                                    away = team;
                                }
                            }
                            String line = home.getTeamName() + " " + match.getScoreHome() + "-" + match.getScoreAway() + " " + away.getTeamName();
                            matchList.add(line);
                        }

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    listDataChild.put(league.getName(), matchList);
                }
                listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);
                fab.show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void requestStoragePermission(){

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            }
            else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
}
