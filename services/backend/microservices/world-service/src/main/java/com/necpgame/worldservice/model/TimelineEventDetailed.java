package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * TimelineEventDetailed
 */


public class TimelineEventDetailed {

  private @Nullable Integer year;

  private JsonNullable<Integer> month = JsonNullable.<Integer>undefined();

  private @Nullable String eventId;

  private @Nullable String name;

  private @Nullable String description;

  @Valid
  private List<String> participants = new ArrayList<>();

  @Valid
  private List<String> locations = new ArrayList<>();

  @Valid
  private List<String> consequences = new ArrayList<>();

  @Valid
  private List<String> relatedQuests = new ArrayList<>();

  public TimelineEventDetailed year(@Nullable Integer year) {
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

  public TimelineEventDetailed month(Integer month) {
    this.month = JsonNullable.of(month);
    return this;
  }

  /**
   * Get month
   * @return month
   */
  
  @Schema(name = "month", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("month")
  public JsonNullable<Integer> getMonth() {
    return month;
  }

  public void setMonth(JsonNullable<Integer> month) {
    this.month = month;
  }

  public TimelineEventDetailed eventId(@Nullable String eventId) {
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

  public TimelineEventDetailed name(@Nullable String name) {
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

  public TimelineEventDetailed description(@Nullable String description) {
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

  public TimelineEventDetailed participants(List<String> participants) {
    this.participants = participants;
    return this;
  }

  public TimelineEventDetailed addParticipantsItem(String participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<String> getParticipants() {
    return participants;
  }

  public void setParticipants(List<String> participants) {
    this.participants = participants;
  }

  public TimelineEventDetailed locations(List<String> locations) {
    this.locations = locations;
    return this;
  }

  public TimelineEventDetailed addLocationsItem(String locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }
    this.locations.add(locationsItem);
    return this;
  }

  /**
   * Get locations
   * @return locations
   */
  
  @Schema(name = "locations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locations")
  public List<String> getLocations() {
    return locations;
  }

  public void setLocations(List<String> locations) {
    this.locations = locations;
  }

  public TimelineEventDetailed consequences(List<String> consequences) {
    this.consequences = consequences;
    return this;
  }

  public TimelineEventDetailed addConsequencesItem(String consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<String> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<String> consequences) {
    this.consequences = consequences;
  }

  public TimelineEventDetailed relatedQuests(List<String> relatedQuests) {
    this.relatedQuests = relatedQuests;
    return this;
  }

  public TimelineEventDetailed addRelatedQuestsItem(String relatedQuestsItem) {
    if (this.relatedQuests == null) {
      this.relatedQuests = new ArrayList<>();
    }
    this.relatedQuests.add(relatedQuestsItem);
    return this;
  }

  /**
   * Get relatedQuests
   * @return relatedQuests
   */
  
  @Schema(name = "related_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("related_quests")
  public List<String> getRelatedQuests() {
    return relatedQuests;
  }

  public void setRelatedQuests(List<String> relatedQuests) {
    this.relatedQuests = relatedQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TimelineEventDetailed timelineEventDetailed = (TimelineEventDetailed) o;
    return Objects.equals(this.year, timelineEventDetailed.year) &&
        equalsNullable(this.month, timelineEventDetailed.month) &&
        Objects.equals(this.eventId, timelineEventDetailed.eventId) &&
        Objects.equals(this.name, timelineEventDetailed.name) &&
        Objects.equals(this.description, timelineEventDetailed.description) &&
        Objects.equals(this.participants, timelineEventDetailed.participants) &&
        Objects.equals(this.locations, timelineEventDetailed.locations) &&
        Objects.equals(this.consequences, timelineEventDetailed.consequences) &&
        Objects.equals(this.relatedQuests, timelineEventDetailed.relatedQuests);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(year, hashCodeNullable(month), eventId, name, description, participants, locations, consequences, relatedQuests);
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
    sb.append("class TimelineEventDetailed {\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    month: ").append(toIndentedString(month)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    locations: ").append(toIndentedString(locations)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
    sb.append("    relatedQuests: ").append(toIndentedString(relatedQuests)).append("\n");
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

