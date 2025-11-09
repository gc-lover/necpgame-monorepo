package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ShootRequestAimPoint;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ShootRequest
 */


public class ShootRequest {

  private String shooterId;

  private String weaponId;

  private String targetId;

  /**
   * Целевая часть тела. Органика: head (x2), torso (x1), arms/legs (x0.7) Кибер: cyber_head (x1.5), cyber_torso (x0.8), cyber_arms/legs (x0.5) 
   */
  public enum TargetBodyPartEnum {
    HEAD("head"),
    
    TORSO("torso"),
    
    ARMS("arms"),
    
    LEGS("legs"),
    
    CYBER_HEAD("cyber_head"),
    
    CYBER_TORSO("cyber_torso"),
    
    CYBER_ARMS("cyber_arms"),
    
    CYBER_LEGS("cyber_legs");

    private final String value;

    TargetBodyPartEnum(String value) {
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
    public static TargetBodyPartEnum fromValue(String value) {
      for (TargetBodyPartEnum b : TargetBodyPartEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TargetBodyPartEnum targetBodyPart;

  private ShootRequestAimPoint aimPoint;

  private @Nullable Boolean isMoving;

  public ShootRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ShootRequest(String shooterId, String weaponId, String targetId, ShootRequestAimPoint aimPoint) {
    this.shooterId = shooterId;
    this.weaponId = weaponId;
    this.targetId = targetId;
    this.aimPoint = aimPoint;
  }

  public ShootRequest shooterId(String shooterId) {
    this.shooterId = shooterId;
    return this;
  }

  /**
   * ID стреляющего персонажа
   * @return shooterId
   */
  @NotNull 
  @Schema(name = "shooter_id", description = "ID стреляющего персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("shooter_id")
  public String getShooterId() {
    return shooterId;
  }

  public void setShooterId(String shooterId) {
    this.shooterId = shooterId;
  }

  public ShootRequest weaponId(String weaponId) {
    this.weaponId = weaponId;
    return this;
  }

  /**
   * ID оружия
   * @return weaponId
   */
  @NotNull 
  @Schema(name = "weapon_id", description = "ID оружия", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weapon_id")
  public String getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(String weaponId) {
    this.weaponId = weaponId;
  }

  public ShootRequest targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * ID цели
   * @return targetId
   */
  @NotNull 
  @Schema(name = "target_id", description = "ID цели", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_id")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public ShootRequest targetBodyPart(@Nullable TargetBodyPartEnum targetBodyPart) {
    this.targetBodyPart = targetBodyPart;
    return this;
  }

  /**
   * Целевая часть тела. Органика: head (x2), torso (x1), arms/legs (x0.7) Кибер: cyber_head (x1.5), cyber_torso (x0.8), cyber_arms/legs (x0.5) 
   * @return targetBodyPart
   */
  
  @Schema(name = "target_body_part", description = "Целевая часть тела. Органика: head (x2), torso (x1), arms/legs (x0.7) Кибер: cyber_head (x1.5), cyber_torso (x0.8), cyber_arms/legs (x0.5) ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_body_part")
  public @Nullable TargetBodyPartEnum getTargetBodyPart() {
    return targetBodyPart;
  }

  public void setTargetBodyPart(@Nullable TargetBodyPartEnum targetBodyPart) {
    this.targetBodyPart = targetBodyPart;
  }

  public ShootRequest aimPoint(ShootRequestAimPoint aimPoint) {
    this.aimPoint = aimPoint;
    return this;
  }

  /**
   * Get aimPoint
   * @return aimPoint
   */
  @NotNull @Valid 
  @Schema(name = "aim_point", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("aim_point")
  public ShootRequestAimPoint getAimPoint() {
    return aimPoint;
  }

  public void setAimPoint(ShootRequestAimPoint aimPoint) {
    this.aimPoint = aimPoint;
  }

  public ShootRequest isMoving(@Nullable Boolean isMoving) {
    this.isMoving = isMoving;
    return this;
  }

  /**
   * Двигается ли стреляющий
   * @return isMoving
   */
  
  @Schema(name = "is_moving", description = "Двигается ли стреляющий", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_moving")
  public @Nullable Boolean getIsMoving() {
    return isMoving;
  }

  public void setIsMoving(@Nullable Boolean isMoving) {
    this.isMoving = isMoving;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShootRequest shootRequest = (ShootRequest) o;
    return Objects.equals(this.shooterId, shootRequest.shooterId) &&
        Objects.equals(this.weaponId, shootRequest.weaponId) &&
        Objects.equals(this.targetId, shootRequest.targetId) &&
        Objects.equals(this.targetBodyPart, shootRequest.targetBodyPart) &&
        Objects.equals(this.aimPoint, shootRequest.aimPoint) &&
        Objects.equals(this.isMoving, shootRequest.isMoving);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shooterId, weaponId, targetId, targetBodyPart, aimPoint, isMoving);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShootRequest {\n");
    sb.append("    shooterId: ").append(toIndentedString(shooterId)).append("\n");
    sb.append("    weaponId: ").append(toIndentedString(weaponId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    targetBodyPart: ").append(toIndentedString(targetBodyPart)).append("\n");
    sb.append("    aimPoint: ").append(toIndentedString(aimPoint)).append("\n");
    sb.append("    isMoving: ").append(toIndentedString(isMoving)).append("\n");
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

