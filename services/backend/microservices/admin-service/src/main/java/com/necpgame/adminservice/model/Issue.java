package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Issue
 */


public class Issue {

  private String id;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
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

  private SeverityEnum severity;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    SECURITY("security"),
    
    PERFORMANCE("performance"),
    
    SCALABILITY("scalability"),
    
    MAINTAINABILITY("maintainability"),
    
    BUG("bug");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CategoryEnum category;

  private String description;

  @Valid
  private List<String> affectedEndpoints = new ArrayList<>();

  private @Nullable Integer estimatedFixTime;

  private @Nullable String recommendation;

  public Issue() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Issue(String id, SeverityEnum severity, String description) {
    this.id = id;
    this.severity = severity;
    this.description = description;
  }

  public Issue id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "issue_001", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Issue severity(SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  @NotNull 
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("severity")
  public SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(SeverityEnum severity) {
    this.severity = severity;
  }

  public Issue category(@Nullable CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(@Nullable CategoryEnum category) {
    this.category = category;
  }

  public Issue description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Database queries not optimized for large datasets", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Issue affectedEndpoints(List<String> affectedEndpoints) {
    this.affectedEndpoints = affectedEndpoints;
    return this;
  }

  public Issue addAffectedEndpointsItem(String affectedEndpointsItem) {
    if (this.affectedEndpoints == null) {
      this.affectedEndpoints = new ArrayList<>();
    }
    this.affectedEndpoints.add(affectedEndpointsItem);
    return this;
  }

  /**
   * Get affectedEndpoints
   * @return affectedEndpoints
   */
  
  @Schema(name = "affected_endpoints", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_endpoints")
  public List<String> getAffectedEndpoints() {
    return affectedEndpoints;
  }

  public void setAffectedEndpoints(List<String> affectedEndpoints) {
    this.affectedEndpoints = affectedEndpoints;
  }

  public Issue estimatedFixTime(@Nullable Integer estimatedFixTime) {
    this.estimatedFixTime = estimatedFixTime;
    return this;
  }

  /**
   * Время на исправление (часы)
   * @return estimatedFixTime
   */
  
  @Schema(name = "estimated_fix_time", example = "8", description = "Время на исправление (часы)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_fix_time")
  public @Nullable Integer getEstimatedFixTime() {
    return estimatedFixTime;
  }

  public void setEstimatedFixTime(@Nullable Integer estimatedFixTime) {
    this.estimatedFixTime = estimatedFixTime;
  }

  public Issue recommendation(@Nullable String recommendation) {
    this.recommendation = recommendation;
    return this;
  }

  /**
   * Рекомендация по исправлению
   * @return recommendation
   */
  
  @Schema(name = "recommendation", example = "Implement database indexing and query optimization", description = "Рекомендация по исправлению", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendation")
  public @Nullable String getRecommendation() {
    return recommendation;
  }

  public void setRecommendation(@Nullable String recommendation) {
    this.recommendation = recommendation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Issue issue = (Issue) o;
    return Objects.equals(this.id, issue.id) &&
        Objects.equals(this.severity, issue.severity) &&
        Objects.equals(this.category, issue.category) &&
        Objects.equals(this.description, issue.description) &&
        Objects.equals(this.affectedEndpoints, issue.affectedEndpoints) &&
        Objects.equals(this.estimatedFixTime, issue.estimatedFixTime) &&
        Objects.equals(this.recommendation, issue.recommendation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, severity, category, description, affectedEndpoints, estimatedFixTime, recommendation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Issue {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    affectedEndpoints: ").append(toIndentedString(affectedEndpoints)).append("\n");
    sb.append("    estimatedFixTime: ").append(toIndentedString(estimatedFixTime)).append("\n");
    sb.append("    recommendation: ").append(toIndentedString(recommendation)).append("\n");
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

