package com.hugo.currency.data.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.hugo.currency.data.Currency;

public interface CoinMarketCapClient {

  JsonNode getCurrency(Currency currency);
}
