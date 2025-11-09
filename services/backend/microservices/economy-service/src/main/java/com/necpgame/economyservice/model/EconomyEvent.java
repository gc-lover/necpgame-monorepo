package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EconomyEvent
 */


public class EconomyEvent {

  private @Nullable UUID eventId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CRISIS("CRISIS"),
    
    INFLATION("INFLATION"),
    
    RECESSION("RECESSION"),
    
    BOOM("BOOM"),
    
    TRADE_WAR("TRADE_WAR"),
    
    CORPORATE("CORPORATE"),
    
    COMMODITY("COMMODITY");

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
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    MINOR("MINOR"),
    
    MODERATE("MODERATE"),
    
    MAJOR("MAJOR"),
    
    CATASTROPHIC("CATASTROPHIC");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SeverityEnum severity;

  @Valid
  private List<String> affectedRegions = new ArrayList<>();

  @Valid
  private List<String> affectedSectors = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> endDate = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable Boolean isActive;

  public EconomyEvent eventId(@Nullable UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @Valid 
  @Schema(name = "event_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event_id")
  public @Nullable UUID getEventId() {
    return eventId;
  }

  public void setEventId(@Nullable UUID eventId) {
    this.eventId = eventId;
  }

  public EconomyEvent name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Corporate Stock Market Crash", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public EconomyEvent type(@Nullable TypeEnum type) {
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

  public EconomyEvent severity(@Nullable SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable SeverityEnum severity) {
    this.severity = severity;
  }

  public EconomyEvent affectedRegions(List<String> affectedRegions) {
    this.affectedRegions = affectedRegions;
    return this;
  }

  public EconomyEvent addAffectedRegionsItem(String affectedRegionsItem) {
    if (this.affectedRegions == null) {
      this.affectedRegions = new ArrayList<>();
    }
    this.affectedRegions.add(affectedRegionsItem);
    return this;
  }

  /**
   * Get affectedRegions
   * @return affectedRegions
   */
  
  @Schema(name = "affected_regions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_regions")
  public List<String> getAffectedRegions() {
    return affectedRegions;
  }

  public void setAffectedRegions(List<String> affectedRegions) {
    this.affectedRegions = affectedRegions;
  }

  public EconomyEvent affectedSectors(List<String> affectedSectors) {
    this.affectedSectors = affectedSectors;
    return this;
  }

  public EconomyEvent addAffectedSectorsItem(String affectedSectorsItem) {
    if (this.affectedSectors == null) {
      this.affectedSectors = new ArrayList<>();
    }
    this.affectedSectors.add(affectedSectorsItem);
    return this;
  }

  /**
   * Get affectedSectors
   * @return affectedSectors
   */
  
  @Schema(name = "affected_sectors", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_sectors")
  public List<String> getAffectedSectors() {
    return affectedSectors;
  }

  public void setAffectedSectors(List<String> affectedSectors) {
    this.affectedSectors = affectedSectors;
  }

  public EconomyEvent startDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
   */
  @Valid 
  @Schema(name = "start_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("start_date")
  public @Nullable OffsetDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(@Nullable OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public EconomyEvent endDate(OffsetDateTime endDate) {
    this.endDate = JsonNullable.of(endDate);
    return this;
  }

  /**
   * Get endDate
   * @return endDate
   */
  @Valid 
  @Schema(name = "end_date", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("end_date")
  public JsonNullable<OffsetDateTime> getEndDate() {
    return endDate;
  }

  public void setEndDate(JsonNullable<OffsetDateTime> endDate) {
    this.endDate = endDate;
  }

  public EconomyEvent isActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
    return this;
  }

  /**
   * Get isActive
   * @return isActive
   */
  
  @Schema(name = "is_active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_active")
  public @Nullable Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(@Nullable Boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomyEvent economyEvent = (EconomyEvent) o;
    return Objects.equals(this.eventId, economyEvent.eventId) &&
        Objects.equals(this.name, economyEvent.name) &&
        Objects.equals(this.type, economyEvent.type) &&
        Objects.equals(this.severity, economyEvent.severity) &&
        Objects.equals(this.affectedRegions, economyEvent.affectedRegions) &&
        Objects.equals(this.affectedSectors, economyEvent.affectedSectors) &&
        Objects.equals(this.startDate, economyEvent.startDate) &&
        equalsNullable(this.endDate, economyEvent.endDate) &&
        Objects.equals(this.isActive, economyEvent.isActive);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, type, severity, affectedRegions, affectedSectors, startDate, hashCodeNullable(endDate), isActive);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomyEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    affectedRegions: ").append(toIndentedString(affectedRegions)).append("\n");
    sb.append("    affectedSectors: ").append(toIndentedString(affectedSectors)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
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

