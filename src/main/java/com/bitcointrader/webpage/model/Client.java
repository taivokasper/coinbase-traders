package com.bitcointrader.webpage.model;

import com.coinbase.api.Coinbase;
import com.coinbase.api.entity.Transfer;

import java.math.BigDecimal;

public class Client {
    private String apiKey;
    private Coinbase coinbase;
    private BigDecimal limit;
    private BigDecimal amount;
    private Transfer.Type type;

    public Client(Coinbase coinbase, String apiKey, BigDecimal limit, BigDecimal amount, Transfer.Type type) {
        this.coinbase = coinbase;
        this.apiKey = apiKey;
        this.limit = limit;
        this.amount = amount;
        this.type = type;
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

    @Override
    public String toString() {
        return "Client{" +
                "apiKey='" + apiKey + '\'' +
                ", coinbase=" + coinbase +
                ", limit=" + limit +
                ", amount=" + amount +
                ", type=" + type +
                '}';
    }
}