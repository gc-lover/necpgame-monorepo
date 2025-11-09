package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * Информация о прогрессии киберпсихоза
 */

@Schema(name = "ProgressionInfo", description = "Информация о прогрессии киберпсихоза")

public class ProgressionInfo {

  private Float currentProgressionRate;

  @Valid
  private List<Object> factors = new ArrayList<>();

  @Valid
  private List<Object> triggers = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> nextCheckTime = JsonNullable.<OffsetDateTime>undefined();

  public ProgressionInfo() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ProgressionInfo(Float currentProgressionRate, List<Object> factors, List<Object> triggers) {
    this.currentProgressionRate = currentProgressionRate;
    this.factors = factors;
    this.triggers = triggers;
  }

  public ProgressionInfo currentProgressionRate(Float currentProgressionRate) {
    this.currentProgressionRate = currentProgressionRate;
    return this;
  }

  /**
   * Текущая скорость прогрессии
   * @return currentProgressionRate
   */
  @NotNull 
  @Schema(name = "current_progression_rate", description = "Текущая скорость прогрессии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("current_progression_rate")
  public Float getCurrentProgressionRate() {
    return currentProgressionRate;
  }

  public void setCurrentProgressionRate(Float currentProgressionRate) {
    this.currentProgressionRate = currentProgressionRate;
  }

  public ProgressionInfo factors(List<Object> factors) {
    this.factors = factors;
    return this;
  }

  public ProgressionInfo addFactorsItem(Object factorsItem) {
    if (this.factors == null) {
      this.factors = new ArrayList<>();
    }
    this.factors.add(factorsItem);
    return this;
  }

  /**
   * Активные факторы прогрессии
   * @return factors
   */
  @NotNull 
  @Schema(name = "factors", description = "Активные факторы прогрессии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factors")
  public List<Object> getFactors() {
    return factors;
  }

  public void setFactors(List<Object> factors) {
    this.factors = factors;
  }

  public ProgressionInfo triggers(List<Object> triggers) {
    this.triggers = triggers;
    return this;
  }

  public ProgressionInfo addTriggersItem(Object triggersItem) {
    if (this.triggers == null) {
      this.triggers = new ArrayList<>();
    }
    this.triggers.add(triggersItem);
    return this;
  }

  /**
   * Активные триггеры прогрессии
   * @return triggers
   */
  @NotNull 
  @Schema(name = "triggers", description = "Активные триггеры прогрессии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("triggers")
  public List<Object> getTriggers() {
    return triggers;
  }

  public void setTriggers(List<Object> triggers) {
    this.triggers = triggers;
  }

  public ProgressionInfo nextCheckTime(OffsetDateTime nextCheckTime) {
    this.nextCheckTime = JsonNullable.of(nextCheckTime);
    return this;
  }

  /**
   * Время следующей проверки прогрессии
   * @return nextCheckTime
   */
  @Valid 
  @Schema(name = "next_check_time", description = "Время следующей проверки прогрессии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("next_check_time")
  public JsonNullable<OffsetDateTime> getNextCheckTime() {
    return nextCheckTime;
  }

  public void setNextCheckTime(JsonNullable<OffsetDateTime> nextCheckTime) {
    this.nextCheckTime = nextCheckTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProgressionInfo progressionInfo = (ProgressionInfo) o;
    return Objects.equals(this.currentProgressionRate, progressionInfo.currentProgressionRate) &&
        Objects.equals(this.factors, progressionInfo.factors) &&
        Objects.equals(this.triggers, progressionInfo.triggers) &&
        equalsNullable(this.nextCheckTime, progressionInfo.nextCheckTime);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentProgressionRate, factors, triggers, hashCodeNullable(nextCheckTime));
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
    sb.append("class ProgressionInfo {\n");
    sb.append("    currentProgressionRate: ").append(toIndentedString(currentProgressionRate)).append("\n");
    sb.append("    factors: ").append(toIndentedString(factors)).append("\n");
    sb.append("    triggers: ").append(toIndentedString(triggers)).append("\n");
    sb.append("    nextCheckTime: ").append(toIndentedString(nextCheckTime)).append("\n");
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

