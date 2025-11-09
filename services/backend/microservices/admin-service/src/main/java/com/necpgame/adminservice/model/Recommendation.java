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
 * Recommendation
 */


public class Recommendation {

  private String id;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PriorityEnum priority;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    SECURITY("security"),
    
    PERFORMANCE("performance"),
    
    SCALABILITY("scalability"),
    
    MAINTAINABILITY("maintainability"),
    
    TESTING("testing"),
    
    DOCUMENTATION("documentation");

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

  private @Nullable String rationale;

  private @Nullable Integer estimatedEffort;

  /**
   * Ожидаемый impact
   */
  public enum ImpactEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high");

    private final String value;

    ImpactEnum(String value) {
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
    public static ImpactEnum fromValue(String value) {
      for (ImpactEnum b : ImpactEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ImpactEnum impact;

  @Valid
  private List<String> affectedComponents = new ArrayList<>();

  public Recommendation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Recommendation(String id, PriorityEnum priority, String description) {
    this.id = id;
    this.priority = priority;
    this.description = description;
  }

  public Recommendation id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "rec_001", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Recommendation priority(PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  @NotNull 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("priority")
  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  public Recommendation category(@Nullable CategoryEnum category) {
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

  public Recommendation description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", example = "Implement Redis caching for frequently accessed data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Recommendation rationale(@Nullable String rationale) {
    this.rationale = rationale;
    return this;
  }

  /**
   * Обоснование рекомендации
   * @return rationale
   */
  
  @Schema(name = "rationale", example = "Will reduce database load by 40% and improve response times", description = "Обоснование рекомендации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rationale")
  public @Nullable String getRationale() {
    return rationale;
  }

  public void setRationale(@Nullable String rationale) {
    this.rationale = rationale;
  }

  public Recommendation estimatedEffort(@Nullable Integer estimatedEffort) {
    this.estimatedEffort = estimatedEffort;
    return this;
  }

  /**
   * Оценка усилий (часы)
   * @return estimatedEffort
   */
  
  @Schema(name = "estimated_effort", example = "16", description = "Оценка усилий (часы)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_effort")
  public @Nullable Integer getEstimatedEffort() {
    return estimatedEffort;
  }

  public void setEstimatedEffort(@Nullable Integer estimatedEffort) {
    this.estimatedEffort = estimatedEffort;
  }

  public Recommendation impact(@Nullable ImpactEnum impact) {
    this.impact = impact;
    return this;
  }

  /**
   * Ожидаемый impact
   * @return impact
   */
  
  @Schema(name = "impact", description = "Ожидаемый impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact")
  public @Nullable ImpactEnum getImpact() {
    return impact;
  }

  public void setImpact(@Nullable ImpactEnum impact) {
    this.impact = impact;
  }

  public Recommendation affectedComponents(List<String> affectedComponents) {
    this.affectedComponents = affectedComponents;
    return this;
  }

  public Recommendation addAffectedComponentsItem(String affectedComponentsItem) {
    if (this.affectedComponents == null) {
      this.affectedComponents = new ArrayList<>();
    }
    this.affectedComponents.add(affectedComponentsItem);
    return this;
  }

  /**
   * Get affectedComponents
   * @return affectedComponents
   */
  
  @Schema(name = "affected_components", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_components")
  public List<String> getAffectedComponents() {
    return affectedComponents;
  }

  public void setAffectedComponents(List<String> affectedComponents) {
    this.affectedComponents = affectedComponents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Recommendation recommendation = (Recommendation) o;
    return Objects.equals(this.id, recommendation.id) &&
        Objects.equals(this.priority, recommendation.priority) &&
        Objects.equals(this.category, recommendation.category) &&
        Objects.equals(this.description, recommendation.description) &&
        Objects.equals(this.rationale, recommendation.rationale) &&
        Objects.equals(this.estimatedEffort, recommendation.estimatedEffort) &&
        Objects.equals(this.impact, recommendation.impact) &&
        Objects.equals(this.affectedComponents, recommendation.affectedComponents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, priority, category, description, rationale, estimatedEffort, impact, affectedComponents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Recommendation {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    rationale: ").append(toIndentedString(rationale)).append("\n");
    sb.append("    estimatedEffort: ").append(toIndentedString(estimatedEffort)).append("\n");
    sb.append("    impact: ").append(toIndentedString(impact)).append("\n");
    sb.append("    affectedComponents: ").append(toIndentedString(affectedComponents)).append("\n");
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

