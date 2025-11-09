package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TechnicalDebtCategoryPriorityDistribution
 */

@JsonTypeName("TechnicalDebtCategory_priority_distribution")

public class TechnicalDebtCategoryPriorityDistribution {

  private @Nullable Integer critical;

  private @Nullable Integer high;

  private @Nullable Integer medium;

  private @Nullable Integer low;

  public TechnicalDebtCategoryPriorityDistribution critical(@Nullable Integer critical) {
    this.critical = critical;
    return this;
  }

  /**
   * Get critical
   * @return critical
   */
  
  @Schema(name = "critical", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("critical")
  public @Nullable Integer getCritical() {
    return critical;
  }

  public void setCritical(@Nullable Integer critical) {
    this.critical = critical;
  }

  public TechnicalDebtCategoryPriorityDistribution high(@Nullable Integer high) {
    this.high = high;
    return this;
  }

  /**
   * Get high
   * @return high
   */
  
  @Schema(name = "high", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("high")
  public @Nullable Integer getHigh() {
    return high;
  }

  public void setHigh(@Nullable Integer high) {
    this.high = high;
  }

  public TechnicalDebtCategoryPriorityDistribution medium(@Nullable Integer medium) {
    this.medium = medium;
    return this;
  }

  /**
   * Get medium
   * @return medium
   */
  
  @Schema(name = "medium", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("medium")
  public @Nullable Integer getMedium() {
    return medium;
  }

  public void setMedium(@Nullable Integer medium) {
    this.medium = medium;
  }

  public TechnicalDebtCategoryPriorityDistribution low(@Nullable Integer low) {
    this.low = low;
    return this;
  }

  /**
   * Get low
   * @return low
   */
  
  @Schema(name = "low", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("low")
  public @Nullable Integer getLow() {
    return low;
  }

  public void setLow(@Nullable Integer low) {
    this.low = low;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnicalDebtCategoryPriorityDistribution technicalDebtCategoryPriorityDistribution = (TechnicalDebtCategoryPriorityDistribution) o;
    return Objects.equals(this.critical, technicalDebtCategoryPriorityDistribution.critical) &&
        Objects.equals(this.high, technicalDebtCategoryPriorityDistribution.high) &&
        Objects.equals(this.medium, technicalDebtCategoryPriorityDistribution.medium) &&
        Objects.equals(this.low, technicalDebtCategoryPriorityDistribution.low);
  }

  @Override
  public int hashCode() {
    return Objects.hash(critical, high, medium, low);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnicalDebtCategoryPriorityDistribution {\n");
    sb.append("    critical: ").append(toIndentedString(critical)).append("\n");
    sb.append("    high: ").append(toIndentedString(high)).append("\n");
    sb.append("    medium: ").append(toIndentedString(medium)).append("\n");
    sb.append("    low: ").append(toIndentedString(low)).append("\n");
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

