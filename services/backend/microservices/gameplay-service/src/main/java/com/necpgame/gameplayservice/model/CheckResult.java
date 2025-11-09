package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CheckResult
 */


public class CheckResult {

  private @Nullable Integer roll;

  private @Nullable Integer attributeModifier;

  private @Nullable Integer skillBonus;

  private @Nullable Integer situationModifiers;

  private @Nullable Integer total;

  private @Nullable Integer dc;

  private @Nullable Boolean success;

  private @Nullable Boolean critical;

  /**
   * Gets or Sets criticalType
   */
  public enum CriticalTypeEnum {
    SUCCESS("success"),
    
    FAILURE("failure");

    private final String value;

    CriticalTypeEnum(String value) {
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
    public static CriticalTypeEnum fromValue(String value) {
      for (CriticalTypeEnum b : CriticalTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CriticalTypeEnum criticalType;

  private @Nullable Integer margin;

  public CheckResult roll(@Nullable Integer roll) {
    this.roll = roll;
    return this;
  }

  /**
   * Результат броска кубика
   * @return roll
   */
  
  @Schema(name = "roll", description = "Результат броска кубика", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll")
  public @Nullable Integer getRoll() {
    return roll;
  }

  public void setRoll(@Nullable Integer roll) {
    this.roll = roll;
  }

  public CheckResult attributeModifier(@Nullable Integer attributeModifier) {
    this.attributeModifier = attributeModifier;
    return this;
  }

  /**
   * Get attributeModifier
   * @return attributeModifier
   */
  
  @Schema(name = "attribute_modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_modifier")
  public @Nullable Integer getAttributeModifier() {
    return attributeModifier;
  }

  public void setAttributeModifier(@Nullable Integer attributeModifier) {
    this.attributeModifier = attributeModifier;
  }

  public CheckResult skillBonus(@Nullable Integer skillBonus) {
    this.skillBonus = skillBonus;
    return this;
  }

  /**
   * Get skillBonus
   * @return skillBonus
   */
  
  @Schema(name = "skill_bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_bonus")
  public @Nullable Integer getSkillBonus() {
    return skillBonus;
  }

  public void setSkillBonus(@Nullable Integer skillBonus) {
    this.skillBonus = skillBonus;
  }

  public CheckResult situationModifiers(@Nullable Integer situationModifiers) {
    this.situationModifiers = situationModifiers;
    return this;
  }

  /**
   * Get situationModifiers
   * @return situationModifiers
   */
  
  @Schema(name = "situation_modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("situation_modifiers")
  public @Nullable Integer getSituationModifiers() {
    return situationModifiers;
  }

  public void setSituationModifiers(@Nullable Integer situationModifiers) {
    this.situationModifiers = situationModifiers;
  }

  public CheckResult total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Итоговое значение проверки
   * @return total
   */
  
  @Schema(name = "total", description = "Итоговое значение проверки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  public CheckResult dc(@Nullable Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Get dc
   * @return dc
   */
  
  @Schema(name = "dc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dc")
  public @Nullable Integer getDc() {
    return dc;
  }

  public void setDc(@Nullable Integer dc) {
    this.dc = dc;
  }

  public CheckResult success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public CheckResult critical(@Nullable Boolean critical) {
    this.critical = critical;
    return this;
  }

  /**
   * Get critical
   * @return critical
   */
  
  @Schema(name = "critical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical")
  public @Nullable Boolean getCritical() {
    return critical;
  }

  public void setCritical(@Nullable Boolean critical) {
    this.critical = critical;
  }

  public CheckResult criticalType(@Nullable CriticalTypeEnum criticalType) {
    this.criticalType = criticalType;
    return this;
  }

  /**
   * Get criticalType
   * @return criticalType
   */
  
  @Schema(name = "critical_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_type")
  public @Nullable CriticalTypeEnum getCriticalType() {
    return criticalType;
  }

  public void setCriticalType(@Nullable CriticalTypeEnum criticalType) {
    this.criticalType = criticalType;
  }

  public CheckResult margin(@Nullable Integer margin) {
    this.margin = margin;
    return this;
  }

  /**
   * Разница между total и DC
   * @return margin
   */
  
  @Schema(name = "margin", description = "Разница между total и DC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("margin")
  public @Nullable Integer getMargin() {
    return margin;
  }

  public void setMargin(@Nullable Integer margin) {
    this.margin = margin;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CheckResult checkResult = (CheckResult) o;
    return Objects.equals(this.roll, checkResult.roll) &&
        Objects.equals(this.attributeModifier, checkResult.attributeModifier) &&
        Objects.equals(this.skillBonus, checkResult.skillBonus) &&
        Objects.equals(this.situationModifiers, checkResult.situationModifiers) &&
        Objects.equals(this.total, checkResult.total) &&
        Objects.equals(this.dc, checkResult.dc) &&
        Objects.equals(this.success, checkResult.success) &&
        Objects.equals(this.critical, checkResult.critical) &&
        Objects.equals(this.criticalType, checkResult.criticalType) &&
        Objects.equals(this.margin, checkResult.margin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roll, attributeModifier, skillBonus, situationModifiers, total, dc, success, critical, criticalType, margin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CheckResult {\n");
    sb.append("    roll: ").append(toIndentedString(roll)).append("\n");
    sb.append("    attributeModifier: ").append(toIndentedString(attributeModifier)).append("\n");
    sb.append("    skillBonus: ").append(toIndentedString(skillBonus)).append("\n");
    sb.append("    situationModifiers: ").append(toIndentedString(situationModifiers)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    critical: ").append(toIndentedString(critical)).append("\n");
    sb.append("    criticalType: ").append(toIndentedString(criticalType)).append("\n");
    sb.append("    margin: ").append(toIndentedString(margin)).append("\n");
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

