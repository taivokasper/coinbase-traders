package com.coinbasetrader.webpage.scheduled;

import com.coinbase.api.Coinbase;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbasetrader.webpage.model.Client;
import com.coinbasetrader.webpage.service.ClientService;
import com.coinbasetrader.webpage.service.TransactionService;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
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

    @Scheduled(cron = "0/5 * * * * *")
    public void poll() {
        LOG.debug("Executing a poll");

        try {
            Money spotPrice = coinbase.getSpotPrice(CurrencyUnit.USD);
            LOG.info("Current price is " + spotPrice);

            List<Client> clients = clientService.getClientsWhoMatch(spotPrice.getAmount());
            clients.forEach((client) -> {
                boolean done = transactionService.executeTransaction(client, spotPrice.getAmount());
                if (done) {
                    LOG.info("Transaction is done");
                    clientService.removeByApiKey(client.getApiKey());
                }
            });
        } catch (IOException | CoinbaseException e) {
            LOG.error("An error occurred", e);
        }
    }

}