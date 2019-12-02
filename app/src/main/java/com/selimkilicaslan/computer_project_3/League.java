package com.selimkilicaslan.computer_project_3;

import java.util.List;

public class League {
    private String LeagueID;
    private String Name;
    private List<Season> Seasons;

    public League(String leagueID, String name) {
        LeagueID = leagueID;
        Name = name;
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

    public List<Season> getSeasons() {
        return Seasons;
    }

    public void setSeasons(List<Season> seasons) {
        Seasons = seasons;
    }


}
