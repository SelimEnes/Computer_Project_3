package com.selimkilicaslan.computer_project_3;

import androidx.annotation.NonNull;

public class MatchAction {
    private float LocationX;
    private float LocationY;
    private Player Person1;
    private Player Person2;
    private String Type;
    private String actionTeamName;
    private long Time;

    public Player getPerson2() {
        return Person2;
    }

    public void setPerson2(Player person2) {
        Person2 = person2;
    }

    public String getActionTeamName() {
        return actionTeamName;
    }

    public void setActionTeamName(String actionTeamName) {
        this.actionTeamName = actionTeamName;
    }

    public long getTime() {
        return Time;
    }

    public void setTime(long time) {
        this.Time = time;
    }

    public float getLocationX() {
        return LocationX;
    }

    public void setLocationX(float locationX) {
        this.LocationX = locationX;
    }

    public float getLocationY() {
        return LocationY;
    }

    public void setLocationY(float locationY) {
        this.LocationY = locationY;
    }

    public Player getPerson1() {
        return Person1;
    }

    public void setPerson1(Player person1) {
        this.Person1 = person1;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }

    @NonNull
    @Override
    public String toString() {
        String toReturn = "";
        toReturn += "{ ";
        toReturn += "\"locationX\": " + LocationX + ", ";
        toReturn += "\"locationY\": " + LocationY + ", ";
        toReturn += "\"player1\": " + Person1.toString() + ", ";
        if(Person2 != null) {
            toReturn += "\"player2\": " + Person2.toString() + ", ";
        }
        toReturn += "\"type\": " + "\"" + Type + "\"" + ", ";
        toReturn += "\"actionTeamName\": " + "\"" + actionTeamName + "\"" + ", ";
        toReturn += "\"time\": " + Time + " ";
        toReturn += "}";

        return toReturn;
    }
}
