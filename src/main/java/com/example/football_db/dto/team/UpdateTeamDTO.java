package com.example.football_db.dto.team;

import java.math.BigDecimal;

public class UpdateTeamDTO {
    private String name;
    private String acronym;
    private BigDecimal budget;

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
}

