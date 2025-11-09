package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * PendingConsequence
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class PendingConsequence {

  private @Nullable UUID consequenceId;

  private @Nullable String actionId;

  private @Nullable String description;

  private @Nullable String triggerCondition;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> estimatedTriggerTime = JsonNullable.<OffsetDateTime>undefined();

  /**
   * Gets or Sets impactLevel
   */
  public enum ImpactLevelEnum {
    MINOR("MINOR"),
    
    MODERATE("MODERATE"),
    
    MAJOR("MAJOR"),
    
    WORLD_CHANGING("WORLD_CHANGING");

    private final String value;

    ImpactLevelEnum(String value) {
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
    public static ImpactLevelEnum fromValue(String value) {
      for (ImpactLevelEnum b : ImpactLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ImpactLevelEnum impactLevel;

  public PendingConsequence consequenceId(@Nullable UUID consequenceId) {
    this.consequenceId = consequenceId;
    return this;
  }

  /**
   * Get consequenceId
   * @return consequenceId
   */
  @Valid 
  @Schema(name = "consequence_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequence_id")
  public @Nullable UUID getConsequenceId() {
    return consequenceId;
  }

  public void setConsequenceId(@Nullable UUID consequenceId) {
    this.consequenceId = consequenceId;
  }

  public PendingConsequence actionId(@Nullable String actionId) {
    this.actionId = actionId;
    return this;
  }

  /**
   * Get actionId
   * @return actionId
   */
  
  @Schema(name = "action_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("action_id")
  public @Nullable String getActionId() {
    return actionId;
  }

  public void setActionId(@Nullable String actionId) {
    this.actionId = actionId;
  }

  public PendingConsequence description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public PendingConsequence triggerCondition(@Nullable String triggerCondition) {
    this.triggerCondition = triggerCondition;
    return this;
  }

  /**
   * Когда проявится
   * @return triggerCondition
   */
  
  @Schema(name = "trigger_condition", description = "Когда проявится", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trigger_condition")
  public @Nullable String getTriggerCondition() {
    return triggerCondition;
  }

  public void setTriggerCondition(@Nullable String triggerCondition) {
    this.triggerCondition = triggerCondition;
  }

  public PendingConsequence estimatedTriggerTime(OffsetDateTime estimatedTriggerTime) {
    this.estimatedTriggerTime = JsonNullable.of(estimatedTriggerTime);
    return this;
  }

  /**
   * Get estimatedTriggerTime
   * @return estimatedTriggerTime
   */
  @Valid 
  @Schema(name = "estimated_trigger_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_trigger_time")
  public JsonNullable<OffsetDateTime> getEstimatedTriggerTime() {
    return estimatedTriggerTime;
  }

  public void setEstimatedTriggerTime(JsonNullable<OffsetDateTime> estimatedTriggerTime) {
    this.estimatedTriggerTime = estimatedTriggerTime;
  }

  public PendingConsequence impactLevel(@Nullable ImpactLevelEnum impactLevel) {
    this.impactLevel = impactLevel;
    return this;
  }

  /**
   * Get impactLevel
   * @return impactLevel
   */
  
  @Schema(name = "impact_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact_level")
  public @Nullable ImpactLevelEnum getImpactLevel() {
    return impactLevel;
  }

  public void setImpactLevel(@Nullable ImpactLevelEnum impactLevel) {
    this.impactLevel = impactLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PendingConsequence pendingConsequence = (PendingConsequence) o;
    return Objects.equals(this.consequenceId, pendingConsequence.consequenceId) &&
        Objects.equals(this.actionId, pendingConsequence.actionId) &&
        Objects.equals(this.description, pendingConsequence.description) &&
        Objects.equals(this.triggerCondition, pendingConsequence.triggerCondition) &&
        equalsNullable(this.estimatedTriggerTime, pendingConsequence.estimatedTriggerTime) &&
        Objects.equals(this.impactLevel, pendingConsequence.impactLevel);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(consequenceId, actionId, description, triggerCondition, hashCodeNullable(estimatedTriggerTime), impactLevel);
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
    sb.append("class PendingConsequence {\n");
    sb.append("    consequenceId: ").append(toIndentedString(consequenceId)).append("\n");
    sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    triggerCondition: ").append(toIndentedString(triggerCondition)).append("\n");
    sb.append("    estimatedTriggerTime: ").append(toIndentedString(estimatedTriggerTime)).append("\n");
    sb.append("    impactLevel: ").append(toIndentedString(impactLevel)).append("\n");
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

