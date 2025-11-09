package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GetPriceHistory200ResponseDataPointsInner;
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
 * GetPriceHistory200Response
 */

@JsonTypeName("getPriceHistory_200_response")

public class GetPriceHistory200Response {

  private @Nullable String itemId;

  private @Nullable String period;

  @Valid
  private List<@Valid GetPriceHistory200ResponseDataPointsInner> dataPoints = new ArrayList<>();

  public GetPriceHistory200Response itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "item_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_id")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public GetPriceHistory200Response period(@Nullable String period) {
    this.period = period;
    return this;
  }

  /**
   * Get period
   * @return period
   */
  
  @Schema(name = "period", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("period")
  public @Nullable String getPeriod() {
    return period;
  }

  public void setPeriod(@Nullable String period) {
    this.period = period;
  }

  public GetPriceHistory200Response dataPoints(List<@Valid GetPriceHistory200ResponseDataPointsInner> dataPoints) {
    this.dataPoints = dataPoints;
    return this;
  }

  public GetPriceHistory200Response addDataPointsItem(GetPriceHistory200ResponseDataPointsInner dataPointsItem) {
    if (this.dataPoints == null) {
      this.dataPoints = new ArrayList<>();
    }
    this.dataPoints.add(dataPointsItem);
    return this;
  }

  /**
   * Get dataPoints
   * @return dataPoints
   */
  @Valid 
  @Schema(name = "data_points", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data_points")
  public List<@Valid GetPriceHistory200ResponseDataPointsInner> getDataPoints() {
    return dataPoints;
  }

  public void setDataPoints(List<@Valid GetPriceHistory200ResponseDataPointsInner> dataPoints) {
    this.dataPoints = dataPoints;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPriceHistory200Response getPriceHistory200Response = (GetPriceHistory200Response) o;
    return Objects.equals(this.itemId, getPriceHistory200Response.itemId) &&
        Objects.equals(this.period, getPriceHistory200Response.period) &&
        Objects.equals(this.dataPoints, getPriceHistory200Response.dataPoints);
  }

  @Override
  public int hashCode() {
    return Objects.hash(itemId, period, dataPoints);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPriceHistory200Response {\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    dataPoints: ").append(toIndentedString(dataPoints)).append("\n");
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

