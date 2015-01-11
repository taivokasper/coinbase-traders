package com.coinbasetraders.scheduled;

import com.coinbase.api.Coinbase;
import com.coinbase.api.entity.Quote;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbasetraders.model.Client;
import com.coinbasetraders.model.Stats;
import com.coinbasetraders.repository.StatsRepository;
import com.coinbasetraders.service.ClientService;
import com.coinbasetraders.service.StatsService;
import com.coinbasetraders.service.TransactionService;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
public class PricePoller {
    private static final Logger LOG = LoggerFactory.getLogger(PricePoller.class);

    @Autowired
    private Coinbase coinbase;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private StatsService statsService;
    @Autowired
    private StatsRepository statsRepository;

    @Scheduled(cron = "0/5 * * * * *")
    public void poll() {
        LOG.debug("Executing a poll");

        try {
            this.pollSpotPrice();
            this.pollPrices();
        } catch (IOException | CoinbaseException e) {
            LOG.error("An error occurred", e);
        }
    }

    private void pollSpotPrice() throws IOException, CoinbaseException {
        Money spotPrice = coinbase.getSpotPrice(CurrencyUnit.USD);
        LOG.info("Current price is " + spotPrice);

        List<Client> clients = clientService.getClientsWhoMatch(spotPrice.getAmount());
        clients.forEach((client) -> {
            boolean done = transactionService.executeTransaction(client, spotPrice.getAmount());
            if (done) {
                LOG.info("Transaction is done");
                clientService.removeByRandomId(client.getRandomId());
            }
        });
    }

    private void pollPrices() throws IOException, CoinbaseException {
        Money oneBitcoin = Money.parse("BTC 1.0");

        Quote buyQuote = coinbase.getBuyQuote(oneBitcoin);
        LOG.info("Current buy price is " + buyQuote.getSubtotal());

        Quote sellQuote = coinbase.getSellQuote(oneBitcoin);
        LOG.info("Current sell price is " + sellQuote.getSubtotal());

        Stats stats = new Stats(new Date(), buyQuote.getSubtotal().getAmount(), sellQuote.getSubtotal().getAmount());
        statsService.setStats(stats);
        statsRepository.save(stats);
    }
}