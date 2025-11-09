package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.RecurringEvent;
import com.necpgame.worldservice.model.SeasonalEvent;
import com.necpgame.worldservice.model.UniqueEvent;
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
 * EventScheduleResponse
 */


public class EventScheduleResponse {

  @Valid
  private List<RecurringEvent> recurring = new ArrayList<>();

  @Valid
  private List<SeasonalEvent> seasonal = new ArrayList<>();

  @Valid
  private List<UniqueEvent> unique = new ArrayList<>();

  private @Nullable String timezone;

  private @Nullable String locale;

  public EventScheduleResponse recurring(List<RecurringEvent> recurring) {
    this.recurring = recurring;
    return this;
  }

  public EventScheduleResponse addRecurringItem(RecurringEvent recurringItem) {
    if (this.recurring == null) {
      this.recurring = new ArrayList<>();
    }
    this.recurring.add(recurringItem);
    return this;
  }

  /**
   * Get recurring
   * @return recurring
   */
  @Valid 
  @Schema(name = "recurring", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recurring")
  public List<RecurringEvent> getRecurring() {
    return recurring;
  }

  public void setRecurring(List<RecurringEvent> recurring) {
    this.recurring = recurring;
  }

  public EventScheduleResponse seasonal(List<SeasonalEvent> seasonal) {
    this.seasonal = seasonal;
    return this;
  }

  public EventScheduleResponse addSeasonalItem(SeasonalEvent seasonalItem) {
    if (this.seasonal == null) {
      this.seasonal = new ArrayList<>();
    }
    this.seasonal.add(seasonalItem);
    return this;
  }

  /**
   * Get seasonal
   * @return seasonal
   */
  @Valid 
  @Schema(name = "seasonal", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasonal")
  public List<SeasonalEvent> getSeasonal() {
    return seasonal;
  }

  public void setSeasonal(List<SeasonalEvent> seasonal) {
    this.seasonal = seasonal;
  }

  public EventScheduleResponse unique(List<UniqueEvent> unique) {
    this.unique = unique;
    return this;
  }

  public EventScheduleResponse addUniqueItem(UniqueEvent uniqueItem) {
    if (this.unique == null) {
      this.unique = new ArrayList<>();
    }
    this.unique.add(uniqueItem);
    return this;
  }

  /**
   * Get unique
   * @return unique
   */
  @Valid 
  @Schema(name = "unique", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unique")
  public List<UniqueEvent> getUnique() {
    return unique;
  }

  public void setUnique(List<UniqueEvent> unique) {
    this.unique = unique;
  }

  public EventScheduleResponse timezone(@Nullable String timezone) {
    this.timezone = timezone;
    return this;
  }

  /**
   * Get timezone
   * @return timezone
   */
  
  @Schema(name = "timezone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timezone")
  public @Nullable String getTimezone() {
    return timezone;
  }

  public void setTimezone(@Nullable String timezone) {
    this.timezone = timezone;
  }

  public EventScheduleResponse locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventScheduleResponse eventScheduleResponse = (EventScheduleResponse) o;
    return Objects.equals(this.recurring, eventScheduleResponse.recurring) &&
        Objects.equals(this.seasonal, eventScheduleResponse.seasonal) &&
        Objects.equals(this.unique, eventScheduleResponse.unique) &&
        Objects.equals(this.timezone, eventScheduleResponse.timezone) &&
        Objects.equals(this.locale, eventScheduleResponse.locale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recurring, seasonal, unique, timezone, locale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventScheduleResponse {\n");
    sb.append("    recurring: ").append(toIndentedString(recurring)).append("\n");
    sb.append("    seasonal: ").append(toIndentedString(seasonal)).append("\n");
    sb.append("    unique: ").append(toIndentedString(unique)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
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

