package com.example.football_db.constant;

public enum PlayerPosition {
    DEFENSEUR("Defender"),
    ATTAQUANT("Attacker"),
    MILIEU("Midfielder"),
    GARDIEN("Goalkeeper");

    private final String label;
    PlayerPosition(String label) {
        this.label = label;
    }
    public String getLabel() {
        return label;
    }

}

