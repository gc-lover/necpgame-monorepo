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
 * PlayerOrderPenaltyRequest
 */


public class PlayerOrderPenaltyRequest {

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

  /**
   * Gets or Sets penaltyType
   */
  public enum PenaltyTypeEnum {
    LATE_PAYMENT("late_payment"),
    
    DISPUTE_LOST("dispute_lost"),
    
    TOXIC_BEHAVIOR("toxic_behavior"),
    
    BREACH_OF_CONTRACT("breach_of_contract"),
    
    FRAUD_DETECTED("fraud_detected");

    private final String value;

    PenaltyTypeEnum(String value) {
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
    public static PenaltyTypeEnum fromValue(String value) {
      for (PenaltyTypeEnum b : PenaltyTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PenaltyTypeEnum penaltyType;

  private Float delta;

  private String reason;

  private @Nullable UUID appliedBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable UUID linkedOrderId;

  private Boolean notifyPlayer = true;

  public PlayerOrderPenaltyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderPenaltyRequest(UUID playerId, RoleEnum role, PenaltyTypeEnum penaltyType, Float delta, String reason) {
    this.playerId = playerId;
    this.role = role;
    this.penaltyType = penaltyType;
    this.delta = delta;
    this.reason = reason;
  }

  public PlayerOrderPenaltyRequest playerId(UUID playerId) {
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

  public PlayerOrderPenaltyRequest role(RoleEnum role) {
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

  public PlayerOrderPenaltyRequest penaltyType(PenaltyTypeEnum penaltyType) {
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
  public PenaltyTypeEnum getPenaltyType() {
    return penaltyType;
  }

  public void setPenaltyType(PenaltyTypeEnum penaltyType) {
    this.penaltyType = penaltyType;
  }

  public PlayerOrderPenaltyRequest delta(Float delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * minimum: -100
   * maximum: 0
   * @return delta
   */
  @NotNull @DecimalMin(value = "-100") @DecimalMax(value = "0") 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Float getDelta() {
    return delta;
  }

  public void setDelta(Float delta) {
    this.delta = delta;
  }

  public PlayerOrderPenaltyRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull @Size(min = 10, max = 1024) 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public PlayerOrderPenaltyRequest appliedBy(@Nullable UUID appliedBy) {
    this.appliedBy = appliedBy;
    return this;
  }

  /**
   * Get appliedBy
   * @return appliedBy
   */
  @Valid 
  @Schema(name = "appliedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appliedBy")
  public @Nullable UUID getAppliedBy() {
    return appliedBy;
  }

  public void setAppliedBy(@Nullable UUID appliedBy) {
    this.appliedBy = appliedBy;
  }

  public PlayerOrderPenaltyRequest expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public PlayerOrderPenaltyRequest linkedOrderId(@Nullable UUID linkedOrderId) {
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

  public PlayerOrderPenaltyRequest notifyPlayer(Boolean notifyPlayer) {
    this.notifyPlayer = notifyPlayer;
    return this;
  }

  /**
   * Get notifyPlayer
   * @return notifyPlayer
   */
  
  @Schema(name = "notifyPlayer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyPlayer")
  public Boolean getNotifyPlayer() {
    return notifyPlayer;
  }

  public void setNotifyPlayer(Boolean notifyPlayer) {
    this.notifyPlayer = notifyPlayer;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderPenaltyRequest playerOrderPenaltyRequest = (PlayerOrderPenaltyRequest) o;
    return Objects.equals(this.playerId, playerOrderPenaltyRequest.playerId) &&
        Objects.equals(this.role, playerOrderPenaltyRequest.role) &&
        Objects.equals(this.penaltyType, playerOrderPenaltyRequest.penaltyType) &&
        Objects.equals(this.delta, playerOrderPenaltyRequest.delta) &&
        Objects.equals(this.reason, playerOrderPenaltyRequest.reason) &&
        Objects.equals(this.appliedBy, playerOrderPenaltyRequest.appliedBy) &&
        Objects.equals(this.expiresAt, playerOrderPenaltyRequest.expiresAt) &&
        Objects.equals(this.linkedOrderId, playerOrderPenaltyRequest.linkedOrderId) &&
        Objects.equals(this.notifyPlayer, playerOrderPenaltyRequest.notifyPlayer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, role, penaltyType, delta, reason, appliedBy, expiresAt, linkedOrderId, notifyPlayer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderPenaltyRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    penaltyType: ").append(toIndentedString(penaltyType)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    appliedBy: ").append(toIndentedString(appliedBy)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    linkedOrderId: ").append(toIndentedString(linkedOrderId)).append("\n");
    sb.append("    notifyPlayer: ").append(toIndentedString(notifyPlayer)).append("\n");
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

