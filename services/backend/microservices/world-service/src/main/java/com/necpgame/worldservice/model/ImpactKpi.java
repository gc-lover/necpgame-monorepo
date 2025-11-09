package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ImpactKpi
 */


public class ImpactKpi {

  private @Nullable Float orderEconomicIndex;

  private @Nullable Float serviceDemandIndex;

  private @Nullable Float volatilityIndex;

  private @Nullable Float sentimentScore;

  private @Nullable Integer populationShift;

  public ImpactKpi orderEconomicIndex(@Nullable Float orderEconomicIndex) {
    this.orderEconomicIndex = orderEconomicIndex;
    return this;
  }

  /**
   * Get orderEconomicIndex
   * @return orderEconomicIndex
   */
  
  @Schema(name = "orderEconomicIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("orderEconomicIndex")
  public @Nullable Float getOrderEconomicIndex() {
    return orderEconomicIndex;
  }

  public void setOrderEconomicIndex(@Nullable Float orderEconomicIndex) {
    this.orderEconomicIndex = orderEconomicIndex;
  }

  public ImpactKpi serviceDemandIndex(@Nullable Float serviceDemandIndex) {
    this.serviceDemandIndex = serviceDemandIndex;
    return this;
  }

  /**
   * Get serviceDemandIndex
   * @return serviceDemandIndex
   */
  
  @Schema(name = "serviceDemandIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serviceDemandIndex")
  public @Nullable Float getServiceDemandIndex() {
    return serviceDemandIndex;
  }

  public void setServiceDemandIndex(@Nullable Float serviceDemandIndex) {
    this.serviceDemandIndex = serviceDemandIndex;
  }

  public ImpactKpi volatilityIndex(@Nullable Float volatilityIndex) {
    this.volatilityIndex = volatilityIndex;
    return this;
  }

  /**
   * Get volatilityIndex
   * @return volatilityIndex
   */
  
  @Schema(name = "volatilityIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("volatilityIndex")
  public @Nullable Float getVolatilityIndex() {
    return volatilityIndex;
  }

  public void setVolatilityIndex(@Nullable Float volatilityIndex) {
    this.volatilityIndex = volatilityIndex;
  }

  public ImpactKpi sentimentScore(@Nullable Float sentimentScore) {
    this.sentimentScore = sentimentScore;
    return this;
  }

  /**
   * Get sentimentScore
   * @return sentimentScore
   */
  
  @Schema(name = "sentimentScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sentimentScore")
  public @Nullable Float getSentimentScore() {
    return sentimentScore;
  }

  public void setSentimentScore(@Nullable Float sentimentScore) {
    this.sentimentScore = sentimentScore;
  }

  public ImpactKpi populationShift(@Nullable Integer populationShift) {
    this.populationShift = populationShift;
    return this;
  }

  /**
   * Get populationShift
   * @return populationShift
   */
  
  @Schema(name = "populationShift", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("populationShift")
  public @Nullable Integer getPopulationShift() {
    return populationShift;
  }

  public void setPopulationShift(@Nullable Integer populationShift) {
    this.populationShift = populationShift;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImpactKpi impactKpi = (ImpactKpi) o;
    return Objects.equals(this.orderEconomicIndex, impactKpi.orderEconomicIndex) &&
        Objects.equals(this.serviceDemandIndex, impactKpi.serviceDemandIndex) &&
        Objects.equals(this.volatilityIndex, impactKpi.volatilityIndex) &&
        Objects.equals(this.sentimentScore, impactKpi.sentimentScore) &&
        Objects.equals(this.populationShift, impactKpi.populationShift);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderEconomicIndex, serviceDemandIndex, volatilityIndex, sentimentScore, populationShift);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImpactKpi {\n");
    sb.append("    orderEconomicIndex: ").append(toIndentedString(orderEconomicIndex)).append("\n");
    sb.append("    serviceDemandIndex: ").append(toIndentedString(serviceDemandIndex)).append("\n");
    sb.append("    volatilityIndex: ").append(toIndentedString(volatilityIndex)).append("\n");
    sb.append("    sentimentScore: ").append(toIndentedString(sentimentScore)).append("\n");
    sb.append("    populationShift: ").append(toIndentedString(populationShift)).append("\n");
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

