package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SkillCheck
 */


public class SkillCheck {

  private String skill;

  private Integer dc;

  private @Nullable String successNode;

  private @Nullable String failureNode;

  private @Nullable String criticalSuccessNode;

  private @Nullable String criticalFailureNode;

  public SkillCheck() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SkillCheck(String skill, Integer dc) {
    this.skill = skill;
    this.dc = dc;
  }

  public SkillCheck skill(String skill) {
    this.skill = skill;
    return this;
  }

  /**
   * Название навыка
   * @return skill
   */
  @NotNull 
  @Schema(name = "skill", example = "hacking", description = "Название навыка", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skill")
  public String getSkill() {
    return skill;
  }

  public void setSkill(String skill) {
    this.skill = skill;
  }

  public SkillCheck dc(Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Difficulty Class
   * minimum: 1
   * maximum: 30
   * @return dc
   */
  @NotNull @Min(value = 1) @Max(value = 30) 
  @Schema(name = "dc", example = "18", description = "Difficulty Class", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dc")
  public Integer getDc() {
    return dc;
  }

  public void setDc(Integer dc) {
    this.dc = dc;
  }

  public SkillCheck successNode(@Nullable String successNode) {
    this.successNode = successNode;
    return this;
  }

  /**
   * Node при успехе
   * @return successNode
   */
  
  @Schema(name = "success_node", example = "node_success", description = "Node при успехе", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success_node")
  public @Nullable String getSuccessNode() {
    return successNode;
  }

  public void setSuccessNode(@Nullable String successNode) {
    this.successNode = successNode;
  }

  public SkillCheck failureNode(@Nullable String failureNode) {
    this.failureNode = failureNode;
    return this;
  }

  /**
   * Node при провале
   * @return failureNode
   */
  
  @Schema(name = "failure_node", example = "node_failure", description = "Node при провале", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("failure_node")
  public @Nullable String getFailureNode() {
    return failureNode;
  }

  public void setFailureNode(@Nullable String failureNode) {
    this.failureNode = failureNode;
  }

  public SkillCheck criticalSuccessNode(@Nullable String criticalSuccessNode) {
    this.criticalSuccessNode = criticalSuccessNode;
    return this;
  }

  /**
   * Node при крит. успехе (nat 20)
   * @return criticalSuccessNode
   */
  
  @Schema(name = "critical_success_node", example = "node_crit_success", description = "Node при крит. успехе (nat 20)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_success_node")
  public @Nullable String getCriticalSuccessNode() {
    return criticalSuccessNode;
  }

  public void setCriticalSuccessNode(@Nullable String criticalSuccessNode) {
    this.criticalSuccessNode = criticalSuccessNode;
  }

  public SkillCheck criticalFailureNode(@Nullable String criticalFailureNode) {
    this.criticalFailureNode = criticalFailureNode;
    return this;
  }

  /**
   * Node при крит. провале (nat 1)
   * @return criticalFailureNode
   */
  
  @Schema(name = "critical_failure_node", example = "node_crit_fail", description = "Node при крит. провале (nat 1)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_failure_node")
  public @Nullable String getCriticalFailureNode() {
    return criticalFailureNode;
  }

  public void setCriticalFailureNode(@Nullable String criticalFailureNode) {
    this.criticalFailureNode = criticalFailureNode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillCheck skillCheck = (SkillCheck) o;
    return Objects.equals(this.skill, skillCheck.skill) &&
        Objects.equals(this.dc, skillCheck.dc) &&
        Objects.equals(this.successNode, skillCheck.successNode) &&
        Objects.equals(this.failureNode, skillCheck.failureNode) &&
        Objects.equals(this.criticalSuccessNode, skillCheck.criticalSuccessNode) &&
        Objects.equals(this.criticalFailureNode, skillCheck.criticalFailureNode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skill, dc, successNode, failureNode, criticalSuccessNode, criticalFailureNode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillCheck {\n");
    sb.append("    skill: ").append(toIndentedString(skill)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
    sb.append("    successNode: ").append(toIndentedString(successNode)).append("\n");
    sb.append("    failureNode: ").append(toIndentedString(failureNode)).append("\n");
    sb.append("    criticalSuccessNode: ").append(toIndentedString(criticalSuccessNode)).append("\n");
    sb.append("    criticalFailureNode: ").append(toIndentedString(criticalFailureNode)).append("\n");
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

