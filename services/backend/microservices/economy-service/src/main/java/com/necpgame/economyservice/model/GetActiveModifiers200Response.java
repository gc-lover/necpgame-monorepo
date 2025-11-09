package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetActiveModifiers200Response
 */

@JsonTypeName("getActiveModifiers_200_response")

public class GetActiveModifiers200Response {

  @Valid
  private Map<String, BigDecimal> regionalModifiers = new HashMap<>();

  @Valid
  private List<Object> eventModifiers = new ArrayList<>();

  @Valid
  private Map<String, BigDecimal> factionModifiers = new HashMap<>();

  public GetActiveModifiers200Response regionalModifiers(Map<String, BigDecimal> regionalModifiers) {
    this.regionalModifiers = regionalModifiers;
    return this;
  }

  public GetActiveModifiers200Response putRegionalModifiersItem(String key, BigDecimal regionalModifiersItem) {
    if (this.regionalModifiers == null) {
      this.regionalModifiers = new HashMap<>();
    }
    this.regionalModifiers.put(key, regionalModifiersItem);
    return this;
  }

  /**
   * Get regionalModifiers
   * @return regionalModifiers
   */
  @Valid 
  @Schema(name = "regional_modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regional_modifiers")
  public Map<String, BigDecimal> getRegionalModifiers() {
    return regionalModifiers;
  }

  public void setRegionalModifiers(Map<String, BigDecimal> regionalModifiers) {
    this.regionalModifiers = regionalModifiers;
  }

  public GetActiveModifiers200Response eventModifiers(List<Object> eventModifiers) {
    this.eventModifiers = eventModifiers;
    return this;
  }

  public GetActiveModifiers200Response addEventModifiersItem(Object eventModifiersItem) {
    if (this.eventModifiers == null) {
      this.eventModifiers = new ArrayList<>();
    }
    this.eventModifiers.add(eventModifiersItem);
    return this;
  }

  /**
   * Get eventModifiers
   * @return eventModifiers
   */
  
  @Schema(name = "event_modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_modifiers")
  public List<Object> getEventModifiers() {
    return eventModifiers;
  }

  public void setEventModifiers(List<Object> eventModifiers) {
    this.eventModifiers = eventModifiers;
  }

  public GetActiveModifiers200Response factionModifiers(Map<String, BigDecimal> factionModifiers) {
    this.factionModifiers = factionModifiers;
    return this;
  }

  public GetActiveModifiers200Response putFactionModifiersItem(String key, BigDecimal factionModifiersItem) {
    if (this.factionModifiers == null) {
      this.factionModifiers = new HashMap<>();
    }
    this.factionModifiers.put(key, factionModifiersItem);
    return this;
  }

  /**
   * Get factionModifiers
   * @return factionModifiers
   */
  @Valid 
  @Schema(name = "faction_modifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_modifiers")
  public Map<String, BigDecimal> getFactionModifiers() {
    return factionModifiers;
  }

  public void setFactionModifiers(Map<String, BigDecimal> factionModifiers) {
    this.factionModifiers = factionModifiers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetActiveModifiers200Response getActiveModifiers200Response = (GetActiveModifiers200Response) o;
    return Objects.equals(this.regionalModifiers, getActiveModifiers200Response.regionalModifiers) &&
        Objects.equals(this.eventModifiers, getActiveModifiers200Response.eventModifiers) &&
        Objects.equals(this.factionModifiers, getActiveModifiers200Response.factionModifiers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regionalModifiers, eventModifiers, factionModifiers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveModifiers200Response {\n");
    sb.append("    regionalModifiers: ").append(toIndentedString(regionalModifiers)).append("\n");
    sb.append("    eventModifiers: ").append(toIndentedString(eventModifiers)).append("\n");
    sb.append("    factionModifiers: ").append(toIndentedString(factionModifiers)).append("\n");
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

