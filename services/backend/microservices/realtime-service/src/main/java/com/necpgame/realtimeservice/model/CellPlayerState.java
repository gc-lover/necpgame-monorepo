package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.realtimeservice.model.CellPlayerStatePosition;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CellPlayerState
 */


public class CellPlayerState {

  private String playerId;

  private CellPlayerStatePosition position;

  private @Nullable CellPlayerStatePosition velocity;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    IDLE("idle"),
    
    MOVING("moving"),
    
    COMBAT("combat"),
    
    DOWNED("downed");

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

  private @Nullable String equipmentTier;

  public CellPlayerState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CellPlayerState(String playerId, CellPlayerStatePosition position) {
    this.playerId = playerId;
    this.position = position;
  }

  public CellPlayerState playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public CellPlayerState position(CellPlayerStatePosition position) {
    this.position = position;
    return this;
  }

  /**
   * Get position
   * @return position
   */
  @NotNull @Valid 
  @Schema(name = "position", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("position")
  public CellPlayerStatePosition getPosition() {
    return position;
  }

  public void setPosition(CellPlayerStatePosition position) {
    this.position = position;
  }

  public CellPlayerState velocity(@Nullable CellPlayerStatePosition velocity) {
    this.velocity = velocity;
    return this;
  }

  /**
   * Get velocity
   * @return velocity
   */
  @Valid 
  @Schema(name = "velocity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("velocity")
  public @Nullable CellPlayerStatePosition getVelocity() {
    return velocity;
  }

  public void setVelocity(@Nullable CellPlayerStatePosition velocity) {
    this.velocity = velocity;
  }

  public CellPlayerState status(@Nullable StatusEnum status) {
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

  public CellPlayerState equipmentTier(@Nullable String equipmentTier) {
    this.equipmentTier = equipmentTier;
    return this;
  }

  /**
   * Get equipmentTier
   * @return equipmentTier
   */
  
  @Schema(name = "equipmentTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("equipmentTier")
  public @Nullable String getEquipmentTier() {
    return equipmentTier;
  }

  public void setEquipmentTier(@Nullable String equipmentTier) {
    this.equipmentTier = equipmentTier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CellPlayerState cellPlayerState = (CellPlayerState) o;
    return Objects.equals(this.playerId, cellPlayerState.playerId) &&
        Objects.equals(this.position, cellPlayerState.position) &&
        Objects.equals(this.velocity, cellPlayerState.velocity) &&
        Objects.equals(this.status, cellPlayerState.status) &&
        Objects.equals(this.equipmentTier, cellPlayerState.equipmentTier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, position, velocity, status, equipmentTier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CellPlayerState {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    position: ").append(toIndentedString(position)).append("\n");
    sb.append("    velocity: ").append(toIndentedString(velocity)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    equipmentTier: ").append(toIndentedString(equipmentTier)).append("\n");
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

