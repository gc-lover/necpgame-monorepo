package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.EventFrequency;
import com.necpgame.worldservice.model.EventStatus;
import com.necpgame.worldservice.model.EventWindow;
import com.necpgame.worldservice.model.LocalizationBundle;
import com.necpgame.worldservice.model.RewardDescriptor;
import com.necpgame.worldservice.model.TelemetrySnapshot;
import com.necpgame.worldservice.model.TriggerCondition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SeasonalEvent
 */


public class SeasonalEvent {

  private UUID eventId;

  private String name;

  private EventFrequency frequency;

  private @Nullable EventStatus status;

  private EventWindow window;

  @Valid
  private List<@Valid TriggerCondition> triggerConditions = new ArrayList<>();

  @Valid
  private List<String> requiredResearch = new ArrayList<>();

  private @Nullable String contactNpc;

  private @Nullable String location;

  @Valid
  private List<@Valid RewardDescriptor> rewards = new ArrayList<>();

  private @Nullable TelemetrySnapshot telemetry;

  @Valid
  private List<@Valid LocalizationBundle> localization = new ArrayList<>();

  private @Nullable String season;

  private @Nullable Integer year;

  @Valid
  private List<String> featuredFactions = new ArrayList<>();

  public SeasonalEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SeasonalEvent(UUID eventId, String name, EventFrequency frequency, EventWindow window) {
    this.eventId = eventId;
    this.name = name;
    this.frequency = frequency;
    this.window = window;
  }

