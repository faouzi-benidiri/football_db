package com.example.football_db.entity;

import com.example.football_db.constant.PlayerPosition;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private PlayerPosition position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Player() {}
    public Player(String name, PlayerPosition position) {
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

