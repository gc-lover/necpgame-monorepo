package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.ShootResultModifiersAppliedInner;
import com.necpgame.gameplayservice.model.ShootResultTargetStatus;
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
 * ShootResult
 */


public class ShootResult {

  private Boolean hit;

  /**
   * Часть тела, в которую попал выстрел
   */
  public enum BodyPartHitEnum {
    HEAD("head"),
    
    TORSO("torso"),
    
    ARMS("arms"),
    
    LEGS("legs"),
    
    CYBER_HEAD("cyber_head"),
    
    CYBER_TORSO("cyber_torso"),
    
    CYBER_ARMS("cyber_arms"),
    
    CYBER_LEGS("cyber_legs");

    private final String value;

    BodyPartHitEnum(String value) {
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
    public static BodyPartHitEnum fromValue(String value) {
      for (BodyPartHitEnum b : BodyPartHitEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable BodyPartHitEnum bodyPartHit;

  private BigDecimal damageDealt;

  /**
   * Тип урона: - physical: Кинетическое оружие - energy: Лазеры, плазма - chemical: Яд (только органика) - thermal: Огненный урон - emp: Только кибер-части - cyber: Специальный урон по кибер-частям - poison: Отравление (органика) - electromagnetic: Электромагнитное оружие 
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

  private Boolean isCritical;

  @Valid
  private List<@Valid ShootResultModifiersAppliedInner> modifiersApplied = new ArrayList<>();

  private @Nullable ShootResultTargetStatus targetStatus;

  public ShootResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ShootResult(Boolean hit, BigDecimal damageDealt, DamageTypeEnum damageType, Boolean isCritical) {
    this.hit = hit;
    this.damageDealt = damageDealt;
    this.damageType = damageType;
    this.isCritical = isCritical;
  }

  public ShootResult hit(Boolean hit) {
    this.hit = hit;
    return this;
  }

  /**
   * Попал ли выстрел в цель
   * @return hit
   */
  @NotNull 
  @Schema(name = "hit", description = "Попал ли выстрел в цель", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hit")
  public Boolean getHit() {
    return hit;
  }

  public void setHit(Boolean hit) {
    this.hit = hit;
  }

  public ShootResult bodyPartHit(@Nullable BodyPartHitEnum bodyPartHit) {
    this.bodyPartHit = bodyPartHit;
    return this;
  }

  /**
   * Часть тела, в которую попал выстрел
   * @return bodyPartHit
   */
  
  @Schema(name = "body_part_hit", description = "Часть тела, в которую попал выстрел", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body_part_hit")
  public @Nullable BodyPartHitEnum getBodyPartHit() {
    return bodyPartHit;
  }

  public void setBodyPartHit(@Nullable BodyPartHitEnum bodyPartHit) {
    this.bodyPartHit = bodyPartHit;
  }

  public ShootResult damageDealt(BigDecimal damageDealt) {
    this.damageDealt = damageDealt;
    return this;
  }

  /**
   * Фактический нанесенный урон с учетом всех модификаторов
   * minimum: 0
   * @return damageDealt
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "damage_dealt", description = "Фактический нанесенный урон с учетом всех модификаторов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("damage_dealt")
  public BigDecimal getDamageDealt() {
    return damageDealt;
  }

  public void setDamageDealt(BigDecimal damageDealt) {
    this.damageDealt = damageDealt;
  }

  public ShootResult damageType(DamageTypeEnum damageType) {
    this.damageType = damageType;
    return this;
  }

  /**
   * Тип урона: - physical: Кинетическое оружие - energy: Лазеры, плазма - chemical: Яд (только органика) - thermal: Огненный урон - emp: Только кибер-части - cyber: Специальный урон по кибер-частям - poison: Отравление (органика) - electromagnetic: Электромагнитное оружие 
   * @return damageType
   */
  @NotNull 
  @Schema(name = "damage_type", description = "Тип урона: - physical: Кинетическое оружие - energy: Лазеры, плазма - chemical: Яд (только органика) - thermal: Огненный урон - emp: Только кибер-части - cyber: Специальный урон по кибер-частям - poison: Отравление (органика) - electromagnetic: Электромагнитное оружие ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("damage_type")
  public DamageTypeEnum getDamageType() {
    return damageType;
  }

  public void setDamageType(DamageTypeEnum damageType) {
    this.damageType = damageType;
  }

  public ShootResult isCritical(Boolean isCritical) {
    this.isCritical = isCritical;
    return this;
  }

  /**
   * Критическое попадание
   * @return isCritical
   */
  @NotNull 
  @Schema(name = "is_critical", description = "Критическое попадание", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("is_critical")
  public Boolean getIsCritical() {
    return isCritical;
  }

  public void setIsCritical(Boolean isCritical) {
    this.isCritical = isCritical;
  }

  public ShootResult modifiersApplied(List<@Valid ShootResultModifiersAppliedInner> modifiersApplied) {
    this.modifiersApplied = modifiersApplied;
    return this;
  }

  public ShootResult addModifiersAppliedItem(ShootResultModifiersAppliedInner modifiersAppliedItem) {
    if (this.modifiersApplied == null) {
      this.modifiersApplied = new ArrayList<>();
    }
    this.modifiersApplied.add(modifiersAppliedItem);
    return this;
  }

  /**
   * Примененные модификаторы урона
   * @return modifiersApplied
   */
  @Valid 
  @Schema(name = "modifiers_applied", description = "Примененные модификаторы урона", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers_applied")
  public List<@Valid ShootResultModifiersAppliedInner> getModifiersApplied() {
    return modifiersApplied;
  }

  public void setModifiersApplied(List<@Valid ShootResultModifiersAppliedInner> modifiersApplied) {
    this.modifiersApplied = modifiersApplied;
  }

  public ShootResult targetStatus(@Nullable ShootResultTargetStatus targetStatus) {
    this.targetStatus = targetStatus;
    return this;
  }

  /**
   * Get targetStatus
   * @return targetStatus
   */
  @Valid 
  @Schema(name = "target_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_status")
  public @Nullable ShootResultTargetStatus getTargetStatus() {
    return targetStatus;
  }

  public void setTargetStatus(@Nullable ShootResultTargetStatus targetStatus) {
    this.targetStatus = targetStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ShootResult shootResult = (ShootResult) o;
    return Objects.equals(this.hit, shootResult.hit) &&
        Objects.equals(this.bodyPartHit, shootResult.bodyPartHit) &&
        Objects.equals(this.damageDealt, shootResult.damageDealt) &&
        Objects.equals(this.damageType, shootResult.damageType) &&
        Objects.equals(this.isCritical, shootResult.isCritical) &&
        Objects.equals(this.modifiersApplied, shootResult.modifiersApplied) &&
        Objects.equals(this.targetStatus, shootResult.targetStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hit, bodyPartHit, damageDealt, damageType, isCritical, modifiersApplied, targetStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ShootResult {\n");
    sb.append("    hit: ").append(toIndentedString(hit)).append("\n");
    sb.append("    bodyPartHit: ").append(toIndentedString(bodyPartHit)).append("\n");
    sb.append("    damageDealt: ").append(toIndentedString(damageDealt)).append("\n");
    sb.append("    damageType: ").append(toIndentedString(damageType)).append("\n");
    sb.append("    isCritical: ").append(toIndentedString(isCritical)).append("\n");
    sb.append("    modifiersApplied: ").append(toIndentedString(modifiersApplied)).append("\n");
    sb.append("    targetStatus: ").append(toIndentedString(targetStatus)).append("\n");
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

