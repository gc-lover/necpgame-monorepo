package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.EconomicIndexRegion;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PlayerOrderEconomicIndex
 */


public class PlayerOrderEconomicIndex {

  private Float orderEconomicIndex;

  private Float serviceDemandIndex;

  private Float marketVolatility;

  @Valid
  private List<@Valid EconomicIndexRegion> regionalBreakdown = new ArrayList<>();

  private @Nullable String commentary;

  private @Nullable Integer ttl;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  public PlayerOrderEconomicIndex() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderEconomicIndex(Float orderEconomicIndex, Float serviceDemandIndex, Float marketVolatility, OffsetDateTime updatedAt) {
    this.orderEconomicIndex = orderEconomicIndex;
    this.serviceDemandIndex = serviceDemandIndex;
    this.marketVolatility = marketVolatility;
    this.updatedAt = updatedAt;
  }

  public PlayerOrderEconomicIndex orderEconomicIndex(Float orderEconomicIndex) {
    this.orderEconomicIndex = orderEconomicIndex;
    return this;
  }

  /**
   * Сводный индекс влияния заказов на экономику.
   * @return orderEconomicIndex
   */
  @NotNull 
  @Schema(name = "orderEconomicIndex", description = "Сводный индекс влияния заказов на экономику.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderEconomicIndex")
  public Float getOrderEconomicIndex() {
    return orderEconomicIndex;
  }

  public void setOrderEconomicIndex(Float orderEconomicIndex) {
    this.orderEconomicIndex = orderEconomicIndex;
  }

  public PlayerOrderEconomicIndex serviceDemandIndex(Float serviceDemandIndex) {
    this.serviceDemandIndex = serviceDemandIndex;
    return this;
  }

  /**
   * Индекс спроса на услуги по категориям заказов.
   * @return serviceDemandIndex
   */
  @NotNull 
  @Schema(name = "serviceDemandIndex", description = "Индекс спроса на услуги по категориям заказов.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("serviceDemandIndex")
  public Float getServiceDemandIndex() {
    return serviceDemandIndex;
  }

  public void setServiceDemandIndex(Float serviceDemandIndex) {
    this.serviceDemandIndex = serviceDemandIndex;
  }

  public PlayerOrderEconomicIndex marketVolatility(Float marketVolatility) {
    this.marketVolatility = marketVolatility;
    return this;
  }

  /**
   * Волатильность рынка заказов.
   * @return marketVolatility
   */
  @NotNull 
  @Schema(name = "marketVolatility", description = "Волатильность рынка заказов.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("marketVolatility")
  public Float getMarketVolatility() {
    return marketVolatility;
  }

  public void setMarketVolatility(Float marketVolatility) {
    this.marketVolatility = marketVolatility;
  }

  public PlayerOrderEconomicIndex regionalBreakdown(List<@Valid EconomicIndexRegion> regionalBreakdown) {
    this.regionalBreakdown = regionalBreakdown;
    return this;
  }

  public PlayerOrderEconomicIndex addRegionalBreakdownItem(EconomicIndexRegion regionalBreakdownItem) {
    if (this.regionalBreakdown == null) {
      this.regionalBreakdown = new ArrayList<>();
    }
    this.regionalBreakdown.add(regionalBreakdownItem);
    return this;
  }

  /**
   * Get regionalBreakdown
   * @return regionalBreakdown
   */
  @Valid 
  @Schema(name = "regionalBreakdown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regionalBreakdown")
  public List<@Valid EconomicIndexRegion> getRegionalBreakdown() {
    return regionalBreakdown;
  }

  public void setRegionalBreakdown(List<@Valid EconomicIndexRegion> regionalBreakdown) {
    this.regionalBreakdown = regionalBreakdown;
  }

  public PlayerOrderEconomicIndex commentary(@Nullable String commentary) {
    this.commentary = commentary;
    return this;
  }

  /**
   * Текстовый комментарий аналитики.
   * @return commentary
   */
  
  @Schema(name = "commentary", description = "Текстовый комментарий аналитики.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("commentary")
  public @Nullable String getCommentary() {
    return commentary;
  }

  public void setCommentary(@Nullable String commentary) {
    this.commentary = commentary;
  }

  public PlayerOrderEconomicIndex ttl(@Nullable Integer ttl) {
    this.ttl = ttl;
    return this;
  }

  /**
   * Время жизни данных в секундах.
   * @return ttl
   */
  
  @Schema(name = "ttl", description = "Время жизни данных в секундах.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ttl")
  public @Nullable Integer getTtl() {
    return ttl;
  }

  public void setTtl(@Nullable Integer ttl) {
    this.ttl = ttl;
  }

  public PlayerOrderEconomicIndex updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @NotNull @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedAt")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderEconomicIndex playerOrderEconomicIndex = (PlayerOrderEconomicIndex) o;
    return Objects.equals(this.orderEconomicIndex, playerOrderEconomicIndex.orderEconomicIndex) &&
        Objects.equals(this.serviceDemandIndex, playerOrderEconomicIndex.serviceDemandIndex) &&
        Objects.equals(this.marketVolatility, playerOrderEconomicIndex.marketVolatility) &&
        Objects.equals(this.regionalBreakdown, playerOrderEconomicIndex.regionalBreakdown) &&
        Objects.equals(this.commentary, playerOrderEconomicIndex.commentary) &&
        Objects.equals(this.ttl, playerOrderEconomicIndex.ttl) &&
        Objects.equals(this.updatedAt, playerOrderEconomicIndex.updatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderEconomicIndex, serviceDemandIndex, marketVolatility, regionalBreakdown, commentary, ttl, updatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderEconomicIndex {\n");
    sb.append("    orderEconomicIndex: ").append(toIndentedString(orderEconomicIndex)).append("\n");
    sb.append("    serviceDemandIndex: ").append(toIndentedString(serviceDemandIndex)).append("\n");
    sb.append("    marketVolatility: ").append(toIndentedString(marketVolatility)).append("\n");
    sb.append("    regionalBreakdown: ").append(toIndentedString(regionalBreakdown)).append("\n");
    sb.append("    commentary: ").append(toIndentedString(commentary)).append("\n");
    sb.append("    ttl: ").append(toIndentedString(ttl)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
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

