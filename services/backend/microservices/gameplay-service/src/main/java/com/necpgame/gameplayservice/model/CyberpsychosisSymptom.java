package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CyberpsychosisSymptomEffectsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CyberpsychosisSymptom
 */


public class CyberpsychosisSymptom {

  private @Nullable String symptomName;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    MILD("mild"),
    
    MODERATE("moderate"),
    
    SEVERE("severe"),
    
    CRITICAL("critical");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SeverityEnum severity;

  @Valid
  private List<@Valid CyberpsychosisSymptomEffectsInner> effects = new ArrayList<>();

  public CyberpsychosisSymptom symptomName(@Nullable String symptomName) {
    this.symptomName = symptomName;
    return this;
  }

  /**
   * Get symptomName
   * @return symptomName
   */
  
  @Schema(name = "symptom_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("symptom_name")
  public @Nullable String getSymptomName() {
    return symptomName;
  }

  public void setSymptomName(@Nullable String symptomName) {
    this.symptomName = symptomName;
  }

  public CyberpsychosisSymptom severity(@Nullable SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable SeverityEnum severity) {
    this.severity = severity;
  }

  public CyberpsychosisSymptom effects(List<@Valid CyberpsychosisSymptomEffectsInner> effects) {
    this.effects = effects;
    return this;
  }

  public CyberpsychosisSymptom addEffectsItem(CyberpsychosisSymptomEffectsInner effectsItem) {
    if (this.effects == null) {
      this.effects = new ArrayList<>();
    }
    this.effects.add(effectsItem);
    return this;
  }

  /**
   * Эффекты симптома
   * @return effects
   */
  @Valid 
  @Schema(name = "effects", description = "Эффекты симптома", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("effects")
  public List<@Valid CyberpsychosisSymptomEffectsInner> getEffects() {
    return effects;
  }

  public void setEffects(List<@Valid CyberpsychosisSymptomEffectsInner> effects) {
    this.effects = effects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CyberpsychosisSymptom cyberpsychosisSymptom = (CyberpsychosisSymptom) o;
    return Objects.equals(this.symptomName, cyberpsychosisSymptom.symptomName) &&
        Objects.equals(this.severity, cyberpsychosisSymptom.severity) &&
        Objects.equals(this.effects, cyberpsychosisSymptom.effects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symptomName, severity, effects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CyberpsychosisSymptom {\n");
    sb.append("    symptomName: ").append(toIndentedString(symptomName)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    effects: ").append(toIndentedString(effects)).append("\n");
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

