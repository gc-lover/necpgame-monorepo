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
 * Gets or Sets BranchType
 */


public enum BranchType {
  
  ESCORT("ESCORT"),
  
  SABOTAGE("SABOTAGE"),
  
  ARCHIVE("ARCHIVE"),
  
  TRIBUNAL("TRIBUNAL"),
  
  INFILTRATION("INFILTRATION"),
  
  EXTRACTION("EXTRACTION");

  private final String value;

  BranchType(String value) {
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
  public static BranchType fromValue(String value) {
    for (BranchType b : BranchType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

