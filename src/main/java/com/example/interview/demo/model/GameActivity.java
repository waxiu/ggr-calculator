package com.example.interview.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameActivity {
    private long id;
    private long playerId;
    private double betAmount;
    private double winAmount;
    private String currency;

}
