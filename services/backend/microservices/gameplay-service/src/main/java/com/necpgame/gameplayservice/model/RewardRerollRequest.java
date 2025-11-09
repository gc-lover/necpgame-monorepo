package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RewardRerollRequest
 */


public class RewardRerollRequest {

  private Integer level;

  /**
   * Gets or Sets track
   */
  public enum TrackEnum {
    FREE("FREE"),
    
    PREMIUM("PREMIUM");

    private final String value;

    TrackEnum(String value) {
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
    public static TrackEnum fromValue(String value) {
      for (TrackEnum b : TrackEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TrackEnum track;

  private @Nullable String tokenId;

  public RewardRerollRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RewardRerollRequest(Integer level, TrackEnum track) {
    this.level = level;
    this.track = track;
  }

  public RewardRerollRequest level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  @NotNull 
  @Schema(name = "level", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public RewardRerollRequest track(TrackEnum track) {
    this.track = track;
    return this;
  }

  /**
   * Get track
   * @return track
   */
  @NotNull 
  @Schema(name = "track", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("track")
  public TrackEnum getTrack() {
    return track;
  }

  public void setTrack(TrackEnum track) {
    this.track = track;
  }

  public RewardRerollRequest tokenId(@Nullable String tokenId) {
    this.tokenId = tokenId;
    return this;
  }

  /**
   * Get tokenId
   * @return tokenId
   */
  
  @Schema(name = "tokenId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tokenId")
  public @Nullable String getTokenId() {
    return tokenId;
  }

  public void setTokenId(@Nullable String tokenId) {
    this.tokenId = tokenId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardRerollRequest rewardRerollRequest = (RewardRerollRequest) o;
    return Objects.equals(this.level, rewardRerollRequest.level) &&
        Objects.equals(this.track, rewardRerollRequest.track) &&
        Objects.equals(this.tokenId, rewardRerollRequest.tokenId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(level, track, tokenId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardRerollRequest {\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    track: ").append(toIndentedString(track)).append("\n");
    sb.append("    tokenId: ").append(toIndentedString(tokenId)).append("\n");
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

