package com.coinbasetraders.service;

import com.coinbase.api.Coinbase;
import com.coinbase.api.entity.Transfer;
import com.coinbase.api.exception.CoinbaseException;
import com.coinbasetraders.model.Client;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;

import static com.coinbase.api.entity.Transfer.Status.CANCELED;
import static com.coinbase.api.entity.Transfer.Status.COMPLETE;
import static com.coinbase.api.entity.Transfer.Status.REVERSED;
import static com.coinbase.api.entity.Transfer.Type.BUY;
import static com.coinbase.api.entity.Transfer.Type.SELL;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceTest {
    @Mock(answer = RETURNS_DEEP_STUBS)
    private Coinbase publicCoinbase;
    @Mock
    private ClientService clientService;
    @Mock(answer = RETURNS_DEEP_STUBS)
    private Coinbase privateCoinbase;

    @InjectMocks
    private TransactionService transactionService;

    @Before
    public void setUp() throws Exception {
        when(publicCoinbase.getExchangeRates().get("eur_to_usd")).thenReturn(BigDecimal.valueOf(1.222938));
    }

    @Test
    public void testExecuteSellTransaction_notChangedToSomePowerOfTen() throws Exception {
        Client client = createTestClient(BigDecimal.valueOf(412.08), BigDecimal.valueOf(0.00000001), SELL);
        ArgumentCaptor<Money> sellCaptor = ArgumentCaptor.forClass(Money.class);

        transactionService.executeTransaction(client, BigDecimal.valueOf(412.09));
        verify(privateCoinbase).sell(sellCaptor.capture());

        assertThat(sellCaptor.getValue(), is(Money.parse("BTC 0.00000001")));
    }

    @Test
    public void testExecuteSellTransaction_CorrectNrOfScale() throws Exception {
        Client client = createTestClient(BigDecimal.valueOf(412.08), BigDecimal.valueOf(1.00), SELL);
        ArgumentCaptor<Money> sellCaptor = ArgumentCaptor.forClass(Money.class);

        transactionService.executeTransaction(client, BigDecimal.valueOf(412.09));
        verify(privateCoinbase).sell(sellCaptor.capture());

        assertThat(sellCaptor.getValue(), is(Money.parse("BTC 1.00000000")));
    }

    @Test
    public void testExecuteSellTransaction_coinbaseTransferStatus() throws Exception {
        Client client = createTestClient(BigDecimal.valueOf(412.08), BigDecimal.valueOf(32.12), SELL);
        //noinspection unchecked
        when(privateCoinbase.sell(anyObject()).getStatus()).thenReturn(COMPLETE);
        boolean transactionDone = transactionService.executeTransaction(client, BigDecimal.valueOf(312.07));
        assertThat(transactionDone, is(true));

        when(privateCoinbase.sell(anyObject()).getStatus()).thenReturn(CANCELED);
        transactionDone = transactionService.executeTransaction(client, BigDecimal.valueOf(312.07));
        assertThat(transactionDone, is(false));

        when(privateCoinbase.sell(anyObject()).getStatus()).thenReturn(REVERSED);
        transactionDone = transactionService.executeTransaction(client, BigDecimal.valueOf(312.07));
        assertThat(transactionDone, is(false));
    }

    private Client createTestClient(BigDecimal limit, BigDecimal amount, Transfer.Type type) {
        Client client = new Client("RANDOM", "API_KEY", "API_SECRET", limit, amount, type);
        client.setCoinbase(privateCoinbase);
        return client;
    }

    @Test
    public void testExecuteBuyTransaction_correctAmountGivenToCoinbaseApi() throws Exception {
        Client client = createTestClient(BigDecimal.valueOf(412.08), BigDecimal.valueOf(32.12), BUY);
        ArgumentCaptor<Money> buyCaptor = ArgumentCaptor.forClass(Money.class);

        transactionService.executeTransaction(client, BigDecimal.valueOf(312.07));
        verify(privateCoinbase).buy(buyCaptor.capture());

        assertThat(buyCaptor.getValue(), is(Money.parse("BTC 0.12587165")));
    }

    @Test
    public void testExecuteBuyTransaction_coinbaseTransferStatus() throws Exception {
        Client client = createTestClient(BigDecimal.valueOf(412.08), BigDecimal.valueOf(32.12), BUY);
        //noinspection unchecked
        when(privateCoinbase.buy(anyObject()).getStatus()).thenReturn(COMPLETE);
        boolean transactionDone = transactionService.executeTransaction(client, BigDecimal.valueOf(312.07));
        assertThat(transactionDone, is(true));

        when(privateCoinbase.buy(anyObject()).getStatus()).thenReturn(CANCELED);
        transactionDone = transactionService.executeTransaction(client, BigDecimal.valueOf(312.07));
        assertThat(transactionDone, is(false));

        when(privateCoinbase.buy(anyObject()).getStatus()).thenReturn(REVERSED);
        transactionDone = transactionService.executeTransaction(client, BigDecimal.valueOf(312.07));
        assertThat(transactionDone, is(false));
    }

    @Test
    public void testExecuteBuyTransaction_invalidCredentialsRemovesTheRegisteredClient() throws Exception {
        //noinspection unchecked
        when(privateCoinbase.buy(anyObject()).getStatus()).thenThrow(CoinbaseException.class);

        Client client = createTestClient(BigDecimal.valueOf(412.08), BigDecimal.valueOf(32.12), BUY);
        boolean transactionDone = transactionService.executeTransaction(client, BigDecimal.valueOf(312.07));

        verify(clientService, times(1)).removeByRandomId("RANDOM");
        assertThat(transactionDone, is(false));
    }

    @Test
    public void testExecuteBuyTransaction_retunsFalseWhenIOException() throws Exception {
        //noinspection unchecked
        when(privateCoinbase.buy(anyObject()).getStatus()).thenThrow(IOException.class);

        Client client = createTestClient(BigDecimal.valueOf(412.08), BigDecimal.valueOf(32.12), BUY);
        boolean transactionDone = transactionService.executeTransaction(client, BigDecimal.valueOf(312.07));
        assertThat(transactionDone, is(false));
    }
}