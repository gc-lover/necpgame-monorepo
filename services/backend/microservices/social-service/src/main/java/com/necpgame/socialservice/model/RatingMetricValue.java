package com.necpgame.socialservice.model;

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
 * RatingMetricValue
 */


public class RatingMetricValue {

  private String code;

  private @Nullable String label;

  private Float normalizedValue;

  private @Nullable Float weightedContribution;

  private @Nullable Integer sampleSize;

  public RatingMetricValue() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingMetricValue(String code, Float normalizedValue) {
    this.code = code;
    this.normalizedValue = normalizedValue;
  }

  public RatingMetricValue code(String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  @NotNull 
  @Schema(name = "code", example = "completion_rate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public RatingMetricValue label(@Nullable String label) {
    this.label = label;
    return this;
  }

  /**
   * Get label
   * @return label
   */
  
  @Schema(name = "label", example = "Completion Rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("label")
  public @Nullable String getLabel() {
    return label;
  }

  public void setLabel(@Nullable String label) {
    this.label = label;
  }

  public RatingMetricValue normalizedValue(Float normalizedValue) {
    this.normalizedValue = normalizedValue;
    return this;
  }

  /**
   * Get normalizedValue
   * minimum: 0
   * maximum: 100
   * @return normalizedValue
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "normalizedValue", example = "84.2", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("normalizedValue")
  public Float getNormalizedValue() {
    return normalizedValue;
  }

  public void setNormalizedValue(Float normalizedValue) {
    this.normalizedValue = normalizedValue;
  }

  public RatingMetricValue weightedContribution(@Nullable Float weightedContribution) {
    this.weightedContribution = weightedContribution;
    return this;
  }

  /**
   * Get weightedContribution
   * @return weightedContribution
   */
  
  @Schema(name = "weightedContribution", example = "0.24", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weightedContribution")
  public @Nullable Float getWeightedContribution() {
    return weightedContribution;
  }

  public void setWeightedContribution(@Nullable Float weightedContribution) {
    this.weightedContribution = weightedContribution;
  }

  public RatingMetricValue sampleSize(@Nullable Integer sampleSize) {
    this.sampleSize = sampleSize;
    return this;
  }

  /**
   * Get sampleSize
   * minimum: 0
   * @return sampleSize
   */
  @Min(value = 0) 
  @Schema(name = "sampleSize", example = "120", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sampleSize")
  public @Nullable Integer getSampleSize() {
    return sampleSize;
  }

  public void setSampleSize(@Nullable Integer sampleSize) {
    this.sampleSize = sampleSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingMetricValue ratingMetricValue = (RatingMetricValue) o;
    return Objects.equals(this.code, ratingMetricValue.code) &&
        Objects.equals(this.label, ratingMetricValue.label) &&
        Objects.equals(this.normalizedValue, ratingMetricValue.normalizedValue) &&
        Objects.equals(this.weightedContribution, ratingMetricValue.weightedContribution) &&
        Objects.equals(this.sampleSize, ratingMetricValue.sampleSize);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, label, normalizedValue, weightedContribution, sampleSize);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingMetricValue {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    normalizedValue: ").append(toIndentedString(normalizedValue)).append("\n");
    sb.append("    weightedContribution: ").append(toIndentedString(weightedContribution)).append("\n");
    sb.append("    sampleSize: ").append(toIndentedString(sampleSize)).append("\n");
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

