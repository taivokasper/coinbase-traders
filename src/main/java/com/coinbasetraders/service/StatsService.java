package com.coinbasetraders.service;

import com.coinbase.api.Coinbase;
import com.coinbase.api.entity.Quote;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbasetraders.model.Stats;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class StatsService {

    private static final Logger LOG = LoggerFactory.getLogger(StatsService.class);

    @Autowired
    private Coinbase coinbase;

    public Stats getStats() {
        LOG.debug("Starting to collect Coinbase stats");
        Stats stats = new Stats();

        try {
            Money oneBitcoin = Money.parse("BTC 1.0");

            Quote buyQuote = coinbase.getBuyQuote(oneBitcoin);
            stats.setBuyPrice(buyQuote.getSubtotal().getAmount());
            LOG.info("Current buy price is " + buyQuote.getSubtotal());

            Quote sellQuote = coinbase.getSellQuote(oneBitcoin);
            stats.setSellPrice(sellQuote.getSubtotal().getAmount());
            LOG.info("Current sell price is " + sellQuote.getSubtotal());
        } catch (IOException | CoinbaseException e) {
            LOG.error("An error occurred", e);
        }
        return stats;
    }
}