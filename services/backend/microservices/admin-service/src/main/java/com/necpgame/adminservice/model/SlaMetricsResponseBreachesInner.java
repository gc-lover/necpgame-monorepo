package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * SlaMetricsResponseBreachesInner
 */

@JsonTypeName("SlaMetricsResponse_breaches_inner")

public class SlaMetricsResponseBreachesInner {

  private @Nullable String ticketId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime breachedAt;

  public SlaMetricsResponseBreachesInner ticketId(@Nullable String ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  /**
   * Get ticketId
   * @return ticketId
   */
  
  @Schema(name = "ticketId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticketId")
  public @Nullable String getTicketId() {
    return ticketId;
  }

  public void setTicketId(@Nullable String ticketId) {
    this.ticketId = ticketId;
  }

  public SlaMetricsResponseBreachesInner breachedAt(@Nullable OffsetDateTime breachedAt) {
    this.breachedAt = breachedAt;
    return this;
  }

  /**
   * Get breachedAt
   * @return breachedAt
   */
  @Valid 
  @Schema(name = "breachedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("breachedAt")
  public @Nullable OffsetDateTime getBreachedAt() {
    return breachedAt;
  }

  public void setBreachedAt(@Nullable OffsetDateTime breachedAt) {
    this.breachedAt = breachedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SlaMetricsResponseBreachesInner slaMetricsResponseBreachesInner = (SlaMetricsResponseBreachesInner) o;
    return Objects.equals(this.ticketId, slaMetricsResponseBreachesInner.ticketId) &&
        Objects.equals(this.breachedAt, slaMetricsResponseBreachesInner.breachedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, breachedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SlaMetricsResponseBreachesInner {\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    breachedAt: ").append(toIndentedString(breachedAt)).append("\n");
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

