package com.necpgame.lootservice.model;

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
 * CurrencyExpectation
 */


public class CurrencyExpectation {

  private String type;

  private Float average;

  public CurrencyExpectation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CurrencyExpectation(String type, Float average) {
    this.type = type;
    this.average = average;
  }

  public CurrencyExpectation type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public CurrencyExpectation average(Float average) {
    this.average = average;
    return this;
  }

  /**
   * Get average
   * @return average
   */
  @NotNull 
  @Schema(name = "average", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("average")
  public Float getAverage() {
    return average;
  }

  public void setAverage(Float average) {
    this.average = average;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurrencyExpectation currencyExpectation = (CurrencyExpectation) o;
    return Objects.equals(this.type, currencyExpectation.type) &&
        Objects.equals(this.average, currencyExpectation.average);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, average);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurrencyExpectation {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    average: ").append(toIndentedString(average)).append("\n");
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

