package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.RiskAlert;
import com.necpgame.economyservice.model.RiskFactorBreakdown;
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
 * RiskScore
 */


public class RiskScore {

  private Float score;

  /**
   * Gets or Sets grade
   */
  public enum GradeEnum {
    LOW("low"),
    
    MODERATE("moderate"),
    
    ELEVATED("elevated"),
    
    HIGH("high"),
    
    SEVERE("severe");

    private final String value;

    GradeEnum(String value) {
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
    public static GradeEnum fromValue(String value) {
      for (GradeEnum b : GradeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private GradeEnum grade;

  private Float riskModifier;

  private @Nullable Float recommendedEscrowRate;

  private @Nullable Float recommendedCommissionRate;

  /**
   * Gets or Sets insuranceTier
   */
  public enum InsuranceTierEnum {
    BASIC("basic"),
    
    ENHANCED("enhanced"),
    
    PREMIUM("premium"),
    
    CORPORATE("corporate");

    private final String value;

    InsuranceTierEnum(String value) {
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
    public static InsuranceTierEnum fromValue(String value) {
      for (InsuranceTierEnum b : InsuranceTierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable InsuranceTierEnum insuranceTier;

  @Valid
  private List<@Valid RiskFactorBreakdown> factors = new ArrayList<>();

  @Valid
  private List<@Valid RiskAlert> alerts = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime evaluatedAt;

  public RiskScore() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RiskScore(Float score, GradeEnum grade, Float riskModifier, List<@Valid RiskFactorBreakdown> factors) {
    this.score = score;
    this.grade = grade;
    this.riskModifier = riskModifier;
    this.factors = factors;
  }

  public RiskScore score(Float score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * minimum: 0
   * maximum: 100
   * @return score
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("score")
  public Float getScore() {
    return score;
  }

  public void setScore(Float score) {
    this.score = score;
  }

  public RiskScore grade(GradeEnum grade) {
    this.grade = grade;
    return this;
  }

  /**
   * Get grade
   * @return grade
   */
  @NotNull 
  @Schema(name = "grade", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("grade")
  public GradeEnum getGrade() {
    return grade;
  }

  public void setGrade(GradeEnum grade) {
    this.grade = grade;
  }

  public RiskScore riskModifier(Float riskModifier) {
    this.riskModifier = riskModifier;
    return this;
  }

  /**
   * Get riskModifier
   * minimum: 0.5
   * maximum: 2.0
   * @return riskModifier
   */
  @NotNull @DecimalMin(value = "0.5") @DecimalMax(value = "2.0") 
  @Schema(name = "riskModifier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("riskModifier")
  public Float getRiskModifier() {
    return riskModifier;
  }

  public void setRiskModifier(Float riskModifier) {
    this.riskModifier = riskModifier;
  }

  public RiskScore recommendedEscrowRate(@Nullable Float recommendedEscrowRate) {
    this.recommendedEscrowRate = recommendedEscrowRate;
    return this;
  }

  /**
   * Get recommendedEscrowRate
   * minimum: 0.1
   * maximum: 0.3
   * @return recommendedEscrowRate
   */
  @DecimalMin(value = "0.1") @DecimalMax(value = "0.3") 
  @Schema(name = "recommendedEscrowRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedEscrowRate")
  public @Nullable Float getRecommendedEscrowRate() {
    return recommendedEscrowRate;
  }

  public void setRecommendedEscrowRate(@Nullable Float recommendedEscrowRate) {
    this.recommendedEscrowRate = recommendedEscrowRate;
  }

  public RiskScore recommendedCommissionRate(@Nullable Float recommendedCommissionRate) {
    this.recommendedCommissionRate = recommendedCommissionRate;
    return this;
  }

  /**
   * Get recommendedCommissionRate
   * minimum: 0.05
   * maximum: 0.12
   * @return recommendedCommissionRate
   */
  @DecimalMin(value = "0.05") @DecimalMax(value = "0.12") 
  @Schema(name = "recommendedCommissionRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedCommissionRate")
  public @Nullable Float getRecommendedCommissionRate() {
    return recommendedCommissionRate;
  }

  public void setRecommendedCommissionRate(@Nullable Float recommendedCommissionRate) {
    this.recommendedCommissionRate = recommendedCommissionRate;
  }

  public RiskScore insuranceTier(@Nullable InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
    return this;
  }

  /**
   * Get insuranceTier
   * @return insuranceTier
   */
  
  @Schema(name = "insuranceTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("insuranceTier")
  public @Nullable InsuranceTierEnum getInsuranceTier() {
    return insuranceTier;
  }

  public void setInsuranceTier(@Nullable InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
  }

  public RiskScore factors(List<@Valid RiskFactorBreakdown> factors) {
    this.factors = factors;
    return this;
  }

  public RiskScore addFactorsItem(RiskFactorBreakdown factorsItem) {
    if (this.factors == null) {
      this.factors = new ArrayList<>();
    }
    this.factors.add(factorsItem);
    return this;
  }

  /**
   * Get factors
   * @return factors
   */
  @NotNull @Valid 
  @Schema(name = "factors", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factors")
  public List<@Valid RiskFactorBreakdown> getFactors() {
    return factors;
  }

  public void setFactors(List<@Valid RiskFactorBreakdown> factors) {
    this.factors = factors;
  }

  public RiskScore alerts(List<@Valid RiskAlert> alerts) {
    this.alerts = alerts;
    return this;
  }

  public RiskScore addAlertsItem(RiskAlert alertsItem) {
    if (this.alerts == null) {
      this.alerts = new ArrayList<>();
    }
    this.alerts.add(alertsItem);
    return this;
  }

  /**
   * Get alerts
   * @return alerts
   */
  @Valid 
  @Schema(name = "alerts", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("alerts")
  public List<@Valid RiskAlert> getAlerts() {
    return alerts;
  }

  public void setAlerts(List<@Valid RiskAlert> alerts) {
    this.alerts = alerts;
  }

  public RiskScore evaluatedAt(@Nullable OffsetDateTime evaluatedAt) {
    this.evaluatedAt = evaluatedAt;
    return this;
  }

  /**
   * Get evaluatedAt
   * @return evaluatedAt
   */
  @Valid 
  @Schema(name = "evaluatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evaluatedAt")
  public @Nullable OffsetDateTime getEvaluatedAt() {
    return evaluatedAt;
  }

  public void setEvaluatedAt(@Nullable OffsetDateTime evaluatedAt) {
    this.evaluatedAt = evaluatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskScore riskScore = (RiskScore) o;
    return Objects.equals(this.score, riskScore.score) &&
        Objects.equals(this.grade, riskScore.grade) &&
        Objects.equals(this.riskModifier, riskScore.riskModifier) &&
        Objects.equals(this.recommendedEscrowRate, riskScore.recommendedEscrowRate) &&
        Objects.equals(this.recommendedCommissionRate, riskScore.recommendedCommissionRate) &&
        Objects.equals(this.insuranceTier, riskScore.insuranceTier) &&
        Objects.equals(this.factors, riskScore.factors) &&
        Objects.equals(this.alerts, riskScore.alerts) &&
        Objects.equals(this.evaluatedAt, riskScore.evaluatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, grade, riskModifier, recommendedEscrowRate, recommendedCommissionRate, insuranceTier, factors, alerts, evaluatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RiskScore {\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    grade: ").append(toIndentedString(grade)).append("\n");
    sb.append("    riskModifier: ").append(toIndentedString(riskModifier)).append("\n");
    sb.append("    recommendedEscrowRate: ").append(toIndentedString(recommendedEscrowRate)).append("\n");
    sb.append("    recommendedCommissionRate: ").append(toIndentedString(recommendedCommissionRate)).append("\n");
    sb.append("    insuranceTier: ").append(toIndentedString(insuranceTier)).append("\n");
    sb.append("    factors: ").append(toIndentedString(factors)).append("\n");
    sb.append("    alerts: ").append(toIndentedString(alerts)).append("\n");
    sb.append("    evaluatedAt: ").append(toIndentedString(evaluatedAt)).append("\n");
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

