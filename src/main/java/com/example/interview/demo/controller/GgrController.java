package com.example.interview.demo.controller;

import com.example.interview.demo.service.GgrService;
import com.example.interview.demo.model.Player;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GgrController {

    GgrService ggrService;

    public GgrController(GgrService ggrService){
        this.ggrService = ggrService;
    }

    @GetMapping("/biggest-loser/{page}/{pageSize}")
    public Player calculateBiggestLooser(@PathVariable int page, @PathVariable int pageSize){
        return ggrService.calculateBiggestLoser(page, pageSize);
    }
}
