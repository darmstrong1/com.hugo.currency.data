package com.hugo.currency.data.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.databind.JsonNode;
import com.hugo.currency.data.Currency;

public class SimpleCoinMarketCapClientTest {

  @Test
  public void testGetCurrency() {
    Currency currency = Currency.BITCOIN;
    String baseURL = "testBaseURL";
    Client client = Mockito.mock(Client.class);
    WebTarget target = Mockito.mock(WebTarget.class);
    Mockito.when(client.target(baseURL)).thenReturn(target);
    Mockito.when(target.path(currency.id())).thenReturn(target);
    Builder builder = Mockito.mock(Builder.class);
    Mockito.when(target.request(MediaType.APPLICATION_JSON)).thenReturn(builder);
    JsonNode expected = Mockito.mock(JsonNode.class);
    Mockito.when(builder.get(JsonNode.class)).thenReturn(expected);

    CoinMarketCapClient cmcClient = CoinMarketCapClients.newSimple(client, baseURL);
    JsonNode actual = cmcClient.getCurrency(currency);
    Assert.assertEquals(expected, actual);
  }

}
