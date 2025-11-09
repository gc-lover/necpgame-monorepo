package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.TravelEvent;
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
 * GetPeriodTravelEvents200Response
 */

@JsonTypeName("getPeriodTravelEvents_200_response")

public class GetPeriodTravelEvents200Response {

  private @Nullable String period;

  private @Nullable Object eraCharacteristics;

  @Valid
  private List<@Valid TravelEvent> events = new ArrayList<>();

  public GetPeriodTravelEvents200Response period(@Nullable String period) {
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

  public GetPeriodTravelEvents200Response eraCharacteristics(@Nullable Object eraCharacteristics) {
    this.eraCharacteristics = eraCharacteristics;
    return this;
  }

  /**
   * Get eraCharacteristics
   * @return eraCharacteristics
   */
  
  @Schema(name = "era_characteristics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("era_characteristics")
  public @Nullable Object getEraCharacteristics() {
    return eraCharacteristics;
  }

  public void setEraCharacteristics(@Nullable Object eraCharacteristics) {
    this.eraCharacteristics = eraCharacteristics;
  }

  public GetPeriodTravelEvents200Response events(List<@Valid TravelEvent> events) {
    this.events = events;
    return this;
  }

  public GetPeriodTravelEvents200Response addEventsItem(TravelEvent eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Get events
   * @return events
   */
  @Valid 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<@Valid TravelEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid TravelEvent> events) {
    this.events = events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPeriodTravelEvents200Response getPeriodTravelEvents200Response = (GetPeriodTravelEvents200Response) o;
    return Objects.equals(this.period, getPeriodTravelEvents200Response.period) &&
        Objects.equals(this.eraCharacteristics, getPeriodTravelEvents200Response.eraCharacteristics) &&
        Objects.equals(this.events, getPeriodTravelEvents200Response.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(period, eraCharacteristics, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPeriodTravelEvents200Response {\n");
    sb.append("    period: ").append(toIndentedString(period)).append("\n");
    sb.append("    eraCharacteristics: ").append(toIndentedString(eraCharacteristics)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

