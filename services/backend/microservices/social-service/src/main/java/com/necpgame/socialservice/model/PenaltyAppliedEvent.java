package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * PenaltyAppliedEvent
 */


public class PenaltyAppliedEvent {

  private UUID eventId;

  private UUID penaltyId;

  private UUID playerId;

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    EXECUTOR("executor"),
    
    CLIENT("client");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RoleEnum role;

  private @Nullable String penaltyType;

  private Float delta;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime appliedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public PenaltyAppliedEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PenaltyAppliedEvent(UUID eventId, UUID penaltyId, UUID playerId, RoleEnum role, Float delta, OffsetDateTime appliedAt) {
    this.eventId = eventId;
    this.penaltyId = penaltyId;
    this.playerId = playerId;
    this.role = role;
    this.delta = delta;
    this.appliedAt = appliedAt;
  }

  public PenaltyAppliedEvent eventId(UUID eventId) {
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

  public PenaltyAppliedEvent penaltyId(UUID penaltyId) {
    this.penaltyId = penaltyId;
    return this;
  }

  /**
   * Get penaltyId
   * @return penaltyId
   */
  @NotNull @Valid 
  @Schema(name = "penaltyId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("penaltyId")
  public UUID getPenaltyId() {
    return penaltyId;
  }

  public void setPenaltyId(UUID penaltyId) {
    this.penaltyId = penaltyId;
  }

  public PenaltyAppliedEvent playerId(UUID playerId) {
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

  public PenaltyAppliedEvent role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public PenaltyAppliedEvent penaltyType(@Nullable String penaltyType) {
    this.penaltyType = penaltyType;
    return this;
  }

  /**
   * Get penaltyType
   * @return penaltyType
   */
  
  @Schema(name = "penaltyType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("penaltyType")
  public @Nullable String getPenaltyType() {
    return penaltyType;
  }

  public void setPenaltyType(@Nullable String penaltyType) {
    this.penaltyType = penaltyType;
  }

  public PenaltyAppliedEvent delta(Float delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  @NotNull 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Float getDelta() {
    return delta;
  }

  public void setDelta(Float delta) {
    this.delta = delta;
  }

  public PenaltyAppliedEvent appliedAt(OffsetDateTime appliedAt) {
    this.appliedAt = appliedAt;
    return this;
  }

  /**
   * Get appliedAt
   * @return appliedAt
   */
  @NotNull @Valid 
  @Schema(name = "appliedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("appliedAt")
  public OffsetDateTime getAppliedAt() {
    return appliedAt;
  }

  public void setAppliedAt(OffsetDateTime appliedAt) {
    this.appliedAt = appliedAt;
  }

  public PenaltyAppliedEvent expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PenaltyAppliedEvent penaltyAppliedEvent = (PenaltyAppliedEvent) o;
    return Objects.equals(this.eventId, penaltyAppliedEvent.eventId) &&
        Objects.equals(this.penaltyId, penaltyAppliedEvent.penaltyId) &&
        Objects.equals(this.playerId, penaltyAppliedEvent.playerId) &&
        Objects.equals(this.role, penaltyAppliedEvent.role) &&
        Objects.equals(this.penaltyType, penaltyAppliedEvent.penaltyType) &&
        Objects.equals(this.delta, penaltyAppliedEvent.delta) &&
        Objects.equals(this.appliedAt, penaltyAppliedEvent.appliedAt) &&
        Objects.equals(this.expiresAt, penaltyAppliedEvent.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, penaltyId, playerId, role, penaltyType, delta, appliedAt, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PenaltyAppliedEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    penaltyId: ").append(toIndentedString(penaltyId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    penaltyType: ").append(toIndentedString(penaltyType)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    appliedAt: ").append(toIndentedString(appliedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

