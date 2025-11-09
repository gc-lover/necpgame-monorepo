package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DamageRequest
 */


public class DamageRequest {

  private String attackerId;

  private String targetId;

  private Integer damageAmount;

  /**
   * Gets or Sets damageType
   */
  public enum DamageTypeEnum {
    PHYSICAL("PHYSICAL"),
    
    ENERGY("ENERGY"),
    
    FIRE("FIRE"),
    
    ICE("ICE"),
    
    ELECTRIC("ELECTRIC"),
    
    POISON("POISON"),
    
    PSYCHIC("PSYCHIC");

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

  private @Nullable DamageTypeEnum damageType;

  private Boolean isCritical = false;

  private JsonNullable<UUID> weaponId = JsonNullable.<UUID>undefined();

  private JsonNullable<String> abilityId = JsonNullable.<String>undefined();

  public DamageRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DamageRequest(String attackerId, String targetId, Integer damageAmount) {
    this.attackerId = attackerId;
    this.targetId = targetId;
    this.damageAmount = damageAmount;
  }

  public DamageRequest attackerId(String attackerId) {
    this.attackerId = attackerId;
    return this;
  }

  /**
   * Get attackerId
   * @return attackerId
   */
  @NotNull 
  @Schema(name = "attacker_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("attacker_id")
  public String getAttackerId() {
    return attackerId;
  }

  public void setAttackerId(String attackerId) {
    this.attackerId = attackerId;
  }

  public DamageRequest targetId(String targetId) {
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

  public DamageRequest damageAmount(Integer damageAmount) {
    this.damageAmount = damageAmount;
    return this;
  }

  /**
   * Get damageAmount
   * @return damageAmount
   */
  @NotNull 
  @Schema(name = "damage_amount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("damage_amount")
  public Integer getDamageAmount() {
    return damageAmount;
  }

  public void setDamageAmount(Integer damageAmount) {
    this.damageAmount = damageAmount;
  }

  public DamageRequest damageType(@Nullable DamageTypeEnum damageType) {
    this.damageType = damageType;
    return this;
  }

  /**
   * Get damageType
   * @return damageType
   */
  
  @Schema(name = "damage_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("damage_type")
  public @Nullable DamageTypeEnum getDamageType() {
    return damageType;
  }

  public void setDamageType(@Nullable DamageTypeEnum damageType) {
    this.damageType = damageType;
  }

  public DamageRequest isCritical(Boolean isCritical) {
    this.isCritical = isCritical;
    return this;
  }

  /**
   * Get isCritical
   * @return isCritical
   */
  
  @Schema(name = "is_critical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_critical")
  public Boolean getIsCritical() {
    return isCritical;
  }

  public void setIsCritical(Boolean isCritical) {
    this.isCritical = isCritical;
  }

  public DamageRequest weaponId(UUID weaponId) {
    this.weaponId = JsonNullable.of(weaponId);
    return this;
  }

  /**
   * Get weaponId
   * @return weaponId
   */
  @Valid 
  @Schema(name = "weapon_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weapon_id")
  public JsonNullable<UUID> getWeaponId() {
    return weaponId;
  }

  public void setWeaponId(JsonNullable<UUID> weaponId) {
    this.weaponId = weaponId;
  }

  public DamageRequest abilityId(String abilityId) {
    this.abilityId = JsonNullable.of(abilityId);
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  
  @Schema(name = "ability_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ability_id")
  public JsonNullable<String> getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(JsonNullable<String> abilityId) {
    this.abilityId = abilityId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DamageRequest damageRequest = (DamageRequest) o;
    return Objects.equals(this.attackerId, damageRequest.attackerId) &&
        Objects.equals(this.targetId, damageRequest.targetId) &&
        Objects.equals(this.damageAmount, damageRequest.damageAmount) &&
        Objects.equals(this.damageType, damageRequest.damageType) &&
        Objects.equals(this.isCritical, damageRequest.isCritical) &&
        equalsNullable(this.weaponId, damageRequest.weaponId) &&
        equalsNullable(this.abilityId, damageRequest.abilityId);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(attackerId, targetId, damageAmount, damageType, isCritical, hashCodeNullable(weaponId), hashCodeNullable(abilityId));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DamageRequest {\n");
    sb.append("    attackerId: ").append(toIndentedString(attackerId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    damageAmount: ").append(toIndentedString(damageAmount)).append("\n");
    sb.append("    damageType: ").append(toIndentedString(damageType)).append("\n");
    sb.append("    isCritical: ").append(toIndentedString(isCritical)).append("\n");
    sb.append("    weaponId: ").append(toIndentedString(weaponId)).append("\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
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

