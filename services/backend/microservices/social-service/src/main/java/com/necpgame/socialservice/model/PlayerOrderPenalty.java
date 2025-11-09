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
 * PlayerOrderPenalty
 */


public class PlayerOrderPenalty {

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

  private String penaltyType;

  private Float delta;

  private @Nullable String reason;

  private UUID appliedBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime appliedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    EXPIRED("expired"),
    
    REVERSED("reversed");

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

  private @Nullable UUID linkedOrderId;

  private @Nullable UUID kafkaEventId;

  public PlayerOrderPenalty() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderPenalty(UUID penaltyId, UUID playerId, RoleEnum role, String penaltyType, Float delta, UUID appliedBy, OffsetDateTime appliedAt) {
    this.penaltyId = penaltyId;
    this.playerId = playerId;
    this.role = role;
    this.penaltyType = penaltyType;
    this.delta = delta;
    this.appliedBy = appliedBy;
    this.appliedAt = appliedAt;
  }

  public PlayerOrderPenalty penaltyId(UUID penaltyId) {
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

  public PlayerOrderPenalty playerId(UUID playerId) {
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

  public PlayerOrderPenalty role(RoleEnum role) {
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

  public PlayerOrderPenalty penaltyType(String penaltyType) {
    this.penaltyType = penaltyType;
    return this;
  }

  /**
   * Get penaltyType
   * @return penaltyType
   */
  @NotNull 
  @Schema(name = "penaltyType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("penaltyType")
  public String getPenaltyType() {
    return penaltyType;
  }

  public void setPenaltyType(String penaltyType) {
    this.penaltyType = penaltyType;
  }

  public PlayerOrderPenalty delta(Float delta) {
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

  public PlayerOrderPenalty reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public PlayerOrderPenalty appliedBy(UUID appliedBy) {
    this.appliedBy = appliedBy;
    return this;
  }

  /**
   * Get appliedBy
   * @return appliedBy
   */
  @NotNull @Valid 
  @Schema(name = "appliedBy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("appliedBy")
  public UUID getAppliedBy() {
    return appliedBy;
  }

  public void setAppliedBy(UUID appliedBy) {
    this.appliedBy = appliedBy;
  }

  public PlayerOrderPenalty appliedAt(OffsetDateTime appliedAt) {
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

  public PlayerOrderPenalty expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public PlayerOrderPenalty status(@Nullable StatusEnum status) {
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

  public PlayerOrderPenalty linkedOrderId(@Nullable UUID linkedOrderId) {
    this.linkedOrderId = linkedOrderId;
    return this;
  }

  /**
   * Get linkedOrderId
   * @return linkedOrderId
   */
  @Valid 
  @Schema(name = "linkedOrderId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("linkedOrderId")
  public @Nullable UUID getLinkedOrderId() {
    return linkedOrderId;
  }

  public void setLinkedOrderId(@Nullable UUID linkedOrderId) {
    this.linkedOrderId = linkedOrderId;
  }

  public PlayerOrderPenalty kafkaEventId(@Nullable UUID kafkaEventId) {
    this.kafkaEventId = kafkaEventId;
    return this;
  }

  /**
   * Get kafkaEventId
   * @return kafkaEventId
   */
  @Valid 
  @Schema(name = "kafkaEventId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kafkaEventId")
  public @Nullable UUID getKafkaEventId() {
    return kafkaEventId;
  }

  public void setKafkaEventId(@Nullable UUID kafkaEventId) {
    this.kafkaEventId = kafkaEventId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderPenalty playerOrderPenalty = (PlayerOrderPenalty) o;
    return Objects.equals(this.penaltyId, playerOrderPenalty.penaltyId) &&
        Objects.equals(this.playerId, playerOrderPenalty.playerId) &&
        Objects.equals(this.role, playerOrderPenalty.role) &&
        Objects.equals(this.penaltyType, playerOrderPenalty.penaltyType) &&
        Objects.equals(this.delta, playerOrderPenalty.delta) &&
        Objects.equals(this.reason, playerOrderPenalty.reason) &&
        Objects.equals(this.appliedBy, playerOrderPenalty.appliedBy) &&
        Objects.equals(this.appliedAt, playerOrderPenalty.appliedAt) &&
        Objects.equals(this.expiresAt, playerOrderPenalty.expiresAt) &&
        Objects.equals(this.status, playerOrderPenalty.status) &&
        Objects.equals(this.linkedOrderId, playerOrderPenalty.linkedOrderId) &&
        Objects.equals(this.kafkaEventId, playerOrderPenalty.kafkaEventId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(penaltyId, playerId, role, penaltyType, delta, reason, appliedBy, appliedAt, expiresAt, status, linkedOrderId, kafkaEventId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderPenalty {\n");
    sb.append("    penaltyId: ").append(toIndentedString(penaltyId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    penaltyType: ").append(toIndentedString(penaltyType)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    appliedBy: ").append(toIndentedString(appliedBy)).append("\n");
    sb.append("    appliedAt: ").append(toIndentedString(appliedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    linkedOrderId: ").append(toIndentedString(linkedOrderId)).append("\n");
    sb.append("    kafkaEventId: ").append(toIndentedString(kafkaEventId)).append("\n");
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

