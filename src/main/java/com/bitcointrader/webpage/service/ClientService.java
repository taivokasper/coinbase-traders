package com.bitcointrader.webpage.service;

import com.bitcointrader.webpage.model.Client;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.coinbase.api.entity.Transfer.Type.BUY;
import static com.coinbase.api.entity.Transfer.Type.SELL;

@Service
public class ClientService {

    private List<Client> registeredClients = new ArrayList<>();

    public List<Client> getClientsWhoMatch(BigDecimal currentPrice) {
        return registeredClients.stream()
                .filter((i) ->
                        SELL.equals(i.getType()) && currentPrice.compareTo(i.getLimit()) >= 0 ||
                        BUY.equals(i.getType()) && currentPrice.compareTo(i.getLimit()) <= 0)
                .collect(Collectors.toList());
    }

    public Client getByApiKey(String apiKey) {
        for (Client registeredClient : registeredClients) {
            if (registeredClient.getApiKey().equals(apiKey)) {
                return registeredClient;
            }
        }
        return null;
    }

    public void registerNewClient(Client client) {
        registeredClients.add(client);
    }

    public void removeByApiKey(String apiKey) {
        registeredClients.removeIf((i) -> apiKey.equals(i.getApiKey()));
    }
}