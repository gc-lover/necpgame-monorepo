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
 * AbilityActivationRequest
 */


public class AbilityActivationRequest {

  /**
   * Gets or Sets context
   */
  public enum ContextEnum {
    PVE("pve"),
    
    ARENA("arena"),
    
    RAID("raid");

    private final String value;

    ContextEnum(String value) {
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
    public static ContextEnum fromValue(String value) {
      for (ContextEnum b : ContextEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ContextEnum context;

  private @Nullable String targetId;

  private @Nullable Boolean manualOverride;

  private @Nullable Integer energyOverride;

  public AbilityActivationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AbilityActivationRequest(ContextEnum context) {
    this.context = context;
  }

  public AbilityActivationRequest context(ContextEnum context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @NotNull 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("context")
  public ContextEnum getContext() {
    return context;
  }

  public void setContext(ContextEnum context) {
    this.context = context;
  }

  public AbilityActivationRequest targetId(@Nullable String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "targetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetId")
  public @Nullable String getTargetId() {
    return targetId;
  }

  public void setTargetId(@Nullable String targetId) {
    this.targetId = targetId;
  }

  public AbilityActivationRequest manualOverride(@Nullable Boolean manualOverride) {
    this.manualOverride = manualOverride;
    return this;
  }

  /**
   * Get manualOverride
   * @return manualOverride
   */
  
  @Schema(name = "manualOverride", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("manualOverride")
  public @Nullable Boolean getManualOverride() {
    return manualOverride;
  }

  public void setManualOverride(@Nullable Boolean manualOverride) {
    this.manualOverride = manualOverride;
  }

  public AbilityActivationRequest energyOverride(@Nullable Integer energyOverride) {
    this.energyOverride = energyOverride;
    return this;
  }

  /**
   * Новое значение энергии при ручном триггере
   * @return energyOverride
   */
  
  @Schema(name = "energyOverride", description = "Новое значение энергии при ручном триггере", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energyOverride")
  public @Nullable Integer getEnergyOverride() {
    return energyOverride;
  }

  public void setEnergyOverride(@Nullable Integer energyOverride) {
    this.energyOverride = energyOverride;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityActivationRequest abilityActivationRequest = (AbilityActivationRequest) o;
    return Objects.equals(this.context, abilityActivationRequest.context) &&
        Objects.equals(this.targetId, abilityActivationRequest.targetId) &&
        Objects.equals(this.manualOverride, abilityActivationRequest.manualOverride) &&
        Objects.equals(this.energyOverride, abilityActivationRequest.energyOverride);
  }

  @Override
  public int hashCode() {
    return Objects.hash(context, targetId, manualOverride, energyOverride);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityActivationRequest {\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    manualOverride: ").append(toIndentedString(manualOverride)).append("\n");
    sb.append("    energyOverride: ").append(toIndentedString(energyOverride)).append("\n");
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

