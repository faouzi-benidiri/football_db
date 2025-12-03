package com.example.football_db.dto.team;

import com.example.football_db.dto.player.PlayerDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TeamDTO {
    private UUID id;
    private String name;
    private String acronym;
    private BigDecimal budget;
    private List<PlayerDTO> players;

    public TeamDTO(UUID id, String name, String acronym, BigDecimal budget, List<PlayerDTO> players) {
        this.id = id;
        this.name = name;
        this.acronym = acronym;
        this.budget = budget;
        this.players = players;
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAcronym() {
        return acronym;
    }
    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }
    public BigDecimal getBudget() {
        return budget;
    }
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
    public List<PlayerDTO> getPlayers() {
        return players;
    }
}

