package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DamageCalculationRequest
 */


public class DamageCalculationRequest {

  private String weaponId;

  /**
   * Gets or Sets targetBodyPart
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

  private TargetBodyPartEnum targetBodyPart;

  private @Nullable BigDecimal targetArmor;

  @Valid
  private List<String> targetImplants = new ArrayList<>();

  /**
   * Gets or Sets damageType
   */
  public enum DamageTypeEnum {
    PHYSICAL("physical"),
    
    ENERGY("energy"),
    
    CHEMICAL("chemical"),
    
    THERMAL("thermal"),
    
    EMP("emp"),
    
    CYBER("cyber"),
    
    POISON("poison"),
    
    ELECTROMAGNETIC("electromagnetic");

    private final String value;

    DamageTypeEnum(String value) {
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
    public static DamageTypeEnum fromValue(String value) {
      for (DamageTypeEnum b : DamageTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private DamageTypeEnum damageType;

  /**
   * Тип зоны для TTK модификатора
   */
  public enum ZoneTypeEnum {
    OPEN_WORLD("open_world"),
    
    PVP("pvp"),
    
    ARENA("arena"),
    
    EXTRACTION("extraction"),
    
    RAID("raid");

    private final String value;

    ZoneTypeEnum(String value) {
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
    public static ZoneTypeEnum fromValue(String value) {
      for (ZoneTypeEnum b : ZoneTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ZoneTypeEnum zoneType;

  public DamageCalculationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DamageCalculationRequest(String weaponId, TargetBodyPartEnum targetBodyPart, DamageTypeEnum damageType) {
    this.weaponId = weaponId;
    this.targetBodyPart = targetBodyPart;
    this.damageType = damageType;
  }

  public DamageCalculationRequest weaponId(String weaponId) {
    this.weaponId = weaponId;
    return this;
  }

  /**
   * Get weaponId
   * @return weaponId
   */
  @NotNull 
  @Schema(name = "weapon_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weapon_id")
  public String getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(String weaponId) {
    this.weaponId = weaponId;
  }

  public DamageCalculationRequest targetBodyPart(TargetBodyPartEnum targetBodyPart) {
    this.targetBodyPart = targetBodyPart;
    return this;
  }

  /**
   * Get targetBodyPart
   * @return targetBodyPart
   */
  @NotNull 
  @Schema(name = "target_body_part", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_body_part")
  public TargetBodyPartEnum getTargetBodyPart() {
    return targetBodyPart;
  }

  public void setTargetBodyPart(TargetBodyPartEnum targetBodyPart) {
    this.targetBodyPart = targetBodyPart;
  }

  public DamageCalculationRequest targetArmor(@Nullable BigDecimal targetArmor) {
    this.targetArmor = targetArmor;
    return this;
  }

  /**
   * Get targetArmor
   * minimum: 0
   * @return targetArmor
   */
  @Valid @DecimalMin(value = "0") 
  @Schema(name = "target_armor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_armor")
  public @Nullable BigDecimal getTargetArmor() {
    return targetArmor;
  }

  public void setTargetArmor(@Nullable BigDecimal targetArmor) {
    this.targetArmor = targetArmor;
  }

  public DamageCalculationRequest targetImplants(List<String> targetImplants) {
    this.targetImplants = targetImplants;
    return this;
  }

  public DamageCalculationRequest addTargetImplantsItem(String targetImplantsItem) {
    if (this.targetImplants == null) {
      this.targetImplants = new ArrayList<>();
    }
    this.targetImplants.add(targetImplantsItem);
    return this;
  }

  /**
   * Get targetImplants
   * @return targetImplants
   */
  
  @Schema(name = "target_implants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_implants")
  public List<String> getTargetImplants() {
    return targetImplants;
  }

  public void setTargetImplants(List<String> targetImplants) {
    this.targetImplants = targetImplants;
  }

  public DamageCalculationRequest damageType(DamageTypeEnum damageType) {
    this.damageType = damageType;
    return this;
  }

  /**
   * Get damageType
   * @return damageType
   */
  @NotNull 
  @Schema(name = "damage_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("damage_type")
  public DamageTypeEnum getDamageType() {
    return damageType;
  }

  public void setDamageType(DamageTypeEnum damageType) {
    this.damageType = damageType;
  }

  public DamageCalculationRequest zoneType(@Nullable ZoneTypeEnum zoneType) {
    this.zoneType = zoneType;
    return this;
  }

  /**
   * Тип зоны для TTK модификатора
   * @return zoneType
   */
  
  @Schema(name = "zone_type", description = "Тип зоны для TTK модификатора", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone_type")
  public @Nullable ZoneTypeEnum getZoneType() {
    return zoneType;
  }

  public void setZoneType(@Nullable ZoneTypeEnum zoneType) {
    this.zoneType = zoneType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DamageCalculationRequest damageCalculationRequest = (DamageCalculationRequest) o;
    return Objects.equals(this.weaponId, damageCalculationRequest.weaponId) &&
        Objects.equals(this.targetBodyPart, damageCalculationRequest.targetBodyPart) &&
        Objects.equals(this.targetArmor, damageCalculationRequest.targetArmor) &&
        Objects.equals(this.targetImplants, damageCalculationRequest.targetImplants) &&
        Objects.equals(this.damageType, damageCalculationRequest.damageType) &&
        Objects.equals(this.zoneType, damageCalculationRequest.zoneType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(weaponId, targetBodyPart, targetArmor, targetImplants, damageType, zoneType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DamageCalculationRequest {\n");
    sb.append("    weaponId: ").append(toIndentedString(weaponId)).append("\n");
    sb.append("    targetBodyPart: ").append(toIndentedString(targetBodyPart)).append("\n");
    sb.append("    targetArmor: ").append(toIndentedString(targetArmor)).append("\n");
    sb.append("    targetImplants: ").append(toIndentedString(targetImplants)).append("\n");
    sb.append("    damageType: ").append(toIndentedString(damageType)).append("\n");
    sb.append("    zoneType: ").append(toIndentedString(zoneType)).append("\n");
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

