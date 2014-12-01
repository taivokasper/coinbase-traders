package com.coinbasetrader.webpage.controller;

import com.coinbase.api.Coinbase;
import com.coinbasetrader.webpage.converter.ClientConverter;
import com.coinbasetrader.webpage.dto.ClientDTO;
import com.coinbasetrader.webpage.model.Client;
import com.coinbasetrader.webpage.model.RandomId;
import com.coinbasetrader.webpage.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "rest/client")
public class ClientController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerTransaction(@RequestBody ClientDTO clientDTO) {
        LOG.info("Registering a new client: " + clientDTO.toString());

        Coinbase coinbase = clientService.getCoinbaseByApiKey(clientDTO.getApiKey());
        clientService.registerNewClient(ClientConverter.dtoToModel(clientDTO, coinbase));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{apiKey}")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientDTO> getRegisteredClient(@PathVariable("apiKey") String apiKey) {
        LOG.info("Requesting clients with apiKey: " + apiKey);
        List<Client> clients = clientService.getByApiKey(apiKey);
        LOG.info("Found clients: " + clients);

        if (clients == null) {
            return null;
        }
        return ClientConverter.modelToDto(clients);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/random")
    @ResponseStatus(HttpStatus.OK)
    public RandomId generateRandomHash() {
        return new RandomId(UUID.randomUUID().toString().replace("-", ""));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transactions/stats")
    @ResponseStatus(HttpStatus.OK)
    public Long numberOfRegisteredTransactions() {
        return clientService.getNumberOfRegisteredTransactions();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{randomId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeClient(@PathVariable("randomId") String randomId) {
        LOG.info("Removing client with randomId: " + randomId);
        clientService.removeByRandomId(randomId);
    }

}