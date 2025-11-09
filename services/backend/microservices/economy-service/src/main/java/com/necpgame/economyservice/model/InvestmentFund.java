package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * InvestmentFund
 */


public class InvestmentFund {

  private @Nullable UUID fundId;

  private @Nullable String name;

  private @Nullable String manager;

  private @Nullable String type;

  private @Nullable Integer totalValue;

  private @Nullable Integer investorsCount;

  private @Nullable BigDecimal performanceYtd;

  private @Nullable Integer minInvestment;

  private @Nullable Float managementFee;

  public InvestmentFund fundId(@Nullable UUID fundId) {
    this.fundId = fundId;
    return this;
  }

  /**
   * Get fundId
   * @return fundId
   */
  @Valid 
  @Schema(name = "fund_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fund_id")
  public @Nullable UUID getFundId() {
    return fundId;
  }

  public void setFundId(@Nullable UUID fundId) {
    this.fundId = fundId;
  }

  public InvestmentFund name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public InvestmentFund manager(@Nullable String manager) {
    this.manager = manager;
    return this;
  }

  /**
   * Кто управляет фондом
   * @return manager
   */
  
  @Schema(name = "manager", description = "Кто управляет фондом", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("manager")
  public @Nullable String getManager() {
    return manager;
  }

  public void setManager(@Nullable String manager) {
    this.manager = manager;
  }

  public InvestmentFund type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public InvestmentFund totalValue(@Nullable Integer totalValue) {
    this.totalValue = totalValue;
    return this;
  }

  /**
   * Get totalValue
   * @return totalValue
   */
  
  @Schema(name = "total_value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_value")
  public @Nullable Integer getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(@Nullable Integer totalValue) {
    this.totalValue = totalValue;
  }

  public InvestmentFund investorsCount(@Nullable Integer investorsCount) {
    this.investorsCount = investorsCount;
    return this;
  }

  /**
   * Get investorsCount
   * @return investorsCount
   */
  
  @Schema(name = "investors_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("investors_count")
  public @Nullable Integer getInvestorsCount() {
    return investorsCount;
  }

  public void setInvestorsCount(@Nullable Integer investorsCount) {
    this.investorsCount = investorsCount;
  }

  public InvestmentFund performanceYtd(@Nullable BigDecimal performanceYtd) {
    this.performanceYtd = performanceYtd;
    return this;
  }

  /**
   * Year-to-date performance (%)
   * @return performanceYtd
   */
  @Valid 
  @Schema(name = "performance_ytd", description = "Year-to-date performance (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("performance_ytd")
  public @Nullable BigDecimal getPerformanceYtd() {
    return performanceYtd;
  }

  public void setPerformanceYtd(@Nullable BigDecimal performanceYtd) {
    this.performanceYtd = performanceYtd;
  }

  public InvestmentFund minInvestment(@Nullable Integer minInvestment) {
    this.minInvestment = minInvestment;
    return this;
  }

  /**
   * Get minInvestment
   * @return minInvestment
   */
  
  @Schema(name = "min_investment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_investment")
  public @Nullable Integer getMinInvestment() {
    return minInvestment;
  }

  public void setMinInvestment(@Nullable Integer minInvestment) {
    this.minInvestment = minInvestment;
  }

  public InvestmentFund managementFee(@Nullable Float managementFee) {
    this.managementFee = managementFee;
    return this;
  }

  /**
   * Комиссия управления (%)
   * @return managementFee
   */
  
  @Schema(name = "management_fee", description = "Комиссия управления (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("management_fee")
  public @Nullable Float getManagementFee() {
    return managementFee;
  }

  public void setManagementFee(@Nullable Float managementFee) {
    this.managementFee = managementFee;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvestmentFund investmentFund = (InvestmentFund) o;
    return Objects.equals(this.fundId, investmentFund.fundId) &&
        Objects.equals(this.name, investmentFund.name) &&
        Objects.equals(this.manager, investmentFund.manager) &&
        Objects.equals(this.type, investmentFund.type) &&
        Objects.equals(this.totalValue, investmentFund.totalValue) &&
        Objects.equals(this.investorsCount, investmentFund.investorsCount) &&
        Objects.equals(this.performanceYtd, investmentFund.performanceYtd) &&
        Objects.equals(this.minInvestment, investmentFund.minInvestment) &&
        Objects.equals(this.managementFee, investmentFund.managementFee);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fundId, name, manager, type, totalValue, investorsCount, performanceYtd, minInvestment, managementFee);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InvestmentFund {\n");
    sb.append("    fundId: ").append(toIndentedString(fundId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    manager: ").append(toIndentedString(manager)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    totalValue: ").append(toIndentedString(totalValue)).append("\n");
    sb.append("    investorsCount: ").append(toIndentedString(investorsCount)).append("\n");
    sb.append("    performanceYtd: ").append(toIndentedString(performanceYtd)).append("\n");
    sb.append("    minInvestment: ").append(toIndentedString(minInvestment)).append("\n");
    sb.append("    managementFee: ").append(toIndentedString(managementFee)).append("\n");
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

