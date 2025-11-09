package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * Р—Р°РїСЂРѕСЃ РЅР° СЂР°СЃС‡РµС‚ РїСЂРѕРіСЂРµСЃСЃРёРё
 */

@Schema(name = "CalculateProgressionRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° СЂР°СЃС‡РµС‚ РїСЂРѕРіСЂРµСЃСЃРёРё")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:56:57.236771400+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CalculateProgressionRequest {

  private Float timePeriod;

  @Valid
  private List<Object> events = new ArrayList<>();

  public CalculateProgressionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CalculateProgressionRequest(Float timePeriod) {
    this.timePeriod = timePeriod;
  }

  public CalculateProgressionRequest timePeriod(Float timePeriod) {
    this.timePeriod = timePeriod;
    return this;
  }

  /**
   * РџРµСЂРёРѕРґ РІСЂРµРјРµРЅРё РґР»СЏ СЂР°СЃС‡РµС‚Р° РІ СЃРµРєСѓРЅРґР°С…
   * minimum: 0
   * @return timePeriod
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "time_period", description = "РџРµСЂРёРѕРґ РІСЂРµРјРµРЅРё РґР»СЏ СЂР°СЃС‡РµС‚Р° РІ СЃРµРєСѓРЅРґР°С…", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("time_period")
  public Float getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(Float timePeriod) {
    this.timePeriod = timePeriod;
  }

  public CalculateProgressionRequest events(List<Object> events) {
    this.events = events;
    return this;
  }

  public CalculateProgressionRequest addEventsItem(Object eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * РЎРѕР±С‹С‚РёСЏ Р·Р° РїРµСЂРёРѕРґ (Р±РѕРё, РїРѕРІСЂРµР¶РґРµРЅРёСЏ РёРјРїР»Р°РЅС‚РѕРІ, СЃС‚СЂРµСЃСЃРѕРІС‹Рµ СЃРѕР±С‹С‚РёСЏ)
   * @return events
   */
  
  @Schema(name = "events", description = "РЎРѕР±С‹С‚РёСЏ Р·Р° РїРµСЂРёРѕРґ (Р±РѕРё, РїРѕРІСЂРµР¶РґРµРЅРёСЏ РёРјРїР»Р°РЅС‚РѕРІ, СЃС‚СЂРµСЃСЃРѕРІС‹Рµ СЃРѕР±С‹С‚РёСЏ)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<Object> getEvents() {
    return events;
  }

  public void setEvents(List<Object> events) {
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
    CalculateProgressionRequest calculateProgressionRequest = (CalculateProgressionRequest) o;
    return Objects.equals(this.timePeriod, calculateProgressionRequest.timePeriod) &&
        Objects.equals(this.events, calculateProgressionRequest.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timePeriod, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateProgressionRequest {\n");
    sb.append("    timePeriod: ").append(toIndentedString(timePeriod)).append("\n");
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

