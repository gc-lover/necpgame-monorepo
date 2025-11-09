package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * AbilitySynergyBonusEffectsInner
 */

@JsonTypeName("AbilitySynergy_bonus_effects_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T22:49:04.787810800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class AbilitySynergyBonusEffectsInner {

  private @Nullable String effectType;

  private @Nullable BigDecimal value;

  public AbilitySynergyBonusEffectsInner effectType(@Nullable String effectType) {
    this.effectType = effectType;
    return this;
  }

  /**
   * Get effectType
   * @return effectType
   */
  
  @Schema(name = "effect_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effect_type")
  public @Nullable String getEffectType() {
    return effectType;
  }

  public void setEffectType(@Nullable String effectType) {
    this.effectType = effectType;
  }

  public AbilitySynergyBonusEffectsInner value(@Nullable BigDecimal value) {
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
    AbilitySynergyBonusEffectsInner abilitySynergyBonusEffectsInner = (AbilitySynergyBonusEffectsInner) o;
    return Objects.equals(this.effectType, abilitySynergyBonusEffectsInner.effectType) &&
        Objects.equals(this.value, abilitySynergyBonusEffectsInner.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(effectType, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilitySynergyBonusEffectsInner {\n");
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

