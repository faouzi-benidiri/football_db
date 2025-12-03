package com.example.football_db.service;

import com.example.football_db.dto.player.PlayerDTO;
import com.example.football_db.dto.team.CreateTeamDTO;
import com.example.football_db.dto.team.TeamDTO;
import com.example.football_db.dto.team.UpdateTeamDTO;
import com.example.football_db.entity.Player;
import com.example.football_db.entity.Team;
import com.example.football_db.exception.TeamNotFoundException;
import com.example.football_db.repository.PlayerRepository;
import com.example.football_db.repository.TeamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for team management operations.
 */
@Service
public class TeamServiceImpl implements TeamService {
    private static final Logger logger = LoggerFactory.getLogger(TeamServiceImpl.class);
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    public TeamServiceImpl(TeamRepository teamRepository, PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Get all teams with pagination.
     */
    @Override
    public Page<TeamDTO> getAllTeams(Pageable pageable) {
        return teamRepository.findAll(pageable).map(this::toDTO);
    }

    /**
     * Get team by ID.
     */
    @Override
    public TeamDTO getTeamById(UUID id) {
        return teamRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Team not found - id: {}", id);
                    return new TeamNotFoundException("Team not found with ID: " + id);
                });
    }

    /**
     * Create a new team.
     */
    @Override
    public TeamDTO save(CreateTeamDTO createTeamDto) {
        Team team = new Team();
        team.setName(createTeamDto.getName());
        team.setAcronym(createTeamDto.getAcronym());
        team.setBudget(createTeamDto.getBudget());

        // Save players if provided
        if (createTeamDto.getPlayers() != null && !createTeamDto.getPlayers().isEmpty()) {
            team.setPlayers(createTeamDto.getPlayers().stream()
                    .map(createPlayerDTO -> {
                        Player player = new Player();
                        player.setName(createPlayerDTO.getName());
                        player.setPosition(createPlayerDTO.getPosition());
                        playerRepository.save(player);
                        return player;
                    })
                    .collect(Collectors.toList()));
        }

        Team saved = teamRepository.save(team);
        logger.info("Team saved - id: {}", saved.getId());
        return toDTO(saved);
    }

    /**
     * Update an existing team.
     */
    @Override
    public TeamDTO update(UUID id, UpdateTeamDTO updateTeamDto) {
        Team team = findTeamById(id);

        if (updateTeamDto.getName() != null) {
            team.setName(updateTeamDto.getName());
        }
        if (updateTeamDto.getAcronym() != null) {
            team.setAcronym(updateTeamDto.getAcronym());
        }
        if (updateTeamDto.getBudget() != null) {
            team.setBudget(updateTeamDto.getBudget());
        }

        Team saved = teamRepository.save(team);
        logger.info("Team updated - id: {}", id);
        return toDTO(saved);
    }

    /**
     * Delete a team by ID.
     */
    @Override
    public void deleteById(UUID id) {
        findTeamById(id);
        teamRepository.deleteById(id);
        logger.info("Team deleted - id: {}", id);
    }

    private Team findTeamById(UUID id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Team not found - id: {}", id);
                    return new TeamNotFoundException("Team not found with ID: " + id);
                });
    }

    private TeamDTO toDTO(Team team) {
        return new TeamDTO(
                team.getId(),
                team.getName(),
                team.getAcronym(),
                team.getBudget(),
                team.getPlayers().stream()
                        .map(player -> new PlayerDTO(player.getId(), player.getName(), player.getPosition()))
                        .collect(Collectors.toList())
        );
    }
}
