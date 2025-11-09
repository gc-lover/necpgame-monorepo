package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner;
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
 * GetEventsImpact200ResponseEventsInner
 */

@JsonTypeName("getEventsImpact_200_response_events_inner")

public class GetEventsImpact200ResponseEventsInner {

  private @Nullable String eventId;

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    CORP_WAR("corp_war"),
    
    QUEST_COMPLETION("quest_completion"),
    
    GLOBAL_EVENT("global_event"),
    
    FACTION_WAR("faction_war");

    private final String value;

    EventTypeEnum(String value) {
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
    public static EventTypeEnum fromValue(String value) {
      for (EventTypeEnum b : EventTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable EventTypeEnum eventType;

  @Valid
  private List<@Valid GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner> affectedCompanies = new ArrayList<>();

  public GetEventsImpact200ResponseEventsInner eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable String getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public GetEventsImpact200ResponseEventsInner eventType(@Nullable EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  
  @Schema(name = "event_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_type")
  public @Nullable EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(@Nullable EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public GetEventsImpact200ResponseEventsInner affectedCompanies(List<@Valid GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner> affectedCompanies) {
    this.affectedCompanies = affectedCompanies;
    return this;
  }

  public GetEventsImpact200ResponseEventsInner addAffectedCompaniesItem(GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner affectedCompaniesItem) {
    if (this.affectedCompanies == null) {
      this.affectedCompanies = new ArrayList<>();
    }
    this.affectedCompanies.add(affectedCompaniesItem);
    return this;
  }

  /**
   * Get affectedCompanies
   * @return affectedCompanies
   */
  @Valid 
  @Schema(name = "affected_companies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_companies")
  public List<@Valid GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner> getAffectedCompanies() {
    return affectedCompanies;
  }

  public void setAffectedCompanies(List<@Valid GetEventsImpact200ResponseEventsInnerAffectedCompaniesInner> affectedCompanies) {
    this.affectedCompanies = affectedCompanies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetEventsImpact200ResponseEventsInner getEventsImpact200ResponseEventsInner = (GetEventsImpact200ResponseEventsInner) o;
    return Objects.equals(this.eventId, getEventsImpact200ResponseEventsInner.eventId) &&
        Objects.equals(this.eventType, getEventsImpact200ResponseEventsInner.eventType) &&
        Objects.equals(this.affectedCompanies, getEventsImpact200ResponseEventsInner.affectedCompanies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, eventType, affectedCompanies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetEventsImpact200ResponseEventsInner {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    affectedCompanies: ").append(toIndentedString(affectedCompanies)).append("\n");
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

