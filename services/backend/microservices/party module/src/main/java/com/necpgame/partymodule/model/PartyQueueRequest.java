package com.necpgame.partymodule.model;

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
 * PartyQueueRequest
 */


public class PartyQueueRequest {

  private String contentType;

  private @Nullable String difficulty;

  /**
   * Gets or Sets matchmakingMode
   */
  public enum MatchmakingModeEnum {
    FILL("FILL"),
    
    FLEX("FLEX"),
    
    RANKED("RANKED");

    private final String value;

    MatchmakingModeEnum(String value) {
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
    public static MatchmakingModeEnum fromValue(String value) {
      for (MatchmakingModeEnum b : MatchmakingModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MatchmakingModeEnum matchmakingMode;

  private @Nullable String region;

  private @Nullable String idempotencyKey;

  public PartyQueueRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PartyQueueRequest(String contentType) {
    this.contentType = contentType;
  }

  public PartyQueueRequest contentType(String contentType) {
    this.contentType = contentType;
    return this;
  }

  /**
   * Get contentType
   * @return contentType
   */
  @NotNull 
  @Schema(name = "contentType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("contentType")
  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public PartyQueueRequest difficulty(@Nullable String difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  
  @Schema(name = "difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable String getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable String difficulty) {
    this.difficulty = difficulty;
  }

  public PartyQueueRequest matchmakingMode(@Nullable MatchmakingModeEnum matchmakingMode) {
    this.matchmakingMode = matchmakingMode;
    return this;
  }

  /**
   * Get matchmakingMode
   * @return matchmakingMode
   */
  
  @Schema(name = "matchmakingMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("matchmakingMode")
  public @Nullable MatchmakingModeEnum getMatchmakingMode() {
    return matchmakingMode;
  }

  public void setMatchmakingMode(@Nullable MatchmakingModeEnum matchmakingMode) {
    this.matchmakingMode = matchmakingMode;
  }

  public PartyQueueRequest region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public PartyQueueRequest idempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
    return this;
  }

  /**
   * Get idempotencyKey
   * @return idempotencyKey
   */
  
  @Schema(name = "idempotencyKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idempotencyKey")
  public @Nullable String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyQueueRequest partyQueueRequest = (PartyQueueRequest) o;
    return Objects.equals(this.contentType, partyQueueRequest.contentType) &&
        Objects.equals(this.difficulty, partyQueueRequest.difficulty) &&
        Objects.equals(this.matchmakingMode, partyQueueRequest.matchmakingMode) &&
        Objects.equals(this.region, partyQueueRequest.region) &&
        Objects.equals(this.idempotencyKey, partyQueueRequest.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contentType, difficulty, matchmakingMode, region, idempotencyKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyQueueRequest {\n");
    sb.append("    contentType: ").append(toIndentedString(contentType)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    matchmakingMode: ").append(toIndentedString(matchmakingMode)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
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

