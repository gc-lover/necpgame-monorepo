package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * RewardHistoryEntry
 */


public class RewardHistoryEntry {

  private @Nullable String rewardId;

  private @Nullable Integer level;

  private @Nullable String track;

  private @Nullable String rewardType;

  private @Nullable Integer amount;

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    LEVEL("LEVEL"),
    
    CHALLENGE("CHALLENGE"),
    
    BONUS("BONUS");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SourceEnum source;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime claimedAt;

  public RewardHistoryEntry rewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
    return this;
  }

  /**
   * Get rewardId
   * @return rewardId
   */
  
  @Schema(name = "rewardId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardId")
  public @Nullable String getRewardId() {
    return rewardId;
  }

  public void setRewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
  }

  public RewardHistoryEntry level(@Nullable Integer level) {
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

  public RewardHistoryEntry track(@Nullable String track) {
    this.track = track;
    return this;
  }

  /**
   * Get track
   * @return track
   */
  
  @Schema(name = "track", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("track")
  public @Nullable String getTrack() {
    return track;
  }

  public void setTrack(@Nullable String track) {
    this.track = track;
  }

  public RewardHistoryEntry rewardType(@Nullable String rewardType) {
    this.rewardType = rewardType;
    return this;
  }

  /**
   * Get rewardType
   * @return rewardType
   */
  
  @Schema(name = "rewardType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardType")
  public @Nullable String getRewardType() {
    return rewardType;
  }

  public void setRewardType(@Nullable String rewardType) {
    this.rewardType = rewardType;
  }

  public RewardHistoryEntry amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  public RewardHistoryEntry source(@Nullable SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable SourceEnum getSource() {
    return source;
  }

  public void setSource(@Nullable SourceEnum source) {
    this.source = source;
  }

  public RewardHistoryEntry claimedAt(@Nullable OffsetDateTime claimedAt) {
    this.claimedAt = claimedAt;
    return this;
  }

  /**
   * Get claimedAt
   * @return claimedAt
   */
  @Valid 
  @Schema(name = "claimedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("claimedAt")
  public @Nullable OffsetDateTime getClaimedAt() {
    return claimedAt;
  }

  public void setClaimedAt(@Nullable OffsetDateTime claimedAt) {
    this.claimedAt = claimedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardHistoryEntry rewardHistoryEntry = (RewardHistoryEntry) o;
    return Objects.equals(this.rewardId, rewardHistoryEntry.rewardId) &&
        Objects.equals(this.level, rewardHistoryEntry.level) &&
        Objects.equals(this.track, rewardHistoryEntry.track) &&
        Objects.equals(this.rewardType, rewardHistoryEntry.rewardType) &&
        Objects.equals(this.amount, rewardHistoryEntry.amount) &&
        Objects.equals(this.source, rewardHistoryEntry.source) &&
        Objects.equals(this.claimedAt, rewardHistoryEntry.claimedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardId, level, track, rewardType, amount, source, claimedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardHistoryEntry {\n");
    sb.append("    rewardId: ").append(toIndentedString(rewardId)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    track: ").append(toIndentedString(track)).append("\n");
    sb.append("    rewardType: ").append(toIndentedString(rewardType)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    claimedAt: ").append(toIndentedString(claimedAt)).append("\n");
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

