package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * TriggerProgressionRequest
 */


public class TriggerProgressionRequest {

  /**
   * Триггер, запускающий скачок прогрессии
   */
  public enum TriggerTypeEnum {
    COMBAT_BERSERK("combat_berserk"),
    
    CYBERDECK_BLACKOUT("cyberdeck_blackout"),
    
    IMPLANT_FAILURE("implant_failure"),
    
    MASS_CASUALTIES("mass_casualties"),
    
    BETRAYAL_EVENT("betrayal_event");

    private final String value;

    TriggerTypeEnum(String value) {
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
    public static TriggerTypeEnum fromValue(String value) {
      for (TriggerTypeEnum b : TriggerTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TriggerTypeEnum triggerType;

  private BigDecimal intensity;

  private @Nullable Integer durationMinutes;

  private @Nullable Integer collateralDamage;

  public TriggerProgressionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TriggerProgressionRequest(TriggerTypeEnum triggerType, BigDecimal intensity) {
    this.triggerType = triggerType;
    this.intensity = intensity;
  }

  public TriggerProgressionRequest triggerType(TriggerTypeEnum triggerType) {
    this.triggerType = triggerType;
    return this;
  }

  /**
   * Триггер, запускающий скачок прогрессии
   * @return triggerType
   */
  @NotNull 
  @Schema(name = "trigger_type", example = "combat_berserk", description = "Триггер, запускающий скачок прогрессии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trigger_type")
  public TriggerTypeEnum getTriggerType() {
    return triggerType;
  }

  public void setTriggerType(TriggerTypeEnum triggerType) {
    this.triggerType = triggerType;
  }

  public TriggerProgressionRequest intensity(BigDecimal intensity) {
    this.intensity = intensity;
    return this;
  }

  /**
   * Интенсивность события (1-10)
   * minimum: 1
   * maximum: 10
   * @return intensity
   */
  @NotNull @Valid @DecimalMin(value = "1") @DecimalMax(value = "10") 
  @Schema(name = "intensity", example = "7", description = "Интенсивность события (1-10)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("intensity")
  public BigDecimal getIntensity() {
    return intensity;
  }

  public void setIntensity(BigDecimal intensity) {
    this.intensity = intensity;
  }

  public TriggerProgressionRequest durationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Продолжительность события в минутах
   * minimum: 1
   * @return durationMinutes
   */
  @Min(value = 1) 
  @Schema(name = "duration_minutes", example = "12", description = "Продолжительность события в минутах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_minutes")
  public @Nullable Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public TriggerProgressionRequest collateralDamage(@Nullable Integer collateralDamage) {
    this.collateralDamage = collateralDamage;
    return this;
  }

  /**
   * Количество жертв/пострадавших, влияющее на прогрессию
   * minimum: 0
   * @return collateralDamage
   */
  @Min(value = 0) 
  @Schema(name = "collateral_damage", example = "3", description = "Количество жертв/пострадавших, влияющее на прогрессию", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("collateral_damage")
  public @Nullable Integer getCollateralDamage() {
    return collateralDamage;
  }

  public void setCollateralDamage(@Nullable Integer collateralDamage) {
    this.collateralDamage = collateralDamage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerProgressionRequest triggerProgressionRequest = (TriggerProgressionRequest) o;
    return Objects.equals(this.triggerType, triggerProgressionRequest.triggerType) &&
        Objects.equals(this.intensity, triggerProgressionRequest.intensity) &&
        Objects.equals(this.durationMinutes, triggerProgressionRequest.durationMinutes) &&
        Objects.equals(this.collateralDamage, triggerProgressionRequest.collateralDamage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(triggerType, intensity, durationMinutes, collateralDamage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerProgressionRequest {\n");
    sb.append("    triggerType: ").append(toIndentedString(triggerType)).append("\n");
    sb.append("    intensity: ").append(toIndentedString(intensity)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    collateralDamage: ").append(toIndentedString(collateralDamage)).append("\n");
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

