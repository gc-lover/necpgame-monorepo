package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.GetRateLimits200ResponseEndpointLimitsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetRateLimits200Response
 */

@JsonTypeName("getRateLimits_200_response")

public class GetRateLimits200Response {

  private @Nullable Object globalLimits;

  @Valid
  private List<@Valid GetRateLimits200ResponseEndpointLimitsInner> endpointLimits = new ArrayList<>();

  public GetRateLimits200Response globalLimits(@Nullable Object globalLimits) {
    this.globalLimits = globalLimits;
    return this;
  }

  /**
   * Get globalLimits
   * @return globalLimits
   */
  
  @Schema(name = "global_limits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("global_limits")
  public @Nullable Object getGlobalLimits() {
    return globalLimits;
  }

  public void setGlobalLimits(@Nullable Object globalLimits) {
    this.globalLimits = globalLimits;
  }

  public GetRateLimits200Response endpointLimits(List<@Valid GetRateLimits200ResponseEndpointLimitsInner> endpointLimits) {
    this.endpointLimits = endpointLimits;
    return this;
  }

  public GetRateLimits200Response addEndpointLimitsItem(GetRateLimits200ResponseEndpointLimitsInner endpointLimitsItem) {
    if (this.endpointLimits == null) {
      this.endpointLimits = new ArrayList<>();
    }
    this.endpointLimits.add(endpointLimitsItem);
    return this;
  }

  /**
   * Get endpointLimits
   * @return endpointLimits
   */
  @Valid 
  @Schema(name = "endpoint_limits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endpoint_limits")
  public List<@Valid GetRateLimits200ResponseEndpointLimitsInner> getEndpointLimits() {
    return endpointLimits;
  }

  public void setEndpointLimits(List<@Valid GetRateLimits200ResponseEndpointLimitsInner> endpointLimits) {
    this.endpointLimits = endpointLimits;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetRateLimits200Response getRateLimits200Response = (GetRateLimits200Response) o;
    return Objects.equals(this.globalLimits, getRateLimits200Response.globalLimits) &&
        Objects.equals(this.endpointLimits, getRateLimits200Response.endpointLimits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(globalLimits, endpointLimits);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRateLimits200Response {\n");
    sb.append("    globalLimits: ").append(toIndentedString(globalLimits)).append("\n");
    sb.append("    endpointLimits: ").append(toIndentedString(endpointLimits)).append("\n");
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

