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
 * Gets or Sets PlayerOrderDraftStatus
 */


public enum PlayerOrderDraftStatus {
  
  DRAFT("draft"),
  
  PENDING_ESTIMATE("pending_estimate"),
  
  PENDING_VALIDATION("pending_validation"),
  
  READY_TO_PUBLISH("ready_to_publish"),
  
  PUBLISHED("published"),
  
  CANCELLED("cancelled");

  private final String value;

  PlayerOrderDraftStatus(String value) {
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
  public static PlayerOrderDraftStatus fromValue(String value) {
    for (PlayerOrderDraftStatus b : PlayerOrderDraftStatus.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

