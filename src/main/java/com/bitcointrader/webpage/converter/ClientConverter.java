package com.bitcointrader.webpage.converter;

import com.bitcointrader.webpage.dto.ClientDTO;
import com.bitcointrader.webpage.model.Client;
import com.coinbase.api.Coinbase;
import com.coinbase.api.CoinbaseBuilder;
import com.coinbase.api.entity.Transfer;

import static com.coinbase.api.entity.Transfer.Type.BUY;
import static com.coinbase.api.entity.Transfer.Type.SELL;

public class ClientConverter {

    public static Client dtoToModel(ClientDTO clientDTO) {
        Coinbase coinbase = new CoinbaseBuilder()
                .withApiKey(clientDTO.getApiKey(), clientDTO.getApiSecret())
                .build();

        String type = clientDTO.getType();
        Transfer.Type typeEnum;
        switch (type) {
            case "sell":
                typeEnum = SELL;
                break;
            case "buy":
                typeEnum = BUY;
                break;
            default:
                throw new RuntimeException("Not supported transaction type");
        }
        return new Client(coinbase, clientDTO.getApiKey(), clientDTO.getLimit(), clientDTO.getAmount(), typeEnum);
    }

    public static ClientDTO modelToDto(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setApiKey(client.getApiKey());
        dto.setAmount(client.getAmount());
        dto.setLimit(client.getLimit());
        dto.setType(client.getType().toString().toLowerCase());
        return dto;
    }
}