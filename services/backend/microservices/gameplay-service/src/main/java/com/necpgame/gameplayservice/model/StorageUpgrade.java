package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * StorageUpgrade
 */


public class StorageUpgrade {

  private @Nullable Integer level;

  private @Nullable Integer bonusSlots;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime acquiredAt;

  public StorageUpgrade level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public StorageUpgrade bonusSlots(@Nullable Integer bonusSlots) {
    this.bonusSlots = bonusSlots;
    return this;
  }

  /**
   * Get bonusSlots
   * @return bonusSlots
   */
  
  @Schema(name = "bonusSlots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonusSlots")
  public @Nullable Integer getBonusSlots() {
    return bonusSlots;
  }

  public void setBonusSlots(@Nullable Integer bonusSlots) {
    this.bonusSlots = bonusSlots;
  }

  public StorageUpgrade acquiredAt(@Nullable OffsetDateTime acquiredAt) {
    this.acquiredAt = acquiredAt;
    return this;
  }

  /**
   * Get acquiredAt
   * @return acquiredAt
   */
  @Valid 
  @Schema(name = "acquiredAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("acquiredAt")
  public @Nullable OffsetDateTime getAcquiredAt() {
    return acquiredAt;
  }

  public void setAcquiredAt(@Nullable OffsetDateTime acquiredAt) {
    this.acquiredAt = acquiredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StorageUpgrade storageUpgrade = (StorageUpgrade) o;
    return Objects.equals(this.level, storageUpgrade.level) &&
        Objects.equals(this.bonusSlots, storageUpgrade.bonusSlots) &&
        Objects.equals(this.acquiredAt, storageUpgrade.acquiredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, bonusSlots, acquiredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StorageUpgrade {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    bonusSlots: ").append(toIndentedString(bonusSlots)).append("\n");
    sb.append("    acquiredAt: ").append(toIndentedString(acquiredAt)).append("\n");
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

