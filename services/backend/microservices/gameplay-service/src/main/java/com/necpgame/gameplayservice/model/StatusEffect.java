package com.necpgame.gameplayservice.model;

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
 * StatusEffect
 */


public class StatusEffect {

  private @Nullable String effectId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    BUFF("BUFF"),
    
    DEBUFF("DEBUFF"),
    
    DOT("DOT"),
    
    HOT("HOT"),
    
    STUN("STUN"),
    
    ROOT("ROOT");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable Integer durationSeconds;

  private Integer stacks = 1;

  public StatusEffect effectId(@Nullable String effectId) {
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

  public StatusEffect name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Burning", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public StatusEffect type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public StatusEffect durationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
    return this;
  }

  /**
   * Get durationSeconds
   * @return durationSeconds
   */
  
  @Schema(name = "duration_seconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_seconds")
  public @Nullable Integer getDurationSeconds() {
    return durationSeconds;
  }

  public void setDurationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public StatusEffect stacks(Integer stacks) {
    this.stacks = stacks;
    return this;
  }

  /**
   * Get stacks
   * @return stacks
   */
  
  @Schema(name = "stacks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stacks")
  public Integer getStacks() {
    return stacks;
  }

  public void setStacks(Integer stacks) {
    this.stacks = stacks;
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
        Objects.equals(this.name, statusEffect.name) &&
        Objects.equals(this.type, statusEffect.type) &&
        Objects.equals(this.durationSeconds, statusEffect.durationSeconds) &&
        Objects.equals(this.stacks, statusEffect.stacks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(effectId, name, type, durationSeconds, stacks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusEffect {\n");
    sb.append("    effectId: ").append(toIndentedString(effectId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    durationSeconds: ").append(toIndentedString(durationSeconds)).append("\n");
    sb.append("    stacks: ").append(toIndentedString(stacks)).append("\n");
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

