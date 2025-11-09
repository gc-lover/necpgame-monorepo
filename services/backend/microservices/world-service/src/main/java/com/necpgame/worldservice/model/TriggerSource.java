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
 * Gets or Sets TriggerSource
 */


public enum TriggerSource {
  
  SCHEDULED_JOB("SCHEDULED_JOB"),
  
  GM_OVERRIDE("GM_OVERRIDE"),
  
  SYSTEM_ALERT("SYSTEM_ALERT"),
  
  EMERGENCY("EMERGENCY"),
  
  RESEARCH_GATE("RESEARCH_GATE");

  private final String value;

  TriggerSource(String value) {
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
  public static TriggerSource fromValue(String value) {
    for (TriggerSource b : TriggerSource.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

