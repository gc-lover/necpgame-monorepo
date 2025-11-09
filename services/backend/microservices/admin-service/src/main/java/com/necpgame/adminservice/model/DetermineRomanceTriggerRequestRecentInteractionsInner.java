package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DetermineRomanceTriggerRequestRecentInteractionsInner
 */

@JsonTypeName("determineRomanceTrigger_request_recent_interactions_inner")

public class DetermineRomanceTriggerRequestRecentInteractionsInner {

  private @Nullable String interactionType;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable BigDecimal impactScore;

  public DetermineRomanceTriggerRequestRecentInteractionsInner interactionType(@Nullable String interactionType) {
    this.interactionType = interactionType;
    return this;
  }

  /**
   * Get interactionType
   * @return interactionType
   */
  
  @Schema(name = "interaction_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("interaction_type")
  public @Nullable String getInteractionType() {
    return interactionType;
  }

  public void setInteractionType(@Nullable String interactionType) {
    this.interactionType = interactionType;
  }

  public DetermineRomanceTriggerRequestRecentInteractionsInner timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public DetermineRomanceTriggerRequestRecentInteractionsInner impactScore(@Nullable BigDecimal impactScore) {
    this.impactScore = impactScore;
    return this;
  }

  /**
   * Get impactScore
   * @return impactScore
   */
  @Valid 
  @Schema(name = "impact_score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact_score")
  public @Nullable BigDecimal getImpactScore() {
    return impactScore;
  }

  public void setImpactScore(@Nullable BigDecimal impactScore) {
    this.impactScore = impactScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DetermineRomanceTriggerRequestRecentInteractionsInner determineRomanceTriggerRequestRecentInteractionsInner = (DetermineRomanceTriggerRequestRecentInteractionsInner) o;
    return Objects.equals(this.interactionType, determineRomanceTriggerRequestRecentInteractionsInner.interactionType) &&
        Objects.equals(this.timestamp, determineRomanceTriggerRequestRecentInteractionsInner.timestamp) &&
        Objects.equals(this.impactScore, determineRomanceTriggerRequestRecentInteractionsInner.impactScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(interactionType, timestamp, impactScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DetermineRomanceTriggerRequestRecentInteractionsInner {\n");
    sb.append("    interactionType: ").append(toIndentedString(interactionType)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    impactScore: ").append(toIndentedString(impactScore)).append("\n");
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

