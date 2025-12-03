package com.example.football_db.dto.player;

import com.example.football_db.constant.PlayerPosition;

import java.util.UUID;

public class PlayerDTO {
    private UUID id;
    private String name;
    private PlayerPosition position;

    public PlayerDTO(UUID id, String name, PlayerPosition position) {
        this.id = id;
        this.name = name;
        this.position = position;
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
    public PlayerPosition getPosition() {
        return position;
    }
    public void setPosition(PlayerPosition position) {
        this.position = position;
    }
}

