package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import javax.annotation.Generated;

/**
 * CalculateROIRequest
 */

@JsonTypeName("calculateROI_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-08T01:01:47.984013400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CalculateROIRequest {

  private @Nullable String opportunityId;

  private @Nullable Integer amount;

  private @Nullable Integer durationDays;

  public CalculateROIRequest opportunityId(@Nullable String opportunityId) {
    this.opportunityId = opportunityId;
    return this;
  }

  /**
   * Get opportunityId
   * @return opportunityId
   */
  
  @Schema(name = "opportunity_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opportunity_id")
  public @Nullable String getOpportunityId() {
    return opportunityId;
  }

  public void setOpportunityId(@Nullable String opportunityId) {
    this.opportunityId = opportunityId;
  }

  public CalculateROIRequest amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  public CalculateROIRequest durationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
    return this;
  }

  /**
   * Get durationDays
   * @return durationDays
   */
  
  @Schema(name = "duration_days", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_days")
  public @Nullable Integer getDurationDays() {
    return durationDays;
  }

  public void setDurationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateROIRequest calculateROIRequest = (CalculateROIRequest) o;
    return Objects.equals(this.opportunityId, calculateROIRequest.opportunityId) &&
        Objects.equals(this.amount, calculateROIRequest.amount) &&
        Objects.equals(this.durationDays, calculateROIRequest.durationDays);
  }

  @Override
  public int hashCode() {
    return Objects.hash(opportunityId, amount, durationDays);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateROIRequest {\n");
    sb.append("    opportunityId: ").append(toIndentedString(opportunityId)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    durationDays: ").append(toIndentedString(durationDays)).append("\n");
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

