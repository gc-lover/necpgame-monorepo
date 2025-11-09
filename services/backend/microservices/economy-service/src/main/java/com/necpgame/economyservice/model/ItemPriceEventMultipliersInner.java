package com.necpgame.economyservice.model;

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
 * ItemPriceEventMultipliersInner
 */

@JsonTypeName("ItemPrice_event_multipliers_inner")

public class ItemPriceEventMultipliersInner {

  private @Nullable String eventName;

  private @Nullable BigDecimal multiplier;

  public ItemPriceEventMultipliersInner eventName(@Nullable String eventName) {
    this.eventName = eventName;
    return this;
  }

  /**
   * Get eventName
   * @return eventName
   */
  
  @Schema(name = "event_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_name")
  public @Nullable String getEventName() {
    return eventName;
  }

  public void setEventName(@Nullable String eventName) {
    this.eventName = eventName;
  }

  public ItemPriceEventMultipliersInner multiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
    return this;
  }

  /**
   * Get multiplier
   * @return multiplier
   */
  @Valid 
  @Schema(name = "multiplier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("multiplier")
  public @Nullable BigDecimal getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(@Nullable BigDecimal multiplier) {
    this.multiplier = multiplier;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ItemPriceEventMultipliersInner itemPriceEventMultipliersInner = (ItemPriceEventMultipliersInner) o;
    return Objects.equals(this.eventName, itemPriceEventMultipliersInner.eventName) &&
        Objects.equals(this.multiplier, itemPriceEventMultipliersInner.multiplier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventName, multiplier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ItemPriceEventMultipliersInner {\n");
    sb.append("    eventName: ").append(toIndentedString(eventName)).append("\n");
    sb.append("    multiplier: ").append(toIndentedString(multiplier)).append("\n");
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

