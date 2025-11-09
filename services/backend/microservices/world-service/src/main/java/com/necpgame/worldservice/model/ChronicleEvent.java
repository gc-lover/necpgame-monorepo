package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ChronicleEventImpact;
import com.necpgame.worldservice.model.ChronicleEventLinksInner;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ChronicleEvent
 */


public class ChronicleEvent {

  private UUID eventId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CONTROL_SHIFT("control_shift"),
    
    LOGISTICS("logistics"),
    
    RAID("raid"),
    
    STORY_ARC("story_arc"),
    
    ECONOMY("economy"),
    
    ACTION_XP("action_xp");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private UUID regionId;

  private @Nullable UUID routeId;

  @Valid
  private List<UUID> factionIds = new ArrayList<>();

  private String summary;

  private @Nullable String description;

  private @Nullable ChronicleEventImpact impact;

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    INFO("info"),
    
    WARNING("warning"),
    
    CRITICAL("critical");

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
  private List<String> tags = new ArrayList<>();

  @Valid
  private List<@Valid ChronicleEventLinksInner> links = new ArrayList<>();

  @Valid
  private List<String> broadcastChannels = new ArrayList<>();

  public ChronicleEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChronicleEvent(UUID eventId, TypeEnum type, OffsetDateTime timestamp, UUID regionId, String summary) {
    this.eventId = eventId;
    this.type = type;
    this.timestamp = timestamp;
    this.regionId = regionId;
    this.summary = summary;
  }

  public ChronicleEvent eventId(UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull @Valid 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public ChronicleEvent type(TypeEnum type) {
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

  public ChronicleEvent timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public ChronicleEvent regionId(UUID regionId) {
    this.regionId = regionId;
    return this;
  }

  /**
   * Get regionId
   * @return regionId
   */
  @NotNull @Valid 
  @Schema(name = "regionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regionId")
  public UUID getRegionId() {
    return regionId;
  }

  public void setRegionId(UUID regionId) {
    this.regionId = regionId;
  }

  public ChronicleEvent routeId(@Nullable UUID routeId) {
    this.routeId = routeId;
    return this;
  }

  /**
   * Get routeId
   * @return routeId
   */
  @Valid 
  @Schema(name = "routeId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("routeId")
  public @Nullable UUID getRouteId() {
    return routeId;
  }

  public void setRouteId(@Nullable UUID routeId) {
    this.routeId = routeId;
  }

  public ChronicleEvent factionIds(List<UUID> factionIds) {
    this.factionIds = factionIds;
    return this;
  }

  public ChronicleEvent addFactionIdsItem(UUID factionIdsItem) {
    if (this.factionIds == null) {
      this.factionIds = new ArrayList<>();
    }
    this.factionIds.add(factionIdsItem);
    return this;
  }

  /**
   * Get factionIds
   * @return factionIds
   */
  @Valid 
  @Schema(name = "factionIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionIds")
  public List<UUID> getFactionIds() {
    return factionIds;
  }

  public void setFactionIds(List<UUID> factionIds) {
    this.factionIds = factionIds;
  }

  public ChronicleEvent summary(String summary) {
    this.summary = summary;
    return this;
  }

  /**
   * Get summary
   * @return summary
   */
  @NotNull 
  @Schema(name = "summary", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("summary")
  public String getSummary() {
    return summary;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public ChronicleEvent description(@Nullable String description) {
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

  public ChronicleEvent impact(@Nullable ChronicleEventImpact impact) {
    this.impact = impact;
    return this;
  }

  /**
   * Get impact
   * @return impact
   */
  @Valid 
  @Schema(name = "impact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact")
  public @Nullable ChronicleEventImpact getImpact() {
    return impact;
  }

  public void setImpact(@Nullable ChronicleEventImpact impact) {
    this.impact = impact;
  }

  public ChronicleEvent severity(@Nullable SeverityEnum severity) {
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

  public ChronicleEvent tags(List<String> tags) {
    this.tags = tags;
    return this;
  }

  public ChronicleEvent addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

  /**
   * Get tags
   * @return tags
   */
  
  @Schema(name = "tags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tags")
  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public ChronicleEvent links(List<@Valid ChronicleEventLinksInner> links) {
    this.links = links;
    return this;
  }

  public ChronicleEvent addLinksItem(ChronicleEventLinksInner linksItem) {
    if (this.links == null) {
      this.links = new ArrayList<>();
    }
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links
   * @return links
   */
  @Valid 
  @Schema(name = "links", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("links")
  public List<@Valid ChronicleEventLinksInner> getLinks() {
    return links;
  }

  public void setLinks(List<@Valid ChronicleEventLinksInner> links) {
    this.links = links;
  }

  public ChronicleEvent broadcastChannels(List<String> broadcastChannels) {
    this.broadcastChannels = broadcastChannels;
    return this;
  }

  public ChronicleEvent addBroadcastChannelsItem(String broadcastChannelsItem) {
    if (this.broadcastChannels == null) {
      this.broadcastChannels = new ArrayList<>();
    }
    this.broadcastChannels.add(broadcastChannelsItem);
    return this;
  }

  /**
   * Get broadcastChannels
   * @return broadcastChannels
   */
  
  @Schema(name = "broadcastChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("broadcastChannels")
  public List<String> getBroadcastChannels() {
    return broadcastChannels;
  }

  public void setBroadcastChannels(List<String> broadcastChannels) {
    this.broadcastChannels = broadcastChannels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChronicleEvent chronicleEvent = (ChronicleEvent) o;
    return Objects.equals(this.eventId, chronicleEvent.eventId) &&
        Objects.equals(this.type, chronicleEvent.type) &&
        Objects.equals(this.timestamp, chronicleEvent.timestamp) &&
        Objects.equals(this.regionId, chronicleEvent.regionId) &&
        Objects.equals(this.routeId, chronicleEvent.routeId) &&
        Objects.equals(this.factionIds, chronicleEvent.factionIds) &&
        Objects.equals(this.summary, chronicleEvent.summary) &&
        Objects.equals(this.description, chronicleEvent.description) &&
        Objects.equals(this.impact, chronicleEvent.impact) &&
        Objects.equals(this.severity, chronicleEvent.severity) &&
        Objects.equals(this.tags, chronicleEvent.tags) &&
        Objects.equals(this.links, chronicleEvent.links) &&
        Objects.equals(this.broadcastChannels, chronicleEvent.broadcastChannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, type, timestamp, regionId, routeId, factionIds, summary, description, impact, severity, tags, links, broadcastChannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChronicleEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    regionId: ").append(toIndentedString(regionId)).append("\n");
    sb.append("    routeId: ").append(toIndentedString(routeId)).append("\n");
    sb.append("    factionIds: ").append(toIndentedString(factionIds)).append("\n");
    sb.append("    summary: ").append(toIndentedString(summary)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    impact: ").append(toIndentedString(impact)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("    broadcastChannels: ").append(toIndentedString(broadcastChannels)).append("\n");
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

