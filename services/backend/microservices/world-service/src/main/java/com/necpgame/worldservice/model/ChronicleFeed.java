package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.ChronicleEvent;
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
 * ChronicleFeed
 */


public class ChronicleFeed {

  @Valid
  private List<@Valid ChronicleEvent> events = new ArrayList<>();

  private @Nullable String nextCursor;

  private Boolean hasMore;

  public ChronicleFeed() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChronicleFeed(List<@Valid ChronicleEvent> events, Boolean hasMore) {
    this.events = events;
    this.hasMore = hasMore;
  }

  public ChronicleFeed events(List<@Valid ChronicleEvent> events) {
    this.events = events;
    return this;
  }

  public ChronicleFeed addEventsItem(ChronicleEvent eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Get events
   * @return events
   */
  @NotNull @Valid 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("events")
  public List<@Valid ChronicleEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid ChronicleEvent> events) {
    this.events = events;
  }

  public ChronicleFeed nextCursor(@Nullable String nextCursor) {
    this.nextCursor = nextCursor;
    return this;
  }

  /**
   * Get nextCursor
   * @return nextCursor
   */
  
  @Schema(name = "nextCursor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nextCursor")
  public @Nullable String getNextCursor() {
    return nextCursor;
  }

  public void setNextCursor(@Nullable String nextCursor) {
    this.nextCursor = nextCursor;
  }

  public ChronicleFeed hasMore(Boolean hasMore) {
    this.hasMore = hasMore;
    return this;
  }

  /**
   * Get hasMore
   * @return hasMore
   */
  @NotNull 
  @Schema(name = "hasMore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hasMore")
  public Boolean getHasMore() {
    return hasMore;
  }

  public void setHasMore(Boolean hasMore) {
    this.hasMore = hasMore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChronicleFeed chronicleFeed = (ChronicleFeed) o;
    return Objects.equals(this.events, chronicleFeed.events) &&
        Objects.equals(this.nextCursor, chronicleFeed.nextCursor) &&
        Objects.equals(this.hasMore, chronicleFeed.hasMore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(events, nextCursor, hasMore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChronicleFeed {\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
    sb.append("    nextCursor: ").append(toIndentedString(nextCursor)).append("\n");
    sb.append("    hasMore: ").append(toIndentedString(hasMore)).append("\n");
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

