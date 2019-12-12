package com.selimkilicaslan.computer_project_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class AddLeagueActivity extends AppCompatActivity {

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/");

    EditText leagueNameEditText;
    Button addLeagueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_league);

        leagueNameEditText = findViewById(R.id.leagueNameEditText);
        addLeagueButton = findViewById(R.id.addLeagueButton);

    }

    public void onAddLeagueButtonClick(View view) {

        String leagueID = UUID.randomUUID().toString();
        League newLeague = new League();
        newLeague.setName(leagueNameEditText.getText().toString());
        newLeague.setSeasons(new HashMap<String, Season>());
        newLeague.setLeagueID(leagueID);

        DatabaseReference leagueRef = ref.child("Leagues");
        leagueRef.child(leagueID).setValue(newLeague);

        Toast.makeText(getApplicationContext(), "League Added", Toast.LENGTH_SHORT);

    }
}
