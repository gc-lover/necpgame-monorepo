package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.BudgetBreakdown;
import com.necpgame.economyservice.model.BudgetEstimateResponseRecommendedBudgetRange;
import com.necpgame.economyservice.model.BudgetWarning;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * BudgetEstimateResponse
 */


public class BudgetEstimateResponse {

  private UUID calculationId;

  private @Nullable UUID auditTraceId;

  private BigDecimal baseReward;

  private BigDecimal escrow;

  private @Nullable BigDecimal escrowRate;

  private BigDecimal commission;

  private @Nullable BigDecimal commissionRate;

  private BigDecimal insuranceFee;

  /**
   * Выбранный страховой пакет.
   */
  public enum InsuranceTierEnum {
    BASIC("basic"),
    
    EXTENDED("extended"),
    
    PREMIUM("premium");

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

  private String currency;

  private @Nullable BudgetEstimateResponseRecommendedBudgetRange recommendedBudgetRange;

  private @Nullable BigDecimal median;

  private @Nullable BigDecimal medianDeviationPercent;

  @Valid
  private List<@Valid BudgetWarning> warnings = new ArrayList<>();

  private BudgetBreakdown breakdown;

  @Valid
  private List<String> recommendedActions = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public BudgetEstimateResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BudgetEstimateResponse(UUID calculationId, BigDecimal baseReward, BigDecimal escrow, BigDecimal commission, BigDecimal insuranceFee, String currency, List<@Valid BudgetWarning> warnings, BudgetBreakdown breakdown, OffsetDateTime timestamp) {
    this.calculationId = calculationId;
    this.baseReward = baseReward;
    this.escrow = escrow;
    this.commission = commission;
    this.insuranceFee = insuranceFee;
    this.currency = currency;
    this.warnings = warnings;
    this.breakdown = breakdown;
    this.timestamp = timestamp;
  }

  public BudgetEstimateResponse calculationId(UUID calculationId) {
    this.calculationId = calculationId;
    return this;
  }

