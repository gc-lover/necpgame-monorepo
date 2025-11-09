package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Запрос на расчет лимита имплантов
 */

@Schema(name = "CalculateLimitRequest", description = "Запрос на расчет лимита имплантов")

public class CalculateLimitRequest {

  private @Nullable Integer classBonus;

  private @Nullable Integer progressionBonus;

  private @Nullable Float humanityLevel;

  public CalculateLimitRequest classBonus(@Nullable Integer classBonus) {
    this.classBonus = classBonus;
    return this;
  }

  /**
   * Бонус от класса
   * minimum: 0
   * @return classBonus
   */
  @Min(value = 0) 
  @Schema(name = "class_bonus", description = "Бонус от класса", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_bonus")
  public @Nullable Integer getClassBonus() {
    return classBonus;
  }

  public void setClassBonus(@Nullable Integer classBonus) {
    this.classBonus = classBonus;
  }

  public CalculateLimitRequest progressionBonus(@Nullable Integer progressionBonus) {
    this.progressionBonus = progressionBonus;
    return this;
  }

  /**
   * Бонус от прокачки
   * minimum: 0
   * @return progressionBonus
   */
  @Min(value = 0) 
  @Schema(name = "progression_bonus", description = "Бонус от прокачки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progression_bonus")
  public @Nullable Integer getProgressionBonus() {
    return progressionBonus;
  }

  public void setProgressionBonus(@Nullable Integer progressionBonus) {
    this.progressionBonus = progressionBonus;
  }

  public CalculateLimitRequest humanityLevel(@Nullable Float humanityLevel) {
    this.humanityLevel = humanityLevel;
    return this;
  }

  /**
   * Уровень человечности (0-100)
   * minimum: 0
   * maximum: 100
   * @return humanityLevel
   */
  @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "humanity_level", description = "Уровень человечности (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_level")
  public @Nullable Float getHumanityLevel() {
    return humanityLevel;
  }

  public void setHumanityLevel(@Nullable Float humanityLevel) {
    this.humanityLevel = humanityLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateLimitRequest calculateLimitRequest = (CalculateLimitRequest) o;
    return Objects.equals(this.classBonus, calculateLimitRequest.classBonus) &&
        Objects.equals(this.progressionBonus, calculateLimitRequest.progressionBonus) &&
        Objects.equals(this.humanityLevel, calculateLimitRequest.humanityLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classBonus, progressionBonus, humanityLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateLimitRequest {\n");
    sb.append("    classBonus: ").append(toIndentedString(classBonus)).append("\n");
    sb.append("    progressionBonus: ").append(toIndentedString(progressionBonus)).append("\n");
    sb.append("    humanityLevel: ").append(toIndentedString(humanityLevel)).append("\n");
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

