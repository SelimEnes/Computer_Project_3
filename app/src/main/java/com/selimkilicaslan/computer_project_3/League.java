package com.selimkilicaslan.computer_project_3;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class League {
    private String LeagueID;
    private String Name;
    private HashMap<String, Season> Seasons;

    public League(String LeagueID, String Name, HashMap<String, Season> Seasons) {
        this.LeagueID = LeagueID;
        this.Name = Name;
        this.Seasons = Seasons;
    }

    public League() {
    }

    public String getLeagueID() {
        return LeagueID;
    }

    public void setLeagueID(String leagueID) {
        LeagueID = leagueID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public HashMap<String, Season> getSeasons() {
        return Seasons;
    }

    public void setSeasons(HashMap<String, Season> seasons) {
        Seasons = seasons;
    }

    @NonNull
    @Override
    public String toString() {
        String toReturn = "";
        toReturn += "{ ";
        toReturn += "\"leagueID\": " + "\"" + LeagueID + "\"" + ", ";
        toReturn += "\"name\": " + "\"" + Name + "\"" + ", ";
        toReturn += "\"seasons\": { ";
        if(Seasons != null) {
            int i = 0;
            for (String key : Seasons.keySet()) {
                toReturn += "\"" + key + "\": " + Seasons.get(key);
                i++;
                if (i < Seasons.size()) {
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
