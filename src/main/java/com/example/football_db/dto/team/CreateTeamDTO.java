package com.example.football_db.dto.team;

import com.example.football_db.dto.player.CreatePlayerDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public class CreateTeamDTO {
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotNull(message = "Acronym is required")
    @NotBlank(message = "Acronym cannot be empty")
    private String acronym;

    @NotNull(message = "Budget is required")
    @Positive(message = "Budget must be positive")
    private BigDecimal budget;

    @Valid
    private List<CreatePlayerDTO> players;

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
    public List<CreatePlayerDTO> getPlayers() {
        return players;
    }
    public void setPlayers(List<CreatePlayerDTO> players) {
        this.players = players;
    }
}

