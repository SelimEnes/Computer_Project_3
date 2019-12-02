package com.selimkilicaslan.computer_project_3;

public class MatchAction {
    private float LocationX;
    private float LocationY;
    private String Person1;
    private String Person2;
    private String Type;
    private String actionTeamName;
    private long Time;

    public String getPerson2() {
        return Person2;
    }

    public void setPerson2(String person2) {
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

    public String getPerson1() {
        return Person1;
    }

    public void setPerson1(String person1) {
        this.Person1 = person1;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        this.Type = type;
    }
}
