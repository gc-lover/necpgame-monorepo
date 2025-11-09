package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * TimelineEntry
 */


public class TimelineEntry {

  /**
   * Gets or Sets entryType
   */
  public enum EntryTypeEnum {
    TICKET_CREATED("ticket.created"),
    
    TICKET_ASSIGNED("ticket.assigned"),
    
    TICKET_RESPONSE("ticket.response"),
    
    TICKET_STATUS_CHANGED("ticket.status_changed"),
    
    TICKET_ESCALATED("ticket.escalated"),
    
    TICKET_FEEDBACK("ticket.feedback");

    private final String value;

    EntryTypeEnum(String value) {
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
    public static EntryTypeEnum fromValue(String value) {
      for (EntryTypeEnum b : EntryTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable EntryTypeEnum entryType;

  private @Nullable String author;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  @Valid
  private Map<String, Object> details = new HashMap<>();

  public TimelineEntry entryType(@Nullable EntryTypeEnum entryType) {
    this.entryType = entryType;
    return this;
  }

  /**
   * Get entryType
   * @return entryType
   */
  
  @Schema(name = "entryType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entryType")
  public @Nullable EntryTypeEnum getEntryType() {
    return entryType;
  }

  public void setEntryType(@Nullable EntryTypeEnum entryType) {
    this.entryType = entryType;
  }

  public TimelineEntry author(@Nullable String author) {
    this.author = author;
    return this;
  }

  /**
   * Get author
   * @return author
   */
  
  @Schema(name = "author", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("author")
  public @Nullable String getAuthor() {
    return author;
  }

  public void setAuthor(@Nullable String author) {
    this.author = author;
  }

  public TimelineEntry timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public TimelineEntry details(Map<String, Object> details) {
    this.details = details;
    return this;
  }

  public TimelineEntry putDetailsItem(String key, Object detailsItem) {
    if (this.details == null) {
      this.details = new HashMap<>();
    }
    this.details.put(key, detailsItem);
    return this;
  }

  /**
   * Get details
   * @return details
   */
  
  @Schema(name = "details", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("details")
  public Map<String, Object> getDetails() {
    return details;
  }

  public void setDetails(Map<String, Object> details) {
    this.details = details;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimelineEntry timelineEntry = (TimelineEntry) o;
    return Objects.equals(this.entryType, timelineEntry.entryType) &&
        Objects.equals(this.author, timelineEntry.author) &&
        Objects.equals(this.timestamp, timelineEntry.timestamp) &&
        Objects.equals(this.details, timelineEntry.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entryType, author, timestamp, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TimelineEntry {\n");
    sb.append("    entryType: ").append(toIndentedString(entryType)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
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

