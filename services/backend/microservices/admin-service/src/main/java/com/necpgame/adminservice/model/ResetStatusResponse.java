package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.ResetTypeStatus;
import java.time.OffsetDateTime;
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
 * ResetStatusResponse
 */


public class ResetStatusResponse {

  private @Nullable ResetTypeStatus daily;

  private @Nullable ResetTypeStatus weekly;

  private @Nullable ResetTypeStatus monthly;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime serverTime;

  public ResetStatusResponse daily(@Nullable ResetTypeStatus daily) {
    this.daily = daily;
    return this;
  }

  /**
   * Get daily
   * @return daily
   */
  @Valid 
  @Schema(name = "daily", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("daily")
  public @Nullable ResetTypeStatus getDaily() {
    return daily;
  }

  public void setDaily(@Nullable ResetTypeStatus daily) {
    this.daily = daily;
  }

  public ResetStatusResponse weekly(@Nullable ResetTypeStatus weekly) {
    this.weekly = weekly;
    return this;
  }

  /**
   * Get weekly
   * @return weekly
   */
  @Valid 
  @Schema(name = "weekly", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("weekly")
  public @Nullable ResetTypeStatus getWeekly() {
    return weekly;
  }

  public void setWeekly(@Nullable ResetTypeStatus weekly) {
    this.weekly = weekly;
  }

  public ResetStatusResponse monthly(@Nullable ResetTypeStatus monthly) {
    this.monthly = monthly;
    return this;
  }

  /**
   * Get monthly
   * @return monthly
   */
  @Valid 
  @Schema(name = "monthly", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("monthly")
  public @Nullable ResetTypeStatus getMonthly() {
    return monthly;
  }

  public void setMonthly(@Nullable ResetTypeStatus monthly) {
    this.monthly = monthly;
  }

  public ResetStatusResponse serverTime(@Nullable OffsetDateTime serverTime) {
    this.serverTime = serverTime;
    return this;
  }

  /**
   * Get serverTime
   * @return serverTime
   */
  @Valid 
  @Schema(name = "server_time", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("server_time")
  public @Nullable OffsetDateTime getServerTime() {
    return serverTime;
  }

  public void setServerTime(@Nullable OffsetDateTime serverTime) {
    this.serverTime = serverTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetStatusResponse resetStatusResponse = (ResetStatusResponse) o;
    return Objects.equals(this.daily, resetStatusResponse.daily) &&
        Objects.equals(this.weekly, resetStatusResponse.weekly) &&
        Objects.equals(this.monthly, resetStatusResponse.monthly) &&
        Objects.equals(this.serverTime, resetStatusResponse.serverTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(daily, weekly, monthly, serverTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetStatusResponse {\n");
    sb.append("    daily: ").append(toIndentedString(daily)).append("\n");
    sb.append("    weekly: ").append(toIndentedString(weekly)).append("\n");
    sb.append("    monthly: ").append(toIndentedString(monthly)).append("\n");
    sb.append("    serverTime: ").append(toIndentedString(serverTime)).append("\n");
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

