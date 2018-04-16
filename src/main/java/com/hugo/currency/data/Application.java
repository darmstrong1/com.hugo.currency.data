package com.hugo.currency.data;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.hugo.currency.data.client.CoinMarketCapClient;
import com.hugo.currency.data.client.CoinMarketCapClients;
import com.hugo.currency.data.client.JsonResponseParser;

public class Application {

  private static final String PRICE_USD = "price_usd";
  private static final String MARKET_CAP = "market_cap_usd";

  public static void main(String[] args) {
    if (args.length == 0) {
      usage();
    } else if (args.length > 1) {
      usage(buildMsg("Application received the following arguments:", args));
    } else {
      try {
        displayPriceAndCap(getCurrency(args[0]));
      } catch (IllegalArgumentException e) {
        usage(buildMsg(e.getMessage(), args[0]));
      }
    }
  }

  private static Currency getCurrency(String id) {
    List<Currency> currencies = Arrays.asList(Currency.values());
    Optional<Currency> currency = currencies.stream()
        .filter(c -> id.toLowerCase().equals(c.id())).findFirst();
    if (currency.isPresent()) {
      return currency.get();
    }
    throw new IllegalArgumentException("Application received an argument that is not a valid type.");
  }

  private static void displayPriceAndCap(Currency currency) {

    try {
      CoinMarketCapClient cmcClient = CoinMarketCapClients.newConfigurable(ClientBuilder.newClient(),
          "application.properties");
      JsonNode head = cmcClient.getCurrency(currency);
      JsonResponseParser parser = new JsonResponseParser(head);

      System.out.println(String.format("%s USD price : %s", currency.id(), parser.getValue(PRICE_USD)));
      System.out.println(String.format("%s Market cap: %s", currency.id(), parser.getValue(MARKET_CAP)));
    } catch (IllegalStateException | IOException | ProcessingException e) {
      System.err.println(
          String.format("The following illegal state prevented us from parsing the response: %s", e.toString()));
    }
  }

  private static void usage(String suffix) {
    StringBuilder sb = new StringBuilder(buildErrPrefix());
    sb.append(System.lineSeparator());
    sb.append(suffix);
    System.err.println(sb.toString());
  }

  private static void usage() {
    System.err.println(buildErrPrefix());
  }

  private static String buildErrPrefix() {
    List<String> currencies = Lists.newArrayList();
    for (Currency c : Currency.values()) {
      currencies.add(c.id());
    }
    return buildMsg("Application requires one argument only that must be one of the following:", currencies);
  }

  private static String buildMsg(String prefix, List<String> values) {
    StringBuilder sb = new StringBuilder(prefix);
    for (String value : values) {
      sb.append(System.lineSeparator()).append("\t");
      sb.append(value);
    }

    return sb.toString();
  }

  private static String buildMsg(String prefix, String... values) {
    return buildMsg(prefix, Arrays.asList(values));
  }

  private static String buildMsg(String prefix, String value) {
    return buildMsg(prefix, ImmutableList.of(value));
  }

}
