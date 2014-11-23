package com.bitcointrader.webpage.controller;

import com.bitcointrader.webpage.converter.ClientConverter;
import com.bitcointrader.webpage.dto.ClientDTO;
import com.bitcointrader.webpage.model.Client;
import com.bitcointrader.webpage.service.ClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/rest/client")
public class ClientController {
    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    @RequestMapping(method = RequestMethod.POST, value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerTransaction(@RequestBody ClientDTO clientDTO) {
        LOG.info("Registering a new client: " + clientDTO.toString());

        clientService.registerNewClient(ClientConverter.dtoToModel(clientDTO));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{apiKey}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO getRegisteredClient(@PathVariable("apiKey") String apiKey) {
        LOG.info("Requesting client with apiKey: " + apiKey);
        Client client = clientService.getByApiKey(apiKey);
        LOG.info("Found client: " + client);

        if (client == null) {
            return null;
        }
        return ClientConverter.modelToDto(client);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{apiKey}")
    @ResponseStatus(HttpStatus.OK)
    public void removeClient(@PathVariable("apiKey") String apiKey) {
        LOG.info("Removing client with apiKey: " + apiKey);
        clientService.removeByApiKey(apiKey);
    }

}