package com.hugo.currency.data.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.client.Client;

import com.fasterxml.jackson.databind.JsonNode;
import com.hugo.currency.data.Currency;

public final class ConfigurableCoinMarketCapClient implements CoinMarketCapClient {

  private final CoinMarketCapClient cmcClient;

  ConfigurableCoinMarketCapClient(Client client, String propFileName) throws IOException {
    Properties prop = new Properties();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    try (InputStream stream = loader.getResourceAsStream(propFileName)) {
      if (stream == null) {
        throw new IllegalStateException(String.format(
            "Failed to create a stream for %s file. Ensure that the file exists and that the path is correct",
            propFileName));
      }
      prop.load(stream);
    }
    String baseURL = prop.getProperty("coin-market-cap.base-url");
    cmcClient = CoinMarketCapClients.newSimple(client, baseURL);
  }

  @Override
  public JsonNode getCurrency(Currency currency) {
    return cmcClient.getCurrency(currency);
  }

}
