package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetHiringMarket200Response
 */

@JsonTypeName("getHiringMarket_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetHiringMarket200Response {

  @Valid
  private Map<String, Integer> averageCosts = new HashMap<>();

  @Valid
  private Map<String, Integer> availability = new HashMap<>();

  @Valid
  private List<Object> marketTrends = new ArrayList<>();

  public GetHiringMarket200Response averageCosts(Map<String, Integer> averageCosts) {
    this.averageCosts = averageCosts;
    return this;
  }

  public GetHiringMarket200Response putAverageCostsItem(String key, Integer averageCostsItem) {
    if (this.averageCosts == null) {
      this.averageCosts = new HashMap<>();
    }
    this.averageCosts.put(key, averageCostsItem);
    return this;
  }

  /**
   * Get averageCosts
   * @return averageCosts
   */
  
  @Schema(name = "average_costs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("average_costs")
  public Map<String, Integer> getAverageCosts() {
    return averageCosts;
  }

  public void setAverageCosts(Map<String, Integer> averageCosts) {
    this.averageCosts = averageCosts;
  }

  public GetHiringMarket200Response availability(Map<String, Integer> availability) {
    this.availability = availability;
    return this;
  }

  public GetHiringMarket200Response putAvailabilityItem(String key, Integer availabilityItem) {
    if (this.availability == null) {
      this.availability = new HashMap<>();
    }
    this.availability.put(key, availabilityItem);
    return this;
  }

  /**
   * Доступность по типам
   * @return availability
   */
  
  @Schema(name = "availability", description = "Доступность по типам", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("availability")
  public Map<String, Integer> getAvailability() {
    return availability;
  }

  public void setAvailability(Map<String, Integer> availability) {
    this.availability = availability;
  }

  public GetHiringMarket200Response marketTrends(List<Object> marketTrends) {
    this.marketTrends = marketTrends;
    return this;
  }

  public GetHiringMarket200Response addMarketTrendsItem(Object marketTrendsItem) {
    if (this.marketTrends == null) {
      this.marketTrends = new ArrayList<>();
    }
    this.marketTrends.add(marketTrendsItem);
    return this;
  }

  /**
   * Get marketTrends
   * @return marketTrends
   */
  
  @Schema(name = "market_trends", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("market_trends")
  public List<Object> getMarketTrends() {
    return marketTrends;
  }

  public void setMarketTrends(List<Object> marketTrends) {
    this.marketTrends = marketTrends;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetHiringMarket200Response getHiringMarket200Response = (GetHiringMarket200Response) o;
    return Objects.equals(this.averageCosts, getHiringMarket200Response.averageCosts) &&
        Objects.equals(this.availability, getHiringMarket200Response.availability) &&
        Objects.equals(this.marketTrends, getHiringMarket200Response.marketTrends);
  }

  @Override
  public int hashCode() {
    return Objects.hash(averageCosts, availability, marketTrends);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetHiringMarket200Response {\n");
    sb.append("    averageCosts: ").append(toIndentedString(averageCosts)).append("\n");
    sb.append("    availability: ").append(toIndentedString(availability)).append("\n");
    sb.append("    marketTrends: ").append(toIndentedString(marketTrends)).append("\n");
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

