package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
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
 * GuildEventCreateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildEventCreateRequest {

  private String title;

  private String type;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime schedule;

  private @Nullable String location;

  private @Nullable Integer maxParticipants;

  private @Nullable String description;

  public GuildEventCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuildEventCreateRequest(String title, String type, OffsetDateTime schedule) {
    this.title = title;
    this.type = type;
    this.schedule = schedule;
  }

  public GuildEventCreateRequest title(String title) {
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

  public GuildEventCreateRequest type(String type) {
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
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public GuildEventCreateRequest schedule(OffsetDateTime schedule) {
    this.schedule = schedule;
    return this;
  }

  /**
   * Get schedule
   * @return schedule
   */
  @NotNull @Valid 
  @Schema(name = "schedule", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("schedule")
  public OffsetDateTime getSchedule() {
    return schedule;
  }

  public void setSchedule(OffsetDateTime schedule) {
    this.schedule = schedule;
  }

  public GuildEventCreateRequest location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public GuildEventCreateRequest maxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
    return this;
  }

  /**
   * Get maxParticipants
   * @return maxParticipants
   */
  
  @Schema(name = "maxParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxParticipants")
  public @Nullable Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public GuildEventCreateRequest description(@Nullable String description) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildEventCreateRequest guildEventCreateRequest = (GuildEventCreateRequest) o;
    return Objects.equals(this.title, guildEventCreateRequest.title) &&
        Objects.equals(this.type, guildEventCreateRequest.type) &&
        Objects.equals(this.schedule, guildEventCreateRequest.schedule) &&
        Objects.equals(this.location, guildEventCreateRequest.location) &&
        Objects.equals(this.maxParticipants, guildEventCreateRequest.maxParticipants) &&
        Objects.equals(this.description, guildEventCreateRequest.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, type, schedule, location, maxParticipants, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildEventCreateRequest {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    schedule: ").append(toIndentedString(schedule)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

