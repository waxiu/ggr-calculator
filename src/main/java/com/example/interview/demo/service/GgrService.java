package com.example.interview.demo.service;

import com.example.interview.demo.exception.ExternalApiException;
import com.example.interview.demo.client.ExternalApiClient;
import com.example.interview.demo.model.GameActivity;
import com.example.interview.demo.model.Player;
import com.example.interview.demo.model.PlayerFilters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GgrService {

    private static final Logger logger = LoggerFactory.getLogger(GgrService.class);

    private final ExternalApiClient externalApiClient;

    public GgrService(ExternalApiClient externalApiClient) {
        this.externalApiClient = externalApiClient;
    }

    public Player calculateBiggestLoser(int page, int pageSize) {
        List<Player> players = getPlayers(createPlayerFilters(page, pageSize));
        return calculateGgr(players);
    }

    public Player calculateGgr(List<Player> players) {
        double highestGgr = Double.NEGATIVE_INFINITY;
        Player playerWithHighestGgr = null;

        for (Player player : players) {
            double ggr = calculatePlayerGgr(player);
            if (ggr > highestGgr) {
                highestGgr = ggr;
                playerWithHighestGgr = player;
            }
        }

        return playerWithHighestGgr;
    }

    public double calculatePlayerGgr(Player player) {
        List<GameActivity> activities = gameActivitiesForPlayer(player.getId());
        double ggr = 0.0;

        for (GameActivity activity : activities) {
            ggr += activity.getBetAmount() - activity.getWinAmount();
        }

        return ggr;
    }

    public List<Player> getPlayers(PlayerFilters playerFilters) {
        try {
            return externalApiClient.getPlayerList(playerFilters.getPage(), playerFilters.getPageSize());
        } catch (Exception e) {
            logger.error("Failed to get player list from API");
            throw new ExternalApiException("Unable to fetch players", e);
        }
    }

    public List<GameActivity> gameActivitiesForPlayer(long id) {
        try {
            return externalApiClient.getGameActivitiesByPlayer(id);
        } catch (Exception e) {
            logger.error("Failed to get game activities for player with id " + id);
            throw new ExternalApiException("Unable to fetch game activities for player", e);
        }
    }
    public PlayerFilters createPlayerFilters(int page, int pageSize) {
        return PlayerFilters.builder()
                .page(page)
                .pageSize(pageSize)
                .build();
    }
}

