package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.MarketIndexSnapshot;
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
 * MarketIndexHistory
 */


public class MarketIndexHistory {

  /**
   * Gets or Sets interval
   */
  public enum IntervalEnum {
    _15M("15m"),
    
    _1H("1h"),
    
    _6H("6h"),
    
    _24H("24h");

    private final String value;

    IntervalEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static IntervalEnum fromValue(String value) {
      for (IntervalEnum b : IntervalEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable IntervalEnum interval;

  @Valid
  private List<@Valid MarketIndexSnapshot> snapshots = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime nextRefreshAt;

  public MarketIndexHistory() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MarketIndexHistory(List<@Valid MarketIndexSnapshot> snapshots) {
    this.snapshots = snapshots;
  }

  public MarketIndexHistory interval(@Nullable IntervalEnum interval) {
    this.interval = interval;
    return this;
  }

  /**
   * Get interval
   * @return interval
   */
  
  @Schema(name = "interval", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("interval")
  public @Nullable IntervalEnum getInterval() {
    return interval;
  }

  public void setInterval(@Nullable IntervalEnum interval) {
    this.interval = interval;
  }

  public MarketIndexHistory snapshots(List<@Valid MarketIndexSnapshot> snapshots) {
    this.snapshots = snapshots;
    return this;
  }

  public MarketIndexHistory addSnapshotsItem(MarketIndexSnapshot snapshotsItem) {
    if (this.snapshots == null) {
      this.snapshots = new ArrayList<>();
    }
    this.snapshots.add(snapshotsItem);
    return this;
  }

  /**
   * Get snapshots
   * @return snapshots
   */
  @NotNull @Valid 
  @Schema(name = "snapshots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("snapshots")
  public List<@Valid MarketIndexSnapshot> getSnapshots() {
    return snapshots;
  }

  public void setSnapshots(List<@Valid MarketIndexSnapshot> snapshots) {
    this.snapshots = snapshots;
  }

  public MarketIndexHistory nextRefreshAt(@Nullable OffsetDateTime nextRefreshAt) {
    this.nextRefreshAt = nextRefreshAt;
    return this;
  }

  /**
   * Get nextRefreshAt
   * @return nextRefreshAt
   */
  @Valid 
  @Schema(name = "nextRefreshAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextRefreshAt")
  public @Nullable OffsetDateTime getNextRefreshAt() {
    return nextRefreshAt;
  }

  public void setNextRefreshAt(@Nullable OffsetDateTime nextRefreshAt) {
    this.nextRefreshAt = nextRefreshAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MarketIndexHistory marketIndexHistory = (MarketIndexHistory) o;
    return Objects.equals(this.interval, marketIndexHistory.interval) &&
        Objects.equals(this.snapshots, marketIndexHistory.snapshots) &&
        Objects.equals(this.nextRefreshAt, marketIndexHistory.nextRefreshAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(interval, snapshots, nextRefreshAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MarketIndexHistory {\n");
    sb.append("    interval: ").append(toIndentedString(interval)).append("\n");
    sb.append("    snapshots: ").append(toIndentedString(snapshots)).append("\n");
    sb.append("    nextRefreshAt: ").append(toIndentedString(nextRefreshAt)).append("\n");
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

