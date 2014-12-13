package com.coinbasetraders.model;

import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.coinbase.api.entity.Transfer;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;

import java.math.BigDecimal;

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
    public Client(String randomId, String apiKey, String  apiSecret, BigDecimal limit, BigDecimal amount, Transfer.Type type) {
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

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public Coinbase getCoinbase() {
        return coinbase;
    }

    public void setCoinbase(Coinbase coinbase) {
        this.coinbase = coinbase;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Transfer.Type getType() {
        return type;
    }

    public void setType(Transfer.Type type) {
        this.type = type;
    }

    public String getRandomId() {
        return randomId;
    }

    public void setRandomId(String randomId) {
        this.randomId = randomId;
    }

    @Override
    public String toString() {
        return "Client{" +
                "randomId='" + randomId + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", limit=" + limit +
                ", amount=" + amount +
                ", type=" + type +
                '}';
    }
}