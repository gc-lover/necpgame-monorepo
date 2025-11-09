package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.GetConversionMetrics200ResponseFunnelStagesInner;
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
 * GetConversionMetrics200Response
 */

@JsonTypeName("getConversionMetrics_200_response")

public class GetConversionMetrics200Response {

  @Valid
  private List<@Valid GetConversionMetrics200ResponseFunnelStagesInner> funnelStages = new ArrayList<>();

  public GetConversionMetrics200Response funnelStages(List<@Valid GetConversionMetrics200ResponseFunnelStagesInner> funnelStages) {
    this.funnelStages = funnelStages;
    return this;
  }

  public GetConversionMetrics200Response addFunnelStagesItem(GetConversionMetrics200ResponseFunnelStagesInner funnelStagesItem) {
    if (this.funnelStages == null) {
      this.funnelStages = new ArrayList<>();
    }
    this.funnelStages.add(funnelStagesItem);
    return this;
  }

  /**
   * Get funnelStages
   * @return funnelStages
   */
  @Valid 
  @Schema(name = "funnel_stages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("funnel_stages")
  public List<@Valid GetConversionMetrics200ResponseFunnelStagesInner> getFunnelStages() {
    return funnelStages;
  }

  public void setFunnelStages(List<@Valid GetConversionMetrics200ResponseFunnelStagesInner> funnelStages) {
    this.funnelStages = funnelStages;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetConversionMetrics200Response getConversionMetrics200Response = (GetConversionMetrics200Response) o;
    return Objects.equals(this.funnelStages, getConversionMetrics200Response.funnelStages);
  }

  @Override
  public int hashCode() {
    return Objects.hash(funnelStages);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetConversionMetrics200Response {\n");
    sb.append("    funnelStages: ").append(toIndentedString(funnelStages)).append("\n");
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

