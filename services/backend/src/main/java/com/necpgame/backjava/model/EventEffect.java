package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.EventEffectModifier;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EventEffect
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class EventEffect {

  private @Nullable String effectId;

  /**
   * Gets or Sets effectType
   */
  public enum EffectTypeEnum {
    ECONOMIC("ECONOMIC"),
    
    SOCIAL("SOCIAL"),
    
    TECHNOLOGICAL("TECHNOLOGICAL"),
    
    COMBAT("COMBAT"),
    
    REPUTATION("REPUTATION");

    private final String value;

    EffectTypeEnum(String value) {
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
    public static EffectTypeEnum fromValue(String value) {
      for (EffectTypeEnum b : EffectTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable EffectTypeEnum effectType;

  private @Nullable String description;

  private @Nullable EventEffectModifier modifier;

  private @Nullable String duration;

  private @Nullable Boolean stackable;

  public EventEffect effectId(@Nullable String effectId) {
    this.effectId = effectId;
    return this;
  }

  /**
   * Get effectId
   * @return effectId
   */
  
  @Schema(name = "effect_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effect_id")
  public @Nullable String getEffectId() {
    return effectId;
  }

  public void setEffectId(@Nullable String effectId) {
    this.effectId = effectId;
  }

  public EventEffect effectType(@Nullable EffectTypeEnum effectType) {
    this.effectType = effectType;
    return this;
  }

  /**
   * Get effectType
   * @return effectType
   */
  
  @Schema(name = "effect_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effect_type")
  public @Nullable EffectTypeEnum getEffectType() {
    return effectType;
  }

  public void setEffectType(@Nullable EffectTypeEnum effectType) {
    this.effectType = effectType;
  }

  public EventEffect description(@Nullable String description) {
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

  public EventEffect modifier(@Nullable EventEffectModifier modifier) {
    this.modifier = modifier;
    return this;
  }

  /**
   * Get modifier
   * @return modifier
   */
  @Valid 
  @Schema(name = "modifier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modifier")
  public @Nullable EventEffectModifier getModifier() {
    return modifier;
  }

  public void setModifier(@Nullable EventEffectModifier modifier) {
    this.modifier = modifier;
  }

  public EventEffect duration(@Nullable String duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Длительность эффекта
   * @return duration
   */
  
  @Schema(name = "duration", description = "Длительность эффекта", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable String getDuration() {
    return duration;
  }

  public void setDuration(@Nullable String duration) {
    this.duration = duration;
  }

  public EventEffect stackable(@Nullable Boolean stackable) {
    this.stackable = stackable;
    return this;
  }

  /**
   * Get stackable
   * @return stackable
   */
  
  @Schema(name = "stackable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stackable")
  public @Nullable Boolean getStackable() {
    return stackable;
  }

  public void setStackable(@Nullable Boolean stackable) {
    this.stackable = stackable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventEffect eventEffect = (EventEffect) o;
    return Objects.equals(this.effectId, eventEffect.effectId) &&
        Objects.equals(this.effectType, eventEffect.effectType) &&
        Objects.equals(this.description, eventEffect.description) &&
        Objects.equals(this.modifier, eventEffect.modifier) &&
        Objects.equals(this.duration, eventEffect.duration) &&
        Objects.equals(this.stackable, eventEffect.stackable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(effectId, effectType, description, modifier, duration, stackable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventEffect {\n");
    sb.append("    effectId: ").append(toIndentedString(effectId)).append("\n");
    sb.append("    effectType: ").append(toIndentedString(effectType)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    modifier: ").append(toIndentedString(modifier)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    stackable: ").append(toIndentedString(stackable)).append("\n");
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

