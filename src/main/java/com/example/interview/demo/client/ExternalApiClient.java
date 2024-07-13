package com.example.interview.demo.client;

import com.example.interview.demo.model.GameActivity;
import com.example.interview.demo.model.Player;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "external-api", url = "https://challenge.dev.amusnetgaming.net")
public interface ExternalApiClient {


    // In a real scenario, we could add delay and multiplier to give some time for the API to recover.
    // For example: backoff = @Backoff(delay = 2000, multiplier = 1.5))
    @Retryable(
            maxAttempts = 5,
            backoff = @Backoff(delay = 1))
    @GetMapping("/players")
    List<Player> getPlayerList(@RequestParam int page, @RequestParam int pageSize);


    @Retryable(
            maxAttempts = 5,
            backoff = @Backoff(delay = 1))
    @GetMapping("/game-activity")
    List<GameActivity> getGameActivitiesByPlayer(@RequestParam("playerIds") long id);
}

