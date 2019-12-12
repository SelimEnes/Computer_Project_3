package com.selimkilicaslan.computer_project_3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddPlayerActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    TextView playerNameEditText, ageEditText, numberEditText;
    Spinner teamsSpinner;
    Button addPlayerButton;

    int selectedTeamIndex = 0;

    final List<BasketballTeam> teamList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);

        playerNameEditText = findViewById(R.id.playerNameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        numberEditText = findViewById(R.id.numberEditText);
        teamsSpinner = findViewById(R.id.teamsSpinner);
        addPlayerButton = findViewById(R.id.addPlayerButton);

        addPlayerButton.setClickable(false);
        setSpinnerListener();

        DatabaseReference teamsRef = ref.child("Teams");
        teamsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                teamList.clear();
                List<String> teamNames = new ArrayList<>();
                for (DataSnapshot teamSnap : dataSnapshot.getChildren()){
                    BasketballTeam team = teamSnap.getValue(BasketballTeam.class);
                    teamList.add(team);
                    teamNames.add(team.getTeamName());
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, teamNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                teamsSpinner.setAdapter(dataAdapter);
                addPlayerButton.setClickable(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setSpinnerListener(){

        teamsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTeamIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void onAddPlayerButtonClick(View view) {
        String playerID = UUID.randomUUID().toString();
        Player newPlayer = new Player();

        newPlayer.setName(playerNameEditText.getText().toString());
        newPlayer.setNumber(Integer.parseInt(numberEditText.getText().toString()));
        newPlayer.setAge(Integer.parseInt(ageEditText.getText().toString()));
        newPlayer.setTeam(teamList.get(selectedTeamIndex).getTeamName());
        newPlayer.setTeamID(teamList.get(selectedTeamIndex).getTeamID());
        newPlayer.setPlayerID(playerID);

        DatabaseReference playersRef = ref.child("Players");
        playersRef.child(playerID).setValue(newPlayer);

        DatabaseReference teamsRef = ref.child("Teams");
        teamsRef.child(newPlayer.getTeamID()).child("players").child(playerID).setValue(newPlayer);

        Toast.makeText(getApplicationContext(), "Player Added", Toast.LENGTH_SHORT);
    }
}
