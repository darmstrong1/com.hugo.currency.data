package com.hugo.currency.data.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import com.hugo.currency.data.Currency;

public final class SimpleCoinMarketCapClient implements CoinMarketCapClient {

  private final Client client;
  private final String baseURL;

  // Default (package) scope to force callers to use CoinMarketCapClients to instantiate
  SimpleCoinMarketCapClient(Client client, String baseURL) {
    Preconditions.checkNotNull(client, "client must not be null.");
    Preconditions.checkNotNull(baseURL, "baseURL must not be null.");

    this.client = client;
    this.baseURL = baseURL;
  }

  public JsonNode getCurrency(Currency currency) {
    WebTarget service = client.target(baseURL).path(currency.id());
    Builder builder = service.request(MediaType.APPLICATION_JSON);
    return builder.get(JsonNode.class);
  }

}
