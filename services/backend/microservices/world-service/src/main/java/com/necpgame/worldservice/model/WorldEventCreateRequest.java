package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WorldEventCreateRequest
 */


public class WorldEventCreateRequest {

  /**
   * Gets or Sets eventType
   */
  public enum EventTypeEnum {
    CRISIS("crisis"),
    
    FESTIVAL("festival"),
    
    ALERT("alert"),
    
    ANNOUNCEMENT("announcement");

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

  private String cityId;

  @Valid
  private List<UUID> relatedEffectIds = new ArrayList<>();

  private String title;

  private String description;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    WARNING("warning"),
    
    ALERT("alert");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startsAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime endsAt;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public WorldEventCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WorldEventCreateRequest(EventTypeEnum eventType, String cityId, String title, String description) {
    this.eventType = eventType;
    this.cityId = cityId;
    this.title = title;
    this.description = description;
  }

  public WorldEventCreateRequest eventType(EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * Get eventType
   * @return eventType
   */
  @NotNull 
  @Schema(name = "eventType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventType")
  public EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public WorldEventCreateRequest cityId(String cityId) {
    this.cityId = cityId;
    return this;
  }

  /**
   * Get cityId
   * @return cityId
   */
  @NotNull @Pattern(regexp = "^[A-Z0-9\\\\-]{3,24}$") 
  @Schema(name = "cityId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cityId")
  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  public WorldEventCreateRequest relatedEffectIds(List<UUID> relatedEffectIds) {
    this.relatedEffectIds = relatedEffectIds;
    return this;
  }

  public WorldEventCreateRequest addRelatedEffectIdsItem(UUID relatedEffectIdsItem) {
    if (this.relatedEffectIds == null) {
      this.relatedEffectIds = new ArrayList<>();
    }
    this.relatedEffectIds.add(relatedEffectIdsItem);
    return this;
  }

  /**
   * Get relatedEffectIds
   * @return relatedEffectIds
   */
  @Valid 
  @Schema(name = "relatedEffectIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relatedEffectIds")
  public List<UUID> getRelatedEffectIds() {
    return relatedEffectIds;
  }

  public void setRelatedEffectIds(List<UUID> relatedEffectIds) {
    this.relatedEffectIds = relatedEffectIds;
  }

  public WorldEventCreateRequest title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public WorldEventCreateRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  @NotNull 
  @Schema(name = "description", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public WorldEventCreateRequest severity(@Nullable SeverityEnum severity) {
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

  public WorldEventCreateRequest startsAt(@Nullable OffsetDateTime startsAt) {
    this.startsAt = startsAt;
    return this;
  }

  /**
   * Get startsAt
   * @return startsAt
   */
  @Valid 
  @Schema(name = "startsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startsAt")
  public @Nullable OffsetDateTime getStartsAt() {
    return startsAt;
  }

  public void setStartsAt(@Nullable OffsetDateTime startsAt) {
    this.startsAt = startsAt;
  }

  public WorldEventCreateRequest endsAt(@Nullable OffsetDateTime endsAt) {
    this.endsAt = endsAt;
    return this;
  }

  /**
   * Get endsAt
   * @return endsAt
   */
  @Valid 
  @Schema(name = "endsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endsAt")
  public @Nullable OffsetDateTime getEndsAt() {
    return endsAt;
  }

  public void setEndsAt(@Nullable OffsetDateTime endsAt) {
    this.endsAt = endsAt;
  }

  public WorldEventCreateRequest metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public WorldEventCreateRequest putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldEventCreateRequest worldEventCreateRequest = (WorldEventCreateRequest) o;
    return Objects.equals(this.eventType, worldEventCreateRequest.eventType) &&
        Objects.equals(this.cityId, worldEventCreateRequest.cityId) &&
        Objects.equals(this.relatedEffectIds, worldEventCreateRequest.relatedEffectIds) &&
        Objects.equals(this.title, worldEventCreateRequest.title) &&
        Objects.equals(this.description, worldEventCreateRequest.description) &&
        Objects.equals(this.severity, worldEventCreateRequest.severity) &&
        Objects.equals(this.startsAt, worldEventCreateRequest.startsAt) &&
        Objects.equals(this.endsAt, worldEventCreateRequest.endsAt) &&
        Objects.equals(this.metadata, worldEventCreateRequest.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventType, cityId, relatedEffectIds, title, description, severity, startsAt, endsAt, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldEventCreateRequest {\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    cityId: ").append(toIndentedString(cityId)).append("\n");
    sb.append("    relatedEffectIds: ").append(toIndentedString(relatedEffectIds)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    startsAt: ").append(toIndentedString(startsAt)).append("\n");
    sb.append("    endsAt: ").append(toIndentedString(endsAt)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

