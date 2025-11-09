package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * ZoneStatus
 */


public class ZoneStatus {

  private @Nullable BigDecimal timeRemaining;

  private @Nullable BigDecimal currentDifficulty;

  @Valid
  private List<String> activeEvents = new ArrayList<>();

  private @Nullable BigDecimal factionActivity;

  public ZoneStatus timeRemaining(@Nullable BigDecimal timeRemaining) {
    this.timeRemaining = timeRemaining;
    return this;
  }

  /**
   * Get timeRemaining
   * @return timeRemaining
   */
  @Valid 
  @Schema(name = "time_remaining", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_remaining")
  public @Nullable BigDecimal getTimeRemaining() {
    return timeRemaining;
  }

  public void setTimeRemaining(@Nullable BigDecimal timeRemaining) {
    this.timeRemaining = timeRemaining;
  }

  public ZoneStatus currentDifficulty(@Nullable BigDecimal currentDifficulty) {
    this.currentDifficulty = currentDifficulty;
    return this;
  }

  /**
   * Get currentDifficulty
   * @return currentDifficulty
   */
  @Valid 
  @Schema(name = "current_difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_difficulty")
  public @Nullable BigDecimal getCurrentDifficulty() {
    return currentDifficulty;
  }

  public void setCurrentDifficulty(@Nullable BigDecimal currentDifficulty) {
    this.currentDifficulty = currentDifficulty;
  }

  public ZoneStatus activeEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
    return this;
  }

  public ZoneStatus addActiveEventsItem(String activeEventsItem) {
    if (this.activeEvents == null) {
      this.activeEvents = new ArrayList<>();
    }
    this.activeEvents.add(activeEventsItem);
    return this;
  }

  /**
   * Get activeEvents
   * @return activeEvents
   */
  
  @Schema(name = "active_events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_events")
  public List<String> getActiveEvents() {
    return activeEvents;
  }

  public void setActiveEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
  }

  public ZoneStatus factionActivity(@Nullable BigDecimal factionActivity) {
    this.factionActivity = factionActivity;
    return this;
  }

  /**
   * Get factionActivity
   * @return factionActivity
   */
  @Valid 
  @Schema(name = "faction_activity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_activity")
  public @Nullable BigDecimal getFactionActivity() {
    return factionActivity;
  }

  public void setFactionActivity(@Nullable BigDecimal factionActivity) {
    this.factionActivity = factionActivity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZoneStatus zoneStatus = (ZoneStatus) o;
    return Objects.equals(this.timeRemaining, zoneStatus.timeRemaining) &&
        Objects.equals(this.currentDifficulty, zoneStatus.currentDifficulty) &&
        Objects.equals(this.activeEvents, zoneStatus.activeEvents) &&
        Objects.equals(this.factionActivity, zoneStatus.factionActivity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timeRemaining, currentDifficulty, activeEvents, factionActivity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZoneStatus {\n");
    sb.append("    timeRemaining: ").append(toIndentedString(timeRemaining)).append("\n");
    sb.append("    currentDifficulty: ").append(toIndentedString(currentDifficulty)).append("\n");
    sb.append("    activeEvents: ").append(toIndentedString(activeEvents)).append("\n");
    sb.append("    factionActivity: ").append(toIndentedString(factionActivity)).append("\n");
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

