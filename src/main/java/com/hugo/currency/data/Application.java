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
        displayPricesAndCaps(getCurrencyIds(args));
    }

    private static List<String> getCurrencyIds(String[] args) {
        List<String> ids;
        if (args.length == 0) {
            ids = getCurrencyIds();
        } else {
            ids = Lists.newArrayList();
            for (String arg : args) {
                ids.add(arg);
            }
        }
        return ids;
    }

    private static List<String> getCurrencyIds() {
        List<String> ids = Lists.newArrayList();
        List<Currency> currencies = Arrays.asList(Currency.values());
        currencies.forEach(c -> ids.add(c.id()));
        return ids;
    }

    private static Currency getCurrency(String id) {
        List<Currency> currencies = Arrays.asList(Currency.values());
        Optional<Currency> currency = currencies.stream().filter(c -> id.toLowerCase().equals(c.id())).findFirst();
        if (currency.isPresent()) {
            return currency.get();
        }
        throw new IllegalArgumentException("Application received an argument that is not a valid type.");
    }

    private static void displayPricesAndCaps(List<String> ids) {
        ids.forEach(id -> {
            displayPriceAndCap(id);
        });
    }

    private static void displayPriceAndCap(String id) {
        try {
            displayPriceAndCap(getCurrency(id));
        } catch (IllegalArgumentException e) {
            usage(buildMsg(e.getMessage(), id));
        }
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
            System.err.println(String.format("The following illegal state prevented us from parsing the response: %s",
                    e.toString()));
        }
    }

    private static void usage(String suffix) {
        StringBuilder sb = new StringBuilder(buildErrPrefix());
        sb.append(System.lineSeparator());
        sb.append(suffix);
        System.err.println(sb.toString());
    }

    private static String buildErrPrefix() {
        List<String> currencies = Lists.newArrayList();
        for (Currency c : Currency.values()) {
            currencies.add(c.id());
        }
        return buildMsg("Application arguments must be one of the following:", currencies);
    }

    private static String buildMsg(String prefix, List<String> values) {
        StringBuilder sb = new StringBuilder(prefix);
        for (String value : values) {
            sb.append(System.lineSeparator()).append("\t");
            sb.append(value);
        }

        return sb.toString();
    }

    private static String buildMsg(String prefix, String value) {
        return buildMsg(prefix, ImmutableList.of(value));
    }

}
