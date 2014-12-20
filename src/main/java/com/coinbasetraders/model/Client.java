package com.coinbasetraders.model;

import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.coinbase.api.entity.Transfer;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

@Data
@ToString(exclude="apiSecret")
public class Client {

    @Id
    private String randomId;
    private String apiKey;
    private String apiSecret;
    private BigDecimal limit;
    private BigDecimal amount;
    private Transfer.Type type;

    @Transient
    private Coinbase coinbase;

    @PersistenceConstructor
    public Client(String randomId, String apiKey, String apiSecret, BigDecimal limit, BigDecimal amount, Transfer.Type type) {
        this.randomId = randomId;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.limit = limit;
        this.amount = amount;
        this.type = type;

        coinbase = new CoinbaseBuilder()
            .withApiKey(apiKey, apiSecret)
            .build();
    }
}