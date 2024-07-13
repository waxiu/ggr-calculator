package com.example.interview.demo;

import com.example.interview.demo.client.ExternalApiClient;
import com.example.interview.demo.model.GameActivity;
import com.example.interview.demo.model.Player;
import com.example.interview.demo.model.PlayerFilters;
import com.example.interview.demo.service.GgrService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GgrServiceTest {

    @Mock
    private ExternalApiClient externalApiClient;

    @InjectMocks
    private GgrService ggrService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateBiggestLoser_shouldReturnPlayerWithHighestGgr() {
        // Arrange
        Player player1 = new Player(1L, "John", "Doe");
        Player player2 = new Player(2L, "Jane", "Smith");
        List<Player> players = Arrays.asList(player1, player2);

        GameActivity activity1 = new GameActivity(1L, 1L, 100.0, 50.0, "USD");
        GameActivity activity2 = new GameActivity(2L, 1L, 200.0, 100.0, "USD");
        GameActivity activity3 = new GameActivity(3L, 2L, 300.0, 150.0, "USD");
        GameActivity activity4 = new GameActivity(4L, 2L, 400.0, 300.0, "USD");

        when(externalApiClient.getPlayerList(anyInt(), anyInt())).thenReturn(players);
        when(externalApiClient.getGameActivitiesByPlayer(1)).thenReturn(Arrays.asList(activity1, activity2));
        when(externalApiClient.getGameActivitiesByPlayer(2)).thenReturn(Arrays.asList(activity3, activity4));

        // Act
        Player result = ggrService.calculateBiggestLoser(1, 10);

        // Assert
        assertNotNull(result);
        assertEquals(player2, result);
    }

    @Test
    void calculateGgr_shouldReturnPlayerWithHighestGgr() {
        // Arrange
        Player player1 = new Player(1L, "John", "Doe");
        Player player2 = new Player(2L, "Jane", "Smith");
        List<Player> players = Arrays.asList(player1, player2);

        when(ggrService.gameActivitiesForPlayer(1L)).thenReturn(Arrays.asList(
                new GameActivity(1L, 1L, 100.0, 50.0, "USD"),
                new GameActivity(2L, 1L, 200.0, 100.0, "USD")
        ));

        when(ggrService.gameActivitiesForPlayer(2L)).thenReturn(Arrays.asList(
                new GameActivity(3L, 2L, 300.0, 150.0, "USD"),
                new GameActivity(4L, 2L, 400.0, 300.0, "USD")
        ));

        // Act
        Player result = ggrService.calculateGgr(players);

        // Assert
        assertNotNull(result);
        assertEquals(player2, result);
    }

    @Test
    void calculatePlayerGgr_shouldReturnCorrectGgr() {
        // Arrange
        Player player = new Player(1L, "John", "Doe");
        List<GameActivity> activities = Arrays.asList(
                new GameActivity(1L, 1L, 100.0, 50.0, "USD"),
                new GameActivity(2L, 1L, 200.0, 100.0, "USD")
        );

        when(ggrService.gameActivitiesForPlayer(1L)).thenReturn(activities);

        // Act
        double result = ggrService.calculatePlayerGgr(player);

        // Assert
        assertEquals(150.0, result);
    }

    @Test
    void getPlayers_shouldReturnPlayerList() {
        // Arrange
        List<Player> players = Arrays.asList(
                new Player(1L, "John", "Doe"),
                new Player(2L, "Jane", "Smith")
        );
        PlayerFilters filters = PlayerFilters.builder()
                .page(1)
                .pageSize(10)
                .build();

        when(externalApiClient.getPlayerList(filters.getPage(), filters.getPageSize())).thenReturn(players);

        // Act
        List<Player> result = ggrService.getPlayers(filters);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void gameActivitiesForPlayer_shouldReturnActivityList() {
        // Arrange
        List<GameActivity> activities = Arrays.asList(
                new GameActivity(1L, 1L, 100.0, 50.0, "USD"),
                new GameActivity(2L, 1L, 200.0, 100.0, "USD")
        );

        when(externalApiClient.getGameActivitiesByPlayer(1)).thenReturn(activities);

        // Act
        List<GameActivity> result = ggrService.gameActivitiesForPlayer(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
