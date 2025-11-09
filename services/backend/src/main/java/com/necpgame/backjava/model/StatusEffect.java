package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
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
 * StatusEffect
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class StatusEffect {

  private @Nullable String effectId;

  private @Nullable String type;

  private @Nullable Integer stacks;

  private @Nullable Integer durationMs;

  private @Nullable Integer remainingMs;

  private @Nullable String sourceId;

  @Valid
  private Map<String, Object> modifiers = new HashMap<>();

  public StatusEffect effectId(@Nullable String effectId) {
    this.effectId = effectId;
    return this;
  }

  /**
   * Get effectId
   * @return effectId
   */
  
  @Schema(name = "effectId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effectId")
  public @Nullable String getEffectId() {
    return effectId;
  }

  public void setEffectId(@Nullable String effectId) {
    this.effectId = effectId;
  }

  public StatusEffect type(@Nullable String type) {
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

  public StatusEffect stacks(@Nullable Integer stacks) {
    this.stacks = stacks;
    return this;
  }

  /**
   * Get stacks
   * @return stacks
   */
  
  @Schema(name = "stacks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stacks")
  public @Nullable Integer getStacks() {
    return stacks;
  }

  public void setStacks(@Nullable Integer stacks) {
    this.stacks = stacks;
  }

  public StatusEffect durationMs(@Nullable Integer durationMs) {
    this.durationMs = durationMs;
    return this;
  }

  /**
   * Get durationMs
   * @return durationMs
   */
  
  @Schema(name = "durationMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationMs")
  public @Nullable Integer getDurationMs() {
    return durationMs;
  }

  public void setDurationMs(@Nullable Integer durationMs) {
    this.durationMs = durationMs;
  }

  public StatusEffect remainingMs(@Nullable Integer remainingMs) {
    this.remainingMs = remainingMs;
    return this;
  }

  /**
   * Get remainingMs
   * @return remainingMs
   */
  
  @Schema(name = "remainingMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remainingMs")
  public @Nullable Integer getRemainingMs() {
    return remainingMs;
  }

  public void setRemainingMs(@Nullable Integer remainingMs) {
    this.remainingMs = remainingMs;
  }

  public StatusEffect sourceId(@Nullable String sourceId) {
    this.sourceId = sourceId;
    return this;
  }

  /**
   * Get sourceId
   * @return sourceId
   */
  
  @Schema(name = "sourceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceId")
  public @Nullable String getSourceId() {
    return sourceId;
  }

  public void setSourceId(@Nullable String sourceId) {
    this.sourceId = sourceId;
  }

  public StatusEffect modifiers(Map<String, Object> modifiers) {
    this.modifiers = modifiers;
    return this;
  }

  public StatusEffect putModifiersItem(String key, Object modifiersItem) {
    if (this.modifiers == null) {
      this.modifiers = new HashMap<>();
    }
    this.modifiers.put(key, modifiersItem);
    return this;
  }

  /**
   * Get modifiers
   * @return modifiers
   */
  
  @Schema(name = "modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifiers")
  public Map<String, Object> getModifiers() {
    return modifiers;
  }

  public void setModifiers(Map<String, Object> modifiers) {
    this.modifiers = modifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatusEffect statusEffect = (StatusEffect) o;
    return Objects.equals(this.effectId, statusEffect.effectId) &&
        Objects.equals(this.type, statusEffect.type) &&
        Objects.equals(this.stacks, statusEffect.stacks) &&
        Objects.equals(this.durationMs, statusEffect.durationMs) &&
        Objects.equals(this.remainingMs, statusEffect.remainingMs) &&
        Objects.equals(this.sourceId, statusEffect.sourceId) &&
        Objects.equals(this.modifiers, statusEffect.modifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(effectId, type, stacks, durationMs, remainingMs, sourceId, modifiers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusEffect {\n");
    sb.append("    effectId: ").append(toIndentedString(effectId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    stacks: ").append(toIndentedString(stacks)).append("\n");
    sb.append("    durationMs: ").append(toIndentedString(durationMs)).append("\n");
    sb.append("    remainingMs: ").append(toIndentedString(remainingMs)).append("\n");
    sb.append("    sourceId: ").append(toIndentedString(sourceId)).append("\n");
    sb.append("    modifiers: ").append(toIndentedString(modifiers)).append("\n");
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

