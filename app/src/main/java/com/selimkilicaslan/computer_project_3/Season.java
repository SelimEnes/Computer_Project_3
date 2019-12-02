package com.selimkilicaslan.computer_project_3;

import java.util.List;
import java.util.Map;

public class Season {

    private String SeasonYear;
    private Map<String,String> Matches;

    public String getSeasonYear() {
        return SeasonYear;
    }

    public void setSeasonYear(String seasonYear) {
        SeasonYear = seasonYear;
    }

    public Map<String, String> getMatches() {
        return Matches;
    }

    public void setMatches(Map<String, String> matches) {
        Matches = matches;
    }
}
