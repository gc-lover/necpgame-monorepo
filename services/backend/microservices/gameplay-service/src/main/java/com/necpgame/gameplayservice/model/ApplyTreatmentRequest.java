package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ApplyTreatmentRequest
 */


public class ApplyTreatmentRequest {

  /**
   * Выбранный протокол лечения
   */
  public enum TreatmentIdEnum {
    NEURAL_RESET("neural_reset"),
    
    PHARMACOLOGICAL("pharmacological"),
    
    CYBERWARE_ADJUSTMENT("cyberware_adjustment"),
    
    EMPATHY_BOOST("empathy_boost"),
    
    EXTREME_DETOX("extreme_detox");

    private final String value;

    TreatmentIdEnum(String value) {
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
    public static TreatmentIdEnum fromValue(String value) {
      for (TreatmentIdEnum b : TreatmentIdEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TreatmentIdEnum treatmentId;

  /**
   * Уровень медучреждения
   */
  public enum FacilityLevelEnum {
    STREET_CLINIC("street_clinic"),
    
    LICENSED_CLINIC("licensed_clinic"),
    
    HOSPITAL("hospital"),
    
    CORPORATE_CENTER("corporate_center");

    private final String value;

    FacilityLevelEnum(String value) {
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
    public static FacilityLevelEnum fromValue(String value) {
      for (FacilityLevelEnum b : FacilityLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private FacilityLevelEnum facilityLevel;

  private Integer expectedDurationHours;

  private @Nullable Boolean riskAcknowledged;

  private JsonNullable<Float> costOverride = JsonNullable.<Float>undefined();

  public ApplyTreatmentRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplyTreatmentRequest(TreatmentIdEnum treatmentId, FacilityLevelEnum facilityLevel, Integer expectedDurationHours) {
    this.treatmentId = treatmentId;
    this.facilityLevel = facilityLevel;
    this.expectedDurationHours = expectedDurationHours;
  }

  public ApplyTreatmentRequest treatmentId(TreatmentIdEnum treatmentId) {
    this.treatmentId = treatmentId;
    return this;
  }

  /**
   * Выбранный протокол лечения
   * @return treatmentId
   */
  @NotNull 
  @Schema(name = "treatment_id", example = "pharmacological", description = "Выбранный протокол лечения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("treatment_id")
  public TreatmentIdEnum getTreatmentId() {
    return treatmentId;
  }

  public void setTreatmentId(TreatmentIdEnum treatmentId) {
    this.treatmentId = treatmentId;
  }

  public ApplyTreatmentRequest facilityLevel(FacilityLevelEnum facilityLevel) {
    this.facilityLevel = facilityLevel;
    return this;
  }

  /**
   * Уровень медучреждения
   * @return facilityLevel
   */
  @NotNull 
  @Schema(name = "facility_level", example = "licensed_clinic", description = "Уровень медучреждения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("facility_level")
  public FacilityLevelEnum getFacilityLevel() {
    return facilityLevel;
  }

  public void setFacilityLevel(FacilityLevelEnum facilityLevel) {
    this.facilityLevel = facilityLevel;
  }

  public ApplyTreatmentRequest expectedDurationHours(Integer expectedDurationHours) {
    this.expectedDurationHours = expectedDurationHours;
    return this;
  }

  /**
   * Предполагаемая продолжительность лечения
   * minimum: 1
   * maximum: 720
   * @return expectedDurationHours
   */
  @NotNull @Min(value = 1) @Max(value = 720) 
  @Schema(name = "expected_duration_hours", example = "48", description = "Предполагаемая продолжительность лечения", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expected_duration_hours")
  public Integer getExpectedDurationHours() {
    return expectedDurationHours;
  }

  public void setExpectedDurationHours(Integer expectedDurationHours) {
    this.expectedDurationHours = expectedDurationHours;
  }

  public ApplyTreatmentRequest riskAcknowledged(@Nullable Boolean riskAcknowledged) {
    this.riskAcknowledged = riskAcknowledged;
    return this;
  }

  /**
   * Подтверждено ли согласие пациента с рисками
   * @return riskAcknowledged
   */
  
  @Schema(name = "risk_acknowledged", example = "true", description = "Подтверждено ли согласие пациента с рисками", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_acknowledged")
  public @Nullable Boolean getRiskAcknowledged() {
    return riskAcknowledged;
  }

  public void setRiskAcknowledged(@Nullable Boolean riskAcknowledged) {
    this.riskAcknowledged = riskAcknowledged;
  }

  public ApplyTreatmentRequest costOverride(Float costOverride) {
    this.costOverride = JsonNullable.of(costOverride);
    return this;
  }

  /**
   * Пользовательское значение стоимости лечения (если применяется скидка или льгота)
   * @return costOverride
   */
  
  @Schema(name = "cost_override", description = "Пользовательское значение стоимости лечения (если применяется скидка или льгота)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_override")
  public JsonNullable<Float> getCostOverride() {
    return costOverride;
  }

  public void setCostOverride(JsonNullable<Float> costOverride) {
    this.costOverride = costOverride;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplyTreatmentRequest applyTreatmentRequest = (ApplyTreatmentRequest) o;
    return Objects.equals(this.treatmentId, applyTreatmentRequest.treatmentId) &&
        Objects.equals(this.facilityLevel, applyTreatmentRequest.facilityLevel) &&
        Objects.equals(this.expectedDurationHours, applyTreatmentRequest.expectedDurationHours) &&
        Objects.equals(this.riskAcknowledged, applyTreatmentRequest.riskAcknowledged) &&
        equalsNullable(this.costOverride, applyTreatmentRequest.costOverride);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(treatmentId, facilityLevel, expectedDurationHours, riskAcknowledged, hashCodeNullable(costOverride));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplyTreatmentRequest {\n");
    sb.append("    treatmentId: ").append(toIndentedString(treatmentId)).append("\n");
    sb.append("    facilityLevel: ").append(toIndentedString(facilityLevel)).append("\n");
    sb.append("    expectedDurationHours: ").append(toIndentedString(expectedDurationHours)).append("\n");
    sb.append("    riskAcknowledged: ").append(toIndentedString(riskAcknowledged)).append("\n");
    sb.append("    costOverride: ").append(toIndentedString(costOverride)).append("\n");
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

