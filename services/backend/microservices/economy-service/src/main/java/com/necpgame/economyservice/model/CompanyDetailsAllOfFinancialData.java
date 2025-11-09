package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CompanyDetailsAllOfFinancialData
 */

@JsonTypeName("CompanyDetails_allOf_financial_data")

public class CompanyDetailsAllOfFinancialData {

  private @Nullable BigDecimal revenue;

  private @Nullable BigDecimal profit;

  private @Nullable BigDecimal assets;

  public CompanyDetailsAllOfFinancialData revenue(@Nullable BigDecimal revenue) {
    this.revenue = revenue;
    return this;
  }

  /**
   * Get revenue
   * @return revenue
   */
  @Valid 
  @Schema(name = "revenue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("revenue")
  public @Nullable BigDecimal getRevenue() {
    return revenue;
  }

  public void setRevenue(@Nullable BigDecimal revenue) {
    this.revenue = revenue;
  }

  public CompanyDetailsAllOfFinancialData profit(@Nullable BigDecimal profit) {
    this.profit = profit;
    return this;
  }

  /**
   * Get profit
   * @return profit
   */
  @Valid 
  @Schema(name = "profit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("profit")
  public @Nullable BigDecimal getProfit() {
    return profit;
  }

  public void setProfit(@Nullable BigDecimal profit) {
    this.profit = profit;
  }

  public CompanyDetailsAllOfFinancialData assets(@Nullable BigDecimal assets) {
    this.assets = assets;
    return this;
  }

  /**
   * Get assets
   * @return assets
   */
  @Valid 
  @Schema(name = "assets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assets")
  public @Nullable BigDecimal getAssets() {
    return assets;
  }

  public void setAssets(@Nullable BigDecimal assets) {
    this.assets = assets;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanyDetailsAllOfFinancialData companyDetailsAllOfFinancialData = (CompanyDetailsAllOfFinancialData) o;
    return Objects.equals(this.revenue, companyDetailsAllOfFinancialData.revenue) &&
        Objects.equals(this.profit, companyDetailsAllOfFinancialData.profit) &&
        Objects.equals(this.assets, companyDetailsAllOfFinancialData.assets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(revenue, profit, assets);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanyDetailsAllOfFinancialData {\n");
    sb.append("    revenue: ").append(toIndentedString(revenue)).append("\n");
    sb.append("    profit: ").append(toIndentedString(profit)).append("\n");
    sb.append("    assets: ").append(toIndentedString(assets)).append("\n");
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

