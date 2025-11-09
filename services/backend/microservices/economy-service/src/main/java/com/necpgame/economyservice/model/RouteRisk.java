package com.necpgame.economyservice.model;

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
 * RouteRisk
 */


public class RouteRisk {

  /**
   * Gets or Sets riskType
   */
  public enum RiskTypeEnum {
    AMBUSH("AMBUSH"),
    
    WEATHER("WEATHER"),
    
    MECHANICAL("MECHANICAL"),
    
    ACCIDENT("ACCIDENT"),
    
    CHECKPOINT("CHECKPOINT");

    private final String value;

    RiskTypeEnum(String value) {
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
    public static RiskTypeEnum fromValue(String value) {
      for (RiskTypeEnum b : RiskTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RiskTypeEnum riskType;

  private @Nullable Float probability;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH"),
    
    CRITICAL("CRITICAL");

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

  private @Nullable String description;

  public RouteRisk riskType(@Nullable RiskTypeEnum riskType) {
    this.riskType = riskType;
    return this;
  }

  /**
   * Get riskType
   * @return riskType
   */
  
  @Schema(name = "risk_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_type")
  public @Nullable RiskTypeEnum getRiskType() {
    return riskType;
  }

  public void setRiskType(@Nullable RiskTypeEnum riskType) {
    this.riskType = riskType;
  }

  public RouteRisk probability(@Nullable Float probability) {
    this.probability = probability;
    return this;
  }

  /**
   * Get probability
   * @return probability
   */
  
  @Schema(name = "probability", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("probability")
  public @Nullable Float getProbability() {
    return probability;
  }

  public void setProbability(@Nullable Float probability) {
    this.probability = probability;
  }

  public RouteRisk severity(@Nullable SeverityEnum severity) {
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

  public RouteRisk description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RouteRisk routeRisk = (RouteRisk) o;
    return Objects.equals(this.riskType, routeRisk.riskType) &&
        Objects.equals(this.probability, routeRisk.probability) &&
        Objects.equals(this.severity, routeRisk.severity) &&
        Objects.equals(this.description, routeRisk.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(riskType, probability, severity, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RouteRisk {\n");
    sb.append("    riskType: ").append(toIndentedString(riskType)).append("\n");
    sb.append("    probability: ").append(toIndentedString(probability)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

