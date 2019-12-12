package com.selimkilicaslan.computer_project_3;


import java.util.HashMap;

import androidx.annotation.NonNull;

public class BasketballTeam {
    private String TeamID;
    private String TeamName;
    private String LeagueID;
    private HashMap<String, Player> Players;

    public BasketballTeam() {
    }

    public BasketballTeam(String TeamID, String TeamName, String LeagueID, HashMap<String, Player> players) {
        this.TeamID = TeamID;
        this.TeamName = TeamName;
        this.LeagueID = LeagueID;
        Players = players;
    }

    public HashMap<String, Player> getPlayers() {
        return Players;
    }

    public void setPlayers(HashMap<String, Player> players) {
        Players = players;
    }

    public void setTeamID(String teamID) {
        TeamID = teamID;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public void setLeagueID(String leagueID) {
        LeagueID = leagueID;
    }

    public String getTeamID() {
        return TeamID;
    }

    public String getTeamName() {
        return TeamName;
    }

    public String getLeagueID() {
        return LeagueID;
    }

    @NonNull
    @Override
    public String toString() {
        String toReturn = "";
        toReturn += "{ ";
        toReturn += "\"teamID\": " + "\"" + TeamID + "\"" + ", ";
        toReturn += "\"teamName\": " + "\"" + TeamName + "\"" + ", ";
        toReturn += "\"leagueID\": " + "\"" + LeagueID + "\"" + ", ";
        toReturn += "\"players\": { ";
        int i = 0;
        if(Players != null) {
            for(String key : Players.keySet()) {
                toReturn += "\"" + key + "\": " + Players.get(key);
                i++;
                if(i < Players.size()) {
                    toReturn += ", ";
                } else {
                    toReturn += "} ";
                }
            }
        } else {
            toReturn += "}";
        }
        toReturn += "}";

        return toReturn;
    }
}
