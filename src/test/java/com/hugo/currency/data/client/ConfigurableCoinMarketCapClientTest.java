package com.hugo.currency.data.client;

import java.io.IOException;

import javax.ws.rs.client.Client;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import com.fasterxml.jackson.databind.JsonNode;
import com.hugo.currency.data.Currency;

public class ConfigurableCoinMarketCapClientTest {

  @Test
  public void testGetCurrency() throws IOException {
    Currency currency = Currency.BITCOIN;
    String propFilename = "application.properties";
    Client client = Mockito.mock(Client.class);
    CoinMarketCapClient simpleClient = Mockito.mock(CoinMarketCapClient.class);
    JsonNode expected = Mockito.mock(JsonNode.class);
    Mockito.when(simpleClient.getCurrency(currency)).thenReturn(expected);

    CoinMarketCapClient cmcClient = CoinMarketCapClients.newConfigurable(client, propFilename);
    Whitebox.setInternalState(cmcClient, "cmcClient", simpleClient);
    JsonNode actual = cmcClient.getCurrency(currency);

    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCurrencyWrongPropertiesFile() throws IOException {
    String propFilename = "wrong.properties";
    Client client = Mockito.mock(Client.class);

    try {
      CoinMarketCapClients.newConfigurable(client, propFilename);
    } catch (IllegalStateException e) {
      String expected = String.format(
          "Failed to create a stream for %s file. Ensure that the file exists and that the path is correct",
          propFilename);

      Assert.assertEquals(expected, e.getMessage());
      throw e;
    }
  }

  @Test(expected = IllegalStateException.class)
  public void testGetCurrencyPropertiesMissingKey() throws IOException {
    String propFilename = "bad.properties";
    Client client = Mockito.mock(Client.class);

    try {
      CoinMarketCapClients.newConfigurable(client, propFilename);
    } catch (IllegalStateException e) {
      String expected = String.format("No key with value of 'coin-market-cap.base-url' found in %s", propFilename);

      Assert.assertEquals(expected, e.getMessage());
      throw e;
    }
  }

}
