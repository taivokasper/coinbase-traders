package com.coinbasetraders.converter;

import com.coinbasetraders.dto.ClientDTO;
import com.coinbasetraders.model.Client;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.List;

import static com.coinbase.api.entity.Transfer.Type.SELL;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ClientConverterTest {

    @Test
    public void testDtoToModel() throws Exception {
        ClientDTO clientDTO = new ClientDTO("1234", "API_KEY", "API_SECRET", BigDecimal.ONE, BigDecimal.TEN, "sell");
        Client client = ClientConverter.dtoToModel(clientDTO);
        assertThat(client.getRandomId(), is(clientDTO.getRandomId()));
        assertThat(client.getApiKey(), is(clientDTO.getApiKey()));
        assertThat(client.getApiSecret(), is(clientDTO.getApiSecret()));
        assertThat(client.getLimit(), is(clientDTO.getLimit()));
        assertThat(client.getAmount(), is(clientDTO.getAmount()));
        assertThat(client.getType().name().toLowerCase(), is(clientDTO.getType()));
    }

    @Test
    public void testModelToDto() throws Exception {
        Client client = new Client("1234", "API_KEY", "API_SECRET", BigDecimal.ONE, BigDecimal.TEN, SELL);
        ClientDTO clientDTO = ClientConverter.modelToDto(client);
        assertModelToDto(client, clientDTO);
    }

    private void assertModelToDto(Client client, ClientDTO clientDTO) {
        assertThat(clientDTO.getRandomId(), is(client.getRandomId()));
        assertThat(clientDTO.getApiKey(), is(client.getApiKey()));
//        For security we never send APi secret to frontend from backend
        assertThat(clientDTO.getApiSecret(), nullValue());
        assertThat(clientDTO.getLimit(), is(client.getLimit()));
        assertThat(clientDTO.getAmount(), is(client.getAmount()));
        assertThat(clientDTO.getType(), is(client.getType().name().toLowerCase()));
    }

    @Test
    public void testModelToDtoCollections() throws Exception {
        Client client = new Client("1234", "API_KEY", "API_SECRET", BigDecimal.ONE, BigDecimal.TEN, SELL);
        List<ClientDTO> clientDTOs = ClientConverter.modelToDto(asList(client, client));
        clientDTOs.stream().forEach(i -> assertModelToDto(client, i));
    }
}