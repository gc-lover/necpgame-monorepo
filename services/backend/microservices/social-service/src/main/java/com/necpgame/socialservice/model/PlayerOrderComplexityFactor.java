package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * PlayerOrderComplexityFactor
 */


public class PlayerOrderComplexityFactor {

  private String name;

  private BigDecimal weight;

  private BigDecimal value;

  private @Nullable String source;

  public PlayerOrderComplexityFactor() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderComplexityFactor(String name, BigDecimal weight, BigDecimal value) {
    this.name = name;
    this.weight = weight;
    this.value = value;
  }

  public PlayerOrderComplexityFactor name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Название фактора (зона, этап, требуемый навык).
   * @return name
   */
  @NotNull 
  @Schema(name = "name", description = "Название фактора (зона, этап, требуемый навык).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public PlayerOrderComplexityFactor weight(BigDecimal weight) {
    this.weight = weight;
    return this;
  }

  /**
   * Вес фактора при расчёте сложности.
   * minimum: 0
   * @return weight
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "weight", description = "Вес фактора при расчёте сложности.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("weight")
  public BigDecimal getWeight() {
    return weight;
  }

  public void setWeight(BigDecimal weight) {
    this.weight = weight;
  }

  public PlayerOrderComplexityFactor value(BigDecimal value) {
    this.value = value;
    return this;
  }

  /**
   * Нормализованное значение фактора.
   * minimum: 0
   * @return value
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "value", description = "Нормализованное значение фактора.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("value")
  public BigDecimal getValue() {
    return value;
  }

  public void setValue(BigDecimal value) {
    this.value = value;
  }

  public PlayerOrderComplexityFactor source(@Nullable String source) {
    this.source = source;
    return this;
  }

  /**
   * Источник данных (`world-service`, `factions-service`, `content-service`).
   * @return source
   */
  
  @Schema(name = "source", description = "Источник данных (`world-service`, `factions-service`, `content-service`).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable String getSource() {
    return source;
  }

  public void setSource(@Nullable String source) {
    this.source = source;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderComplexityFactor playerOrderComplexityFactor = (PlayerOrderComplexityFactor) o;
    return Objects.equals(this.name, playerOrderComplexityFactor.name) &&
        Objects.equals(this.weight, playerOrderComplexityFactor.weight) &&
        Objects.equals(this.value, playerOrderComplexityFactor.value) &&
        Objects.equals(this.source, playerOrderComplexityFactor.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, weight, value, source);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderComplexityFactor {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    weight: ").append(toIndentedString(weight)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
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

