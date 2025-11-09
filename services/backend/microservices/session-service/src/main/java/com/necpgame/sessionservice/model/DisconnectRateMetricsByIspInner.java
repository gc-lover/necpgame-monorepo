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
 * DisconnectRateMetricsByIspInner
 */

@JsonTypeName("DisconnectRateMetrics_byIsp_inner")

public class DisconnectRateMetricsByIspInner {

  private @Nullable String isp;

  private @Nullable BigDecimal rate;

  public DisconnectRateMetricsByIspInner isp(@Nullable String isp) {
    this.isp = isp;
    return this;
  }

  /**
   * Get isp
   * @return isp
   */
  
  @Schema(name = "isp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isp")
  public @Nullable String getIsp() {
    return isp;
  }

  public void setIsp(@Nullable String isp) {
    this.isp = isp;
  }

  public DisconnectRateMetricsByIspInner rate(@Nullable BigDecimal rate) {
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
    DisconnectRateMetricsByIspInner disconnectRateMetricsByIspInner = (DisconnectRateMetricsByIspInner) o;
    return Objects.equals(this.isp, disconnectRateMetricsByIspInner.isp) &&
        Objects.equals(this.rate, disconnectRateMetricsByIspInner.rate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isp, rate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DisconnectRateMetricsByIspInner {\n");
    sb.append("    isp: ").append(toIndentedString(isp)).append("\n");
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

