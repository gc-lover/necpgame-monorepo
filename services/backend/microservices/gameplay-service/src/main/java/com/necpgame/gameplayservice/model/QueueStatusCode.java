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
 * Gets or Sets QueueStatusCode
 */


public enum QueueStatusCode {
  
  QUEUED("QUEUED"),
  
  MATCHING("MATCHING"),
  
  MATCH_FOUND("MATCH_FOUND"),
  
  CANCELLED("CANCELLED"),
  
  TIMEOUT("TIMEOUT");

  private final String value;

  QueueStatusCode(String value) {
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
  public static QueueStatusCode fromValue(String value) {
    for (QueueStatusCode b : QueueStatusCode.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

