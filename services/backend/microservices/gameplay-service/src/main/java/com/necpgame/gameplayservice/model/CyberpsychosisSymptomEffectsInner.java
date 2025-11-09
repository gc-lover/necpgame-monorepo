package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CyberpsychosisSymptomEffectsInner
 */

@JsonTypeName("CyberpsychosisSymptom_effects_inner")

public class CyberpsychosisSymptomEffectsInner {

  /**
   * Gets or Sets effectType
   */
  public enum EffectTypeEnum {
    ACCURACY_PENALTY("accuracy_penalty"),
    
    SOCIAL_PENALTY("social_penalty"),
    
    HEALTH_PENALTY("health_penalty"),
    
    ENERGY_PENALTY("energy_penalty"),
    
    CONTROL_LOSS("control_loss");

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

  private @Nullable BigDecimal value;

  public CyberpsychosisSymptomEffectsInner effectType(@Nullable EffectTypeEnum effectType) {
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

  public CyberpsychosisSymptomEffectsInner value(@Nullable BigDecimal value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  @Valid 
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable BigDecimal getValue() {
    return value;
  }

  public void setValue(@Nullable BigDecimal value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CyberpsychosisSymptomEffectsInner cyberpsychosisSymptomEffectsInner = (CyberpsychosisSymptomEffectsInner) o;
    return Objects.equals(this.effectType, cyberpsychosisSymptomEffectsInner.effectType) &&
        Objects.equals(this.value, cyberpsychosisSymptomEffectsInner.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(effectType, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CyberpsychosisSymptomEffectsInner {\n");
    sb.append("    effectType: ").append(toIndentedString(effectType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

