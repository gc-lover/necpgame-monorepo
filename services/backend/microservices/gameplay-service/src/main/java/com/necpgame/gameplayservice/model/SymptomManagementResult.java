package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Результат управления симптомами
 */

@Schema(name = "SymptomManagementResult", description = "Результат управления симптомами")

public class SymptomManagementResult {

  @Valid
  private List<UUID> symptomsAffected = new ArrayList<>();

  private Float effectiveness;

  private Float duration;

  private Float cost;

  public SymptomManagementResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SymptomManagementResult(List<UUID> symptomsAffected, Float effectiveness, Float duration, Float cost) {
    this.symptomsAffected = symptomsAffected;
    this.effectiveness = effectiveness;
    this.duration = duration;
    this.cost = cost;
  }

  public SymptomManagementResult symptomsAffected(List<UUID> symptomsAffected) {
    this.symptomsAffected = symptomsAffected;
    return this;
  }

  public SymptomManagementResult addSymptomsAffectedItem(UUID symptomsAffectedItem) {
    if (this.symptomsAffected == null) {
      this.symptomsAffected = new ArrayList<>();
    }
    this.symptomsAffected.add(symptomsAffectedItem);
    return this;
  }

  /**
   * Идентификаторы затронутых симптомов
   * @return symptomsAffected
   */
  @NotNull @Valid 
  @Schema(name = "symptoms_affected", description = "Идентификаторы затронутых симптомов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("symptoms_affected")
  public List<UUID> getSymptomsAffected() {
    return symptomsAffected;
  }

  public void setSymptomsAffected(List<UUID> symptomsAffected) {
    this.symptomsAffected = symptomsAffected;
  }

  public SymptomManagementResult effectiveness(Float effectiveness) {
    this.effectiveness = effectiveness;
    return this;
  }

  /**
   * Эффективность управления симптомами
   * minimum: 0
   * maximum: 100
   * @return effectiveness
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "effectiveness", description = "Эффективность управления симптомами", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effectiveness")
  public Float getEffectiveness() {
    return effectiveness;
  }

  public void setEffectiveness(Float effectiveness) {
    this.effectiveness = effectiveness;
  }

  public SymptomManagementResult duration(Float duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Длительность эффекта в секундах
   * minimum: 0
   * @return duration
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "duration", description = "Длительность эффекта в секундах", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("duration")
  public Float getDuration() {
    return duration;
  }

  public void setDuration(Float duration) {
    this.duration = duration;
  }

  public SymptomManagementResult cost(Float cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Стоимость управления симптомами
   * minimum: 0
   * @return cost
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "cost", description = "Стоимость управления симптомами", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cost")
  public Float getCost() {
    return cost;
  }

  public void setCost(Float cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SymptomManagementResult symptomManagementResult = (SymptomManagementResult) o;
    return Objects.equals(this.symptomsAffected, symptomManagementResult.symptomsAffected) &&
        Objects.equals(this.effectiveness, symptomManagementResult.effectiveness) &&
        Objects.equals(this.duration, symptomManagementResult.duration) &&
        Objects.equals(this.cost, symptomManagementResult.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(symptomsAffected, effectiveness, duration, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SymptomManagementResult {\n");
    sb.append("    symptomsAffected: ").append(toIndentedString(symptomsAffected)).append("\n");
    sb.append("    effectiveness: ").append(toIndentedString(effectiveness)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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

