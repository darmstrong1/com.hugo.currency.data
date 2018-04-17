package com.hugo.currency.data.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.client.Client;

import com.fasterxml.jackson.databind.JsonNode;
import com.hugo.currency.data.Currency;

import jersey.repackaged.com.google.common.base.Preconditions;

public final class ConfigurableCoinMarketCapClient implements CoinMarketCapClient {

  private final CoinMarketCapClient cmcClient;

  ConfigurableCoinMarketCapClient(Client client, String propFileName) throws IOException {
    Preconditions.checkNotNull(propFileName, "propFileName must not be null.");

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
    if (baseURL == null) {
      throw new IllegalStateException(
          String.format("No key with value of 'coin-market-cap.base-url' found in %s", propFileName));
    }
    cmcClient = CoinMarketCapClients.newSimple(client, baseURL);
  }

  @Override
  public JsonNode getCurrency(Currency currency) {
    return cmcClient.getCurrency(currency);
  }

}
