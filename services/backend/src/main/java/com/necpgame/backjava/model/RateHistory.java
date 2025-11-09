package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.RateHistoryDataInner;
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
 * RateHistory
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RateHistory {

  private @Nullable String pair;

  private @Nullable String period;

  private @Nullable String interval;

  @Valid
  private List<@Valid RateHistoryDataInner> data = new ArrayList<>();

  public RateHistory pair(@Nullable String pair) {
    this.pair = pair;
    return this;
  }

  /**
   * Get pair
   * @return pair
   */
  
  @Schema(name = "pair", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pair")
  public @Nullable String getPair() {
    return pair;
  }

  public void setPair(@Nullable String pair) {
    this.pair = pair;
  }

  public RateHistory period(@Nullable String period) {
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

  public RateHistory interval(@Nullable String interval) {
    this.interval = interval;
    return this;
  }

  /**
   * Get interval
   * @return interval
   */
  
  @Schema(name = "interval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("interval")
  public @Nullable String getInterval() {
    return interval;
  }

  public void setInterval(@Nullable String interval) {
    this.interval = interval;
  }

  public RateHistory data(List<@Valid RateHistoryDataInner> data) {
    this.data = data;
    return this;
  }

  public RateHistory addDataItem(RateHistoryDataInner dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data")
  public List<@Valid RateHistoryDataInner> getData() {
    return data;
  }

  public void setData(List<@Valid RateHistoryDataInner> data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RateHistory rateHistory = (RateHistory) o;
    return Objects.equals(this.pair, rateHistory.pair) &&
        Objects.equals(this.period, rateHistory.period) &&
        Objects.equals(this.interval, rateHistory.interval) &&
        Objects.equals(this.data, rateHistory.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pair, period, interval, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RateHistory {\n");
    sb.append("    pair: ").append(toIndentedString(pair)).append("\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    interval: ").append(toIndentedString(interval)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

