package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.ActionXpEntryContext;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ActionXpEntry
 */


public class ActionXpEntry {

  private String skillId;

  private @Nullable String actionType;

  private BigDecimal xpGained;

  private BigDecimal activityMultiplier;

  private BigDecimal fatigueScore;

  private @Nullable UUID sourceEventId;

  private @Nullable ActionXpEntryContext context;

  public ActionXpEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActionXpEntry(String skillId, BigDecimal xpGained, BigDecimal activityMultiplier, BigDecimal fatigueScore) {
    this.skillId = skillId;
    this.xpGained = xpGained;
    this.activityMultiplier = activityMultiplier;
    this.fatigueScore = fatigueScore;
  }

  public ActionXpEntry skillId(String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  @NotNull 
  @Schema(name = "skillId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("skillId")
  public String getSkillId() {
    return skillId;
  }

  public void setSkillId(String skillId) {
    this.skillId = skillId;
  }

  public ActionXpEntry actionType(@Nullable String actionType) {
    this.actionType = actionType;
    return this;
  }

  /**
   * Get actionType
   * @return actionType
   */
  
  @Schema(name = "actionType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actionType")
  public @Nullable String getActionType() {
    return actionType;
  }

  public void setActionType(@Nullable String actionType) {
    this.actionType = actionType;
  }

  public ActionXpEntry xpGained(BigDecimal xpGained) {
    this.xpGained = xpGained;
    return this;
  }

  /**
   * Get xpGained
   * minimum: 0
   * @return xpGained
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "xpGained", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("xpGained")
  public BigDecimal getXpGained() {
    return xpGained;
  }

  public void setXpGained(BigDecimal xpGained) {
    this.xpGained = xpGained;
  }

  public ActionXpEntry activityMultiplier(BigDecimal activityMultiplier) {
    this.activityMultiplier = activityMultiplier;
    return this;
  }

  /**
   * Get activityMultiplier
   * minimum: 0
   * @return activityMultiplier
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "activityMultiplier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activityMultiplier")
  public BigDecimal getActivityMultiplier() {
    return activityMultiplier;
  }

  public void setActivityMultiplier(BigDecimal activityMultiplier) {
    this.activityMultiplier = activityMultiplier;
  }

  public ActionXpEntry fatigueScore(BigDecimal fatigueScore) {
    this.fatigueScore = fatigueScore;
    return this;
  }

  /**
   * Get fatigueScore
   * minimum: 0
   * @return fatigueScore
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "fatigueScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("fatigueScore")
  public BigDecimal getFatigueScore() {
    return fatigueScore;
  }

  public void setFatigueScore(BigDecimal fatigueScore) {
    this.fatigueScore = fatigueScore;
  }

  public ActionXpEntry sourceEventId(@Nullable UUID sourceEventId) {
    this.sourceEventId = sourceEventId;
    return this;
  }

  /**
   * Get sourceEventId
   * @return sourceEventId
   */
  @Valid 
  @Schema(name = "sourceEventId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceEventId")
  public @Nullable UUID getSourceEventId() {
    return sourceEventId;
  }

  public void setSourceEventId(@Nullable UUID sourceEventId) {
    this.sourceEventId = sourceEventId;
  }

  public ActionXpEntry context(@Nullable ActionXpEntryContext context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @Valid 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("context")
  public @Nullable ActionXpEntryContext getContext() {
    return context;
  }

  public void setContext(@Nullable ActionXpEntryContext context) {
    this.context = context;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpEntry actionXpEntry = (ActionXpEntry) o;
    return Objects.equals(this.skillId, actionXpEntry.skillId) &&
        Objects.equals(this.actionType, actionXpEntry.actionType) &&
        Objects.equals(this.xpGained, actionXpEntry.xpGained) &&
        Objects.equals(this.activityMultiplier, actionXpEntry.activityMultiplier) &&
        Objects.equals(this.fatigueScore, actionXpEntry.fatigueScore) &&
        Objects.equals(this.sourceEventId, actionXpEntry.sourceEventId) &&
        Objects.equals(this.context, actionXpEntry.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillId, actionType, xpGained, activityMultiplier, fatigueScore, sourceEventId, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpEntry {\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    actionType: ").append(toIndentedString(actionType)).append("\n");
    sb.append("    xpGained: ").append(toIndentedString(xpGained)).append("\n");
    sb.append("    activityMultiplier: ").append(toIndentedString(activityMultiplier)).append("\n");
    sb.append("    fatigueScore: ").append(toIndentedString(fatigueScore)).append("\n");
    sb.append("    sourceEventId: ").append(toIndentedString(sourceEventId)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
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

