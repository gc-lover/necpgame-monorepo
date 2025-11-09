package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.AdjustmentActionExpectedImpact;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AdjustmentAction
 */


public class AdjustmentAction {

  private @Nullable String actionId;

  private String target;

  private String field;

  private Float delta;

  private @Nullable String reason;

  private @Nullable AdjustmentActionExpectedImpact expectedImpact;

  public AdjustmentAction() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AdjustmentAction(String target, String field, Float delta) {
    this.target = target;
    this.field = field;
    this.delta = delta;
  }

  public AdjustmentAction actionId(@Nullable String actionId) {
    this.actionId = actionId;
    return this;
  }

  /**
   * Get actionId
   * @return actionId
   */
  
  @Schema(name = "actionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actionId")
  public @Nullable String getActionId() {
    return actionId;
  }

  public void setActionId(@Nullable String actionId) {
    this.actionId = actionId;
  }

  public AdjustmentAction target(String target) {
    this.target = target;
    return this;
  }

  /**
   * Например, raidId, questId или economyProfile
   * @return target
   */
  @NotNull 
  @Schema(name = "target", description = "Например, raidId, questId или economyProfile", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target")
  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public AdjustmentAction field(String field) {
    this.field = field;
    return this;
  }

  /**
   * Параметр для изменения, например hpMultiplier, taxRate
   * @return field
   */
  @NotNull 
  @Schema(name = "field", description = "Параметр для изменения, например hpMultiplier, taxRate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("field")
  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public AdjustmentAction delta(Float delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  @NotNull 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Float getDelta() {
    return delta;
  }

  public void setDelta(Float delta) {
    this.delta = delta;
  }

  public AdjustmentAction reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public AdjustmentAction expectedImpact(@Nullable AdjustmentActionExpectedImpact expectedImpact) {
    this.expectedImpact = expectedImpact;
    return this;
  }

  /**
   * Get expectedImpact
   * @return expectedImpact
   */
  @Valid 
  @Schema(name = "expectedImpact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expectedImpact")
  public @Nullable AdjustmentActionExpectedImpact getExpectedImpact() {
    return expectedImpact;
  }

  public void setExpectedImpact(@Nullable AdjustmentActionExpectedImpact expectedImpact) {
    this.expectedImpact = expectedImpact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdjustmentAction adjustmentAction = (AdjustmentAction) o;
    return Objects.equals(this.actionId, adjustmentAction.actionId) &&
        Objects.equals(this.target, adjustmentAction.target) &&
        Objects.equals(this.field, adjustmentAction.field) &&
        Objects.equals(this.delta, adjustmentAction.delta) &&
        Objects.equals(this.reason, adjustmentAction.reason) &&
        Objects.equals(this.expectedImpact, adjustmentAction.expectedImpact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionId, target, field, delta, reason, expectedImpact);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdjustmentAction {\n");
    sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    expectedImpact: ").append(toIndentedString(expectedImpact)).append("\n");
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

