package com.coinbasetraders.repository;

import com.coinbasetraders.model.Client;
import com.coinbasetraders.model.Stats;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatsRepository extends MongoRepository<Stats, String> {
}