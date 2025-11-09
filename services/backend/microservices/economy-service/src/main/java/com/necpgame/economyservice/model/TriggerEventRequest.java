package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.EventEffect;
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
 * TriggerEventRequest
 */


public class TriggerEventRequest {

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    CRISIS("CRISIS"),
    
    INFLATION("INFLATION"),
    
    RECESSION("RECESSION"),
    
    BOOM("BOOM"),
    
    TRADE_WAR("TRADE_WAR"),
    
    CORPORATE("CORPORATE"),
    
    COMMODITY("COMMODITY");

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

  private EventTypeEnum eventType;

  private @Nullable String name;

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

  private @Nullable Integer durationDays;

  @Valid
  private List<String> affectedRegions = new ArrayList<>();

  @Valid
  private List<@Valid EventEffect> customEffects = new ArrayList<>();

  public TriggerEventRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TriggerEventRequest(EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public TriggerEventRequest eventType(EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  @NotNull 
  @Schema(name = "event_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("event_type")
  public EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public TriggerEventRequest name(@Nullable String name) {
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

  public TriggerEventRequest severity(@Nullable SeverityEnum severity) {
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

  public TriggerEventRequest durationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
    return this;
  }

  /**
   * Get durationDays
   * @return durationDays
   */
  
  @Schema(name = "duration_days", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_days")
  public @Nullable Integer getDurationDays() {
    return durationDays;
  }

  public void setDurationDays(@Nullable Integer durationDays) {
    this.durationDays = durationDays;
  }

  public TriggerEventRequest affectedRegions(List<String> affectedRegions) {
    this.affectedRegions = affectedRegions;
    return this;
  }

  public TriggerEventRequest addAffectedRegionsItem(String affectedRegionsItem) {
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

  public TriggerEventRequest customEffects(List<@Valid EventEffect> customEffects) {
    this.customEffects = customEffects;
    return this;
  }

  public TriggerEventRequest addCustomEffectsItem(EventEffect customEffectsItem) {
    if (this.customEffects == null) {
      this.customEffects = new ArrayList<>();
    }
    this.customEffects.add(customEffectsItem);
    return this;
  }

  /**
   * Get customEffects
   * @return customEffects
   */
  @Valid 
  @Schema(name = "custom_effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("custom_effects")
  public List<@Valid EventEffect> getCustomEffects() {
    return customEffects;
  }

  public void setCustomEffects(List<@Valid EventEffect> customEffects) {
    this.customEffects = customEffects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerEventRequest triggerEventRequest = (TriggerEventRequest) o;
    return Objects.equals(this.eventType, triggerEventRequest.eventType) &&
        Objects.equals(this.name, triggerEventRequest.name) &&
        Objects.equals(this.severity, triggerEventRequest.severity) &&
        Objects.equals(this.durationDays, triggerEventRequest.durationDays) &&
        Objects.equals(this.affectedRegions, triggerEventRequest.affectedRegions) &&
        Objects.equals(this.customEffects, triggerEventRequest.customEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, name, severity, durationDays, affectedRegions, customEffects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerEventRequest {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    durationDays: ").append(toIndentedString(durationDays)).append("\n");
    sb.append("    affectedRegions: ").append(toIndentedString(affectedRegions)).append("\n");
    sb.append("    customEffects: ").append(toIndentedString(customEffects)).append("\n");
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

