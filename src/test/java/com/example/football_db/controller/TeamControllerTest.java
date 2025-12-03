package com.example.football_db.controller;

import com.example.football_db.dto.team.CreateTeamDTO;
import com.example.football_db.dto.team.TeamDTO;
import com.example.football_db.dto.team.UpdateTeamDTO;
import com.example.football_db.exception.TeamNotFoundException;
import com.example.football_db.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamControllerTest {
    @Mock
    private TeamService teamService;
    @InjectMocks
    private TeamController teamController;

    private TeamDTO testTeamDTO;
    private UUID testTeamId;

    @BeforeEach
    void setUp() {
        testTeamId = UUID.randomUUID();
        testTeamDTO = new TeamDTO(testTeamId, "FC Barcelona", "FCB", new BigDecimal("100000.00"), new ArrayList<>());
    }

    @Test
    void testFindAll_Success() {
        // Arrange
        Page<TeamDTO> teams = new PageImpl<>(List.of(testTeamDTO), PageRequest.of(0, 10), 1);
        when(teamService.getAllTeams(any())).thenReturn(teams);

        // Act
        ResponseEntity<Page<TeamDTO>> response = teamController.findAll(PageRequest.of(0, 10));

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        verify(teamService, times(1)).getAllTeams(any());
    }

    @Test
    void testFindById_Success() {
        // Arrange
        when(teamService.getTeamById(testTeamId)).thenReturn(testTeamDTO);

        // Act
        ResponseEntity<TeamDTO> response = teamController.findById(testTeamId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testTeamDTO.getId(), response.getBody().getId());
        assertEquals("FC Barcelona", response.getBody().getName());
        verify(teamService, times(1)).getTeamById(testTeamId);
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(teamService.getTeamById(testTeamId))
                .thenThrow(new TeamNotFoundException("Team not found with ID: " + testTeamId));

        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> teamController.findById(testTeamId));
        verify(teamService, times(1)).getTeamById(testTeamId);
    }

    @Test
    void testSave_Success() {
        // Arrange
        CreateTeamDTO createTeamDTO = new CreateTeamDTO();
        createTeamDTO.setName("FC Barcelona");
        createTeamDTO.setAcronym("FCB");
        createTeamDTO.setBudget(new BigDecimal("100000.00"));

        when(teamService.save(any(CreateTeamDTO.class))).thenReturn(testTeamDTO);

        // Act
        ResponseEntity<TeamDTO> response = teamController.save(createTeamDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(testTeamDTO.getId(), response.getBody().getId());
        verify(teamService, times(1)).save(any(CreateTeamDTO.class));
    }

    @Test
    void testUpdate_Success() {
        // Arrange
        UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO();
        updateTeamDTO.setName("Updated Name");

        when(teamService.update(eq(testTeamId), any(UpdateTeamDTO.class))).thenReturn(testTeamDTO);

        // Act
        ResponseEntity<TeamDTO> response = teamController.update(updateTeamDTO, testTeamId);

        // Assert
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        verify(teamService, times(1)).update(eq(testTeamId), any(UpdateTeamDTO.class));
    }

    @Test
    void testUpdate_NotFound() {
        // Arrange
        UpdateTeamDTO updateTeamDTO = new UpdateTeamDTO();
        updateTeamDTO.setName("Updated Name");

        when(teamService.update(eq(testTeamId), any(UpdateTeamDTO.class)))
                .thenThrow(new TeamNotFoundException("Team not found with ID: " + testTeamId));

        // Act & Assert
        assertThrows(TeamNotFoundException.class,
            () -> teamController.update(updateTeamDTO, testTeamId));
        verify(teamService, times(1)).update(eq(testTeamId), any(UpdateTeamDTO.class));
    }

    @Test
    void testDeleteById_Success() {
        // Arrange
        doNothing().when(teamService).deleteById(testTeamId);

        // Act
        ResponseEntity<Void> response = teamController.deleteById(testTeamId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(teamService, times(1)).deleteById(testTeamId);
    }

    @Test
    void testDeleteById_NotFound() {
        // Arrange
        doThrow(new TeamNotFoundException("Team not found with ID: " + testTeamId))
                .when(teamService).deleteById(testTeamId);

        // Act & Assert
        assertThrows(TeamNotFoundException.class, () -> teamController.deleteById(testTeamId));
        verify(teamService, times(1)).deleteById(testTeamId);
    }
}

