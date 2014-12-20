package com.coinbasetraders.service;

import com.coinbasetraders.exception.DuplicatedTransactionException;
import com.coinbasetraders.model.Client;
import com.coinbasetraders.repository.ClientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.coinbase.api.entity.Transfer.Type.BUY;
import static com.coinbase.api.entity.Transfer.Type.SELL;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;

    @Before
    public void setUp() throws Exception {
        testClient = new Client("1234", "API_KEY", "", BigDecimal.ONE, BigDecimal.TEN, SELL);
    }

    @Test
    public void testGetClientsWhoMatchWhenSelling() throws Exception {
        Client[] sellTests = getSellTestClients();

        List<Client> match = clientService.getClientsWhoMatch(BigDecimal.valueOf(11));
        assertThat(match, hasItems(sellTests[0], sellTests[1], sellTests[2]));
        assertThat(match, hasSize(3));

        match = clientService.getClientsWhoMatch(BigDecimal.valueOf(11.00));
        assertThat(match, hasItems(sellTests[0], sellTests[1], sellTests[2]));
        assertThat(match, hasSize(3));

        match = clientService.getClientsWhoMatch(BigDecimal.valueOf(11.53));
        assertThat(match, hasItems(sellTests[0], sellTests[1], sellTests[2], sellTests[3]));
        assertThat(match, hasSize(4));
    }

    private Client[] getSellTestClients() {
        Client[] clients = {
                new Client("10", "API_KEY", "", BigDecimal.TEN, BigDecimal.valueOf(0.00000001), SELL),
                new Client("11", "API_KEY", "", BigDecimal.valueOf(11), BigDecimal.valueOf(0.10000001), SELL),
                new Client("11", "API_KEY", "", BigDecimal.valueOf(11), BigDecimal.valueOf(0.10000001), SELL),
                new Client("11", "API_KEY", "", BigDecimal.valueOf(11.53), BigDecimal.valueOf(0.10000000), SELL),
                new Client("11", "API_KEY", "", BigDecimal.valueOf(11.55), BigDecimal.valueOf(0.10000), SELL)
        };
        Arrays.stream(clients).forEach(clientService::registerNewClient);
        return clients;
    }

    @Test
    public void testGetClientsWhoMatchWhenBuying() throws Exception {
        Client[] buyTests = getBuyTestClients();

        List<Client> match = clientService.getClientsWhoMatch(BigDecimal.valueOf(11));
        assertThat(match, hasItems(buyTests[1], buyTests[2], buyTests[3], buyTests[4]));
        assertThat(match, hasSize(4));

        match = clientService.getClientsWhoMatch(BigDecimal.valueOf(11.00));
        assertThat(match, hasItems(buyTests[1], buyTests[2], buyTests[3], buyTests[4]));
        assertThat(match, hasSize(4));

        match = clientService.getClientsWhoMatch(BigDecimal.valueOf(11.53));
        assertThat(match, hasItems(buyTests[3], buyTests[4]));
        assertThat(match, hasSize(2));
    }

    private Client[] getBuyTestClients() {
        Client[] clients = {
                new Client("10", "API_KEY", "", BigDecimal.TEN, BigDecimal.valueOf(0.00000001), BUY),
                new Client("11", "API_KEY", "", BigDecimal.valueOf(11), BigDecimal.valueOf(0.10000001), BUY),
                new Client("11", "API_KEY", "", BigDecimal.valueOf(11), BigDecimal.valueOf(0.10000001), BUY),
                new Client("11", "API_KEY", "", BigDecimal.valueOf(11.53), BigDecimal.valueOf(0.10000000), BUY),
                new Client("11", "API_KEY", "", BigDecimal.valueOf(11.55), BigDecimal.valueOf(0.10000), BUY)
        };
        Arrays.stream(clients).forEach(clientService::registerNewClient);
        return clients;
    }

    @Test
    public void testGetByApiKey() throws Exception {
        Client testClient2 = new Client("12345", "API_KEY2", "", BigDecimal.ONE, BigDecimal.TEN, SELL);
        Client testClient3 = new Client("12346", "API_KEY2", "", BigDecimal.ONE, BigDecimal.TEN, SELL);
        clientService.registerNewClient(testClient);
        clientService.registerNewClient(testClient2);
        clientService.registerNewClient(testClient3);

        List<Client> byApiKey = clientService.getByApiKey(testClient.getApiKey());
        assertThat(byApiKey, hasSize(1));
        assertThat(byApiKey, hasItem(testClient));

        List<Client> byApiKey3 = clientService.getByApiKey(testClient2.getApiKey());
        assertThat(byApiKey3, hasSize(2));
        assertThat(byApiKey3, hasItems(testClient2, testClient3));

        List<Client> byApiKeyNull = clientService.getByApiKey(null);
        assertThat(byApiKeyNull, hasSize(0));
    }

    @Test
    public void testRegisterNewClient() throws Exception {
        clientService.registerNewClient(testClient);
        Mockito.verify(repository, times(1)).save(testClient);
    }

    @Test(expected = DuplicatedTransactionException.class)
    public void testRegisterNewClientDuplicatesNotAllowed() throws Exception {
        clientService.registerNewClient(testClient);
        clientService.registerNewClient(testClient);
    }

    @Test
    public void testRemoveByRandomId() throws Exception {
        clientService.registerNewClient(testClient);
        assertThat(clientService.getNumberOfRegisteredTransactions(), is(1L));

        clientService.removeByRandomId(null);
        assertThat(clientService.getNumberOfRegisteredTransactions(), is(1L));

        clientService.removeByRandomId("12345");
        assertThat(clientService.getNumberOfRegisteredTransactions(), is(1L));

        clientService.removeByRandomId(testClient.getRandomId());
        assertThat(clientService.getNumberOfRegisteredTransactions(), is(0L));
    }

    @Test
    public void testGetNumberOfRegisteredTransactions() throws Exception {
        assertThat(clientService.getNumberOfRegisteredTransactions(), is(0L));
        clientService.registerNewClient(testClient);
        assertThat(clientService.getNumberOfRegisteredTransactions(), is(1L));
    }
}