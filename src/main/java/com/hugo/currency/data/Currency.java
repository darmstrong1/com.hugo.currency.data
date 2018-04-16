package com.hugo.currency.data;

public enum Currency {

  BITCOIN("bitcoin"),
  ETHEREUM("ethereum"),
  RIPPLE("ripple"),
  BITCOIN_CASH("bitcoin-cash"),
  LITECOIN("litecoin"),
  EOS("eos"),
  CARDANO("cardano"),
  STELLAR("stellar"),
  NEO("neo"),
  IOTA("iota");

  private final String id;

  private Currency(String id) {
    this.id = id;
  }

  public String id() {
    return id;
  }

}
