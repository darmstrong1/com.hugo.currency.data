package com.hugo.currency.data.client;

import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;

public class JsonResponseParser {

  private final JsonNode head;
  private final JsonNode node;

  public JsonResponseParser(JsonNode head) {
    Preconditions.checkNotNull(head, "head must not be null.");

    this.head = head;

    Preconditions.checkState(head.isArray(),
        "JSON response is in an unexpected format. The head node is supposed to be an array.");

    Iterator<JsonNode> itr = head.iterator();

    Preconditions.checkState(itr.hasNext(), "JSON response is in an unexpected format. The head node is empty.");

    node = itr.next();
  }

  public String getValue(String key) {
    JsonNode value = node.get(key);
    if (value != null) {
      return value.asText();
    }
    throw new IllegalStateException("No value for " + key + " was found in the response!");
  }

  public String responseAsString() {
    return head.asText();
  }
}
