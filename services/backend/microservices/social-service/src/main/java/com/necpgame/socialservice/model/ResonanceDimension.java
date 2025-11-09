package com.necpgame.socialservice.model;

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
 * Категория вклада в индекс доверия.
 */


public enum ResonanceDimension {
  
  REPUTATION("REPUTATION"),
  
  ROMANCE("ROMANCE"),
  
  SOCIAL_EVENTS("SOCIAL_EVENTS"),
  
  ALLIANCE("ALLIANCE"),
  
  CRISIS_BUFFER("CRISIS_BUFFER");

  private final String value;

  ResonanceDimension(String value) {
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
  public static ResonanceDimension fromValue(String value) {
    for (ResonanceDimension b : ResonanceDimension.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

