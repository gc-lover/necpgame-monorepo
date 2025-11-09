package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InfrastructureAlert
 */


public class InfrastructureAlert {

  private @Nullable String code;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    WARNING("warning"),
    
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

  private @Nullable String message;

  private @Nullable String impact;

  private @Nullable String recommendedAction;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime detectedAt;

  public InfrastructureAlert code(@Nullable String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  
  @Schema(name = "code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("code")
  public @Nullable String getCode() {
    return code;
  }

  public void setCode(@Nullable String code) {
    this.code = code;
  }

  public InfrastructureAlert severity(@Nullable SeverityEnum severity) {
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

  public InfrastructureAlert message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public InfrastructureAlert impact(@Nullable String impact) {
    this.impact = impact;
    return this;
  }

  /**
   * Get impact
   * @return impact
   */
  
  @Schema(name = "impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact")
  public @Nullable String getImpact() {
    return impact;
  }

  public void setImpact(@Nullable String impact) {
    this.impact = impact;
  }

  public InfrastructureAlert recommendedAction(@Nullable String recommendedAction) {
    this.recommendedAction = recommendedAction;
    return this;
  }

  /**
   * Get recommendedAction
   * @return recommendedAction
   */
  
  @Schema(name = "recommendedAction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedAction")
  public @Nullable String getRecommendedAction() {
    return recommendedAction;
  }

  public void setRecommendedAction(@Nullable String recommendedAction) {
    this.recommendedAction = recommendedAction;
  }

  public InfrastructureAlert detectedAt(@Nullable OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
    return this;
  }

  /**
   * Get detectedAt
   * @return detectedAt
   */
  @Valid 
  @Schema(name = "detectedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detectedAt")
  public @Nullable OffsetDateTime getDetectedAt() {
    return detectedAt;
  }

  public void setDetectedAt(@Nullable OffsetDateTime detectedAt) {
    this.detectedAt = detectedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InfrastructureAlert infrastructureAlert = (InfrastructureAlert) o;
    return Objects.equals(this.code, infrastructureAlert.code) &&
        Objects.equals(this.severity, infrastructureAlert.severity) &&
        Objects.equals(this.message, infrastructureAlert.message) &&
        Objects.equals(this.impact, infrastructureAlert.impact) &&
        Objects.equals(this.recommendedAction, infrastructureAlert.recommendedAction) &&
        Objects.equals(this.detectedAt, infrastructureAlert.detectedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, severity, message, impact, recommendedAction, detectedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InfrastructureAlert {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    impact: ").append(toIndentedString(impact)).append("\n");
    sb.append("    recommendedAction: ").append(toIndentedString(recommendedAction)).append("\n");
    sb.append("    detectedAt: ").append(toIndentedString(detectedAt)).append("\n");
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

