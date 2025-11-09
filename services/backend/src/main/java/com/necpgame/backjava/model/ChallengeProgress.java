package com.necpgame.backjava.model;

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
 * ChallengeProgress
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ChallengeProgress {

  private @Nullable String challengeId;

  private @Nullable String playerId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Integer progressValue;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  public ChallengeProgress challengeId(@Nullable String challengeId) {
    this.challengeId = challengeId;
    return this;
  }

  /**
   * Get challengeId
   * @return challengeId
   */
  
  @Schema(name = "challengeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("challengeId")
  public @Nullable String getChallengeId() {
    return challengeId;
  }

  public void setChallengeId(@Nullable String challengeId) {
    this.challengeId = challengeId;
  }

  public ChallengeProgress playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public ChallengeProgress status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public ChallengeProgress progressValue(@Nullable Integer progressValue) {
    this.progressValue = progressValue;
    return this;
  }

  /**
   * Get progressValue
   * @return progressValue
   */
  
  @Schema(name = "progressValue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progressValue")
  public @Nullable Integer getProgressValue() {
    return progressValue;
  }

  public void setProgressValue(@Nullable Integer progressValue) {
    this.progressValue = progressValue;
  }

  public ChallengeProgress updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChallengeProgress challengeProgress = (ChallengeProgress) o;
    return Objects.equals(this.challengeId, challengeProgress.challengeId) &&
        Objects.equals(this.playerId, challengeProgress.playerId) &&
        Objects.equals(this.status, challengeProgress.status) &&
        Objects.equals(this.progressValue, challengeProgress.progressValue) &&
        Objects.equals(this.updatedAt, challengeProgress.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(challengeId, playerId, status, progressValue, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeProgress {\n");
    sb.append("    challengeId: ").append(toIndentedString(challengeId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    progressValue: ").append(toIndentedString(progressValue)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

