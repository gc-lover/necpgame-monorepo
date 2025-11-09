package com.necpgame.adminservice.model;

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
 * Gets or Sets MetricUnit
 */


public enum MetricUnit {
  
  PERCENTAGE("percentage"),
  
  SECONDS("seconds"),
  
  CREDITS_PER_HOUR("credits_per_hour"),
  
  SCORE("score"),
  
  INDEX("index"),
  
  COMPLIANCE_RATIO("compliance_ratio");

  private final String value;

  MetricUnit(String value) {
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
  public static MetricUnit fromValue(String value) {
    for (MetricUnit b : MetricUnit.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

