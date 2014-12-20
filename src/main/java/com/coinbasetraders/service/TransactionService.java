package com.coinbasetraders.service;

import com.coinbase.api.Coinbase;
import com.coinbase.api.entity.Transfer;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbasetraders.model.Client;
import org.joda.money.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

import static com.coinbase.api.entity.Transfer.Status.CANCELED;
import static com.coinbase.api.entity.Transfer.Status.REVERSED;
import static com.coinbase.api.entity.Transfer.Type.BUY;
import static com.coinbase.api.entity.Transfer.Type.SELL;

@Service
public class TransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

    private static final int COINBASE_BITCOIN_PRECISION = 8;

    @Autowired
    private Coinbase publicCoinbase;
    @Autowired
    private ClientService clientService;

    public boolean executeTransaction(Client client, BigDecimal currentPriceInUsd) {
        try {
            if (BUY.equals(client.getType())) {
                return buy(client, amountToBuyInBtc(client.getAmount(), currentPriceInUsd));
            } else if (SELL.equals(client.getType())) {
                return sell(client);
            }
        } catch (CoinbaseException e) {
            LOG.error("Coinbase exception occurred! Removing randomId \"" + client.getRandomId() + "\" client", e);
            clientService.removeByRandomId(client.getRandomId());
        } catch (IOException e) {
            LOG.error("Unknown IOException occurred!", e);
        }
        return false;
    }

    private boolean buy(Client client, BigDecimal amountToBuyInBtc) throws CoinbaseException, IOException {
        LOG.debug("Trying to buy " + amountToBuyInBtc + " BTC");

        Transfer buy = client.getCoinbase().buy(Money.parse("BTC " + amountToBuyInBtc.toPlainString()));
        LOG.debug("Got " + buy.getTotal() + " BTC for the buy of " + client.getAmount() + " EUR");

        Transfer.Status buyStatus = buy.getStatus();
        return !CANCELED.equals(buyStatus) && !REVERSED.equals(buyStatus);
    }

    private BigDecimal amountToBuyInBtc(BigDecimal amountInEur, BigDecimal currentPriceInUsd) throws IOException, CoinbaseException {
        BigDecimal eurToUsd = publicCoinbase.getExchangeRates().get("eur_to_usd");
        LOG.debug("Exchange rate for EUR to USD" + eurToUsd);

        BigDecimal amountToBuyInUsd = amountInEur.multiply(eurToUsd);
        return amountToBuyInUsd.divide(currentPriceInUsd, COINBASE_BITCOIN_PRECISION, BigDecimal.ROUND_DOWN);
    }

    private boolean sell(Client client) throws CoinbaseException, IOException {
        Transfer.Status sell = client.getCoinbase().sell(bigDecimalToBtcMoney(client.getAmount())).getStatus();
        return !CANCELED.equals(sell) && !REVERSED.equals(sell);
    }

    private Money bigDecimalToBtcMoney(BigDecimal amount) {
        return Money.parse("BTC " + amount.setScale(COINBASE_BITCOIN_PRECISION).toPlainString());
    }
}