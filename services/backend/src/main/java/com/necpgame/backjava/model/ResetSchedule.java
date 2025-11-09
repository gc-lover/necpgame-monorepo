package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.ScheduleConfig;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ResetSchedule
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ResetSchedule {

  private @Nullable ScheduleConfig daily;

  private @Nullable ScheduleConfig weekly;

  private @Nullable ScheduleConfig monthly;

  public ResetSchedule daily(@Nullable ScheduleConfig daily) {
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
  public @Nullable ScheduleConfig getDaily() {
    return daily;
  }

  public void setDaily(@Nullable ScheduleConfig daily) {
    this.daily = daily;
  }

  public ResetSchedule weekly(@Nullable ScheduleConfig weekly) {
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
  public @Nullable ScheduleConfig getWeekly() {
    return weekly;
  }

  public void setWeekly(@Nullable ScheduleConfig weekly) {
    this.weekly = weekly;
  }

  public ResetSchedule monthly(@Nullable ScheduleConfig monthly) {
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
  public @Nullable ScheduleConfig getMonthly() {
    return monthly;
  }

  public void setMonthly(@Nullable ScheduleConfig monthly) {
    this.monthly = monthly;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResetSchedule resetSchedule = (ResetSchedule) o;
    return Objects.equals(this.daily, resetSchedule.daily) &&
        Objects.equals(this.weekly, resetSchedule.weekly) &&
        Objects.equals(this.monthly, resetSchedule.monthly);
  }

  @Override
  public int hashCode() {
    return Objects.hash(daily, weekly, monthly);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ResetSchedule {\n");
    sb.append("    daily: ").append(toIndentedString(daily)).append("\n");
    sb.append("    weekly: ").append(toIndentedString(weekly)).append("\n");
    sb.append("    monthly: ").append(toIndentedString(monthly)).append("\n");
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

