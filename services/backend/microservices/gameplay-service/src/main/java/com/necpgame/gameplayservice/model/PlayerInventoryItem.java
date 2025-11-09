package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.PlayerInventoryItemUsageStats;
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
 * PlayerInventoryItem
 */


public class PlayerInventoryItem {

  private String itemId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime acquiredAt;

  private String source;

  private @Nullable Integer duplicateCount;

  private @Nullable Boolean favorite;

  private @Nullable PlayerInventoryItemUsageStats usageStats;

  public PlayerInventoryItem() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerInventoryItem(String itemId, OffsetDateTime acquiredAt, String source) {
    this.itemId = itemId;
    this.acquiredAt = acquiredAt;
    this.source = source;
  }

  public PlayerInventoryItem itemId(String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @NotNull 
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("itemId")
  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public PlayerInventoryItem acquiredAt(OffsetDateTime acquiredAt) {
    this.acquiredAt = acquiredAt;
    return this;
  }

  /**
   * Get acquiredAt
   * @return acquiredAt
   */
  @NotNull @Valid 
  @Schema(name = "acquiredAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("acquiredAt")
  public OffsetDateTime getAcquiredAt() {
    return acquiredAt;
  }

  public void setAcquiredAt(OffsetDateTime acquiredAt) {
    this.acquiredAt = acquiredAt;
  }

  public PlayerInventoryItem source(String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public PlayerInventoryItem duplicateCount(@Nullable Integer duplicateCount) {
    this.duplicateCount = duplicateCount;
    return this;
  }

  /**
   * Get duplicateCount
   * minimum: 0
   * @return duplicateCount
   */
  @Min(value = 0) 
  @Schema(name = "duplicateCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duplicateCount")
  public @Nullable Integer getDuplicateCount() {
    return duplicateCount;
  }

  public void setDuplicateCount(@Nullable Integer duplicateCount) {
    this.duplicateCount = duplicateCount;
  }

  public PlayerInventoryItem favorite(@Nullable Boolean favorite) {
    this.favorite = favorite;
    return this;
  }

  /**
   * Get favorite
   * @return favorite
   */
  
  @Schema(name = "favorite", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("favorite")
  public @Nullable Boolean getFavorite() {
    return favorite;
  }

  public void setFavorite(@Nullable Boolean favorite) {
    this.favorite = favorite;
  }

  public PlayerInventoryItem usageStats(@Nullable PlayerInventoryItemUsageStats usageStats) {
    this.usageStats = usageStats;
    return this;
  }

  /**
   * Get usageStats
   * @return usageStats
   */
  @Valid 
  @Schema(name = "usageStats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("usageStats")
  public @Nullable PlayerInventoryItemUsageStats getUsageStats() {
    return usageStats;
  }

  public void setUsageStats(@Nullable PlayerInventoryItemUsageStats usageStats) {
    this.usageStats = usageStats;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerInventoryItem playerInventoryItem = (PlayerInventoryItem) o;
    return Objects.equals(this.itemId, playerInventoryItem.itemId) &&
        Objects.equals(this.acquiredAt, playerInventoryItem.acquiredAt) &&
        Objects.equals(this.source, playerInventoryItem.source) &&
        Objects.equals(this.duplicateCount, playerInventoryItem.duplicateCount) &&
        Objects.equals(this.favorite, playerInventoryItem.favorite) &&
        Objects.equals(this.usageStats, playerInventoryItem.usageStats);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, acquiredAt, source, duplicateCount, favorite, usageStats);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerInventoryItem {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    acquiredAt: ").append(toIndentedString(acquiredAt)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    duplicateCount: ").append(toIndentedString(duplicateCount)).append("\n");
    sb.append("    favorite: ").append(toIndentedString(favorite)).append("\n");
    sb.append("    usageStats: ").append(toIndentedString(usageStats)).append("\n");
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

