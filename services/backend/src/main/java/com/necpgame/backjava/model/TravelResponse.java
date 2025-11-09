package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.GameLocation;
import com.necpgame.backjava.model.TravelResponseEventsInner;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TravelResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T21:22:04.712198900+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class TravelResponse {

  private Boolean success;

  private GameLocation newLocation;

  private Integer timeSpent;

  private @Nullable Integer energySpent;

  private String message;

  @Valid
  private List<@Valid TravelResponseEventsInner> events = new ArrayList<>();

  public TravelResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TravelResponse(Boolean success, GameLocation newLocation, Integer timeSpent, String message) {
    this.success = success;
    this.newLocation = newLocation;
    this.timeSpent = timeSpent;
    this.message = message;
  }

  public TravelResponse success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * РЈСЃРїРµС€РЅРѕ Р»Рё РїРµСЂРµРјРµС‰РµРЅРёРµ
   * @return success
   */
  @NotNull 
  @Schema(name = "success", example = "true", description = "РЈСЃРїРµС€РЅРѕ Р»Рё РїРµСЂРµРјРµС‰РµРЅРёРµ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  public TravelResponse newLocation(GameLocation newLocation) {
    this.newLocation = newLocation;
    return this;
  }

  /**
   * Get newLocation
   * @return newLocation
   */
  @NotNull @Valid 
  @Schema(name = "newLocation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newLocation")
  public GameLocation getNewLocation() {
    return newLocation;
  }

  public void setNewLocation(GameLocation newLocation) {
    this.newLocation = newLocation;
  }

  public TravelResponse timeSpent(Integer timeSpent) {
    this.timeSpent = timeSpent;
    return this;
  }

  /**
   * Р—Р°С‚СЂР°С‡РµРЅРЅРѕРµ РІСЂРµРјСЏ РІ РјРёРЅСѓС‚Р°С… (0 РґР»СЏ fast_travel)
   * @return timeSpent
   */
  @NotNull 
  @Schema(name = "timeSpent", example = "15", description = "Р—Р°С‚СЂР°С‡РµРЅРЅРѕРµ РІСЂРµРјСЏ РІ РјРёРЅСѓС‚Р°С… (0 РґР»СЏ fast_travel)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timeSpent")
  public Integer getTimeSpent() {
    return timeSpent;
  }

  public void setTimeSpent(Integer timeSpent) {
    this.timeSpent = timeSpent;
  }

  public TravelResponse energySpent(@Nullable Integer energySpent) {
    this.energySpent = energySpent;
    return this;
  }

  /**
   * Р—Р°С‚СЂР°С‡РµРЅРЅР°СЏ СЌРЅРµСЂРіРёСЏ (0 РґР»СЏ fast_travel)
   * @return energySpent
   */
  
  @Schema(name = "energySpent", example = "10", description = "Р—Р°С‚СЂР°С‡РµРЅРЅР°СЏ СЌРЅРµСЂРіРёСЏ (0 РґР»СЏ fast_travel)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energySpent")
  public @Nullable Integer getEnergySpent() {
    return energySpent;
  }

  public void setEnergySpent(@Nullable Integer energySpent) {
    this.energySpent = energySpent;
  }

  public TravelResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * РЎРѕРѕР±С‰РµРЅРёРµ Рѕ РїРµСЂРµРјРµС‰РµРЅРёРё
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "Р’С‹ РїСЂРёР±С‹Р»Рё РІ Watson - Kabuki", description = "РЎРѕРѕР±С‰РµРЅРёРµ Рѕ РїРµСЂРµРјРµС‰РµРЅРёРё", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public TravelResponse events(List<@Valid TravelResponseEventsInner> events) {
    this.events = events;
    return this;
  }

  public TravelResponse addEventsItem(TravelResponseEventsInner eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * РЎРѕР±С‹С‚РёСЏ, РїСЂРѕРёР·РѕС€РµРґС€РёРµ РІРѕ РІСЂРµРјСЏ РїРµСЂРµРјРµС‰РµРЅРёСЏ (РѕРїС†РёРѕРЅР°Р»СЊРЅРѕ)
   * @return events
   */
  @Valid 
  @Schema(name = "events", description = "РЎРѕР±С‹С‚РёСЏ, РїСЂРѕРёР·РѕС€РµРґС€РёРµ РІРѕ РІСЂРµРјСЏ РїРµСЂРµРјРµС‰РµРЅРёСЏ (РѕРїС†РёРѕРЅР°Р»СЊРЅРѕ)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<@Valid TravelResponseEventsInner> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid TravelResponseEventsInner> events) {
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
    TravelResponse travelResponse = (TravelResponse) o;
    return Objects.equals(this.success, travelResponse.success) &&
        Objects.equals(this.newLocation, travelResponse.newLocation) &&
        Objects.equals(this.timeSpent, travelResponse.timeSpent) &&
        Objects.equals(this.energySpent, travelResponse.energySpent) &&
        Objects.equals(this.message, travelResponse.message) &&
        Objects.equals(this.events, travelResponse.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, newLocation, timeSpent, energySpent, message, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TravelResponse {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    newLocation: ").append(toIndentedString(newLocation)).append("\n");
    sb.append("    timeSpent: ").append(toIndentedString(timeSpent)).append("\n");
    sb.append("    energySpent: ").append(toIndentedString(energySpent)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

