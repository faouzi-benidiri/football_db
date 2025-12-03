package com.example.football_db.integration;

import com.example.football_db.entity.Team;
import com.example.football_db.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeamIntegrationTest {
    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();
    }

    @Test
    void testCreateTeam_Success() {
        // Arrange
        Team team = new Team("FC Barcelona", "FCB", null, new BigDecimal("100000.00"));

        // Act
        Team savedTeam = teamRepository.save(team);

        // Assert
        assertNotNull(savedTeam.getId());
        assertEquals("FC Barcelona", savedTeam.getName());
        assertEquals("FCB", savedTeam.getAcronym());
        assertEquals(new BigDecimal("100000.00"), savedTeam.getBudget());
    }

    @Test
    void testGetAllTeams_Success() {
        // Arrange
        Team team1 = new Team("FC Barcelona", "FCB", null, new BigDecimal("100000.00"));
        Team team2 = new Team("Real Madrid", "RM", null, new BigDecimal("120000.00"));
        teamRepository.saveAll(java.util.List.of(team1, team2));

        // Act
        long count = teamRepository.count();

        // Assert
        assertEquals(2, count);
    }

    @Test
    void testGetTeamById_Success() {
        // Arrange
        Team team = new Team("FC Barcelona", "FCB", null, new BigDecimal("100000.00"));
        Team savedTeam = teamRepository.save(team);

        // Act
        Optional<Team> retrievedTeam = teamRepository.findById(savedTeam.getId());

        // Assert
        assertTrue(retrievedTeam.isPresent());
        assertEquals(savedTeam.getId(), retrievedTeam.get().getId());
        assertEquals("FC Barcelona", retrievedTeam.get().getName());
    }

    @Test
    void testGetTeamById_NotFound() {
        // Act & Assert
        Optional<Team> team = teamRepository.findById(java.util.UUID.randomUUID());
        assertFalse(team.isPresent());
    }

    @Test
    void testUpdateTeam_Success() {
        // Arrange
        Team team = new Team("FC Barcelona", "FCB", null, new BigDecimal("100000.00"));
        Team savedTeam = teamRepository.save(team);

        // Act
        savedTeam.setName("FC Barcelona Updated");
        savedTeam.setBudget(new BigDecimal("150000.00"));
        Team updatedTeam = teamRepository.save(savedTeam);

        // Assert
        assertEquals("FC Barcelona Updated", updatedTeam.getName());
        assertEquals(new BigDecimal("150000.00"), updatedTeam.getBudget());
    }

    @Test
    void testDeleteTeam_Success() {
        // Arrange
        Team team = new Team("FC Barcelona", "FCB", null, new BigDecimal("100000.00"));
        Team savedTeam = teamRepository.save(team);
        assertEquals(1, teamRepository.count());

        // Act
        teamRepository.deleteById(savedTeam.getId());

        // Assert
        assertEquals(0, teamRepository.count());
    }

    @Test
    void testCreateAndRetrieveTeam_CompleteFlow() {
        // Arrange
        Team team = new Team("FC Barcelona", "FCB", null, new BigDecimal("100000.00"));

        // Act - Create team
        Team createdTeam = teamRepository.save(team);
        assertEquals(1, teamRepository.count());

        // Act - Retrieve team
        Optional<Team> retrievedTeam = teamRepository.findById(createdTeam.getId());
        assertTrue(retrievedTeam.isPresent());
        assertEquals("FC Barcelona", retrievedTeam.get().getName());

        // Act - Update team
        createdTeam.setName("FC Barcelona Updated");
        Team updatedTeam = teamRepository.save(createdTeam);
        assertEquals("FC Barcelona Updated", updatedTeam.getName());

        // Act - Delete team
        teamRepository.deleteById(updatedTeam.getId());
        assertEquals(0, teamRepository.count());
    }

    @Test
    void testMultipleTeams_Persistence() {
        // Arrange
        for (int i = 0; i < 15; i++) {
            Team team = new Team("Team " + i, "T" + i, null, new BigDecimal("100000.00"));
            teamRepository.save(team);
        }

        // Act
        long count = teamRepository.count();

        // Assert
        assertEquals(15, count);
    }
}

