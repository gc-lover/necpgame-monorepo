package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * BadLuckProtection
 */


public class BadLuckProtection {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastLegendaryAt;

  private @Nullable Boolean protectionActive;

  private @Nullable String nextGuaranteedIn;

  private @Nullable BigDecimal progressPercent;

  public BadLuckProtection lastLegendaryAt(@Nullable OffsetDateTime lastLegendaryAt) {
    this.lastLegendaryAt = lastLegendaryAt;
    return this;
  }

  /**
   * Get lastLegendaryAt
   * @return lastLegendaryAt
   */
  @Valid 
  @Schema(name = "lastLegendaryAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastLegendaryAt")
  public @Nullable OffsetDateTime getLastLegendaryAt() {
    return lastLegendaryAt;
  }

  public void setLastLegendaryAt(@Nullable OffsetDateTime lastLegendaryAt) {
    this.lastLegendaryAt = lastLegendaryAt;
  }

  public BadLuckProtection protectionActive(@Nullable Boolean protectionActive) {
    this.protectionActive = protectionActive;
    return this;
  }

  /**
   * Get protectionActive
   * @return protectionActive
   */
  
  @Schema(name = "protectionActive", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("protectionActive")
  public @Nullable Boolean getProtectionActive() {
    return protectionActive;
  }

  public void setProtectionActive(@Nullable Boolean protectionActive) {
    this.protectionActive = protectionActive;
  }

  public BadLuckProtection nextGuaranteedIn(@Nullable String nextGuaranteedIn) {
    this.nextGuaranteedIn = nextGuaranteedIn;
    return this;
  }

  /**
   * Get nextGuaranteedIn
   * @return nextGuaranteedIn
   */
  
  @Schema(name = "nextGuaranteedIn", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextGuaranteedIn")
  public @Nullable String getNextGuaranteedIn() {
    return nextGuaranteedIn;
  }

  public void setNextGuaranteedIn(@Nullable String nextGuaranteedIn) {
    this.nextGuaranteedIn = nextGuaranteedIn;
  }

  public BadLuckProtection progressPercent(@Nullable BigDecimal progressPercent) {
    this.progressPercent = progressPercent;
    return this;
  }

  /**
   * Get progressPercent
   * @return progressPercent
   */
  @Valid 
  @Schema(name = "progressPercent", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progressPercent")
  public @Nullable BigDecimal getProgressPercent() {
    return progressPercent;
  }

  public void setProgressPercent(@Nullable BigDecimal progressPercent) {
    this.progressPercent = progressPercent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BadLuckProtection badLuckProtection = (BadLuckProtection) o;
    return Objects.equals(this.lastLegendaryAt, badLuckProtection.lastLegendaryAt) &&
        Objects.equals(this.protectionActive, badLuckProtection.protectionActive) &&
        Objects.equals(this.nextGuaranteedIn, badLuckProtection.nextGuaranteedIn) &&
        Objects.equals(this.progressPercent, badLuckProtection.progressPercent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lastLegendaryAt, protectionActive, nextGuaranteedIn, progressPercent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BadLuckProtection {\n");
    sb.append("    lastLegendaryAt: ").append(toIndentedString(lastLegendaryAt)).append("\n");
    sb.append("    protectionActive: ").append(toIndentedString(protectionActive)).append("\n");
    sb.append("    nextGuaranteedIn: ").append(toIndentedString(nextGuaranteedIn)).append("\n");
    sb.append("    progressPercent: ").append(toIndentedString(progressPercent)).append("\n");
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

