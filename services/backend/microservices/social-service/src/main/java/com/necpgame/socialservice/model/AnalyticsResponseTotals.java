package com.necpgame.socialservice.model;

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
 * AnalyticsResponseTotals
 */

@JsonTypeName("AnalyticsResponse_totals")

public class AnalyticsResponseTotals {

  private @Nullable Integer generatedCodes;

  private @Nullable Integer registeredReferrals;

  private @Nullable Integer completedReferrals;

  public AnalyticsResponseTotals generatedCodes(@Nullable Integer generatedCodes) {
    this.generatedCodes = generatedCodes;
    return this;
  }

  /**
   * Get generatedCodes
   * @return generatedCodes
   */
  
  @Schema(name = "generatedCodes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedCodes")
  public @Nullable Integer getGeneratedCodes() {
    return generatedCodes;
  }

  public void setGeneratedCodes(@Nullable Integer generatedCodes) {
    this.generatedCodes = generatedCodes;
  }

  public AnalyticsResponseTotals registeredReferrals(@Nullable Integer registeredReferrals) {
    this.registeredReferrals = registeredReferrals;
    return this;
  }

  /**
   * Get registeredReferrals
   * @return registeredReferrals
   */
  
  @Schema(name = "registeredReferrals", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("registeredReferrals")
  public @Nullable Integer getRegisteredReferrals() {
    return registeredReferrals;
  }

  public void setRegisteredReferrals(@Nullable Integer registeredReferrals) {
    this.registeredReferrals = registeredReferrals;
  }

  public AnalyticsResponseTotals completedReferrals(@Nullable Integer completedReferrals) {
    this.completedReferrals = completedReferrals;
    return this;
  }

  /**
   * Get completedReferrals
   * @return completedReferrals
   */
  
  @Schema(name = "completedReferrals", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completedReferrals")
  public @Nullable Integer getCompletedReferrals() {
    return completedReferrals;
  }

  public void setCompletedReferrals(@Nullable Integer completedReferrals) {
    this.completedReferrals = completedReferrals;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AnalyticsResponseTotals analyticsResponseTotals = (AnalyticsResponseTotals) o;
    return Objects.equals(this.generatedCodes, analyticsResponseTotals.generatedCodes) &&
        Objects.equals(this.registeredReferrals, analyticsResponseTotals.registeredReferrals) &&
        Objects.equals(this.completedReferrals, analyticsResponseTotals.completedReferrals);
  }

  @Override
  public int hashCode() {
    return Objects.hash(generatedCodes, registeredReferrals, completedReferrals);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AnalyticsResponseTotals {\n");
    sb.append("    generatedCodes: ").append(toIndentedString(generatedCodes)).append("\n");
    sb.append("    registeredReferrals: ").append(toIndentedString(registeredReferrals)).append("\n");
    sb.append("    completedReferrals: ").append(toIndentedString(completedReferrals)).append("\n");
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

