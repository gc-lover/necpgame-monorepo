package com.necpgame.realtimeservice.model;

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
 * RealtimeInstanceMetricsRequestPercentileTick
 */

@JsonTypeName("RealtimeInstanceMetricsRequest_percentileTick")

public class RealtimeInstanceMetricsRequestPercentileTick {

  private BigDecimal p50;

  private BigDecimal p95;

  public RealtimeInstanceMetricsRequestPercentileTick() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RealtimeInstanceMetricsRequestPercentileTick(BigDecimal p50, BigDecimal p95) {
    this.p50 = p50;
    this.p95 = p95;
  }

  public RealtimeInstanceMetricsRequestPercentileTick p50(BigDecimal p50) {
    this.p50 = p50;
    return this;
  }

  /**
   * Get p50
   * @return p50
   */
  @NotNull @Valid 
  @Schema(name = "p50", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("p50")
  public BigDecimal getP50() {
    return p50;
  }

  public void setP50(BigDecimal p50) {
    this.p50 = p50;
  }

  public RealtimeInstanceMetricsRequestPercentileTick p95(BigDecimal p95) {
    this.p95 = p95;
    return this;
  }

  /**
   * Get p95
   * @return p95
   */
  @NotNull @Valid 
  @Schema(name = "p95", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("p95")
  public BigDecimal getP95() {
    return p95;
  }

  public void setP95(BigDecimal p95) {
    this.p95 = p95;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RealtimeInstanceMetricsRequestPercentileTick realtimeInstanceMetricsRequestPercentileTick = (RealtimeInstanceMetricsRequestPercentileTick) o;
    return Objects.equals(this.p50, realtimeInstanceMetricsRequestPercentileTick.p50) &&
        Objects.equals(this.p95, realtimeInstanceMetricsRequestPercentileTick.p95);
  }

  @Override
  public int hashCode() {
    return Objects.hash(p50, p95);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RealtimeInstanceMetricsRequestPercentileTick {\n");
    sb.append("    p50: ").append(toIndentedString(p50)).append("\n");
    sb.append("    p95: ").append(toIndentedString(p95)).append("\n");
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

