package com.coinbasetraders.service;

import com.coinbase.api.Coinbase;
import com.coinbasetraders.model.Client;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public List<Client> getByApiKey(String apiKey) {
        return registeredClients.stream()
                .filter(clientI -> clientI.getApiKey().equals(apiKey))
                .collect(Collectors.toList());
    }

    public Coinbase getCoinbaseByApiKey(String apiKey) {
        List<Client> byApiKey = getByApiKey(apiKey);
        if (byApiKey.isEmpty()) {
            return null;
        }

        Optional<Client> first = byApiKey.stream().filter(clientI -> clientI.getCoinbase() != null).findFirst();
        if (first.isPresent()) {
            return first.get().getCoinbase();
        }
        removeByApiKey(apiKey);
        return null;
    }

    public void registerNewClient(Client client) {
        registeredClients.add(client);
    }

    public void removeByRandomId(String random) {
        registeredClients.removeIf(i -> random.equals(i.getRandomId()));
    }

    public void removeByApiKey(String apiKey) {
        registeredClients.removeIf(i -> i.getApiKey().equals(apiKey));
    }

    public Long getNumberOfRegisteredTransactions() {
        return (long) registeredClients.size();
    }
}