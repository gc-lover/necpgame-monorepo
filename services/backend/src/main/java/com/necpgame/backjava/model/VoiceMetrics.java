package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * VoiceMetrics
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class VoiceMetrics {

  private @Nullable BigDecimal latencyMs;

  private @Nullable BigDecimal packetLossPercent;

  private @Nullable Integer activeSpeakers;

  private @Nullable BigDecimal averageSpeakTimeSeconds;

  private @Nullable Integer peakConcurrent;

  public VoiceMetrics latencyMs(@Nullable BigDecimal latencyMs) {
    this.latencyMs = latencyMs;
    return this;
  }

  /**
   * Get latencyMs
   * @return latencyMs
   */
  @Valid 
  @Schema(name = "latencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latencyMs")
  public @Nullable BigDecimal getLatencyMs() {
    return latencyMs;
  }

  public void setLatencyMs(@Nullable BigDecimal latencyMs) {
    this.latencyMs = latencyMs;
  }

  public VoiceMetrics packetLossPercent(@Nullable BigDecimal packetLossPercent) {
    this.packetLossPercent = packetLossPercent;
    return this;
  }

  /**
   * Get packetLossPercent
   * @return packetLossPercent
   */
  @Valid 
  @Schema(name = "packetLossPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("packetLossPercent")
  public @Nullable BigDecimal getPacketLossPercent() {
    return packetLossPercent;
  }

  public void setPacketLossPercent(@Nullable BigDecimal packetLossPercent) {
    this.packetLossPercent = packetLossPercent;
  }

  public VoiceMetrics activeSpeakers(@Nullable Integer activeSpeakers) {
    this.activeSpeakers = activeSpeakers;
    return this;
  }

  /**
   * Get activeSpeakers
   * @return activeSpeakers
   */
  
  @Schema(name = "activeSpeakers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeSpeakers")
  public @Nullable Integer getActiveSpeakers() {
    return activeSpeakers;
  }

  public void setActiveSpeakers(@Nullable Integer activeSpeakers) {
    this.activeSpeakers = activeSpeakers;
  }

  public VoiceMetrics averageSpeakTimeSeconds(@Nullable BigDecimal averageSpeakTimeSeconds) {
    this.averageSpeakTimeSeconds = averageSpeakTimeSeconds;
    return this;
  }

  /**
   * Get averageSpeakTimeSeconds
   * @return averageSpeakTimeSeconds
   */
  @Valid 
  @Schema(name = "averageSpeakTimeSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("averageSpeakTimeSeconds")
  public @Nullable BigDecimal getAverageSpeakTimeSeconds() {
    return averageSpeakTimeSeconds;
  }

  public void setAverageSpeakTimeSeconds(@Nullable BigDecimal averageSpeakTimeSeconds) {
    this.averageSpeakTimeSeconds = averageSpeakTimeSeconds;
  }

  public VoiceMetrics peakConcurrent(@Nullable Integer peakConcurrent) {
    this.peakConcurrent = peakConcurrent;
    return this;
  }

  /**
   * Get peakConcurrent
   * @return peakConcurrent
   */
  
  @Schema(name = "peakConcurrent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("peakConcurrent")
  public @Nullable Integer getPeakConcurrent() {
    return peakConcurrent;
  }

  public void setPeakConcurrent(@Nullable Integer peakConcurrent) {
    this.peakConcurrent = peakConcurrent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceMetrics voiceMetrics = (VoiceMetrics) o;
    return Objects.equals(this.latencyMs, voiceMetrics.latencyMs) &&
        Objects.equals(this.packetLossPercent, voiceMetrics.packetLossPercent) &&
        Objects.equals(this.activeSpeakers, voiceMetrics.activeSpeakers) &&
        Objects.equals(this.averageSpeakTimeSeconds, voiceMetrics.averageSpeakTimeSeconds) &&
        Objects.equals(this.peakConcurrent, voiceMetrics.peakConcurrent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(latencyMs, packetLossPercent, activeSpeakers, averageSpeakTimeSeconds, peakConcurrent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceMetrics {\n");
    sb.append("    latencyMs: ").append(toIndentedString(latencyMs)).append("\n");
    sb.append("    packetLossPercent: ").append(toIndentedString(packetLossPercent)).append("\n");
    sb.append("    activeSpeakers: ").append(toIndentedString(activeSpeakers)).append("\n");
    sb.append("    averageSpeakTimeSeconds: ").append(toIndentedString(averageSpeakTimeSeconds)).append("\n");
    sb.append("    peakConcurrent: ").append(toIndentedString(peakConcurrent)).append("\n");
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

