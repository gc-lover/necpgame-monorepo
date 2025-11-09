package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DialogueExecutionResultSkillCheckResult
 */

@JsonTypeName("DialogueExecutionResult_skill_check_result")

public class DialogueExecutionResultSkillCheckResult {

  private @Nullable Integer roll;

  private @Nullable Integer modifier;

  private @Nullable Integer total;

  private @Nullable Integer dc;

  private @Nullable Boolean success;

  private @Nullable Boolean critical;

  public DialogueExecutionResultSkillCheckResult roll(@Nullable Integer roll) {
    this.roll = roll;
    return this;
  }

  /**
   * Результат броска (1-20)
   * @return roll
   */
  
  @Schema(name = "roll", example = "17", description = "Результат броска (1-20)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roll")
  public @Nullable Integer getRoll() {
    return roll;
  }

  public void setRoll(@Nullable Integer roll) {
    this.roll = roll;
  }

  public DialogueExecutionResultSkillCheckResult modifier(@Nullable Integer modifier) {
    this.modifier = modifier;
    return this;
  }

  /**
   * Модификатор навыка
   * @return modifier
   */
  
  @Schema(name = "modifier", example = "5", description = "Модификатор навыка", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifier")
  public @Nullable Integer getModifier() {
    return modifier;
  }

  public void setModifier(@Nullable Integer modifier) {
    this.modifier = modifier;
  }

  public DialogueExecutionResultSkillCheckResult total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * roll + modifier
   * @return total
   */
  
  @Schema(name = "total", example = "22", description = "roll + modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  public DialogueExecutionResultSkillCheckResult dc(@Nullable Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Difficulty Class
   * @return dc
   */
  
  @Schema(name = "dc", example = "15", description = "Difficulty Class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dc")
  public @Nullable Integer getDc() {
    return dc;
  }

  public void setDc(@Nullable Integer dc) {
    this.dc = dc;
  }

  public DialogueExecutionResultSkillCheckResult success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", example = "true", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public DialogueExecutionResultSkillCheckResult critical(@Nullable Boolean critical) {
    this.critical = critical;
    return this;
  }

  /**
   * Был ли крит (nat 1 или nat 20)
   * @return critical
   */
  
  @Schema(name = "critical", example = "false", description = "Был ли крит (nat 1 или nat 20)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical")
  public @Nullable Boolean getCritical() {
    return critical;
  }

  public void setCritical(@Nullable Boolean critical) {
    this.critical = critical;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueExecutionResultSkillCheckResult dialogueExecutionResultSkillCheckResult = (DialogueExecutionResultSkillCheckResult) o;
    return Objects.equals(this.roll, dialogueExecutionResultSkillCheckResult.roll) &&
        Objects.equals(this.modifier, dialogueExecutionResultSkillCheckResult.modifier) &&
        Objects.equals(this.total, dialogueExecutionResultSkillCheckResult.total) &&
        Objects.equals(this.dc, dialogueExecutionResultSkillCheckResult.dc) &&
        Objects.equals(this.success, dialogueExecutionResultSkillCheckResult.success) &&
        Objects.equals(this.critical, dialogueExecutionResultSkillCheckResult.critical);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roll, modifier, total, dc, success, critical);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueExecutionResultSkillCheckResult {\n");
    sb.append("    roll: ").append(toIndentedString(roll)).append("\n");
    sb.append("    modifier: ").append(toIndentedString(modifier)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    critical: ").append(toIndentedString(critical)).append("\n");
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

