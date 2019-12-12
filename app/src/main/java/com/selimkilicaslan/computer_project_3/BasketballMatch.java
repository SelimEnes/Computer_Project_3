package com.selimkilicaslan.computer_project_3;

import android.drm.DrmStore;

import java.util.HashMap;

import androidx.annotation.NonNull;

public class BasketballMatch {
    private String Away;
    private String Home;
    private String Season;
    private String MatchID;
    private int ScoreAway;
    private int ScoreHome;
    private HashMap<String, MatchAction> Actions;

    public BasketballMatch(String away, String home, String season, String matchID, int scoreAway, int scoreHome, HashMap<String, MatchAction> actions) {
        Away = away;
        Home = home;
        Season = season;
        MatchID = matchID;
        ScoreAway = scoreAway;
        ScoreHome = scoreHome;
        Actions = actions;
    }

    public BasketballMatch() {
    }

    public String getAway() {
        return Away;
    }

    public void setAway(String away) {
        Away = away;
    }

    public String getHome() {
        return Home;
    }

    public void setHome(String home) {
        Home = home;
    }

    public String getSeason() {
        return Season;
    }

    public void setSeason(String season) {
        Season = season;
    }

    public String getMatchID() {
        return MatchID;
    }

    public void setMatchID(String matchID) {
        MatchID = matchID;
    }

    public int getScoreAway() {
        return ScoreAway;
    }

    public void setScoreAway(int scoreAway) {
        ScoreAway = scoreAway;
    }

    public int getScoreHome() {
        return ScoreHome;
    }

    public void setScoreHome(int scoreHome) {
        ScoreHome = scoreHome;
    }

    public HashMap<String, MatchAction> getActions() {
        return Actions;
    }

    public void setActions(HashMap<String, MatchAction> actions) {
        Actions = actions;
    }


    @NonNull
    @Override
    public String toString() {
        String toReturn = "";
        toReturn += "{ ";
        toReturn += "\"away\": " + "\"" + Away + "\"" + ", ";
        toReturn += "\"home\": " + "\"" + Home + "\"" + ", ";
        toReturn += "\"season\": " + "\"" + Season + "\"" + ", ";
        toReturn += "\"matchID\": " + "\"" + MatchID + "\"" + ", ";
        toReturn += "\"scoreAway\": " + ScoreAway + ", ";
        toReturn += "\"scoreHome\": " + ScoreHome + ", ";

        toReturn += "\"actions\": { ";
        if(Actions != null){
            int i = 0;
            for(String key : Actions.keySet()) {
                toReturn += "\"" + key + "\": " + Actions.get(key);
                i++;
                if(i < Actions.size()) {
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
