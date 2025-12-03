package com.example.football_db.controller;

import com.example.football_db.dto.team.CreateTeamDTO;
import com.example.football_db.dto.team.TeamDTO;
import com.example.football_db.dto.team.UpdateTeamDTO;
import com.example.football_db.service.TeamService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for managing football teams.
 */
@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {
    private static final Logger logger = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Get all teams with pagination.
     */
    @GetMapping
    public ResponseEntity<Page<TeamDTO>> findAll(Pageable pageable) {
        Page<TeamDTO> teams = teamService.getAllTeams(pageable);
        logger.info("Retrieved {} teams", teams.getTotalElements());
        return ResponseEntity.ok(teams);
    }

    /**
     * Get team by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TeamDTO> findById(@PathVariable UUID id) {
        TeamDTO team = teamService.getTeamById(id);
        logger.info("Team found - id: {}", id);
        return ResponseEntity.ok(team);
    }

    /**
     * Create a new team.
     */
    @PostMapping
    public ResponseEntity<TeamDTO> save(@Valid @RequestBody CreateTeamDTO team) {
        TeamDTO createdTeam = teamService.save(team);
        logger.info("Team created - id: {}", createdTeam.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    /**
     * Update an existing team.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TeamDTO> update(@Valid @RequestBody UpdateTeamDTO team, @PathVariable UUID id) {
        TeamDTO updatedTeam = teamService.update(id, team);
        logger.info("Team updated - id: {}", id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedTeam);
    }

    /**
     * Delete a team by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        teamService.deleteById(id);
        logger.info("Team deleted - id: {}", id);
        return ResponseEntity.noContent().build();
    }
}


