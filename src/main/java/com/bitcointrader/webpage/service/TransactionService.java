package com.bitcointrader.webpage.service;

import com.bitcointrader.webpage.model.Client;
import com.coinbase.api.Coinbase;
import com.coinbase.api.entity.Transfer;
import com.coinbase.api.exception.CoinbaseException;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

import static com.coinbase.api.entity.Transfer.Type.BUY;
import static com.coinbase.api.entity.Transfer.Type.SELL;

@Service
public class TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    @Autowired
    private Coinbase coinbase;

    public boolean executeTransaction(Client client, BigDecimal currentPriceInUsd) {
        try {
            if (BUY.equals(client.getType())) {
                return buy(client, currentPriceInUsd);
            } else if (SELL.equals(client.getType())) {
                return sell(client);
            }
        } catch (CoinbaseException | IOException e) {
            LOG.error("Coinbase exception occurred!", e);
            return false;
        }
        return false;
    }

    private boolean buy(Client client, BigDecimal currentPriceInUsd) throws CoinbaseException, IOException {
        BigDecimal eurToUsd = coinbase.getExchangeRates().get("eur_to_usd");
        LOG.debug("Exchange rate for EUR to USD" + eurToUsd);

        BigDecimal amountToBuyInUsd = client.getAmount().divide(eurToUsd, 8, BigDecimal.ROUND_DOWN);
        BigDecimal amountInBtc = amountToBuyInUsd.divide(currentPriceInUsd, 8, BigDecimal.ROUND_DOWN);

        LOG.debug("Trying to buy " + amountInBtc + " BTC");

        Transfer buy = coinbase.buy(Money.parse("BTC " + amountInBtc.toPlainString()));
        LOG.debug("Got " + buy.getTotal() + " BTC for the buy of " + client.getAmount() + " EUR");

        Transfer.Status buyStatus = buy.getStatus();
        return !buyStatus.equals(Transfer.Status.CANCELED) || !buyStatus.equals(Transfer.Status.REVERSED);
    }

    private boolean sell(Client client) throws CoinbaseException, IOException {
        Transfer.Status sell = client.getCoinbase().sell(Money.parse("BTC " + client.getAmount())).getStatus();
        return !sell.equals(Transfer.Status.CANCELED) || !sell.equals(Transfer.Status.REVERSED);
    }
}