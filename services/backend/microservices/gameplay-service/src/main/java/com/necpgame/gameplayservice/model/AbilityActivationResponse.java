package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.CompanionEvent;
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
 * AbilityActivationResponse
 */


public class AbilityActivationResponse {

  private @Nullable String abilityId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime cooldownEndsAt;

  private @Nullable Integer energyCost;

  @Valid
  private List<@Valid CompanionEvent> events = new ArrayList<>();

  public AbilityActivationResponse abilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  
  @Schema(name = "abilityId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilityId")
  public @Nullable String getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
  }

  public AbilityActivationResponse startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "startedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startedAt")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public AbilityActivationResponse cooldownEndsAt(@Nullable OffsetDateTime cooldownEndsAt) {
    this.cooldownEndsAt = cooldownEndsAt;
    return this;
  }

  /**
   * Get cooldownEndsAt
   * @return cooldownEndsAt
   */
  @Valid 
  @Schema(name = "cooldownEndsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownEndsAt")
  public @Nullable OffsetDateTime getCooldownEndsAt() {
    return cooldownEndsAt;
  }

  public void setCooldownEndsAt(@Nullable OffsetDateTime cooldownEndsAt) {
    this.cooldownEndsAt = cooldownEndsAt;
  }

  public AbilityActivationResponse energyCost(@Nullable Integer energyCost) {
    this.energyCost = energyCost;
    return this;
  }

  /**
   * Get energyCost
   * @return energyCost
   */
  
  @Schema(name = "energyCost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energyCost")
  public @Nullable Integer getEnergyCost() {
    return energyCost;
  }

  public void setEnergyCost(@Nullable Integer energyCost) {
    this.energyCost = energyCost;
  }

  public AbilityActivationResponse events(List<@Valid CompanionEvent> events) {
    this.events = events;
    return this;
  }

  public AbilityActivationResponse addEventsItem(CompanionEvent eventsItem) {
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
  @Valid 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<@Valid CompanionEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid CompanionEvent> events) {
    this.events = events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbilityActivationResponse abilityActivationResponse = (AbilityActivationResponse) o;
    return Objects.equals(this.abilityId, abilityActivationResponse.abilityId) &&
        Objects.equals(this.startedAt, abilityActivationResponse.startedAt) &&
        Objects.equals(this.cooldownEndsAt, abilityActivationResponse.cooldownEndsAt) &&
        Objects.equals(this.energyCost, abilityActivationResponse.energyCost) &&
        Objects.equals(this.events, abilityActivationResponse.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilityId, startedAt, cooldownEndsAt, energyCost, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbilityActivationResponse {\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    cooldownEndsAt: ").append(toIndentedString(cooldownEndsAt)).append("\n");
    sb.append("    energyCost: ").append(toIndentedString(energyCost)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

