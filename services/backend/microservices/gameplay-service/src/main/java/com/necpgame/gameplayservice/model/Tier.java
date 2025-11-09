package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets Tier
 */


public enum Tier {
  
  BRONZE("BRONZE"),
  
  SILVER("SILVER"),
  
  GOLD("GOLD"),
  
  PLATINUM("PLATINUM"),
  
  DIAMOND("DIAMOND"),
  
  MASTER("MASTER"),
  
  GRANDMASTER("GRANDMASTER");

  private final String value;

  Tier(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Tier fromValue(String value) {
    for (Tier b : Tier.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

