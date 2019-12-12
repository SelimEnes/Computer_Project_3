package com.selimkilicaslan.computer_project_3;

import androidx.annotation.NonNull;

public class Player {
    private int Age;
    private String Name;
    private int Number;
    private String Team;
    private String TeamID;

    public String getPlayerID() {
        return PlayerID;
    }

    public void setPlayerID(String playerID) {
        PlayerID = playerID;
    }

    private String PlayerID;

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public String getTeam() {
        return Team;
    }

    public void setTeam(String team) {
        Team = team;
    }

    public String getTeamID() {
        return TeamID;
    }

    public void setTeamID(String teamID) {
        TeamID = teamID;
    }

    @NonNull
    @Override
    public String toString() {
        String toReturn = "";
        toReturn += "{ ";
        toReturn += "\"age\": " + Age + ", ";
        toReturn += "\"name\": " + "\"" + Name + "\"" + ", ";
        toReturn += "\"number\": " + Number + ", ";
        toReturn += "\"team\": " + "\"" + Team + "\"" + ", ";
        toReturn += "\"teamID\": " + "\"" + TeamID + "\"" + " ";
        toReturn += "}";

        return toReturn;
    }
}
