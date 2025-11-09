package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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


public class EventEffect {

  /**
   * Gets or Sets effectType
   */
  public enum EffectTypeEnum {
    PRICE_CHANGE("PRICE_CHANGE"),
    
    AVAILABILITY_CHANGE("AVAILABILITY_CHANGE"),
    
    ACCESS_RESTRICTION("ACCESS_RESTRICTION"),
    
    TAX_CHANGE("TAX_CHANGE");

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

  private @Nullable String target;

  private @Nullable Float multiplier;

  private @Nullable String description;

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

  public EventEffect target(@Nullable String target) {
    this.target = target;
    return this;
  }

  /**
   * Что затронуто
   * @return target
   */
  
  @Schema(name = "target", description = "Что затронуто", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target")
  public @Nullable String getTarget() {
    return target;
  }

  public void setTarget(@Nullable String target) {
    this.target = target;
  }

  public EventEffect multiplier(@Nullable Float multiplier) {
    this.multiplier = multiplier;
    return this;
  }

  /**
   * Get multiplier
   * @return multiplier
   */
  
  @Schema(name = "multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("multiplier")
  public @Nullable Float getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(@Nullable Float multiplier) {
    this.multiplier = multiplier;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventEffect eventEffect = (EventEffect) o;
    return Objects.equals(this.effectType, eventEffect.effectType) &&
        Objects.equals(this.target, eventEffect.target) &&
        Objects.equals(this.multiplier, eventEffect.multiplier) &&
        Objects.equals(this.description, eventEffect.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(effectType, target, multiplier, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventEffect {\n");
    sb.append("    effectType: ").append(toIndentedString(effectType)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    multiplier: ").append(toIndentedString(multiplier)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

