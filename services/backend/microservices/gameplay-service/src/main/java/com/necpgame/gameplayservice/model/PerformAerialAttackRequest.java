package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PerformAerialAttackRequest
 */

@JsonTypeName("performAerialAttack_request")

public class PerformAerialAttackRequest {

  private String characterId;

  private String targetId;

  /**
   * Gets or Sets attackType
   */
  public enum AttackTypeEnum {
    DIVE_ATTACK("dive_attack"),
    
    AERIAL_SHOOT("aerial_shoot"),
    
    MANTIS_STRIKE("mantis_strike");

    private final String value;

    AttackTypeEnum(String value) {
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
    public static AttackTypeEnum fromValue(String value) {
      for (AttackTypeEnum b : AttackTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AttackTypeEnum attackType;

  private @Nullable BigDecimal heightAdvantage;

  public PerformAerialAttackRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PerformAerialAttackRequest(String characterId, String targetId, AttackTypeEnum attackType) {
    this.characterId = characterId;
    this.targetId = targetId;
    this.attackType = attackType;
  }

  public PerformAerialAttackRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public PerformAerialAttackRequest targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  @NotNull 
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_id")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public PerformAerialAttackRequest attackType(AttackTypeEnum attackType) {
    this.attackType = attackType;
    return this;
  }

  /**
   * Get attackType
   * @return attackType
   */
  @NotNull 
  @Schema(name = "attack_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("attack_type")
  public AttackTypeEnum getAttackType() {
    return attackType;
  }

  public void setAttackType(AttackTypeEnum attackType) {
    this.attackType = attackType;
  }

  public PerformAerialAttackRequest heightAdvantage(@Nullable BigDecimal heightAdvantage) {
    this.heightAdvantage = heightAdvantage;
    return this;
  }

  /**
   * Преимущество высоты (метры)
   * @return heightAdvantage
   */
  @Valid 
  @Schema(name = "height_advantage", description = "Преимущество высоты (метры)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("height_advantage")
  public @Nullable BigDecimal getHeightAdvantage() {
    return heightAdvantage;
  }

  public void setHeightAdvantage(@Nullable BigDecimal heightAdvantage) {
    this.heightAdvantage = heightAdvantage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PerformAerialAttackRequest performAerialAttackRequest = (PerformAerialAttackRequest) o;
    return Objects.equals(this.characterId, performAerialAttackRequest.characterId) &&
        Objects.equals(this.targetId, performAerialAttackRequest.targetId) &&
        Objects.equals(this.attackType, performAerialAttackRequest.attackType) &&
        Objects.equals(this.heightAdvantage, performAerialAttackRequest.heightAdvantage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetId, attackType, heightAdvantage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PerformAerialAttackRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    attackType: ").append(toIndentedString(attackType)).append("\n");
    sb.append("    heightAdvantage: ").append(toIndentedString(heightAdvantage)).append("\n");
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

