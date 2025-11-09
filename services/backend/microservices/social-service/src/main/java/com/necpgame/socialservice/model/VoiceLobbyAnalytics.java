package com.necpgame.socialservice.model;

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
 * VoiceLobbyAnalytics
 */


public class VoiceLobbyAnalytics {

  private @Nullable Integer openLobbies;

  private @Nullable Integer activeParticipants;

  private @Nullable BigDecimal avgFillTimeMinutes;

  private @Nullable BigDecimal avgReadyCheckSuccessRate;

  private @Nullable Integer avgLatencyMs;

  public VoiceLobbyAnalytics openLobbies(@Nullable Integer openLobbies) {
    this.openLobbies = openLobbies;
    return this;
  }

  /**
   * Get openLobbies
   * @return openLobbies
   */
  
  @Schema(name = "openLobbies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("openLobbies")
  public @Nullable Integer getOpenLobbies() {
    return openLobbies;
  }

  public void setOpenLobbies(@Nullable Integer openLobbies) {
    this.openLobbies = openLobbies;
  }

  public VoiceLobbyAnalytics activeParticipants(@Nullable Integer activeParticipants) {
    this.activeParticipants = activeParticipants;
    return this;
  }

  /**
   * Get activeParticipants
   * @return activeParticipants
   */
  
  @Schema(name = "activeParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeParticipants")
  public @Nullable Integer getActiveParticipants() {
    return activeParticipants;
  }

  public void setActiveParticipants(@Nullable Integer activeParticipants) {
    this.activeParticipants = activeParticipants;
  }

  public VoiceLobbyAnalytics avgFillTimeMinutes(@Nullable BigDecimal avgFillTimeMinutes) {
    this.avgFillTimeMinutes = avgFillTimeMinutes;
    return this;
  }

  /**
   * Get avgFillTimeMinutes
   * @return avgFillTimeMinutes
   */
  @Valid 
  @Schema(name = "avgFillTimeMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avgFillTimeMinutes")
  public @Nullable BigDecimal getAvgFillTimeMinutes() {
    return avgFillTimeMinutes;
  }

  public void setAvgFillTimeMinutes(@Nullable BigDecimal avgFillTimeMinutes) {
    this.avgFillTimeMinutes = avgFillTimeMinutes;
  }

  public VoiceLobbyAnalytics avgReadyCheckSuccessRate(@Nullable BigDecimal avgReadyCheckSuccessRate) {
    this.avgReadyCheckSuccessRate = avgReadyCheckSuccessRate;
    return this;
  }

  /**
   * Get avgReadyCheckSuccessRate
   * @return avgReadyCheckSuccessRate
   */
  @Valid 
  @Schema(name = "avgReadyCheckSuccessRate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avgReadyCheckSuccessRate")
  public @Nullable BigDecimal getAvgReadyCheckSuccessRate() {
    return avgReadyCheckSuccessRate;
  }

  public void setAvgReadyCheckSuccessRate(@Nullable BigDecimal avgReadyCheckSuccessRate) {
    this.avgReadyCheckSuccessRate = avgReadyCheckSuccessRate;
  }

  public VoiceLobbyAnalytics avgLatencyMs(@Nullable Integer avgLatencyMs) {
    this.avgLatencyMs = avgLatencyMs;
    return this;
  }

  /**
   * Get avgLatencyMs
   * @return avgLatencyMs
   */
  
  @Schema(name = "avgLatencyMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("avgLatencyMs")
  public @Nullable Integer getAvgLatencyMs() {
    return avgLatencyMs;
  }

  public void setAvgLatencyMs(@Nullable Integer avgLatencyMs) {
    this.avgLatencyMs = avgLatencyMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceLobbyAnalytics voiceLobbyAnalytics = (VoiceLobbyAnalytics) o;
    return Objects.equals(this.openLobbies, voiceLobbyAnalytics.openLobbies) &&
        Objects.equals(this.activeParticipants, voiceLobbyAnalytics.activeParticipants) &&
        Objects.equals(this.avgFillTimeMinutes, voiceLobbyAnalytics.avgFillTimeMinutes) &&
        Objects.equals(this.avgReadyCheckSuccessRate, voiceLobbyAnalytics.avgReadyCheckSuccessRate) &&
        Objects.equals(this.avgLatencyMs, voiceLobbyAnalytics.avgLatencyMs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(openLobbies, activeParticipants, avgFillTimeMinutes, avgReadyCheckSuccessRate, avgLatencyMs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceLobbyAnalytics {\n");
    sb.append("    openLobbies: ").append(toIndentedString(openLobbies)).append("\n");
    sb.append("    activeParticipants: ").append(toIndentedString(activeParticipants)).append("\n");
    sb.append("    avgFillTimeMinutes: ").append(toIndentedString(avgFillTimeMinutes)).append("\n");
    sb.append("    avgReadyCheckSuccessRate: ").append(toIndentedString(avgReadyCheckSuccessRate)).append("\n");
    sb.append("    avgLatencyMs: ").append(toIndentedString(avgLatencyMs)).append("\n");
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

