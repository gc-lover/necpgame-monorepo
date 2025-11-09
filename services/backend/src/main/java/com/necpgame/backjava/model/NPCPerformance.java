package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * NPCPerformance
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class NPCPerformance {

  private @Nullable UUID hireId;

  private @Nullable Integer tasksCompleted;

  private @Nullable Integer tasksFailed;

  private @Nullable Float successRate;

  private @Nullable Integer valueGenerated;

  private @Nullable Integer costTotal;

  private @Nullable Float roi;

  @Valid
  private List<Object> loyaltyChanges = new ArrayList<>();

  /**
   * Gets or Sets performanceTrend
   */
  public enum PerformanceTrendEnum {
    IMPROVING("IMPROVING"),
    
    STABLE("STABLE"),
    
    DECLINING("DECLINING");

    private final String value;

    PerformanceTrendEnum(String value) {
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
    public static PerformanceTrendEnum fromValue(String value) {
      for (PerformanceTrendEnum b : PerformanceTrendEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable PerformanceTrendEnum performanceTrend;

  public NPCPerformance hireId(@Nullable UUID hireId) {
    this.hireId = hireId;
    return this;
  }

  /**
   * Get hireId
   * @return hireId
   */
  @Valid 
  @Schema(name = "hire_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hire_id")
  public @Nullable UUID getHireId() {
    return hireId;
  }

  public void setHireId(@Nullable UUID hireId) {
    this.hireId = hireId;
  }

  public NPCPerformance tasksCompleted(@Nullable Integer tasksCompleted) {
    this.tasksCompleted = tasksCompleted;
    return this;
  }

  /**
   * Get tasksCompleted
   * @return tasksCompleted
   */
  
  @Schema(name = "tasks_completed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tasks_completed")
  public @Nullable Integer getTasksCompleted() {
    return tasksCompleted;
  }

  public void setTasksCompleted(@Nullable Integer tasksCompleted) {
    this.tasksCompleted = tasksCompleted;
  }

  public NPCPerformance tasksFailed(@Nullable Integer tasksFailed) {
    this.tasksFailed = tasksFailed;
    return this;
  }

  /**
   * Get tasksFailed
   * @return tasksFailed
   */
  
  @Schema(name = "tasks_failed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tasks_failed")
  public @Nullable Integer getTasksFailed() {
    return tasksFailed;
  }

  public void setTasksFailed(@Nullable Integer tasksFailed) {
    this.tasksFailed = tasksFailed;
  }

  public NPCPerformance successRate(@Nullable Float successRate) {
    this.successRate = successRate;
    return this;
  }

  /**
   * Get successRate
   * @return successRate
   */
  
  @Schema(name = "success_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success_rate")
  public @Nullable Float getSuccessRate() {
    return successRate;
  }

  public void setSuccessRate(@Nullable Float successRate) {
    this.successRate = successRate;
  }

  public NPCPerformance valueGenerated(@Nullable Integer valueGenerated) {
    this.valueGenerated = valueGenerated;
    return this;
  }

  /**
   * Экономическая ценность созданная NPC
   * @return valueGenerated
   */
  
  @Schema(name = "value_generated", description = "Экономическая ценность созданная NPC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value_generated")
  public @Nullable Integer getValueGenerated() {
    return valueGenerated;
  }

  public void setValueGenerated(@Nullable Integer valueGenerated) {
    this.valueGenerated = valueGenerated;
  }

  public NPCPerformance costTotal(@Nullable Integer costTotal) {
    this.costTotal = costTotal;
    return this;
  }

  /**
   * Get costTotal
   * @return costTotal
   */
  
  @Schema(name = "cost_total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost_total")
  public @Nullable Integer getCostTotal() {
    return costTotal;
  }

  public void setCostTotal(@Nullable Integer costTotal) {
    this.costTotal = costTotal;
  }

  public NPCPerformance roi(@Nullable Float roi) {
    this.roi = roi;
    return this;
  }

  /**
   * Get roi
   * @return roi
   */
  
  @Schema(name = "roi", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roi")
  public @Nullable Float getRoi() {
    return roi;
  }

  public void setRoi(@Nullable Float roi) {
    this.roi = roi;
  }

  public NPCPerformance loyaltyChanges(List<Object> loyaltyChanges) {
    this.loyaltyChanges = loyaltyChanges;
    return this;
  }

  public NPCPerformance addLoyaltyChangesItem(Object loyaltyChangesItem) {
    if (this.loyaltyChanges == null) {
      this.loyaltyChanges = new ArrayList<>();
    }
    this.loyaltyChanges.add(loyaltyChangesItem);
    return this;
  }

  /**
   * Get loyaltyChanges
   * @return loyaltyChanges
   */
  
  @Schema(name = "loyalty_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loyalty_changes")
  public List<Object> getLoyaltyChanges() {
    return loyaltyChanges;
  }

  public void setLoyaltyChanges(List<Object> loyaltyChanges) {
    this.loyaltyChanges = loyaltyChanges;
  }

  public NPCPerformance performanceTrend(@Nullable PerformanceTrendEnum performanceTrend) {
    this.performanceTrend = performanceTrend;
    return this;
  }

  /**
   * Get performanceTrend
   * @return performanceTrend
   */
  
  @Schema(name = "performance_trend", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performance_trend")
  public @Nullable PerformanceTrendEnum getPerformanceTrend() {
    return performanceTrend;
  }

  public void setPerformanceTrend(@Nullable PerformanceTrendEnum performanceTrend) {
    this.performanceTrend = performanceTrend;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NPCPerformance npCPerformance = (NPCPerformance) o;
    return Objects.equals(this.hireId, npCPerformance.hireId) &&
        Objects.equals(this.tasksCompleted, npCPerformance.tasksCompleted) &&
        Objects.equals(this.tasksFailed, npCPerformance.tasksFailed) &&
        Objects.equals(this.successRate, npCPerformance.successRate) &&
        Objects.equals(this.valueGenerated, npCPerformance.valueGenerated) &&
        Objects.equals(this.costTotal, npCPerformance.costTotal) &&
        Objects.equals(this.roi, npCPerformance.roi) &&
        Objects.equals(this.loyaltyChanges, npCPerformance.loyaltyChanges) &&
        Objects.equals(this.performanceTrend, npCPerformance.performanceTrend);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hireId, tasksCompleted, tasksFailed, successRate, valueGenerated, costTotal, roi, loyaltyChanges, performanceTrend);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NPCPerformance {\n");
    sb.append("    hireId: ").append(toIndentedString(hireId)).append("\n");
    sb.append("    tasksCompleted: ").append(toIndentedString(tasksCompleted)).append("\n");
    sb.append("    tasksFailed: ").append(toIndentedString(tasksFailed)).append("\n");
    sb.append("    successRate: ").append(toIndentedString(successRate)).append("\n");
    sb.append("    valueGenerated: ").append(toIndentedString(valueGenerated)).append("\n");
    sb.append("    costTotal: ").append(toIndentedString(costTotal)).append("\n");
    sb.append("    roi: ").append(toIndentedString(roi)).append("\n");
    sb.append("    loyaltyChanges: ").append(toIndentedString(loyaltyChanges)).append("\n");
    sb.append("    performanceTrend: ").append(toIndentedString(performanceTrend)).append("\n");
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

