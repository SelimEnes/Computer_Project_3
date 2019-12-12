package com.selimkilicaslan.computer_project_3;


import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class Season {

    private String SeasonYear;
    private HashMap<String,String> Matches;

    public Season(String SeasonYear, HashMap<String, String> Matches) {
        this.SeasonYear = SeasonYear;
        this.Matches = Matches;
    }

    public Season() {
    }

    public String getSeasonYear() {
        return SeasonYear;
    }

    public void setSeasonYear(String seasonYear) {
        SeasonYear = seasonYear;
    }

    public HashMap<String, String> getMatches() {
        return Matches;
    }

    public void setMatches(HashMap<String, String> matches) {
        Matches = matches;
    }

    @NonNull
    @Override
    public String toString() {
        String toReturn = "";
        toReturn += "{ ";
        toReturn += "\"seasonYear\": " + "\"" + SeasonYear + "\"" + ", ";
        toReturn += "\"matches\": { ";
        int i = 0;
        if(Matches != null) {
            for (String key : Matches.keySet()) {
                toReturn += "\"" + key + "\": " + Matches.get(key);
                i++;
                if (i < Matches.size()) {
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
