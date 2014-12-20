package com.coinbasetraders.service;

import com.coinbasetraders.exception.DuplicatedTransactionException;
import com.coinbasetraders.model.Client;
import com.coinbasetraders.repository.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.coinbase.api.entity.Transfer.Type.BUY;
import static com.coinbase.api.entity.Transfer.Type.SELL;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.isEmpty;

@Service
public class ClientService {
    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository repository;

    private List<Client> registeredClients;

    public List<Client> getClientsWhoMatch(BigDecimal currentPrice) {
        return getClients().stream()
                .filter(i ->
                    SELL.equals(i.getType()) && currentPrice.compareTo(i.getLimit()) >= 0 ||
                        BUY.equals(i.getType()) && currentPrice.compareTo(i.getLimit()) <= 0)
                .collect(toList());
    }

    public List<Client> getByApiKey(String apiKey) {
        return getClients().stream()
                .filter(clientI -> clientI.getApiKey().equals(apiKey))
                .collect(toList());
    }

    public void registerNewClient(Client client) {
        if (client == null) {
            return;
        }
        if (getClients().stream().findFirst().filter(i -> i.equals(client)).isPresent()) {
            throw new DuplicatedTransactionException();
        }
        repository.save(client);
        getClients().add(client);
    }

    public void removeByRandomId(String random) {
        if (isEmpty(random)) {
            return;
        }
        repository.deleteByRandomId(random);
        getClients().removeIf(i -> random.equals(i.getRandomId()));
    }

    public Long getNumberOfRegisteredTransactions() {
        return (long) getClients().size();
    }

    private List<Client> getClients() {
        if (registeredClients == null) {
            log.debug("No registered clients. Looking from database.");
            registeredClients = repository.findAll();
            log.debug("Found " + registeredClients.size() + " registered clients from database");
        }
        return registeredClients;
    }
}