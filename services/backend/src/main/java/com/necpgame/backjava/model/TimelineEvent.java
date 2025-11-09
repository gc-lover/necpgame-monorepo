package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * TimelineEvent
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TimelineEvent {

  private @Nullable String eventId;

  private @Nullable Integer year;

  private @Nullable String name;

  private @Nullable String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    WAR("WAR"),
    
    ECONOMIC("ECONOMIC"),
    
    TECHNOLOGICAL("TECHNOLOGICAL"),
    
    SOCIAL("SOCIAL"),
    
    CATASTROPHE("CATASTROPHE");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  /**
   * Gets or Sets impactLevel
   */
  public enum ImpactLevelEnum {
    LOCAL("LOCAL"),
    
    REGIONAL("REGIONAL"),
    
    GLOBAL("GLOBAL");

    private final String value;

    ImpactLevelEnum(String value) {
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
    public static ImpactLevelEnum fromValue(String value) {
      for (ImpactLevelEnum b : ImpactLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ImpactLevelEnum impactLevel;

  @Valid
  private List<String> relatedFactions = new ArrayList<>();

  public TimelineEvent eventId(@Nullable String eventId) {
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

  public TimelineEvent year(@Nullable Integer year) {
    this.year = year;
    return this;
  }

  /**
   * Get year
   * @return year
   */
  
  @Schema(name = "year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("year")
  public @Nullable Integer getYear() {
    return year;
  }

  public void setYear(@Nullable Integer year) {
    this.year = year;
  }

  public TimelineEvent name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public TimelineEvent description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public TimelineEvent type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public TimelineEvent impactLevel(@Nullable ImpactLevelEnum impactLevel) {
    this.impactLevel = impactLevel;
    return this;
  }

  /**
   * Get impactLevel
   * @return impactLevel
   */
  
  @Schema(name = "impact_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact_level")
  public @Nullable ImpactLevelEnum getImpactLevel() {
    return impactLevel;
  }

  public void setImpactLevel(@Nullable ImpactLevelEnum impactLevel) {
    this.impactLevel = impactLevel;
  }

  public TimelineEvent relatedFactions(List<String> relatedFactions) {
    this.relatedFactions = relatedFactions;
    return this;
  }

  public TimelineEvent addRelatedFactionsItem(String relatedFactionsItem) {
    if (this.relatedFactions == null) {
      this.relatedFactions = new ArrayList<>();
    }
    this.relatedFactions.add(relatedFactionsItem);
    return this;
  }

  /**
   * Get relatedFactions
   * @return relatedFactions
   */
  
  @Schema(name = "related_factions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("related_factions")
  public List<String> getRelatedFactions() {
    return relatedFactions;
  }

  public void setRelatedFactions(List<String> relatedFactions) {
    this.relatedFactions = relatedFactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimelineEvent timelineEvent = (TimelineEvent) o;
    return Objects.equals(this.eventId, timelineEvent.eventId) &&
        Objects.equals(this.year, timelineEvent.year) &&
        Objects.equals(this.name, timelineEvent.name) &&
        Objects.equals(this.description, timelineEvent.description) &&
        Objects.equals(this.type, timelineEvent.type) &&
        Objects.equals(this.impactLevel, timelineEvent.impactLevel) &&
        Objects.equals(this.relatedFactions, timelineEvent.relatedFactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, year, name, description, type, impactLevel, relatedFactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimelineEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    impactLevel: ").append(toIndentedString(impactLevel)).append("\n");
    sb.append("    relatedFactions: ").append(toIndentedString(relatedFactions)).append("\n");
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