  public SeasonalEvent eventId(UUID eventId) {
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

  public SeasonalEvent name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public SeasonalEvent frequency(EventFrequency frequency) {
    this.frequency = frequency;
    return this;
  }

  /**
   * Get frequency
   * @return frequency
   */
  @NotNull @Valid 
  @Schema(name = "frequency", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("frequency")
  public EventFrequency getFrequency() {
    return frequency;
  }

  public void setFrequency(EventFrequency frequency) {
    this.frequency = frequency;
  }

  public SeasonalEvent status(@Nullable EventStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable EventStatus getStatus() {
    return status;
  }

  public void setStatus(@Nullable EventStatus status) {
    this.status = status;
  }

  public SeasonalEvent window(EventWindow window) {
    this.window = window;
    return this;
  }

  /**
   * Get window
   * @return window
   */
  @NotNull @Valid 
  @Schema(name = "window", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("window")
  public EventWindow getWindow() {
    return window;
  }

  public void setWindow(EventWindow window) {
    this.window = window;
  }

  public SeasonalEvent triggerConditions(List<@Valid TriggerCondition> triggerConditions) {
    this.triggerConditions = triggerConditions;
    return this;
  }

  public SeasonalEvent addTriggerConditionsItem(TriggerCondition triggerConditionsItem) {
    if (this.triggerConditions == null) {
      this.triggerConditions = new ArrayList<>();
    }
    this.triggerConditions.add(triggerConditionsItem);
    return this;
  }

  /**
   * Get triggerConditions
   * @return triggerConditions
   */
  @Valid 
  @Schema(name = "triggerConditions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggerConditions")
  public List<@Valid TriggerCondition> getTriggerConditions() {
    return triggerConditions;
  }

  public void setTriggerConditions(List<@Valid TriggerCondition> triggerConditions) {
    this.triggerConditions = triggerConditions;
  }

  public SeasonalEvent requiredResearch(List<String> requiredResearch) {
    this.requiredResearch = requiredResearch;
    return this;
  }

  public SeasonalEvent addRequiredResearchItem(String requiredResearchItem) {
    if (this.requiredResearch == null) {
      this.requiredResearch = new ArrayList<>();
    }
    this.requiredResearch.add(requiredResearchItem);
    return this;
  }

  /**
   * Get requiredResearch
   * @return requiredResearch
   */
  
  @Schema(name = "requiredResearch", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredResearch")
  public List<String> getRequiredResearch() {
    return requiredResearch;
  }

  public void setRequiredResearch(List<String> requiredResearch) {
    this.requiredResearch = requiredResearch;
  }

  public SeasonalEvent contactNpc(@Nullable String contactNpc) {
    this.contactNpc = contactNpc;
    return this;
  }

  /**
   * Get contactNpc
   * @return contactNpc
   */
  
  @Schema(name = "contactNpc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contactNpc")
  public @Nullable String getContactNpc() {
    return contactNpc;
  }

  public void setContactNpc(@Nullable String contactNpc) {
    this.contactNpc = contactNpc;
  }

  public SeasonalEvent location(@Nullable String location) {
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

  public SeasonalEvent rewards(List<@Valid RewardDescriptor> rewards) {
    this.rewards = rewards;
    return this;
  }

  public SeasonalEvent addRewardsItem(RewardDescriptor rewardsItem) {
    if (this.rewards == null) {
      this.rewards = new ArrayList<>();
    }
    this.rewards.add(rewardsItem);
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public List<@Valid RewardDescriptor> getRewards() {
    return rewards;
  }

  public void setRewards(List<@Valid RewardDescriptor> rewards) {
    this.rewards = rewards;
  }

  public SeasonalEvent telemetry(@Nullable TelemetrySnapshot telemetry) {
    this.telemetry = telemetry;
    return this;
  }

  /**
   * Get telemetry
   * @return telemetry
   */
  @Valid 
  @Schema(name = "telemetry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("telemetry")
  public @Nullable TelemetrySnapshot getTelemetry() {
    return telemetry;
  }

  public void setTelemetry(@Nullable TelemetrySnapshot telemetry) {
    this.telemetry = telemetry;
  }

  public SeasonalEvent localization(List<@Valid LocalizationBundle> localization) {
    this.localization = localization;
    return this;
  }

  public SeasonalEvent addLocalizationItem(LocalizationBundle localizationItem) {
    if (this.localization == null) {
      this.localization = new ArrayList<>();
    }
    this.localization.add(localizationItem);
    return this;
  }

  /**
   * Get localization
   * @return localization
   */
  @Valid 
  @Schema(name = "localization", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("localization")
  public List<@Valid LocalizationBundle> getLocalization() {
    return localization;
  }

  public void setLocalization(List<@Valid LocalizationBundle> localization) {
    this.localization = localization;
  }

  public SeasonalEvent season(@Nullable String season) {
    this.season = season;
    return this;
  }

  /**
   * Get season
   * @return season
   */
  
  @Schema(name = "season", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("season")
  public @Nullable String getSeason() {
    return season;
  }

  public void setSeason(@Nullable String season) {
    this.season = season;
  }

  public SeasonalEvent year(@Nullable Integer year) {
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

  public SeasonalEvent featuredFactions(List<String> featuredFactions) {
    this.featuredFactions = featuredFactions;
    return this;
  }

  public SeasonalEvent addFeaturedFactionsItem(String featuredFactionsItem) {
    if (this.featuredFactions == null) {
      this.featuredFactions = new ArrayList<>();
    }
    this.featuredFactions.add(featuredFactionsItem);
    return this;
  }

  /**
   * Get featuredFactions
   * @return featuredFactions
   */
  
  @Schema(name = "featuredFactions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("featuredFactions")
  public List<String> getFeaturedFactions() {
    return featuredFactions;
  }

  public void setFeaturedFactions(List<String> featuredFactions) {
    this.featuredFactions = featuredFactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SeasonalEvent seasonalEvent = (SeasonalEvent) o;
    return Objects.equals(this.eventId, seasonalEvent.eventId) &&
        Objects.equals(this.name, seasonalEvent.name) &&
        Objects.equals(this.frequency, seasonalEvent.frequency) &&
        Objects.equals(this.status, seasonalEvent.status) &&
        Objects.equals(this.window, seasonalEvent.window) &&
        Objects.equals(this.triggerConditions, seasonalEvent.triggerConditions) &&
        Objects.equals(this.requiredResearch, seasonalEvent.requiredResearch) &&
        Objects.equals(this.contactNpc, seasonalEvent.contactNpc) &&
        Objects.equals(this.location, seasonalEvent.location) &&
        Objects.equals(this.rewards, seasonalEvent.rewards) &&
        Objects.equals(this.telemetry, seasonalEvent.telemetry) &&
        Objects.equals(this.localization, seasonalEvent.localization) &&
        Objects.equals(this.season, seasonalEvent.season) &&
        Objects.equals(this.year, seasonalEvent.year) &&
        Objects.equals(this.featuredFactions, seasonalEvent.featuredFactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, name, frequency, status, window, triggerConditions, requiredResearch, contactNpc, location, rewards, telemetry, localization, season, year, featuredFactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SeasonalEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    frequency: ").append(toIndentedString(frequency)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    window: ").append(toIndentedString(window)).append("\n");
    sb.append("    triggerConditions: ").append(toIndentedString(triggerConditions)).append("\n");
    sb.append("    requiredResearch: ").append(toIndentedString(requiredResearch)).append("\n");
    sb.append("    contactNpc: ").append(toIndentedString(contactNpc)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    telemetry: ").append(toIndentedString(telemetry)).append("\n");
    sb.append("    localization: ").append(toIndentedString(localization)).append("\n");
    sb.append("    season: ").append(toIndentedString(season)).append("\n");
    sb.append("    year: ").append(toIndentedString(year)).append("\n");
    sb.append("    featuredFactions: ").append(toIndentedString(featuredFactions)).append("\n");
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

