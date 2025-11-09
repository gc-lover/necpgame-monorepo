package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HUDDataBuffsDebuffsInner
 */

@JsonTypeName("HUDData_buffs_debuffs_inner")

public class HUDDataBuffsDebuffsInner {

  private @Nullable String name;

  private @Nullable Integer remainingSeconds;

  public HUDDataBuffsDebuffsInner name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public HUDDataBuffsDebuffsInner remainingSeconds(@Nullable Integer remainingSeconds) {
    this.remainingSeconds = remainingSeconds;
    return this;
  }

  /**
   * Get remainingSeconds
   * @return remainingSeconds
   */
  
  @Schema(name = "remaining_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remaining_seconds")
  public @Nullable Integer getRemainingSeconds() {
    return remainingSeconds;
  }

  public void setRemainingSeconds(@Nullable Integer remainingSeconds) {
    this.remainingSeconds = remainingSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HUDDataBuffsDebuffsInner huDDataBuffsDebuffsInner = (HUDDataBuffsDebuffsInner) o;
    return Objects.equals(this.name, huDDataBuffsDebuffsInner.name) &&
        Objects.equals(this.remainingSeconds, huDDataBuffsDebuffsInner.remainingSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, remainingSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HUDDataBuffsDebuffsInner {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    remainingSeconds: ").append(toIndentedString(remainingSeconds)).append("\n");
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

