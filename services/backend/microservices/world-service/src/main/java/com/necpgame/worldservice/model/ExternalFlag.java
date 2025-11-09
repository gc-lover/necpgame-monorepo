package com.necpgame.worldservice.model;

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
 * Gets or Sets ExternalFlag
 */


public enum ExternalFlag {
  
  WAR_METER("WAR_METER"),
  
  CITY_UNREST("CITY_UNREST"),
  
  PROXY_WAR("PROXY_WAR"),
  
  GLOBAL_RESEARCH("GLOBAL_RESEARCH"),
  
  HELIOS_BALANCE("HELIOS_BALANCE"),
  
  SPECTER_ACTIVITY("SPECTER_ACTIVITY");

  private final String value;

  ExternalFlag(String value) {
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
  public static ExternalFlag fromValue(String value) {
    for (ExternalFlag b : ExternalFlag.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

