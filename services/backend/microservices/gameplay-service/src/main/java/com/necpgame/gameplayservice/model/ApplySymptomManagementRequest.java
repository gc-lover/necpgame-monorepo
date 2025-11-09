package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ApplySymptomManagementRequest
 */


public class ApplySymptomManagementRequest {

  @Valid
  private List<String> symptomIds = new ArrayList<>();

  /**
   * Выбранная стратегия управления
   */
  public enum ActionEnum {
    MEDICATION("medication"),
    
    COGNITIVE_THERAPY("cognitive_therapy"),
    
    RESTRAINT_PROTOCOL("restraint_protocol"),
    
    SENSORY_ISOLATION("sensory_isolation"),
    
    EMERGENCY_SEDATION("emergency_sedation");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;

  private JsonNullable<Float> medicationDoseMg = JsonNullable.<Float>undefined();

  private @Nullable Integer sessionLengthMinutes;

  public ApplySymptomManagementRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ApplySymptomManagementRequest(List<String> symptomIds, ActionEnum action) {
    this.symptomIds = symptomIds;
    this.action = action;
  }

  public ApplySymptomManagementRequest symptomIds(List<String> symptomIds) {
    this.symptomIds = symptomIds;
    return this;
  }

  public ApplySymptomManagementRequest addSymptomIdsItem(String symptomIdsItem) {
    if (this.symptomIds == null) {
      this.symptomIds = new ArrayList<>();
    }
    this.symptomIds.add(symptomIdsItem);
    return this;
  }

  /**
   * Идентификаторы симптомов, над которыми проводится работа
   * @return symptomIds
   */
  @NotNull @Size(min = 1) 
  @Schema(name = "symptom_ids", example = "[\"symptom-aggression\",\"symptom-hallucination\"]", description = "Идентификаторы симптомов, над которыми проводится работа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("symptom_ids")
  public List<String> getSymptomIds() {
    return symptomIds;
  }

  public void setSymptomIds(List<String> symptomIds) {
    this.symptomIds = symptomIds;
  }

  public ApplySymptomManagementRequest action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Выбранная стратегия управления
   * @return action
   */
  @NotNull 
  @Schema(name = "action", example = "cognitive_therapy", description = "Выбранная стратегия управления", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public ApplySymptomManagementRequest medicationDoseMg(Float medicationDoseMg) {
    this.medicationDoseMg = JsonNullable.of(medicationDoseMg);
    return this;
  }

  /**
   * Доза медикамента в мг, если используется фармакология
   * @return medicationDoseMg
   */
  
  @Schema(name = "medication_dose_mg", description = "Доза медикамента в мг, если используется фармакология", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medication_dose_mg")
  public JsonNullable<Float> getMedicationDoseMg() {
    return medicationDoseMg;
  }

  public void setMedicationDoseMg(JsonNullable<Float> medicationDoseMg) {
    this.medicationDoseMg = medicationDoseMg;
  }

  public ApplySymptomManagementRequest sessionLengthMinutes(@Nullable Integer sessionLengthMinutes) {
    this.sessionLengthMinutes = sessionLengthMinutes;
    return this;
  }

  /**
   * Продолжительность сессии управления симптомами
   * minimum: 15
   * maximum: 480
   * @return sessionLengthMinutes
   */
  @Min(value = 15) @Max(value = 480) 
  @Schema(name = "session_length_minutes", example = "90", description = "Продолжительность сессии управления симптомами", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_length_minutes")
  public @Nullable Integer getSessionLengthMinutes() {
    return sessionLengthMinutes;
  }

  public void setSessionLengthMinutes(@Nullable Integer sessionLengthMinutes) {
    this.sessionLengthMinutes = sessionLengthMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplySymptomManagementRequest applySymptomManagementRequest = (ApplySymptomManagementRequest) o;
    return Objects.equals(this.symptomIds, applySymptomManagementRequest.symptomIds) &&
        Objects.equals(this.action, applySymptomManagementRequest.action) &&
        equalsNullable(this.medicationDoseMg, applySymptomManagementRequest.medicationDoseMg) &&
        Objects.equals(this.sessionLengthMinutes, applySymptomManagementRequest.sessionLengthMinutes);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(symptomIds, action, hashCodeNullable(medicationDoseMg), sessionLengthMinutes);
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
    sb.append("class ApplySymptomManagementRequest {\n");
    sb.append("    symptomIds: ").append(toIndentedString(symptomIds)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    medicationDoseMg: ").append(toIndentedString(medicationDoseMg)).append("\n");
    sb.append("    sessionLengthMinutes: ").append(toIndentedString(sessionLengthMinutes)).append("\n");
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

