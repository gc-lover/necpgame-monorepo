package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CombatLogEntry;
import com.necpgame.backjava.model.DamagePacket;
import com.necpgame.backjava.model.StatusEffect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ActionResult
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ActionResult {

  private @Nullable String actionId;

  @Valid
  private List<@Valid DamagePacket> applied = new ArrayList<>();

  @Valid
  private List<@Valid StatusEffect> newEffects = new ArrayList<>();

  @Valid
  private Map<String, Object> cooldowns = new HashMap<>();

  private @Nullable CombatLogEntry logEntry;

  public ActionResult actionId(@Nullable String actionId) {
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

  public ActionResult applied(List<@Valid DamagePacket> applied) {
    this.applied = applied;
    return this;
  }

  public ActionResult addAppliedItem(DamagePacket appliedItem) {
    if (this.applied == null) {
      this.applied = new ArrayList<>();
    }
    this.applied.add(appliedItem);
    return this;
  }

  /**
   * Get applied
   * @return applied
   */
  @Valid 
  @Schema(name = "applied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("applied")
  public List<@Valid DamagePacket> getApplied() {
    return applied;
  }

  public void setApplied(List<@Valid DamagePacket> applied) {
    this.applied = applied;
  }

  public ActionResult newEffects(List<@Valid StatusEffect> newEffects) {
    this.newEffects = newEffects;
    return this;
  }

  public ActionResult addNewEffectsItem(StatusEffect newEffectsItem) {
    if (this.newEffects == null) {
      this.newEffects = new ArrayList<>();
    }
    this.newEffects.add(newEffectsItem);
    return this;
  }

  /**
   * Get newEffects
   * @return newEffects
   */
  @Valid 
  @Schema(name = "newEffects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("newEffects")
  public List<@Valid StatusEffect> getNewEffects() {
    return newEffects;
  }

  public void setNewEffects(List<@Valid StatusEffect> newEffects) {
    this.newEffects = newEffects;
  }

  public ActionResult cooldowns(Map<String, Object> cooldowns) {
    this.cooldowns = cooldowns;
    return this;
  }

  public ActionResult putCooldownsItem(String key, Object cooldownsItem) {
    if (this.cooldowns == null) {
      this.cooldowns = new HashMap<>();
    }
    this.cooldowns.put(key, cooldownsItem);
    return this;
  }

  /**
   * Get cooldowns
   * @return cooldowns
   */
  
  @Schema(name = "cooldowns", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldowns")
  public Map<String, Object> getCooldowns() {
    return cooldowns;
  }

  public void setCooldowns(Map<String, Object> cooldowns) {
    this.cooldowns = cooldowns;
  }

  public ActionResult logEntry(@Nullable CombatLogEntry logEntry) {
    this.logEntry = logEntry;
    return this;
  }

  /**
   * Get logEntry
   * @return logEntry
   */
  @Valid 
  @Schema(name = "logEntry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("logEntry")
  public @Nullable CombatLogEntry getLogEntry() {
    return logEntry;
  }

  public void setLogEntry(@Nullable CombatLogEntry logEntry) {
    this.logEntry = logEntry;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionResult actionResult = (ActionResult) o;
    return Objects.equals(this.actionId, actionResult.actionId) &&
        Objects.equals(this.applied, actionResult.applied) &&
        Objects.equals(this.newEffects, actionResult.newEffects) &&
        Objects.equals(this.cooldowns, actionResult.cooldowns) &&
        Objects.equals(this.logEntry, actionResult.logEntry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(actionId, applied, newEffects, cooldowns, logEntry);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionResult {\n");
    sb.append("    actionId: ").append(toIndentedString(actionId)).append("\n");
    sb.append("    applied: ").append(toIndentedString(applied)).append("\n");
    sb.append("    newEffects: ").append(toIndentedString(newEffects)).append("\n");
    sb.append("    cooldowns: ").append(toIndentedString(cooldowns)).append("\n");
    sb.append("    logEntry: ").append(toIndentedString(logEntry)).append("\n");
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

