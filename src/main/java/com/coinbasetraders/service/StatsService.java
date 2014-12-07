package com.coinbasetraders.service;

import com.coinbasetraders.model.Stats;
import org.springframework.stereotype.Service;


@Service
public class StatsService {

    private Stats stats;

    public Stats getStats() {
        return stats;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }
}