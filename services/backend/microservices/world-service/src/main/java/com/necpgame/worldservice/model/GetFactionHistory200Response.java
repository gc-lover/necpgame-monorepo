package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetFactionHistory200Response
 */

@JsonTypeName("getFactionHistory_200_response")

public class GetFactionHistory200Response {

  private @Nullable String factionId;

  private @Nullable Integer foundedYear;

  @Valid
  private List<String> keyEvents = new ArrayList<>();

  @Valid
  private List<String> leadersHistory = new ArrayList<>();

  @Valid
  private List<String> warsParticipated = new ArrayList<>();

  public GetFactionHistory200Response factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public GetFactionHistory200Response foundedYear(@Nullable Integer foundedYear) {
    this.foundedYear = foundedYear;
    return this;
  }

  /**
   * Get foundedYear
   * @return foundedYear
   */
  
  @Schema(name = "founded_year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("founded_year")
  public @Nullable Integer getFoundedYear() {
    return foundedYear;
  }

  public void setFoundedYear(@Nullable Integer foundedYear) {
    this.foundedYear = foundedYear;
  }

  public GetFactionHistory200Response keyEvents(List<String> keyEvents) {
    this.keyEvents = keyEvents;
    return this;
  }

  public GetFactionHistory200Response addKeyEventsItem(String keyEventsItem) {
    if (this.keyEvents == null) {
      this.keyEvents = new ArrayList<>();
    }
    this.keyEvents.add(keyEventsItem);
    return this;
  }

  /**
   * Get keyEvents
   * @return keyEvents
   */
  
  @Schema(name = "key_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key_events")
  public List<String> getKeyEvents() {
    return keyEvents;
  }

  public void setKeyEvents(List<String> keyEvents) {
    this.keyEvents = keyEvents;
  }

  public GetFactionHistory200Response leadersHistory(List<String> leadersHistory) {
    this.leadersHistory = leadersHistory;
    return this;
  }

  public GetFactionHistory200Response addLeadersHistoryItem(String leadersHistoryItem) {
    if (this.leadersHistory == null) {
      this.leadersHistory = new ArrayList<>();
    }
    this.leadersHistory.add(leadersHistoryItem);
    return this;
  }

  /**
   * Get leadersHistory
   * @return leadersHistory
   */
  
  @Schema(name = "leaders_history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leaders_history")
  public List<String> getLeadersHistory() {
    return leadersHistory;
  }

  public void setLeadersHistory(List<String> leadersHistory) {
    this.leadersHistory = leadersHistory;
  }

  public GetFactionHistory200Response warsParticipated(List<String> warsParticipated) {
    this.warsParticipated = warsParticipated;
    return this;
  }

  public GetFactionHistory200Response addWarsParticipatedItem(String warsParticipatedItem) {
    if (this.warsParticipated == null) {
      this.warsParticipated = new ArrayList<>();
    }
    this.warsParticipated.add(warsParticipatedItem);
    return this;
  }

  /**
   * Get warsParticipated
   * @return warsParticipated
   */
  
  @Schema(name = "wars_participated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wars_participated")
  public List<String> getWarsParticipated() {
    return warsParticipated;
  }

  public void setWarsParticipated(List<String> warsParticipated) {
    this.warsParticipated = warsParticipated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFactionHistory200Response getFactionHistory200Response = (GetFactionHistory200Response) o;
    return Objects.equals(this.factionId, getFactionHistory200Response.factionId) &&
        Objects.equals(this.foundedYear, getFactionHistory200Response.foundedYear) &&
        Objects.equals(this.keyEvents, getFactionHistory200Response.keyEvents) &&
        Objects.equals(this.leadersHistory, getFactionHistory200Response.leadersHistory) &&
        Objects.equals(this.warsParticipated, getFactionHistory200Response.warsParticipated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, foundedYear, keyEvents, leadersHistory, warsParticipated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFactionHistory200Response {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    foundedYear: ").append(toIndentedString(foundedYear)).append("\n");
    sb.append("    keyEvents: ").append(toIndentedString(keyEvents)).append("\n");
    sb.append("    leadersHistory: ").append(toIndentedString(leadersHistory)).append("\n");
    sb.append("    warsParticipated: ").append(toIndentedString(warsParticipated)).append("\n");
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

