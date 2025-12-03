package com.example.football_db.dto.player;

import com.example.football_db.constant.PlayerPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreatePlayerDTO {
    @NotNull(message = "Player name is required")
    @NotBlank(message = "Player name cannot be empty")
    private String name;

    private PlayerPosition position;

    public CreatePlayerDTO(String name, PlayerPosition playerPosition) {
        this.name = name;
        this.position = playerPosition;
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

}

