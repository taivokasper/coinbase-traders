package com.coinbasetraders.repository;

import com.coinbasetraders.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ClientRepository extends MongoRepository<Client, String> {
    List<Client> deleteByApiKey(String apiKey);
    List<Client> deleteByRandomId(String randomId);
}
