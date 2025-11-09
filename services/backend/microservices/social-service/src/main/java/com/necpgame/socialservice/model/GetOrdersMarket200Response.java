package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetOrdersMarket200Response
 */

@JsonTypeName("getOrdersMarket_200_response")

public class GetOrdersMarket200Response {

  private @Nullable Integer activeOrdersCount;

  private @Nullable Integer averagePayment;

  @Valid
  private List<Object> popularTypes = new ArrayList<>();

  @Valid
  private List<String> highDemandSkills = new ArrayList<>();

  public GetOrdersMarket200Response activeOrdersCount(@Nullable Integer activeOrdersCount) {
    this.activeOrdersCount = activeOrdersCount;
    return this;
  }

  /**
   * Get activeOrdersCount
   * @return activeOrdersCount
   */
  
  @Schema(name = "active_orders_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_orders_count")
  public @Nullable Integer getActiveOrdersCount() {
    return activeOrdersCount;
  }

  public void setActiveOrdersCount(@Nullable Integer activeOrdersCount) {
    this.activeOrdersCount = activeOrdersCount;
  }

  public GetOrdersMarket200Response averagePayment(@Nullable Integer averagePayment) {
    this.averagePayment = averagePayment;
    return this;
  }

  /**
   * Get averagePayment
   * @return averagePayment
   */
  
  @Schema(name = "average_payment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_payment")
  public @Nullable Integer getAveragePayment() {
    return averagePayment;
  }

  public void setAveragePayment(@Nullable Integer averagePayment) {
    this.averagePayment = averagePayment;
  }

  public GetOrdersMarket200Response popularTypes(List<Object> popularTypes) {
    this.popularTypes = popularTypes;
    return this;
  }

  public GetOrdersMarket200Response addPopularTypesItem(Object popularTypesItem) {
    if (this.popularTypes == null) {
      this.popularTypes = new ArrayList<>();
    }
    this.popularTypes.add(popularTypesItem);
    return this;
  }

  /**
   * Get popularTypes
   * @return popularTypes
   */
  
  @Schema(name = "popular_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("popular_types")
  public List<Object> getPopularTypes() {
    return popularTypes;
  }

  public void setPopularTypes(List<Object> popularTypes) {
    this.popularTypes = popularTypes;
  }

  public GetOrdersMarket200Response highDemandSkills(List<String> highDemandSkills) {
    this.highDemandSkills = highDemandSkills;
    return this;
  }

  public GetOrdersMarket200Response addHighDemandSkillsItem(String highDemandSkillsItem) {
    if (this.highDemandSkills == null) {
      this.highDemandSkills = new ArrayList<>();
    }
    this.highDemandSkills.add(highDemandSkillsItem);
    return this;
  }

  /**
   * Get highDemandSkills
   * @return highDemandSkills
   */
  
  @Schema(name = "high_demand_skills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("high_demand_skills")
  public List<String> getHighDemandSkills() {
    return highDemandSkills;
  }

  public void setHighDemandSkills(List<String> highDemandSkills) {
    this.highDemandSkills = highDemandSkills;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetOrdersMarket200Response getOrdersMarket200Response = (GetOrdersMarket200Response) o;
    return Objects.equals(this.activeOrdersCount, getOrdersMarket200Response.activeOrdersCount) &&
        Objects.equals(this.averagePayment, getOrdersMarket200Response.averagePayment) &&
        Objects.equals(this.popularTypes, getOrdersMarket200Response.popularTypes) &&
        Objects.equals(this.highDemandSkills, getOrdersMarket200Response.highDemandSkills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeOrdersCount, averagePayment, popularTypes, highDemandSkills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetOrdersMarket200Response {\n");
    sb.append("    activeOrdersCount: ").append(toIndentedString(activeOrdersCount)).append("\n");
    sb.append("    averagePayment: ").append(toIndentedString(averagePayment)).append("\n");
    sb.append("    popularTypes: ").append(toIndentedString(popularTypes)).append("\n");
    sb.append("    highDemandSkills: ").append(toIndentedString(highDemandSkills)).append("\n");
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

