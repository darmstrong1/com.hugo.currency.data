package com.hugo.currency.data.client;

import java.io.IOException;

import javax.ws.rs.client.Client;

public class CoinMarketCapClients {

  private CoinMarketCapClients() {
  }

  public static CoinMarketCapClient newSimple(Client client, String baseURL) {
    return new SimpleCoinMarketCapClient(client, baseURL);
  }

  public static CoinMarketCapClient newConfigurable(Client client, String propFileName) throws IOException {
    return new ConfigurableCoinMarketCapClient(client, propFileName);
  }
}
