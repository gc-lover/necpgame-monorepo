package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * RollWithAdvantage200Response
 */

@JsonTypeName("rollWithAdvantage_200_response")

public class RollWithAdvantage200Response {

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

  @Valid
  private List<Integer> rolls = new ArrayList<>();

  private @Nullable Integer selectedRoll;

  public RollWithAdvantage200Response roll(@Nullable Integer roll) {
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

  public RollWithAdvantage200Response attributeModifier(@Nullable Integer attributeModifier) {
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

  public RollWithAdvantage200Response skillBonus(@Nullable Integer skillBonus) {
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

  public RollWithAdvantage200Response situationModifiers(@Nullable Integer situationModifiers) {
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

  public RollWithAdvantage200Response total(@Nullable Integer total) {
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

  public RollWithAdvantage200Response dc(@Nullable Integer dc) {
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

  public RollWithAdvantage200Response success(@Nullable Boolean success) {
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

  public RollWithAdvantage200Response critical(@Nullable Boolean critical) {
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

  public RollWithAdvantage200Response criticalType(@Nullable CriticalTypeEnum criticalType) {
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

  public RollWithAdvantage200Response margin(@Nullable Integer margin) {
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

  public RollWithAdvantage200Response rolls(List<Integer> rolls) {
    this.rolls = rolls;
    return this;
  }

  public RollWithAdvantage200Response addRollsItem(Integer rollsItem) {
    if (this.rolls == null) {
      this.rolls = new ArrayList<>();
    }
    this.rolls.add(rollsItem);
    return this;
  }

  /**
   * Два броска d20
   * @return rolls
   */
  
  @Schema(name = "rolls", description = "Два броска d20", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rolls")
  public List<Integer> getRolls() {
    return rolls;
  }

  public void setRolls(List<Integer> rolls) {
    this.rolls = rolls;
  }

  public RollWithAdvantage200Response selectedRoll(@Nullable Integer selectedRoll) {
    this.selectedRoll = selectedRoll;
    return this;
  }

  /**
   * Get selectedRoll
   * @return selectedRoll
   */
  
  @Schema(name = "selected_roll", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("selected_roll")
  public @Nullable Integer getSelectedRoll() {
    return selectedRoll;
  }

  public void setSelectedRoll(@Nullable Integer selectedRoll) {
    this.selectedRoll = selectedRoll;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RollWithAdvantage200Response rollWithAdvantage200Response = (RollWithAdvantage200Response) o;
    return Objects.equals(this.roll, rollWithAdvantage200Response.roll) &&
        Objects.equals(this.attributeModifier, rollWithAdvantage200Response.attributeModifier) &&
        Objects.equals(this.skillBonus, rollWithAdvantage200Response.skillBonus) &&
        Objects.equals(this.situationModifiers, rollWithAdvantage200Response.situationModifiers) &&
        Objects.equals(this.total, rollWithAdvantage200Response.total) &&
        Objects.equals(this.dc, rollWithAdvantage200Response.dc) &&
        Objects.equals(this.success, rollWithAdvantage200Response.success) &&
        Objects.equals(this.critical, rollWithAdvantage200Response.critical) &&
        Objects.equals(this.criticalType, rollWithAdvantage200Response.criticalType) &&
        Objects.equals(this.margin, rollWithAdvantage200Response.margin) &&
        Objects.equals(this.rolls, rollWithAdvantage200Response.rolls) &&
        Objects.equals(this.selectedRoll, rollWithAdvantage200Response.selectedRoll);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roll, attributeModifier, skillBonus, situationModifiers, total, dc, success, critical, criticalType, margin, rolls, selectedRoll);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollWithAdvantage200Response {\n");
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
    sb.append("    rolls: ").append(toIndentedString(rolls)).append("\n");
    sb.append("    selectedRoll: ").append(toIndentedString(selectedRoll)).append("\n");
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

