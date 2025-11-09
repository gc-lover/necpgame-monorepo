package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetConversionMetrics200ResponseFunnelStagesInner
 */

@JsonTypeName("getConversionMetrics_200_response_funnel_stages_inner")

public class GetConversionMetrics200ResponseFunnelStagesInner {

  private @Nullable String stage;

  private @Nullable Integer users;

  private @Nullable BigDecimal conversionRate;

  public GetConversionMetrics200ResponseFunnelStagesInner stage(@Nullable String stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Get stage
   * @return stage
   */
  
  @Schema(name = "stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage")
  public @Nullable String getStage() {
    return stage;
  }

  public void setStage(@Nullable String stage) {
    this.stage = stage;
  }

  public GetConversionMetrics200ResponseFunnelStagesInner users(@Nullable Integer users) {
    this.users = users;
    return this;
  }

  /**
   * Get users
   * @return users
   */
  
  @Schema(name = "users", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("users")
  public @Nullable Integer getUsers() {
    return users;
  }

  public void setUsers(@Nullable Integer users) {
    this.users = users;
  }

  public GetConversionMetrics200ResponseFunnelStagesInner conversionRate(@Nullable BigDecimal conversionRate) {
    this.conversionRate = conversionRate;
    return this;
  }

  /**
   * Get conversionRate
   * @return conversionRate
   */
  @Valid 
  @Schema(name = "conversion_rate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conversion_rate")
  public @Nullable BigDecimal getConversionRate() {
    return conversionRate;
  }

  public void setConversionRate(@Nullable BigDecimal conversionRate) {
    this.conversionRate = conversionRate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetConversionMetrics200ResponseFunnelStagesInner getConversionMetrics200ResponseFunnelStagesInner = (GetConversionMetrics200ResponseFunnelStagesInner) o;
    return Objects.equals(this.stage, getConversionMetrics200ResponseFunnelStagesInner.stage) &&
        Objects.equals(this.users, getConversionMetrics200ResponseFunnelStagesInner.users) &&
        Objects.equals(this.conversionRate, getConversionMetrics200ResponseFunnelStagesInner.conversionRate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stage, users, conversionRate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetConversionMetrics200ResponseFunnelStagesInner {\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
    sb.append("    conversionRate: ").append(toIndentedString(conversionRate)).append("\n");
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

