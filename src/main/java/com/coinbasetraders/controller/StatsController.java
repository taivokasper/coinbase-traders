package com.coinbasetraders.controller;

import com.coinbasetraders.model.Stats;
import com.coinbasetraders.service.StatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "rest/stats")
public class StatsController {
    private static final Logger LOG = LoggerFactory.getLogger(StatsController.class);

    @Autowired
    private StatsService statsService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Stats getStats() {
        LOG.info("Requesting Coinbase stats");
        return statsService.getStats();
    }
}