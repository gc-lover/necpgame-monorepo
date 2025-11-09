package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ActivityRegistration
 */


public class ActivityRegistration {

  private UUID eventId;

  private UUID playerId;

  private String activityId;

  private @Nullable UUID partyId;

  private @Nullable Float score;

  private @Nullable Integer durationSeconds;

  public ActivityRegistration() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActivityRegistration(UUID eventId, UUID playerId, String activityId) {
    this.eventId = eventId;
    this.playerId = playerId;
    this.activityId = activityId;
  }

  public ActivityRegistration eventId(UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull @Valid 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public ActivityRegistration playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public ActivityRegistration activityId(String activityId) {
    this.activityId = activityId;
    return this;
  }

  /**
   * Get activityId
   * @return activityId
   */
  @NotNull 
  @Schema(name = "activityId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activityId")
  public String getActivityId() {
    return activityId;
  }

  public void setActivityId(String activityId) {
    this.activityId = activityId;
  }

  public ActivityRegistration partyId(@Nullable UUID partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @Valid 
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable UUID getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable UUID partyId) {
    this.partyId = partyId;
  }

  public ActivityRegistration score(@Nullable Float score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * @return score
   */
  
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable Float getScore() {
    return score;
  }

  public void setScore(@Nullable Float score) {
    this.score = score;
  }

  public ActivityRegistration durationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
    return this;
  }

  /**
   * Get durationSeconds
   * minimum: 1
   * @return durationSeconds
   */
  @Min(value = 1) 
  @Schema(name = "durationSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationSeconds")
  public @Nullable Integer getDurationSeconds() {
    return durationSeconds;
  }

  public void setDurationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActivityRegistration activityRegistration = (ActivityRegistration) o;
    return Objects.equals(this.eventId, activityRegistration.eventId) &&
        Objects.equals(this.playerId, activityRegistration.playerId) &&
        Objects.equals(this.activityId, activityRegistration.activityId) &&
        Objects.equals(this.partyId, activityRegistration.partyId) &&
        Objects.equals(this.score, activityRegistration.score) &&
        Objects.equals(this.durationSeconds, activityRegistration.durationSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, playerId, activityId, partyId, score, durationSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActivityRegistration {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    activityId: ").append(toIndentedString(activityId)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    durationSeconds: ").append(toIndentedString(durationSeconds)).append("\n");
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

