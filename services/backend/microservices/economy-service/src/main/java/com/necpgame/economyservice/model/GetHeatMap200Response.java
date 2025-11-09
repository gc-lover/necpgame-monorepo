package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GetHeatMap200ResponseItemsInner;
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
 * GetHeatMap200Response
 */

@JsonTypeName("getHeatMap_200_response")

public class GetHeatMap200Response {

  private @Nullable String category;

  private @Nullable String timeframe;

  @Valid
  private List<@Valid GetHeatMap200ResponseItemsInner> items = new ArrayList<>();

  public GetHeatMap200Response category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public GetHeatMap200Response timeframe(@Nullable String timeframe) {
    this.timeframe = timeframe;
    return this;
  }

  /**
   * Get timeframe
   * @return timeframe
   */
  
  @Schema(name = "timeframe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeframe")
  public @Nullable String getTimeframe() {
    return timeframe;
  }

  public void setTimeframe(@Nullable String timeframe) {
    this.timeframe = timeframe;
  }

  public GetHeatMap200Response items(List<@Valid GetHeatMap200ResponseItemsInner> items) {
    this.items = items;
    return this;
  }

  public GetHeatMap200Response addItemsItem(GetHeatMap200ResponseItemsInner itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<@Valid GetHeatMap200ResponseItemsInner> getItems() {
    return items;
  }

  public void setItems(List<@Valid GetHeatMap200ResponseItemsInner> items) {
    this.items = items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetHeatMap200Response getHeatMap200Response = (GetHeatMap200Response) o;
    return Objects.equals(this.category, getHeatMap200Response.category) &&
        Objects.equals(this.timeframe, getHeatMap200Response.timeframe) &&
        Objects.equals(this.items, getHeatMap200Response.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, timeframe, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetHeatMap200Response {\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    timeframe: ").append(toIndentedString(timeframe)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

