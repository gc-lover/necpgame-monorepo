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
 * Gets or Sets ChannelType
 */


public enum ChannelType {
  
  GLOBAL("GLOBAL"),
  
  LOCAL("LOCAL"),
  
  ZONE("ZONE"),
  
  PARTY("PARTY"),
  
  RAID("RAID"),
  
  GUILD("GUILD"),
  
  GUILD_OFFICER("GUILD_OFFICER"),
  
  WHISPER("WHISPER"),
  
  TRADE("TRADE"),
  
  SYSTEM("SYSTEM"),
  
  CUSTOM("CUSTOM"),
  
  EVENT("EVENT");

  private final String value;

  ChannelType(String value) {
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
  public static ChannelType fromValue(String value) {
    for (ChannelType b : ChannelType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

