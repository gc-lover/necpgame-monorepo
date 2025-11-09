package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.BudgetWarning;
import com.necpgame.economyservice.model.CalculationBreakdown;
import com.necpgame.economyservice.model.PlayerOrderBudgetEstimateResponseRecommendedBudgetRange;
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
 * PlayerOrderBudgetEstimateResponse
 */


public class PlayerOrderBudgetEstimateResponse {

  private Float baseReward;

  private Float escrow;

  private Float commission;

  private @Nullable Float commissionRate;

  private Float insuranceFee;

  /**
   * Gets or Sets insuranceTier
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

  private PlayerOrderBudgetEstimateResponseRecommendedBudgetRange recommendedBudgetRange;

  private @Nullable Float median;

  private @Nullable Float deviationPercent;

  private @Nullable String currency;

  @Valid
  private List<@Valid BudgetWarning> warnings = new ArrayList<>();

  private CalculationBreakdown calculationBreakdown;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime nextRefreshAt;

  private @Nullable UUID jobId;

  public PlayerOrderBudgetEstimateResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderBudgetEstimateResponse(Float baseReward, Float escrow, Float commission, Float insuranceFee, PlayerOrderBudgetEstimateResponseRecommendedBudgetRange recommendedBudgetRange, List<@Valid BudgetWarning> warnings, CalculationBreakdown calculationBreakdown) {
    this.baseReward = baseReward;
    this.escrow = escrow;
    this.commission = commission;
    this.insuranceFee = insuranceFee;
    this.recommendedBudgetRange = recommendedBudgetRange;
    this.warnings = warnings;
    this.calculationBreakdown = calculationBreakdown;
  }

  public PlayerOrderBudgetEstimateResponse baseReward(Float baseReward) {
    this.baseReward = baseReward;
    return this;
  }

  /**
   * Get baseReward
   * minimum: 0
   * @return baseReward
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "baseReward", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baseReward")
  public Float getBaseReward() {
    return baseReward;
  }

  public void setBaseReward(Float baseReward) {
    this.baseReward = baseReward;
  }

  public PlayerOrderBudgetEstimateResponse escrow(Float escrow) {
    this.escrow = escrow;
    return this;
  }

  /**
   * Get escrow
   * minimum: 0
   * @return escrow
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "escrow", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("escrow")
  public Float getEscrow() {
    return escrow;
  }

  public void setEscrow(Float escrow) {
    this.escrow = escrow;
  }

  public PlayerOrderBudgetEstimateResponse commission(Float commission) {
    this.commission = commission;
    return this;
  }

  /**
   * Get commission
   * minimum: 0
   * @return commission
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "commission", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("commission")
  public Float getCommission() {
    return commission;
  }

  public void setCommission(Float commission) {
    this.commission = commission;
  }

  public PlayerOrderBudgetEstimateResponse commissionRate(@Nullable Float commissionRate) {
    this.commissionRate = commissionRate;
    return this;
  }

  /**
   * Get commissionRate
   * minimum: 0.05
   * maximum: 0.12
   * @return commissionRate
   */
  @DecimalMin(value = "0.05") @DecimalMax(value = "0.12") 
  @Schema(name = "commissionRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commissionRate")
  public @Nullable Float getCommissionRate() {
    return commissionRate;
  }

  public void setCommissionRate(@Nullable Float commissionRate) {
    this.commissionRate = commissionRate;
  }

  public PlayerOrderBudgetEstimateResponse insuranceFee(Float insuranceFee) {
    this.insuranceFee = insuranceFee;
    return this;
  }