  /**
   * Идентификатор расчёта бюджета.
   * @return calculationId
   */
  @NotNull @Valid 
  @Schema(name = "calculationId", description = "Идентификатор расчёта бюджета.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("calculationId")
  public UUID getCalculationId() {
    return calculationId;
  }

  public void setCalculationId(UUID calculationId) {
    this.calculationId = calculationId;
  }

  public BudgetEstimateResponse auditTraceId(@Nullable UUID auditTraceId) {
    this.auditTraceId = auditTraceId;
    return this;
  }

  /**
   * Трассовый идентификатор для аудита.
   * @return auditTraceId
   */
  @Valid 
  @Schema(name = "auditTraceId", description = "Трассовый идентификатор для аудита.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("auditTraceId")
  public @Nullable UUID getAuditTraceId() {
    return auditTraceId;
  }

  public void setAuditTraceId(@Nullable UUID auditTraceId) {
    this.auditTraceId = auditTraceId;
  }

  public BudgetEstimateResponse baseReward(BigDecimal baseReward) {
    this.baseReward = baseReward;
    return this;
  }

  /**
   * Рекомендованный базовый бюджет.
   * minimum: 0
   * @return baseReward
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "baseReward", description = "Рекомендованный базовый бюджет.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baseReward")
  public BigDecimal getBaseReward() {
    return baseReward;
  }

  public void setBaseReward(BigDecimal baseReward) {
    this.baseReward = baseReward;
  }

  public BudgetEstimateResponse escrow(BigDecimal escrow) {
    this.escrow = escrow;
    return this;
  }

  /**
   * Рекомендуемая сумма escrow.
   * minimum: 0
   * @return escrow
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "escrow", description = "Рекомендуемая сумма escrow.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("escrow")
  public BigDecimal getEscrow() {
    return escrow;
  }

  public void setEscrow(BigDecimal escrow) {
    this.escrow = escrow;
  }

  public BudgetEstimateResponse escrowRate(@Nullable BigDecimal escrowRate) {
    this.escrowRate = escrowRate;
    return this;
  }

  /**
   * Применённая ставка escrow (10–30%).
   * minimum: 0.1
   * maximum: 0.3
   * @return escrowRate
   */
  @Valid @DecimalMin(value = "0.1") @DecimalMax(value = "0.3") 
  @Schema(name = "escrowRate", description = "Применённая ставка escrow (10–30%).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escrowRate")
  public @Nullable BigDecimal getEscrowRate() {
    return escrowRate;
  }

  public void setEscrowRate(@Nullable BigDecimal escrowRate) {
    this.escrowRate = escrowRate;
  }

  public BudgetEstimateResponse commission(BigDecimal commission) {
    this.commission = commission;
    return this;
  }

  /**
   * Комиссия платформы.
   * minimum: 0
   * @return commission
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "commission", description = "Комиссия платформы.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("commission")
  public BigDecimal getCommission() {
    return commission;
  }

  public void setCommission(BigDecimal commission) {
    this.commission = commission;
  }

  public BudgetEstimateResponse commissionRate(@Nullable BigDecimal commissionRate) {
    this.commissionRate = commissionRate;
    return this;
  }

  /**
   * Ставка комиссии (5–12%).
   * minimum: 0.05
   * maximum: 0.12
   * @return commissionRate
   */
  @Valid @DecimalMin(value = "0.05") @DecimalMax(value = "0.12") 
  @Schema(name = "commissionRate", description = "Ставка комиссии (5–12%).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commissionRate")
  public @Nullable BigDecimal getCommissionRate() {
    return commissionRate;
  }

  public void setCommissionRate(@Nullable BigDecimal commissionRate) {
    this.commissionRate = commissionRate;
  }

  public BudgetEstimateResponse insuranceFee(BigDecimal insuranceFee) {
    this.insuranceFee = insuranceFee;
    return this;
  }

  /**
   * Стоимость страхового плана.
   * minimum: 0
   * @return insuranceFee
   */
  @NotNull @Valid @DecimalMin(value = "0") 
  @Schema(name = "insuranceFee", description = "Стоимость страхового плана.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("insuranceFee")
  public BigDecimal getInsuranceFee() {
    return insuranceFee;
  }

  public void setInsuranceFee(BigDecimal insuranceFee) {
    this.insuranceFee = insuranceFee;
  }

  public BudgetEstimateResponse insuranceTier(@Nullable InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
    return this;
  }

  /**
   * Выбранный страховой пакет.
   * @return insuranceTier
   */
  
  @Schema(name = "insuranceTier", description = "Выбранный страховой пакет.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("insuranceTier")
  public @Nullable InsuranceTierEnum getInsuranceTier() {
    return insuranceTier;
  }

  public void setInsuranceTier(@Nullable InsuranceTierEnum insuranceTier) {
    this.insuranceTier = insuranceTier;
  }

  public BudgetEstimateResponse currency(String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Код валюты расчёта.
   * @return currency
   */
  @NotNull 
  @Schema(name = "currency", description = "Код валюты расчёта.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currency")
  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public BudgetEstimateResponse recommendedBudgetRange(@Nullable BudgetEstimateResponseRecommendedBudgetRange recommendedBudgetRange) {
    this.recommendedBudgetRange = recommendedBudgetRange;
    return this;
  }

  /**
   * Get recommendedBudgetRange
   * @return recommendedBudgetRange
   */
  @Valid 
  @Schema(name = "recommendedBudgetRange", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedBudgetRange")
  public @Nullable BudgetEstimateResponseRecommendedBudgetRange getRecommendedBudgetRange() {
    return recommendedBudgetRange;
  }

  public void setRecommendedBudgetRange(@Nullable BudgetEstimateResponseRecommendedBudgetRange recommendedBudgetRange) {
    this.recommendedBudgetRange = recommendedBudgetRange;
  }

  public BudgetEstimateResponse median(@Nullable BigDecimal median) {
    this.median = median;
    return this;
  }

  /**
   * Медианное значение бюджета.
   * @return median
   */
  @Valid 
  @Schema(name = "median", description = "Медианное значение бюджета.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("median")
  public @Nullable BigDecimal getMedian() {
    return median;
  }

  public void setMedian(@Nullable BigDecimal median) {
    this.median = median;
  }

  public BudgetEstimateResponse medianDeviationPercent(@Nullable BigDecimal medianDeviationPercent) {
    this.medianDeviationPercent = medianDeviationPercent;
    return this;
  }

  /**
   * Отклонение от медианы в процентах.
   * @return medianDeviationPercent
   */
  @Valid 
  @Schema(name = "medianDeviationPercent", description = "Отклонение от медианы в процентах.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medianDeviationPercent")
  public @Nullable BigDecimal getMedianDeviationPercent() {
    return medianDeviationPercent;
  }

  public void setMedianDeviationPercent(@Nullable BigDecimal medianDeviationPercent) {
    this.medianDeviationPercent = medianDeviationPercent;
  }

  public BudgetEstimateResponse warnings(List<@Valid BudgetWarning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public BudgetEstimateResponse addWarningsItem(BudgetWarning warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Предупреждения и рекомендации.
   * @return warnings
   */
  @NotNull @Valid 
  @Schema(name = "warnings", description = "Предупреждения и рекомендации.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid BudgetWarning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid BudgetWarning> warnings) {
    this.warnings = warnings;
  }

  public BudgetEstimateResponse breakdown(BudgetBreakdown breakdown) {
    this.breakdown = breakdown;
    return this;
  }

  /**
   * Get breakdown
   * @return breakdown
   */
  @NotNull @Valid 
  @Schema(name = "breakdown", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("breakdown")
  public BudgetBreakdown getBreakdown() {
    return breakdown;
  }

  public void setBreakdown(BudgetBreakdown breakdown) {
    this.breakdown = breakdown;
  }

  public BudgetEstimateResponse recommendedActions(List<String> recommendedActions) {
    this.recommendedActions = recommendedActions;
    return this;
  }

  public BudgetEstimateResponse addRecommendedActionsItem(String recommendedActionsItem) {
    if (this.recommendedActions == null) {
      this.recommendedActions = new ArrayList<>();
    }
    this.recommendedActions.add(recommendedActionsItem);
    return this;
  }

  /**
   * Рекомендации для заказчика.
   * @return recommendedActions
   */
  
  @Schema(name = "recommendedActions", description = "Рекомендации для заказчика.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedActions")
  public List<String> getRecommendedActions() {
    return recommendedActions;
  }

  public void setRecommendedActions(List<String> recommendedActions) {
    this.recommendedActions = recommendedActions;
  }

  public BudgetEstimateResponse timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Время расчёта.
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", description = "Время расчёта.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public BudgetEstimateResponse expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Момент устаревания расчёта.
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", description = "Момент устаревания расчёта.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BudgetEstimateResponse budgetEstimateResponse = (BudgetEstimateResponse) o;
    return Objects.equals(this.calculationId, budgetEstimateResponse.calculationId) &&
        Objects.equals(this.auditTraceId, budgetEstimateResponse.auditTraceId) &&
        Objects.equals(this.baseReward, budgetEstimateResponse.baseReward) &&
        Objects.equals(this.escrow, budgetEstimateResponse.escrow) &&
        Objects.equals(this.escrowRate, budgetEstimateResponse.escrowRate) &&
        Objects.equals(this.commission, budgetEstimateResponse.commission) &&
        Objects.equals(this.commissionRate, budgetEstimateResponse.commissionRate) &&
        Objects.equals(this.insuranceFee, budgetEstimateResponse.insuranceFee) &&
        Objects.equals(this.insuranceTier, budgetEstimateResponse.insuranceTier) &&
        Objects.equals(this.currency, budgetEstimateResponse.currency) &&
        Objects.equals(this.recommendedBudgetRange, budgetEstimateResponse.recommendedBudgetRange) &&
        Objects.equals(this.median, budgetEstimateResponse.median) &&
        Objects.equals(this.medianDeviationPercent, budgetEstimateResponse.medianDeviationPercent) &&
        Objects.equals(this.warnings, budgetEstimateResponse.warnings) &&
        Objects.equals(this.breakdown, budgetEstimateResponse.breakdown) &&
        Objects.equals(this.recommendedActions, budgetEstimateResponse.recommendedActions) &&
        Objects.equals(this.timestamp, budgetEstimateResponse.timestamp) &&
        Objects.equals(this.expiresAt, budgetEstimateResponse.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(calculationId, auditTraceId, baseReward, escrow, escrowRate, commission, commissionRate, insuranceFee, insuranceTier, currency, recommendedBudgetRange, median, medianDeviationPercent, warnings, breakdown, recommendedActions, timestamp, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BudgetEstimateResponse {\n");
    sb.append("    calculationId: ").append(toIndentedString(calculationId)).append("\n");
    sb.append("    auditTraceId: ").append(toIndentedString(auditTraceId)).append("\n");
    sb.append("    baseReward: ").append(toIndentedString(baseReward)).append("\n");
    sb.append("    escrow: ").append(toIndentedString(escrow)).append("\n");
    sb.append("    escrowRate: ").append(toIndentedString(escrowRate)).append("\n");
    sb.append("    commission: ").append(toIndentedString(commission)).append("\n");
    sb.append("    commissionRate: ").append(toIndentedString(commissionRate)).append("\n");
    sb.append("    insuranceFee: ").append(toIndentedString(insuranceFee)).append("\n");
    sb.append("    insuranceTier: ").append(toIndentedString(insuranceTier)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    recommendedBudgetRange: ").append(toIndentedString(recommendedBudgetRange)).append("\n");
    sb.append("    median: ").append(toIndentedString(median)).append("\n");
    sb.append("    medianDeviationPercent: ").append(toIndentedString(medianDeviationPercent)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    breakdown: ").append(toIndentedString(breakdown)).append("\n");
    sb.append("    recommendedActions: ").append(toIndentedString(recommendedActions)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

