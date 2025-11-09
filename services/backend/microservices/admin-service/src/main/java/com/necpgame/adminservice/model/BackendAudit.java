package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.adminservice.model.ComponentAudit;
import com.necpgame.adminservice.model.Recommendation;
import com.necpgame.adminservice.model.TechnicalDebtSummary;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * BackendAudit
 */


public class BackendAudit {

  private String auditId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  /**
   * Общий статус backend
   */
  public enum OverallStatusEnum {
    EXCELLENT("excellent"),
    
    GOOD("good"),
    
    FAIR("fair"),
    
    POOR("poor"),
    
    CRITICAL("critical");

    private final String value;

    OverallStatusEnum(String value) {
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
    public static OverallStatusEnum fromValue(String value) {
      for (OverallStatusEnum b : OverallStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OverallStatusEnum overallStatus;

  private @Nullable Integer componentsAudited;

  private @Nullable Integer criticalIssues;

  private @Nullable Integer highIssues;

  private @Nullable Integer mediumIssues;

  private @Nullable Integer lowIssues;

  @Valid
  private List<@Valid ComponentAudit> components = new ArrayList<>();

  @Valid
  private List<@Valid Recommendation> recommendations = new ArrayList<>();

  private @Nullable TechnicalDebtSummary technicalDebt;

  public BackendAudit() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BackendAudit(String auditId, OffsetDateTime timestamp, OverallStatusEnum overallStatus) {
    this.auditId = auditId;
    this.timestamp = timestamp;
    this.overallStatus = overallStatus;
  }

  public BackendAudit auditId(String auditId) {
    this.auditId = auditId;
    return this;
  }

  /**
   * Уникальный ID аудита
   * @return auditId
   */
  @NotNull 
  @Schema(name = "audit_id", example = "audit_2025_11_07", description = "Уникальный ID аудита", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("audit_id")
  public String getAuditId() {
    return auditId;
  }

  public void setAuditId(String auditId) {
    this.auditId = auditId;
  }

  public BackendAudit timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Время проведения аудита
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", description = "Время проведения аудита", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public BackendAudit overallStatus(OverallStatusEnum overallStatus) {
    this.overallStatus = overallStatus;
    return this;
  }

  /**
   * Общий статус backend
   * @return overallStatus
   */
  @NotNull 
  @Schema(name = "overall_status", description = "Общий статус backend", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("overall_status")
  public OverallStatusEnum getOverallStatus() {
    return overallStatus;
  }

  public void setOverallStatus(OverallStatusEnum overallStatus) {
    this.overallStatus = overallStatus;
  }

  public BackendAudit componentsAudited(@Nullable Integer componentsAudited) {
    this.componentsAudited = componentsAudited;
    return this;
  }

  /**
   * Количество проверенных компонентов
   * @return componentsAudited
   */
  
  @Schema(name = "components_audited", example = "48", description = "Количество проверенных компонентов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components_audited")
  public @Nullable Integer getComponentsAudited() {
    return componentsAudited;
  }

  public void setComponentsAudited(@Nullable Integer componentsAudited) {
    this.componentsAudited = componentsAudited;
  }

  public BackendAudit criticalIssues(@Nullable Integer criticalIssues) {
    this.criticalIssues = criticalIssues;
    return this;
  }

  /**
   * Количество критических проблем
   * @return criticalIssues
   */
  
  @Schema(name = "critical_issues", example = "0", description = "Количество критических проблем", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical_issues")
  public @Nullable Integer getCriticalIssues() {
    return criticalIssues;
  }

  public void setCriticalIssues(@Nullable Integer criticalIssues) {
    this.criticalIssues = criticalIssues;
  }

  public BackendAudit highIssues(@Nullable Integer highIssues) {
    this.highIssues = highIssues;
    return this;
  }

  /**
   * Количество важных проблем
   * @return highIssues
   */
  
  @Schema(name = "high_issues", example = "2", description = "Количество важных проблем", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("high_issues")
  public @Nullable Integer getHighIssues() {
    return highIssues;
  }

  public void setHighIssues(@Nullable Integer highIssues) {
    this.highIssues = highIssues;
  }

  public BackendAudit mediumIssues(@Nullable Integer mediumIssues) {
    this.mediumIssues = mediumIssues;
    return this;
  }

  /**
   * Get mediumIssues
   * @return mediumIssues
   */
  
  @Schema(name = "medium_issues", example = "5", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medium_issues")
  public @Nullable Integer getMediumIssues() {
    return mediumIssues;
  }

  public void setMediumIssues(@Nullable Integer mediumIssues) {
    this.mediumIssues = mediumIssues;
  }

  public BackendAudit lowIssues(@Nullable Integer lowIssues) {
    this.lowIssues = lowIssues;
    return this;
  }

  /**
   * Get lowIssues
   * @return lowIssues
   */
  
  @Schema(name = "low_issues", example = "12", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("low_issues")
  public @Nullable Integer getLowIssues() {
    return lowIssues;
  }

  public void setLowIssues(@Nullable Integer lowIssues) {
    this.lowIssues = lowIssues;
  }

  public BackendAudit components(List<@Valid ComponentAudit> components) {
    this.components = components;
    return this;
  }

  public BackendAudit addComponentsItem(ComponentAudit componentsItem) {
    if (this.components == null) {
      this.components = new ArrayList<>();
    }
    this.components.add(componentsItem);
    return this;
  }

  /**
   * Детали по каждому компоненту
   * @return components
   */
  @Valid 
  @Schema(name = "components", description = "Детали по каждому компоненту", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components")
  public List<@Valid ComponentAudit> getComponents() {
    return components;
  }

  public void setComponents(List<@Valid ComponentAudit> components) {
    this.components = components;
  }

  public BackendAudit recommendations(List<@Valid Recommendation> recommendations) {
    this.recommendations = recommendations;
    return this;
  }

  public BackendAudit addRecommendationsItem(Recommendation recommendationsItem) {
    if (this.recommendations == null) {
      this.recommendations = new ArrayList<>();
    }
    this.recommendations.add(recommendationsItem);
    return this;
  }

  /**
   * Приоритетные рекомендации
   * @return recommendations
   */
  @Valid 
  @Schema(name = "recommendations", description = "Приоритетные рекомендации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendations")
  public List<@Valid Recommendation> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<@Valid Recommendation> recommendations) {
    this.recommendations = recommendations;
  }

  public BackendAudit technicalDebt(@Nullable TechnicalDebtSummary technicalDebt) {
    this.technicalDebt = technicalDebt;
    return this;
  }

  /**
   * Get technicalDebt
   * @return technicalDebt
   */
  @Valid 
  @Schema(name = "technical_debt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("technical_debt")
  public @Nullable TechnicalDebtSummary getTechnicalDebt() {
    return technicalDebt;
  }

  public void setTechnicalDebt(@Nullable TechnicalDebtSummary technicalDebt) {
    this.technicalDebt = technicalDebt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BackendAudit backendAudit = (BackendAudit) o;
    return Objects.equals(this.auditId, backendAudit.auditId) &&
        Objects.equals(this.timestamp, backendAudit.timestamp) &&
        Objects.equals(this.overallStatus, backendAudit.overallStatus) &&
        Objects.equals(this.componentsAudited, backendAudit.componentsAudited) &&
        Objects.equals(this.criticalIssues, backendAudit.criticalIssues) &&
        Objects.equals(this.highIssues, backendAudit.highIssues) &&
        Objects.equals(this.mediumIssues, backendAudit.mediumIssues) &&
        Objects.equals(this.lowIssues, backendAudit.lowIssues) &&
        Objects.equals(this.components, backendAudit.components) &&
        Objects.equals(this.recommendations, backendAudit.recommendations) &&
        Objects.equals(this.technicalDebt, backendAudit.technicalDebt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(auditId, timestamp, overallStatus, componentsAudited, criticalIssues, highIssues, mediumIssues, lowIssues, components, recommendations, technicalDebt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BackendAudit {\n");
    sb.append("    auditId: ").append(toIndentedString(auditId)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    overallStatus: ").append(toIndentedString(overallStatus)).append("\n");
    sb.append("    componentsAudited: ").append(toIndentedString(componentsAudited)).append("\n");
    sb.append("    criticalIssues: ").append(toIndentedString(criticalIssues)).append("\n");
    sb.append("    highIssues: ").append(toIndentedString(highIssues)).append("\n");
    sb.append("    mediumIssues: ").append(toIndentedString(mediumIssues)).append("\n");
    sb.append("    lowIssues: ").append(toIndentedString(lowIssues)).append("\n");
    sb.append("    components: ").append(toIndentedString(components)).append("\n");
    sb.append("    recommendations: ").append(toIndentedString(recommendations)).append("\n");
    sb.append("    technicalDebt: ").append(toIndentedString(technicalDebt)).append("\n");
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

