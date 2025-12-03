package com.example.football_db.service;

import com.example.football_db.dto.team.CreateTeamDTO;
import com.example.football_db.dto.team.TeamDTO;
import com.example.football_db.dto.team.UpdateTeamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service interface for team management operations.
 */
public interface TeamService {

    Page<TeamDTO> getAllTeams(Pageable pageable);

    TeamDTO getTeamById(UUID id);

    TeamDTO save(CreateTeamDTO team);

    TeamDTO update(UUID id, UpdateTeamDTO team);

    void deleteById(UUID id);
}
