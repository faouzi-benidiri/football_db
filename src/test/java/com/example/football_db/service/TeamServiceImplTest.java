package com.example.football_db.service;

import com.example.football_db.constant.PlayerPosition;
import com.example.football_db.dto.player.CreatePlayerDTO;
import com.example.football_db.dto.team.CreateTeamDTO;
import com.example.football_db.dto.team.TeamDTO;
import com.example.football_db.dto.team.UpdateTeamDTO;
import com.example.football_db.entity.Player;
import com.example.football_db.entity.Team;
import com.example.football_db.exception.TeamNotFoundException;
import com.example.football_db.repository.PlayerRepository;
import com.example.football_db.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private PlayerRepository playerRepository;
    @InjectMocks
    private TeamServiceImpl teamService;

    private Team testTeam;
    private TeamDTO testTeamDTO;
    private UUID testTeamId;

    @BeforeEach
    void setUp() {
        testTeamId = UUID.randomUUID();
        testTeam = new Team("FC Barcelona", "FCB", new ArrayList<>(), new BigDecimal("100000.00"));
        testTeam.setId(testTeamId);

        testTeamDTO = new TeamDTO(testTeamId, "FC Barcelona", "FCB", new BigDecimal("100000.00"), new ArrayList<>());
    }

    @Test
    void testGetAllTeams_Success() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> teamPage = new PageImpl<>(List.of(testTeam), pageable, 1);
        when(teamRepository.findAll(pageable)).thenReturn(teamPage);

        // Act
        Page<TeamDTO> result = teamService.getAllTeams(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(teamRepository, times(1)).findAll(pageable);
    }

    @Test
    void testGetTeamById_Success() {
        // Arrange
        when(teamRepository.findById(testTeamId)).thenReturn(Optional.of(testTeam));

        // Act
        TeamDTO result = teamService.getTeamById(testTeamId);

        // Assert
        assertNotNull(result);
        assertEquals(testTeamDTO.getId(), result.getId());
        assertEquals(testTeamDTO.getName(), result.getName());
        verify(teamRepository, times(1)).findById(testTeamId);
    }

    @Test
    void testGetTeamById_NotFound() {
        // Arrange
        when(teamRepository.findById(testTeamId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> teamService.getTeamById(testTeamId));
        verify(teamRepository, times(1)).findById(testTeamId);
    }

    @Test
    void testSave_WithoutPlayers() {
        // Arrange
        CreateTeamDTO createTeamDTO = new CreateTeamDTO();
        createTeamDTO.setName("New Team");
        createTeamDTO.setAcronym("NT");
        createTeamDTO.setBudget(new BigDecimal("50000.00"));

        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);

        // Act
        TeamDTO result = teamService.save(createTeamDTO);

        // Assert
        assertNotNull(result);
        assertEquals("FC Barcelona", result.getName());
        verify(teamRepository, times(1)).save(any(Team.class));
        verify(playerRepository, never()).save(any(Player.class));
    }

    @Test
    void testSave_WithPlayers() {
        // Arrange
        CreateTeamDTO createTeamDTO = new CreateTeamDTO();
        createTeamDTO.setName("New Team");
        createTeamDTO.setAcronym("NT");
        createTeamDTO.setBudget(new BigDecimal("50000.00"));

        // Add players to the team
        CreatePlayerDTO playerDTO = new CreatePlayerDTO("Player 1", PlayerPosition.DEFENSEUR);
        createTeamDTO.setPlayers(List.of(playerDTO));

        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);
        when(playerRepository.save(any(Player.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        TeamDTO result = teamService.save(createTeamDTO);

        // Assert
        assertNotNull(result);
        verify(teamRepository, times(1)).save(any(Team.class));
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void testUpdate_AllFields() {
        // Arrange
        UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO();
        updateTeamDTO.setName("Updated Name");
        updateTeamDTO.setAcronym("UN");
        updateTeamDTO.setBudget(new BigDecimal("120000.00"));

        when(teamRepository.findById(testTeamId)).thenReturn(Optional.of(testTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);

        // Act
        TeamDTO result = teamService.update(testTeamId, updateTeamDTO);

        // Assert
        assertNotNull(result);
        verify(teamRepository, times(1)).findById(testTeamId);
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void testUpdate_PartialFields() {
        // Arrange
        UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO();
        updateTeamDTO.setName("Updated Name");

        when(teamRepository.findById(testTeamId)).thenReturn(Optional.of(testTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(testTeam);

        // Act
        TeamDTO result = teamService.update(testTeamId, updateTeamDTO);

        // Assert
        assertNotNull(result);
        verify(teamRepository, times(1)).findById(testTeamId);
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void testUpdate_TeamNotFound() {
        // Arrange
        UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO();
        updateTeamDTO.setName("Updated Name");

        when(teamRepository.findById(testTeamId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> teamService.update(testTeamId, updateTeamDTO));
        verify(teamRepository, times(1)).findById(testTeamId);
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        when(teamRepository.findById(testTeamId)).thenReturn(Optional.of(testTeam));

        // Act
        teamService.deleteById(testTeamId);

        // Assert
        verify(teamRepository, times(1)).findById(testTeamId);
        verify(teamRepository, times(1)).deleteById(testTeamId);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        when(teamRepository.findById(testTeamId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> teamService.deleteById(testTeamId));
        verify(teamRepository, times(1)).findById(testTeamId);
        verify(teamRepository, never()).deleteById(testTeamId);
    }
}

