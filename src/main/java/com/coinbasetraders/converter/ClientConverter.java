package com.coinbasetraders.converter;

import com.coinbase.api.entity.Transfer;
import com.coinbasetraders.dto.ClientDTO;
import com.coinbasetraders.exception.CoinbaseTradersRuntimeException;
import com.coinbasetraders.model.Client;

import java.util.Collection;
import java.util.List;

import static com.coinbase.api.entity.Transfer.Type.BUY;
import static com.coinbase.api.entity.Transfer.Type.SELL;
import static java.util.stream.Collectors.toList;

public class ClientConverter {

    public static Client dtoToModel(ClientDTO clientDTO) {
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
                throw new CoinbaseTradersRuntimeException("Not supported transaction type");
        }
        return new Client(clientDTO.getRandomId(), clientDTO.getApiKey(), clientDTO.getApiSecret(), clientDTO.getLimit(), clientDTO.getAmount(), typeEnum);
    }

    public static ClientDTO modelToDto(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setRandomId(client.getRandomId());
        dto.setApiKey(client.getApiKey());
        dto.setAmount(client.getAmount());
        dto.setLimit(client.getLimit());
        dto.setType(client.getType().toString().toLowerCase());
        return dto;
    }

    public static List<ClientDTO> modelToDto(Collection<Client> clients) {
        return clients.stream().map(ClientConverter::modelToDto).collect(toList());
    }
}