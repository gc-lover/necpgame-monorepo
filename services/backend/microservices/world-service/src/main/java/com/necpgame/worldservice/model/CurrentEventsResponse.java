package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.EventDefinition;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * CurrentEventsResponse
 */


public class CurrentEventsResponse {

  @Valid
  private List<@Valid EventDefinition> active = new ArrayList<>();

  @Valid
  private List<@Valid EventDefinition> upcoming = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime generatedAt;

  public CurrentEventsResponse active(List<@Valid EventDefinition> active) {
    this.active = active;
    return this;
  }

  public CurrentEventsResponse addActiveItem(EventDefinition activeItem) {
    if (this.active == null) {
      this.active = new ArrayList<>();
    }
    this.active.add(activeItem);
    return this;
  }

  /**
   * Get active
   * @return active
   */
  @Valid 
  @Schema(name = "active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active")
  public List<@Valid EventDefinition> getActive() {
    return active;
  }

  public void setActive(List<@Valid EventDefinition> active) {
    this.active = active;
  }

  public CurrentEventsResponse upcoming(List<@Valid EventDefinition> upcoming) {
    this.upcoming = upcoming;
    return this;
  }

  public CurrentEventsResponse addUpcomingItem(EventDefinition upcomingItem) {
    if (this.upcoming == null) {
      this.upcoming = new ArrayList<>();
    }
    this.upcoming.add(upcomingItem);
    return this;
  }

  /**
   * Get upcoming
   * @return upcoming
   */
  @Valid 
  @Schema(name = "upcoming", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upcoming")
  public List<@Valid EventDefinition> getUpcoming() {
    return upcoming;
  }

  public void setUpcoming(List<@Valid EventDefinition> upcoming) {
    this.upcoming = upcoming;
  }

  public CurrentEventsResponse generatedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
    return this;
  }

  /**
   * Get generatedAt
   * @return generatedAt
   */
  @Valid 
  @Schema(name = "generatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generatedAt")
  public @Nullable OffsetDateTime getGeneratedAt() {
    return generatedAt;
  }

  public void setGeneratedAt(@Nullable OffsetDateTime generatedAt) {
    this.generatedAt = generatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurrentEventsResponse currentEventsResponse = (CurrentEventsResponse) o;
    return Objects.equals(this.active, currentEventsResponse.active) &&
        Objects.equals(this.upcoming, currentEventsResponse.upcoming) &&
        Objects.equals(this.generatedAt, currentEventsResponse.generatedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(active, upcoming, generatedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurrentEventsResponse {\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
    sb.append("    upcoming: ").append(toIndentedString(upcoming)).append("\n");
    sb.append("    generatedAt: ").append(toIndentedString(generatedAt)).append("\n");
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

