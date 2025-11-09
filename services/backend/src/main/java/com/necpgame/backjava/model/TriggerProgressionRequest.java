package com.necpgame.backjava.model;

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
 * Р—Р°РїСЂРѕСЃ РЅР° С‚СЂРёРіРіРµСЂ РїСЂРѕРіСЂРµСЃСЃРёРё
 */

@Schema(name = "TriggerProgressionRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° С‚СЂРёРіРіРµСЂ РїСЂРѕРіСЂРµСЃСЃРёРё")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class TriggerProgressionRequest {

  /**
   * РўРёРї С‚СЂРёРіРіРµСЂР°
   */
  public enum TriggerTypeEnum {
    CRITICAL_MOMENT("critical_moment"),
    
    STRESS_ACCUMULATION("stress_accumulation"),
    
    TEMPORARY_SURGE("temporary_surge");

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

  private Float intensity;

  public TriggerProgressionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TriggerProgressionRequest(TriggerTypeEnum triggerType, Float intensity) {
    this.triggerType = triggerType;
    this.intensity = intensity;
  }

  public TriggerProgressionRequest triggerType(TriggerTypeEnum triggerType) {
    this.triggerType = triggerType;
    return this;
  }

  /**
   * РўРёРї С‚СЂРёРіРіРµСЂР°
   * @return triggerType
   */
  @NotNull 
  @Schema(name = "trigger_type", description = "РўРёРї С‚СЂРёРіРіРµСЂР°", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trigger_type")
  public TriggerTypeEnum getTriggerType() {
    return triggerType;
  }

  public void setTriggerType(TriggerTypeEnum triggerType) {
    this.triggerType = triggerType;
  }

  public TriggerProgressionRequest intensity(Float intensity) {
    this.intensity = intensity;
    return this;
  }

  /**
   * РРЅС‚РµРЅСЃРёРІРЅРѕСЃС‚СЊ С‚СЂРёРіРіРµСЂР° (0-100%)
   * minimum: 0
   * maximum: 100
   * @return intensity
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "intensity", description = "РРЅС‚РµРЅСЃРёРІРЅРѕСЃС‚СЊ С‚СЂРёРіРіРµСЂР° (0-100%)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("intensity")
  public Float getIntensity() {
    return intensity;
  }

  public void setIntensity(Float intensity) {
    this.intensity = intensity;
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
        Objects.equals(this.intensity, triggerProgressionRequest.intensity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(triggerType, intensity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerProgressionRequest {\n");
    sb.append("    triggerType: ").append(toIndentedString(triggerType)).append("\n");
    sb.append("    intensity: ").append(toIndentedString(intensity)).append("\n");
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

