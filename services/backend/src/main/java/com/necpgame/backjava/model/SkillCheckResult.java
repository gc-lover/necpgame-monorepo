package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * SkillCheckResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SkillCheckResult {

  private @Nullable String skill;

  private @Nullable Integer difficulty;

  private @Nullable Integer roll;

  private @Nullable Integer modifier;

  private @Nullable Integer total;

  private @Nullable Boolean success;

  private @Nullable Boolean criticalSuccess;

  private @Nullable Boolean criticalFailure;

  private @Nullable Boolean advantageUsed;

  @Valid
  private List<Integer> rolls = new ArrayList<>();

  public SkillCheckResult skill(@Nullable String skill) {
    this.skill = skill;
    return this;
  }

  /**
   * Get skill
   * @return skill
   */
  
  @Schema(name = "skill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill")
  public @Nullable String getSkill() {
    return skill;
  }

  public void setSkill(@Nullable String skill) {
    this.skill = skill;
  }

  public SkillCheckResult difficulty(@Nullable Integer difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  
  @Schema(name = "difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable Integer getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable Integer difficulty) {
    this.difficulty = difficulty;
  }

  public SkillCheckResult roll(@Nullable Integer roll) {
    this.roll = roll;
    return this;
  }

  /**
   * Результат броска d20
   * @return roll
   */
  
  @Schema(name = "roll", example = "14", description = "Результат броска d20", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll")
  public @Nullable Integer getRoll() {
    return roll;
  }

  public void setRoll(@Nullable Integer roll) {
    this.roll = roll;
  }

  public SkillCheckResult modifier(@Nullable Integer modifier) {
    this.modifier = modifier;
    return this;
  }

  /**
   * Модификатор навыка персонажа
   * @return modifier
   */
  
  @Schema(name = "modifier", example = "5", description = "Модификатор навыка персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifier")
  public @Nullable Integer getModifier() {
    return modifier;
  }

  public void setModifier(@Nullable Integer modifier) {
    this.modifier = modifier;
  }

  public SkillCheckResult total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * roll + modifier
   * @return total
   */
  
  @Schema(name = "total", example = "19", description = "roll + modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  public SkillCheckResult success(@Nullable Boolean success) {
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

  public SkillCheckResult criticalSuccess(@Nullable Boolean criticalSuccess) {
    this.criticalSuccess = criticalSuccess;
    return this;
  }

  /**
   * Get criticalSuccess
   * @return criticalSuccess
   */
  
  @Schema(name = "critical_success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_success")
  public @Nullable Boolean getCriticalSuccess() {
    return criticalSuccess;
  }

  public void setCriticalSuccess(@Nullable Boolean criticalSuccess) {
    this.criticalSuccess = criticalSuccess;
  }

  public SkillCheckResult criticalFailure(@Nullable Boolean criticalFailure) {
    this.criticalFailure = criticalFailure;
    return this;
  }

  /**
   * Get criticalFailure
   * @return criticalFailure
   */
  
  @Schema(name = "critical_failure", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_failure")
  public @Nullable Boolean getCriticalFailure() {
    return criticalFailure;
  }

  public void setCriticalFailure(@Nullable Boolean criticalFailure) {
    this.criticalFailure = criticalFailure;
  }

  public SkillCheckResult advantageUsed(@Nullable Boolean advantageUsed) {
    this.advantageUsed = advantageUsed;
    return this;
  }

  /**
   * Get advantageUsed
   * @return advantageUsed
   */
  
  @Schema(name = "advantage_used", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("advantage_used")
  public @Nullable Boolean getAdvantageUsed() {
    return advantageUsed;
  }

  public void setAdvantageUsed(@Nullable Boolean advantageUsed) {
    this.advantageUsed = advantageUsed;
  }

  public SkillCheckResult rolls(List<Integer> rolls) {
    this.rolls = rolls;
    return this;
  }

  public SkillCheckResult addRollsItem(Integer rollsItem) {
    if (this.rolls == null) {
      this.rolls = new ArrayList<>();
    }
    this.rolls.add(rollsItem);
    return this;
  }

  /**
   * Если был advantage, показываем оба броска
   * @return rolls
   */
  
  @Schema(name = "rolls", description = "Если был advantage, показываем оба броска", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rolls")
  public List<Integer> getRolls() {
    return rolls;
  }

  public void setRolls(List<Integer> rolls) {
    this.rolls = rolls;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillCheckResult skillCheckResult = (SkillCheckResult) o;
    return Objects.equals(this.skill, skillCheckResult.skill) &&
        Objects.equals(this.difficulty, skillCheckResult.difficulty) &&
        Objects.equals(this.roll, skillCheckResult.roll) &&
        Objects.equals(this.modifier, skillCheckResult.modifier) &&
        Objects.equals(this.total, skillCheckResult.total) &&
        Objects.equals(this.success, skillCheckResult.success) &&
        Objects.equals(this.criticalSuccess, skillCheckResult.criticalSuccess) &&
        Objects.equals(this.criticalFailure, skillCheckResult.criticalFailure) &&
        Objects.equals(this.advantageUsed, skillCheckResult.advantageUsed) &&
        Objects.equals(this.rolls, skillCheckResult.rolls);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skill, difficulty, roll, modifier, total, success, criticalSuccess, criticalFailure, advantageUsed, rolls);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillCheckResult {\n");
    sb.append("    skill: ").append(toIndentedString(skill)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    roll: ").append(toIndentedString(roll)).append("\n");
    sb.append("    modifier: ").append(toIndentedString(modifier)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    criticalSuccess: ").append(toIndentedString(criticalSuccess)).append("\n");
    sb.append("    criticalFailure: ").append(toIndentedString(criticalFailure)).append("\n");
    sb.append("    advantageUsed: ").append(toIndentedString(advantageUsed)).append("\n");
    sb.append("    rolls: ").append(toIndentedString(rolls)).append("\n");
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

