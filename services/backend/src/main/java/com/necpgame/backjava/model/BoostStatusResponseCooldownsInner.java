package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BoostStatusResponseCooldownsInner
 */

@JsonTypeName("BoostStatusResponse_cooldowns_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class BoostStatusResponseCooldownsInner {

  private @Nullable String boostId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime readyAt;

  public BoostStatusResponseCooldownsInner boostId(@Nullable String boostId) {
    this.boostId = boostId;
    return this;
  }

  /**
   * Get boostId
   * @return boostId
   */
  
  @Schema(name = "boostId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("boostId")
  public @Nullable String getBoostId() {
    return boostId;
  }

  public void setBoostId(@Nullable String boostId) {
    this.boostId = boostId;
  }

  public BoostStatusResponseCooldownsInner readyAt(@Nullable OffsetDateTime readyAt) {
    this.readyAt = readyAt;
    return this;
  }

  /**
   * Get readyAt
   * @return readyAt
   */
  @Valid 
  @Schema(name = "readyAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("readyAt")
  public @Nullable OffsetDateTime getReadyAt() {
    return readyAt;
  }

  public void setReadyAt(@Nullable OffsetDateTime readyAt) {
    this.readyAt = readyAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoostStatusResponseCooldownsInner boostStatusResponseCooldownsInner = (BoostStatusResponseCooldownsInner) o;
    return Objects.equals(this.boostId, boostStatusResponseCooldownsInner.boostId) &&
        Objects.equals(this.readyAt, boostStatusResponseCooldownsInner.readyAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(boostId, readyAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BoostStatusResponseCooldownsInner {\n");
    sb.append("    boostId: ").append(toIndentedString(boostId)).append("\n");
    sb.append("    readyAt: ").append(toIndentedString(readyAt)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

