package com.necpgame.gameplayservice.model;

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
 * PlayerInventoryItemUsageStats
 */

@JsonTypeName("PlayerInventoryItem_usageStats")

public class PlayerInventoryItemUsageStats {

  private @Nullable Integer equippedCount;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastEquippedAt;

  public PlayerInventoryItemUsageStats equippedCount(@Nullable Integer equippedCount) {
    this.equippedCount = equippedCount;
    return this;
  }

  /**
   * Get equippedCount
   * @return equippedCount
   */
  
  @Schema(name = "equippedCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("equippedCount")
  public @Nullable Integer getEquippedCount() {
    return equippedCount;
  }

  public void setEquippedCount(@Nullable Integer equippedCount) {
    this.equippedCount = equippedCount;
  }

  public PlayerInventoryItemUsageStats lastEquippedAt(@Nullable OffsetDateTime lastEquippedAt) {
    this.lastEquippedAt = lastEquippedAt;
    return this;
  }

  /**
   * Get lastEquippedAt
   * @return lastEquippedAt
   */
  @Valid 
  @Schema(name = "lastEquippedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastEquippedAt")
  public @Nullable OffsetDateTime getLastEquippedAt() {
    return lastEquippedAt;
  }

  public void setLastEquippedAt(@Nullable OffsetDateTime lastEquippedAt) {
    this.lastEquippedAt = lastEquippedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerInventoryItemUsageStats playerInventoryItemUsageStats = (PlayerInventoryItemUsageStats) o;
    return Objects.equals(this.equippedCount, playerInventoryItemUsageStats.equippedCount) &&
        Objects.equals(this.lastEquippedAt, playerInventoryItemUsageStats.lastEquippedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(equippedCount, lastEquippedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerInventoryItemUsageStats {\n");
    sb.append("    equippedCount: ").append(toIndentedString(equippedCount)).append("\n");
    sb.append("    lastEquippedAt: ").append(toIndentedString(lastEquippedAt)).append("\n");
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

