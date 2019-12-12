package com.selimkilicaslan.computer_project_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddTeamActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    EditText teamNameEditText;
    Button addTeamButton;
    Spinner leagueSpinner;

    int selectedLeagueIndex = 0;

    final List<League> leagueList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team);

        teamNameEditText = findViewById(R.id.teamNameEditText);
        addTeamButton = findViewById(R.id.addTeamButton);
        leagueSpinner = findViewById(R.id.leagueSpinner);
        addTeamButton.setClickable(false);

        setSpinnerListener();

        DatabaseReference leaguesRef = ref.child("Leagues");
        leaguesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leagueList.clear();
                List<String> leagueNames = new ArrayList<>();
                for (DataSnapshot leagueSnap : dataSnapshot.getChildren()){
                    League league = leagueSnap.getValue(League.class);
                    leagueList.add(league);
                    leagueNames.add(league.getName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, leagueNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                leagueSpinner.setAdapter(dataAdapter);
                addTeamButton.setClickable(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onAddTeamButtonClick(View view) {

        String teamID = UUID.randomUUID().toString();
        BasketballTeam newTeam = new BasketballTeam();

        newTeam.setTeamName(teamNameEditText.getText().toString());
        newTeam.setTeamID(teamID);
        newTeam.setLeagueID(leagueList.get(selectedLeagueIndex).getLeagueID());
        newTeam.setPlayers(new HashMap<String, Player>());

        DatabaseReference teamsRef = ref.child("Teams");
        teamsRef.child(teamID).setValue(newTeam);

        Toast.makeText(getApplicationContext(), "Team Added", Toast.LENGTH_SHORT);

    }

    public void setSpinnerListener(){

        leagueSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedLeagueIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
