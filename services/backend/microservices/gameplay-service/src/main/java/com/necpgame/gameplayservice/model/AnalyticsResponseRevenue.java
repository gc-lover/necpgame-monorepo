package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.PurchaseResponseUpdatedBalance;
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
 * AnalyticsResponseRevenue
 */

@JsonTypeName("AnalyticsResponse_revenue")

public class AnalyticsResponseRevenue {

  private @Nullable Integer total;

  @Valid
  private List<@Valid PurchaseResponseUpdatedBalance> byCurrency = new ArrayList<>();

  public AnalyticsResponseRevenue total(@Nullable Integer total) {
    this.total = total;
    return this;
  }

  /**
   * Get total
   * @return total
   */
  
  @Schema(name = "total", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total")
  public @Nullable Integer getTotal() {
    return total;
  }

  public void setTotal(@Nullable Integer total) {
    this.total = total;
  }

  public AnalyticsResponseRevenue byCurrency(List<@Valid PurchaseResponseUpdatedBalance> byCurrency) {
    this.byCurrency = byCurrency;
    return this;
  }

  public AnalyticsResponseRevenue addByCurrencyItem(PurchaseResponseUpdatedBalance byCurrencyItem) {
    if (this.byCurrency == null) {
      this.byCurrency = new ArrayList<>();
    }
    this.byCurrency.add(byCurrencyItem);
    return this;
  }

  /**
   * Get byCurrency
   * @return byCurrency
   */
  @Valid 
  @Schema(name = "byCurrency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("byCurrency")
  public List<@Valid PurchaseResponseUpdatedBalance> getByCurrency() {
    return byCurrency;
  }

  public void setByCurrency(List<@Valid PurchaseResponseUpdatedBalance> byCurrency) {
    this.byCurrency = byCurrency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsResponseRevenue analyticsResponseRevenue = (AnalyticsResponseRevenue) o;
    return Objects.equals(this.total, analyticsResponseRevenue.total) &&
        Objects.equals(this.byCurrency, analyticsResponseRevenue.byCurrency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, byCurrency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsResponseRevenue {\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    byCurrency: ").append(toIndentedString(byCurrency)).append("\n");
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

