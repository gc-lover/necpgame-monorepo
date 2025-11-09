package com.necpgame.sessionservice.model;

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
 * DisconnectRateMetricsByRegionInner
 */

@JsonTypeName("DisconnectRateMetrics_byRegion_inner")

public class DisconnectRateMetricsByRegionInner {

  private @Nullable String region;

  private @Nullable BigDecimal rate;

  public DisconnectRateMetricsByRegionInner region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public DisconnectRateMetricsByRegionInner rate(@Nullable BigDecimal rate) {
    this.rate = rate;
    return this;
  }

  /**
   * Get rate
   * @return rate
   */
  @Valid 
  @Schema(name = "rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rate")
  public @Nullable BigDecimal getRate() {
    return rate;
  }

  public void setRate(@Nullable BigDecimal rate) {
    this.rate = rate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DisconnectRateMetricsByRegionInner disconnectRateMetricsByRegionInner = (DisconnectRateMetricsByRegionInner) o;
    return Objects.equals(this.region, disconnectRateMetricsByRegionInner.region) &&
        Objects.equals(this.rate, disconnectRateMetricsByRegionInner.rate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(region, rate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DisconnectRateMetricsByRegionInner {\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    rate: ").append(toIndentedString(rate)).append("\n");
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

