package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Combo
 */


public class Combo {

  private @Nullable String comboId;

  private @Nullable String name;

  private @Nullable String type;

  @Valid
  private List<String> requiredAbilities = new ArrayList<>();

  private @Nullable BigDecimal executionWindow;

  private @Nullable Object effects;

  private @Nullable String skillCeiling;

  public Combo comboId(@Nullable String comboId) {
    this.comboId = comboId;
    return this;
  }

  /**
   * Get comboId
   * @return comboId
   */
  
  @Schema(name = "combo_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("combo_id")
  public @Nullable String getComboId() {
    return comboId;
  }

  public void setComboId(@Nullable String comboId) {
    this.comboId = comboId;
  }

  public Combo name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Combo type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public Combo requiredAbilities(List<String> requiredAbilities) {
    this.requiredAbilities = requiredAbilities;
    return this;
  }

  public Combo addRequiredAbilitiesItem(String requiredAbilitiesItem) {
    if (this.requiredAbilities == null) {
      this.requiredAbilities = new ArrayList<>();
    }
    this.requiredAbilities.add(requiredAbilitiesItem);
    return this;
  }

  /**
   * Get requiredAbilities
   * @return requiredAbilities
   */
  
  @Schema(name = "required_abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_abilities")
  public List<String> getRequiredAbilities() {
    return requiredAbilities;
  }

  public void setRequiredAbilities(List<String> requiredAbilities) {
    this.requiredAbilities = requiredAbilities;
  }

  public Combo executionWindow(@Nullable BigDecimal executionWindow) {
    this.executionWindow = executionWindow;
    return this;
  }

  /**
   * Get executionWindow
   * @return executionWindow
   */
  @Valid 
  @Schema(name = "execution_window", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("execution_window")
  public @Nullable BigDecimal getExecutionWindow() {
    return executionWindow;
  }

  public void setExecutionWindow(@Nullable BigDecimal executionWindow) {
    this.executionWindow = executionWindow;
  }

  public Combo effects(@Nullable Object effects) {
    this.effects = effects;
    return this;
  }

  /**
   * Get effects
   * @return effects
   */
  
  @Schema(name = "effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects")
  public @Nullable Object getEffects() {
    return effects;
  }

  public void setEffects(@Nullable Object effects) {
    this.effects = effects;
  }

  public Combo skillCeiling(@Nullable String skillCeiling) {
    this.skillCeiling = skillCeiling;
    return this;
  }

  /**
   * Get skillCeiling
   * @return skillCeiling
   */
  
  @Schema(name = "skill_ceiling", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_ceiling")
  public @Nullable String getSkillCeiling() {
    return skillCeiling;
  }

  public void setSkillCeiling(@Nullable String skillCeiling) {
    this.skillCeiling = skillCeiling;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Combo combo = (Combo) o;
    return Objects.equals(this.comboId, combo.comboId) &&
        Objects.equals(this.name, combo.name) &&
        Objects.equals(this.type, combo.type) &&
        Objects.equals(this.requiredAbilities, combo.requiredAbilities) &&
        Objects.equals(this.executionWindow, combo.executionWindow) &&
        Objects.equals(this.effects, combo.effects) &&
        Objects.equals(this.skillCeiling, combo.skillCeiling);
  }

  @Override
  public int hashCode() {
    return Objects.hash(comboId, name, type, requiredAbilities, executionWindow, effects, skillCeiling);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Combo {\n");
    sb.append("    comboId: ").append(toIndentedString(comboId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    requiredAbilities: ").append(toIndentedString(requiredAbilities)).append("\n");
    sb.append("    executionWindow: ").append(toIndentedString(executionWindow)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
    sb.append("    skillCeiling: ").append(toIndentedString(skillCeiling)).append("\n");
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

