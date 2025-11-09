package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.EventEffect;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetItemEventImpact200Response
 */

@JsonTypeName("getItemEventImpact_200_response")

public class GetItemEventImpact200Response {

  private @Nullable UUID itemId;

  private @Nullable Integer basePrice;

  private @Nullable Integer currentPrice;

  private @Nullable BigDecimal priceMultiplier;

  @Valid
  private List<@Valid EventEffect> affectingEvents = new ArrayList<>();

  public GetItemEventImpact200Response itemId(@Nullable UUID itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  @Valid 
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable UUID getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable UUID itemId) {
    this.itemId = itemId;
  }

  public GetItemEventImpact200Response basePrice(@Nullable Integer basePrice) {
    this.basePrice = basePrice;
    return this;
  }

  /**
   * Get basePrice
   * @return basePrice
   */
  
  @Schema(name = "base_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("base_price")
  public @Nullable Integer getBasePrice() {
    return basePrice;
  }

  public void setBasePrice(@Nullable Integer basePrice) {
    this.basePrice = basePrice;
  }

  public GetItemEventImpact200Response currentPrice(@Nullable Integer currentPrice) {
    this.currentPrice = currentPrice;
    return this;
  }

  /**
   * Get currentPrice
   * @return currentPrice
   */
  
  @Schema(name = "current_price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_price")
  public @Nullable Integer getCurrentPrice() {
    return currentPrice;
  }

  public void setCurrentPrice(@Nullable Integer currentPrice) {
    this.currentPrice = currentPrice;
  }

  public GetItemEventImpact200Response priceMultiplier(@Nullable BigDecimal priceMultiplier) {
    this.priceMultiplier = priceMultiplier;
    return this;
  }

  /**
   * Get priceMultiplier
   * @return priceMultiplier
   */
  @Valid 
  @Schema(name = "price_multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price_multiplier")
  public @Nullable BigDecimal getPriceMultiplier() {
    return priceMultiplier;
  }

  public void setPriceMultiplier(@Nullable BigDecimal priceMultiplier) {
    this.priceMultiplier = priceMultiplier;
  }

  public GetItemEventImpact200Response affectingEvents(List<@Valid EventEffect> affectingEvents) {
    this.affectingEvents = affectingEvents;
    return this;
  }

  public GetItemEventImpact200Response addAffectingEventsItem(EventEffect affectingEventsItem) {
    if (this.affectingEvents == null) {
      this.affectingEvents = new ArrayList<>();
    }
    this.affectingEvents.add(affectingEventsItem);
    return this;
  }

  /**
   * Get affectingEvents
   * @return affectingEvents
   */
  @Valid 
  @Schema(name = "affecting_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affecting_events")
  public List<@Valid EventEffect> getAffectingEvents() {
    return affectingEvents;
  }

  public void setAffectingEvents(List<@Valid EventEffect> affectingEvents) {
    this.affectingEvents = affectingEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetItemEventImpact200Response getItemEventImpact200Response = (GetItemEventImpact200Response) o;
    return Objects.equals(this.itemId, getItemEventImpact200Response.itemId) &&
        Objects.equals(this.basePrice, getItemEventImpact200Response.basePrice) &&
        Objects.equals(this.currentPrice, getItemEventImpact200Response.currentPrice) &&
        Objects.equals(this.priceMultiplier, getItemEventImpact200Response.priceMultiplier) &&
        Objects.equals(this.affectingEvents, getItemEventImpact200Response.affectingEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, basePrice, currentPrice, priceMultiplier, affectingEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetItemEventImpact200Response {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    basePrice: ").append(toIndentedString(basePrice)).append("\n");
    sb.append("    currentPrice: ").append(toIndentedString(currentPrice)).append("\n");
    sb.append("    priceMultiplier: ").append(toIndentedString(priceMultiplier)).append("\n");
    sb.append("    affectingEvents: ").append(toIndentedString(affectingEvents)).append("\n");
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

