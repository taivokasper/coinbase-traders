package com.coinbasetraders.model;

import java.math.BigDecimal;

public class Stats {

    private BigDecimal buyPrice;
    private BigDecimal sellPrice;

    public Stats(BigDecimal buyPrice, BigDecimal sellPrice) {
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    public BigDecimal getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(BigDecimal buyPrice) {
        this.buyPrice = buyPrice;
    }

    public BigDecimal getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(BigDecimal sellPrice) {
        this.sellPrice = sellPrice;
    }
}