  /**
   * Get insuranceFee
   * minimum: 0
   * @return insuranceFee
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "insuranceFee", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("insuranceFee")
  public Float getInsuranceFee() {
    return insuranceFee;
  }

  public void setInsuranceFee(Float insuranceFee) {
    this.insuranceFee = insuranceFee;
  }

  public PlayerOrderBudgetEstimateResponse insuranceTier(@Nullable InsuranceTierEnum insuranceTier) {
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

  public PlayerOrderBudgetEstimateResponse recommendedBudgetRange(PlayerOrderBudgetEstimateResponseRecommendedBudgetRange recommendedBudgetRange) {
    this.recommendedBudgetRange = recommendedBudgetRange;
    return this;
  }

  /**
   * Get recommendedBudgetRange
   * @return recommendedBudgetRange
   */
  @NotNull @Valid 
  @Schema(name = "recommendedBudgetRange", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("recommendedBudgetRange")
  public PlayerOrderBudgetEstimateResponseRecommendedBudgetRange getRecommendedBudgetRange() {
    return recommendedBudgetRange;
  }

  public void setRecommendedBudgetRange(PlayerOrderBudgetEstimateResponseRecommendedBudgetRange recommendedBudgetRange) {
    this.recommendedBudgetRange = recommendedBudgetRange;
  }

  public PlayerOrderBudgetEstimateResponse median(@Nullable Float median) {
    this.median = median;
    return this;
  }

  /**
   * Get median
   * @return median
   */
  
  @Schema(name = "median", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("median")
  public @Nullable Float getMedian() {
    return median;
  }

  public void setMedian(@Nullable Float median) {
    this.median = median;
  }

  public PlayerOrderBudgetEstimateResponse deviationPercent(@Nullable Float deviationPercent) {
    this.deviationPercent = deviationPercent;
    return this;
  }

  /**
   * Get deviationPercent
   * @return deviationPercent
   */
  
  @Schema(name = "deviationPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deviationPercent")
  public @Nullable Float getDeviationPercent() {
    return deviationPercent;
  }

  public void setDeviationPercent(@Nullable Float deviationPercent) {
    this.deviationPercent = deviationPercent;
  }

  public PlayerOrderBudgetEstimateResponse currency(@Nullable String currency) {
    this.currency = currency;
    return this;
  }

  /**
   * Get currency
   * @return currency
   */
  @Pattern(regexp = "^[A-Z]{3}$") 
  @Schema(name = "currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency")
  public @Nullable String getCurrency() {
    return currency;
  }

  public void setCurrency(@Nullable String currency) {
    this.currency = currency;
  }

  public PlayerOrderBudgetEstimateResponse warnings(List<@Valid BudgetWarning> warnings) {
    this.warnings = warnings;
    return this;
  }

  public PlayerOrderBudgetEstimateResponse addWarningsItem(BudgetWarning warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  @NotNull @Valid 
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("warnings")
  public List<@Valid BudgetWarning> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<@Valid BudgetWarning> warnings) {
    this.warnings = warnings;
  }

  public PlayerOrderBudgetEstimateResponse calculationBreakdown(CalculationBreakdown calculationBreakdown) {
    this.calculationBreakdown = calculationBreakdown;
    return this;
  }

  /**
   * Get calculationBreakdown
   * @return calculationBreakdown
   */
  @NotNull @Valid 
  @Schema(name = "calculationBreakdown", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("calculationBreakdown")
  public CalculationBreakdown getCalculationBreakdown() {
    return calculationBreakdown;
  }

  public void setCalculationBreakdown(CalculationBreakdown calculationBreakdown) {
    this.calculationBreakdown = calculationBreakdown;
  }

  public PlayerOrderBudgetEstimateResponse nextRefreshAt(@Nullable OffsetDateTime nextRefreshAt) {
    this.nextRefreshAt = nextRefreshAt;
    return this;
  }

  /**
   * Get nextRefreshAt
   * @return nextRefreshAt
   */
  @Valid 
  @Schema(name = "nextRefreshAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextRefreshAt")
  public @Nullable OffsetDateTime getNextRefreshAt() {
    return nextRefreshAt;
  }

  public void setNextRefreshAt(@Nullable OffsetDateTime nextRefreshAt) {
    this.nextRefreshAt = nextRefreshAt;
  }

  public PlayerOrderBudgetEstimateResponse jobId(@Nullable UUID jobId) {
    this.jobId = jobId;
    return this;
  }

  /**
   * Get jobId
   * @return jobId
   */
  @Valid 
  @Schema(name = "jobId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("jobId")
  public @Nullable UUID getJobId() {
    return jobId;
  }

  public void setJobId(@Nullable UUID jobId) {
    this.jobId = jobId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderBudgetEstimateResponse playerOrderBudgetEstimateResponse = (PlayerOrderBudgetEstimateResponse) o;
    return Objects.equals(this.baseReward, playerOrderBudgetEstimateResponse.baseReward) &&
        Objects.equals(this.escrow, playerOrderBudgetEstimateResponse.escrow) &&
        Objects.equals(this.commission, playerOrderBudgetEstimateResponse.commission) &&
        Objects.equals(this.commissionRate, playerOrderBudgetEstimateResponse.commissionRate) &&
        Objects.equals(this.insuranceFee, playerOrderBudgetEstimateResponse.insuranceFee) &&
        Objects.equals(this.insuranceTier, playerOrderBudgetEstimateResponse.insuranceTier) &&
        Objects.equals(this.recommendedBudgetRange, playerOrderBudgetEstimateResponse.recommendedBudgetRange) &&
        Objects.equals(this.median, playerOrderBudgetEstimateResponse.median) &&
        Objects.equals(this.deviationPercent, playerOrderBudgetEstimateResponse.deviationPercent) &&
        Objects.equals(this.currency, playerOrderBudgetEstimateResponse.currency) &&
        Objects.equals(this.warnings, playerOrderBudgetEstimateResponse.warnings) &&
        Objects.equals(this.calculationBreakdown, playerOrderBudgetEstimateResponse.calculationBreakdown) &&
        Objects.equals(this.nextRefreshAt, playerOrderBudgetEstimateResponse.nextRefreshAt) &&
        Objects.equals(this.jobId, playerOrderBudgetEstimateResponse.jobId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseReward, escrow, commission, commissionRate, insuranceFee, insuranceTier, recommendedBudgetRange, median, deviationPercent, currency, warnings, calculationBreakdown, nextRefreshAt, jobId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderBudgetEstimateResponse {\n");
    sb.append("    baseReward: ").append(toIndentedString(baseReward)).append("\n");
    sb.append("    escrow: ").append(toIndentedString(escrow)).append("\n");
    sb.append("    commission: ").append(toIndentedString(commission)).append("\n");
    sb.append("    commissionRate: ").append(toIndentedString(commissionRate)).append("\n");
    sb.append("    insuranceFee: ").append(toIndentedString(insuranceFee)).append("\n");
    sb.append("    insuranceTier: ").append(toIndentedString(insuranceTier)).append("\n");
    sb.append("    recommendedBudgetRange: ").append(toIndentedString(recommendedBudgetRange)).append("\n");
    sb.append("    median: ").append(toIndentedString(median)).append("\n");
    sb.append("    deviationPercent: ").append(toIndentedString(deviationPercent)).append("\n");
    sb.append("    currency: ").append(toIndentedString(currency)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
    sb.append("    calculationBreakdown: ").append(toIndentedString(calculationBreakdown)).append("\n");
    sb.append("    nextRefreshAt: ").append(toIndentedString(nextRefreshAt)).append("\n");
    sb.append("    jobId: ").append(toIndentedString(jobId)).append("\n");
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

