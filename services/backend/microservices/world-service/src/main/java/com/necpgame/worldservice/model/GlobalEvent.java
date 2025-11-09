package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GlobalEvent
 */


public class GlobalEvent {

  private String eventId;

  private String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    POLITICAL("political"),
    
    ECONOMIC("economic"),
    
    TECHNOLOGICAL("technological"),
    
    ENVIRONMENTAL("environmental"),
    
    SOCIAL("social");

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

  private TypeEnum type;

  private String era;

  private @Nullable Integer yearStart;

  private @Nullable Integer yearEnd;

  private @Nullable Boolean isActive;

  private @Nullable String shortDescription;

  public GlobalEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GlobalEvent(String eventId, String name, TypeEnum type, String era) {
    this.eventId = eventId;
    this.name = name;
    this.type = type;
    this.era = era;
  }

  public GlobalEvent eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull 
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("event_id")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public GlobalEvent name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public GlobalEvent type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public GlobalEvent era(String era) {
    this.era = era;
    return this;
  }

  /**
   * Эпоха события
   * @return era
   */
  @NotNull 
  @Schema(name = "era", description = "Эпоха события", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("era")
  public String getEra() {
    return era;
  }

  public void setEra(String era) {
    this.era = era;
  }

  public GlobalEvent yearStart(@Nullable Integer yearStart) {
    this.yearStart = yearStart;
    return this;
  }

  /**
   * Год начала события
   * @return yearStart
   */
  
  @Schema(name = "year_start", description = "Год начала события", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("year_start")
  public @Nullable Integer getYearStart() {
    return yearStart;
  }

  public void setYearStart(@Nullable Integer yearStart) {
    this.yearStart = yearStart;
  }

  public GlobalEvent yearEnd(@Nullable Integer yearEnd) {
    this.yearEnd = yearEnd;
    return this;
  }

  /**
   * Год окончания события (если длительное)
   * @return yearEnd
   */
  
  @Schema(name = "year_end", description = "Год окончания события (если длительное)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("year_end")
  public @Nullable Integer getYearEnd() {
    return yearEnd;
  }

  public void setYearEnd(@Nullable Integer yearEnd) {
    this.yearEnd = yearEnd;
  }

  public GlobalEvent isActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * Активно ли событие в текущий момент
   * @return isActive
   */
  
  @Schema(name = "is_active", description = "Активно ли событие в текущий момент", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_active")
  public @Nullable Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
  }

  public GlobalEvent shortDescription(@Nullable String shortDescription) {
    this.shortDescription = shortDescription;
    return this;
  }

  /**
   * Get shortDescription
   * @return shortDescription
   */
  
  @Schema(name = "short_description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("short_description")
  public @Nullable String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(@Nullable String shortDescription) {
    this.shortDescription = shortDescription;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GlobalEvent globalEvent = (GlobalEvent) o;
    return Objects.equals(this.eventId, globalEvent.eventId) &&
        Objects.equals(this.name, globalEvent.name) &&
        Objects.equals(this.type, globalEvent.type) &&
        Objects.equals(this.era, globalEvent.era) &&
        Objects.equals(this.yearStart, globalEvent.yearStart) &&
        Objects.equals(this.yearEnd, globalEvent.yearEnd) &&
        Objects.equals(this.isActive, globalEvent.isActive) &&
        Objects.equals(this.shortDescription, globalEvent.shortDescription);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, type, era, yearStart, yearEnd, isActive, shortDescription);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GlobalEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
    sb.append("    yearStart: ").append(toIndentedString(yearStart)).append("\n");
    sb.append("    yearEnd: ").append(toIndentedString(yearEnd)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
    sb.append("    shortDescription: ").append(toIndentedString(shortDescription)).append("\n");
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

