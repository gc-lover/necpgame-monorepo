package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.Issue;
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
 * ComponentDebt
 */


public class ComponentDebt {

  private @Nullable String componentId;

  private @Nullable String componentName;

  private @Nullable Integer debtHours;

  @Valid
  private List<@Valid Issue> issues = new ArrayList<>();

  public ComponentDebt componentId(@Nullable String componentId) {
    this.componentId = componentId;
    return this;
  }

  /**
   * Get componentId
   * @return componentId
   */
  
  @Schema(name = "component_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("component_id")
  public @Nullable String getComponentId() {
    return componentId;
  }

  public void setComponentId(@Nullable String componentId) {
    this.componentId = componentId;
  }

  public ComponentDebt componentName(@Nullable String componentName) {
    this.componentName = componentName;
    return this;
  }

  /**
   * Get componentName
   * @return componentName
   */
  
  @Schema(name = "component_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("component_name")
  public @Nullable String getComponentName() {
    return componentName;
  }

  public void setComponentName(@Nullable String componentName) {
    this.componentName = componentName;
  }

  public ComponentDebt debtHours(@Nullable Integer debtHours) {
    this.debtHours = debtHours;
    return this;
  }

  /**
   * Get debtHours
   * @return debtHours
   */
  
  @Schema(name = "debt_hours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("debt_hours")
  public @Nullable Integer getDebtHours() {
    return debtHours;
  }

  public void setDebtHours(@Nullable Integer debtHours) {
    this.debtHours = debtHours;
  }

  public ComponentDebt issues(List<@Valid Issue> issues) {
    this.issues = issues;
    return this;
  }

  public ComponentDebt addIssuesItem(Issue issuesItem) {
    if (this.issues == null) {
      this.issues = new ArrayList<>();
    }
    this.issues.add(issuesItem);
    return this;
  }

  /**
   * Get issues
   * @return issues
   */
  @Valid 
  @Schema(name = "issues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issues")
  public List<@Valid Issue> getIssues() {
    return issues;
  }

  public void setIssues(List<@Valid Issue> issues) {
    this.issues = issues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ComponentDebt componentDebt = (ComponentDebt) o;
    return Objects.equals(this.componentId, componentDebt.componentId) &&
        Objects.equals(this.componentName, componentDebt.componentName) &&
        Objects.equals(this.debtHours, componentDebt.debtHours) &&
        Objects.equals(this.issues, componentDebt.issues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(componentId, componentName, debtHours, issues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ComponentDebt {\n");
    sb.append("    componentId: ").append(toIndentedString(componentId)).append("\n");
    sb.append("    componentName: ").append(toIndentedString(componentName)).append("\n");
    sb.append("    debtHours: ").append(toIndentedString(debtHours)).append("\n");
    sb.append("    issues: ").append(toIndentedString(issues)).append("\n");
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

