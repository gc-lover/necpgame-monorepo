package com.necpgame.partymodule.model;

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
 * PartyQueueStatus
 */


public class PartyQueueStatus {

  private @Nullable String queueId;

  /**
   * Gets or Sets state
   */
  public enum StateEnum {
    IDLE("IDLE"),
    
    QUEUED("QUEUED"),
    
    MATCH_FOUND("MATCH_FOUND"),
    
    CANCELLED("CANCELLED");

    private final String value;

    StateEnum(String value) {
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
    public static StateEnum fromValue(String value) {
      for (StateEnum b : StateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StateEnum state;

  private @Nullable Integer estimatedWait;

  private @Nullable String matchId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime placedAt;

  public PartyQueueStatus queueId(@Nullable String queueId) {
    this.queueId = queueId;
    return this;
  }

  /**
   * Get queueId
   * @return queueId
   */
  
  @Schema(name = "queueId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueId")
  public @Nullable String getQueueId() {
    return queueId;
  }

  public void setQueueId(@Nullable String queueId) {
    this.queueId = queueId;
  }

  public PartyQueueStatus state(@Nullable StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public @Nullable StateEnum getState() {
    return state;
  }

  public void setState(@Nullable StateEnum state) {
    this.state = state;
  }

  public PartyQueueStatus estimatedWait(@Nullable Integer estimatedWait) {
    this.estimatedWait = estimatedWait;
    return this;
  }

  /**
   * Get estimatedWait
   * @return estimatedWait
   */
  
  @Schema(name = "estimatedWait", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimatedWait")
  public @Nullable Integer getEstimatedWait() {
    return estimatedWait;
  }

  public void setEstimatedWait(@Nullable Integer estimatedWait) {
    this.estimatedWait = estimatedWait;
  }

  public PartyQueueStatus matchId(@Nullable String matchId) {
    this.matchId = matchId;
    return this;
  }

  /**
   * Get matchId
   * @return matchId
   */
  
  @Schema(name = "matchId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("matchId")
  public @Nullable String getMatchId() {
    return matchId;
  }

  public void setMatchId(@Nullable String matchId) {
    this.matchId = matchId;
  }

  public PartyQueueStatus placedAt(@Nullable OffsetDateTime placedAt) {
    this.placedAt = placedAt;
    return this;
  }

  /**
   * Get placedAt
   * @return placedAt
   */
  @Valid 
  @Schema(name = "placedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("placedAt")
  public @Nullable OffsetDateTime getPlacedAt() {
    return placedAt;
  }

  public void setPlacedAt(@Nullable OffsetDateTime placedAt) {
    this.placedAt = placedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyQueueStatus partyQueueStatus = (PartyQueueStatus) o;
    return Objects.equals(this.queueId, partyQueueStatus.queueId) &&
        Objects.equals(this.state, partyQueueStatus.state) &&
        Objects.equals(this.estimatedWait, partyQueueStatus.estimatedWait) &&
        Objects.equals(this.matchId, partyQueueStatus.matchId) &&
        Objects.equals(this.placedAt, partyQueueStatus.placedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(queueId, state, estimatedWait, matchId, placedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyQueueStatus {\n");
    sb.append("    queueId: ").append(toIndentedString(queueId)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    estimatedWait: ").append(toIndentedString(estimatedWait)).append("\n");
    sb.append("    matchId: ").append(toIndentedString(matchId)).append("\n");
    sb.append("    placedAt: ").append(toIndentedString(placedAt)).append("\n");
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

