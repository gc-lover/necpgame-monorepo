package com.necpgame.realtimeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.realtimeservice.model.RealtimeInstanceMetricsRequestPercentileTick;
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
 * RealtimeInstanceMetricsRequest
 */


public class RealtimeInstanceMetricsRequest {

  private RealtimeInstanceMetricsRequestPercentileTick percentileTick;

  private Integer packetsPerSecond;

  private @Nullable BigDecimal redisLatencyMs;

  private @Nullable Integer interestQueueDepth;

  private @Nullable BigDecimal netBandwidthMbps;

  public RealtimeInstanceMetricsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RealtimeInstanceMetricsRequest(RealtimeInstanceMetricsRequestPercentileTick percentileTick, Integer packetsPerSecond) {
    this.percentileTick = percentileTick;
    this.packetsPerSecond = packetsPerSecond;
  }

  public RealtimeInstanceMetricsRequest percentileTick(RealtimeInstanceMetricsRequestPercentileTick percentileTick) {
    this.percentileTick = percentileTick;
    return this;
  }

  /**
   * Get percentileTick
   * @return percentileTick
   */
  @NotNull @Valid 
  @Schema(name = "percentileTick", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("percentileTick")
  public RealtimeInstanceMetricsRequestPercentileTick getPercentileTick() {
    return percentileTick;
  }

  public void setPercentileTick(RealtimeInstanceMetricsRequestPercentileTick percentileTick) {
    this.percentileTick = percentileTick;
  }

  public RealtimeInstanceMetricsRequest packetsPerSecond(Integer packetsPerSecond) {
    this.packetsPerSecond = packetsPerSecond;
    return this;
  }

  /**
   * Get packetsPerSecond
   * @return packetsPerSecond
   */
  @NotNull 
  @Schema(name = "packetsPerSecond", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("packetsPerSecond")
  public Integer getPacketsPerSecond() {
    return packetsPerSecond;
  }

  public void setPacketsPerSecond(Integer packetsPerSecond) {
    this.packetsPerSecond = packetsPerSecond;
  }

  public RealtimeInstanceMetricsRequest redisLatencyMs(@Nullable BigDecimal redisLatencyMs) {
    this.redisLatencyMs = redisLatencyMs;
    return this;
  }

  /**
   * Get redisLatencyMs
   * @return redisLatencyMs
   */
  @Valid 
  @Schema(name = "redisLatencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("redisLatencyMs")
  public @Nullable BigDecimal getRedisLatencyMs() {
    return redisLatencyMs;
  }

  public void setRedisLatencyMs(@Nullable BigDecimal redisLatencyMs) {
    this.redisLatencyMs = redisLatencyMs;
  }

  public RealtimeInstanceMetricsRequest interestQueueDepth(@Nullable Integer interestQueueDepth) {
    this.interestQueueDepth = interestQueueDepth;
    return this;
  }

  /**
   * Get interestQueueDepth
   * @return interestQueueDepth
   */
  
  @Schema(name = "interestQueueDepth", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("interestQueueDepth")
  public @Nullable Integer getInterestQueueDepth() {
    return interestQueueDepth;
  }

  public void setInterestQueueDepth(@Nullable Integer interestQueueDepth) {
    this.interestQueueDepth = interestQueueDepth;
  }

  public RealtimeInstanceMetricsRequest netBandwidthMbps(@Nullable BigDecimal netBandwidthMbps) {
    this.netBandwidthMbps = netBandwidthMbps;
    return this;
  }

  /**
   * Get netBandwidthMbps
   * @return netBandwidthMbps
   */
  @Valid 
  @Schema(name = "netBandwidthMbps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("netBandwidthMbps")
  public @Nullable BigDecimal getNetBandwidthMbps() {
    return netBandwidthMbps;
  }

  public void setNetBandwidthMbps(@Nullable BigDecimal netBandwidthMbps) {
    this.netBandwidthMbps = netBandwidthMbps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RealtimeInstanceMetricsRequest realtimeInstanceMetricsRequest = (RealtimeInstanceMetricsRequest) o;
    return Objects.equals(this.percentileTick, realtimeInstanceMetricsRequest.percentileTick) &&
        Objects.equals(this.packetsPerSecond, realtimeInstanceMetricsRequest.packetsPerSecond) &&
        Objects.equals(this.redisLatencyMs, realtimeInstanceMetricsRequest.redisLatencyMs) &&
        Objects.equals(this.interestQueueDepth, realtimeInstanceMetricsRequest.interestQueueDepth) &&
        Objects.equals(this.netBandwidthMbps, realtimeInstanceMetricsRequest.netBandwidthMbps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(percentileTick, packetsPerSecond, redisLatencyMs, interestQueueDepth, netBandwidthMbps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RealtimeInstanceMetricsRequest {\n");
    sb.append("    percentileTick: ").append(toIndentedString(percentileTick)).append("\n");
    sb.append("    packetsPerSecond: ").append(toIndentedString(packetsPerSecond)).append("\n");
    sb.append("    redisLatencyMs: ").append(toIndentedString(redisLatencyMs)).append("\n");
    sb.append("    interestQueueDepth: ").append(toIndentedString(interestQueueDepth)).append("\n");
    sb.append("    netBandwidthMbps: ").append(toIndentedString(netBandwidthMbps)).append("\n");
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

