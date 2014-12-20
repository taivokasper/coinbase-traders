package com.coinbasetraders.service;

import com.coinbasetraders.model.Stats;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;


@Service
public class StatsService {
    @Getter
    @Setter
    private Stats stats;
}