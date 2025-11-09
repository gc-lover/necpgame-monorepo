package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.EventOutcomeConsequencesCurrencyChange;
import com.necpgame.worldservice.model.EventOutcomeConsequencesItemsGainedInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EventOutcomeConsequences
 */

@JsonTypeName("EventOutcome_consequences")

public class EventOutcomeConsequences {

  @Valid
  private Map<String, Integer> reputationChanges = new HashMap<>();

  @Valid
  private List<@Valid EventOutcomeConsequencesItemsGainedInner> itemsGained = new ArrayList<>();

  @Valid
  private List<@Valid EventOutcomeConsequencesItemsGainedInner> itemsLost = new ArrayList<>();

  private @Nullable EventOutcomeConsequencesCurrencyChange currencyChange;

  private @Nullable Integer experienceGained;

  @Valid
  private List<String> unlocks = new ArrayList<>();

  @Valid
  private List<String> followUpEvents = new ArrayList<>();

  public EventOutcomeConsequences reputationChanges(Map<String, Integer> reputationChanges) {
    this.reputationChanges = reputationChanges;
    return this;
  }

  public EventOutcomeConsequences putReputationChangesItem(String key, Integer reputationChangesItem) {
    if (this.reputationChanges == null) {
      this.reputationChanges = new HashMap<>();
    }
    this.reputationChanges.put(key, reputationChangesItem);
    return this;
  }

  /**
   * Get reputationChanges
   * @return reputationChanges
   */
  
  @Schema(name = "reputation_changes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation_changes")
  public Map<String, Integer> getReputationChanges() {
    return reputationChanges;
  }

  public void setReputationChanges(Map<String, Integer> reputationChanges) {
    this.reputationChanges = reputationChanges;
  }

  public EventOutcomeConsequences itemsGained(List<@Valid EventOutcomeConsequencesItemsGainedInner> itemsGained) {
    this.itemsGained = itemsGained;
    return this;
  }

  public EventOutcomeConsequences addItemsGainedItem(EventOutcomeConsequencesItemsGainedInner itemsGainedItem) {
    if (this.itemsGained == null) {
      this.itemsGained = new ArrayList<>();
    }
    this.itemsGained.add(itemsGainedItem);
    return this;
  }

  /**
   * Get itemsGained
   * @return itemsGained
   */
  @Valid 
  @Schema(name = "items_gained", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items_gained")
  public List<@Valid EventOutcomeConsequencesItemsGainedInner> getItemsGained() {
    return itemsGained;
  }

  public void setItemsGained(List<@Valid EventOutcomeConsequencesItemsGainedInner> itemsGained) {
    this.itemsGained = itemsGained;
  }

  public EventOutcomeConsequences itemsLost(List<@Valid EventOutcomeConsequencesItemsGainedInner> itemsLost) {
    this.itemsLost = itemsLost;
    return this;
  }

  public EventOutcomeConsequences addItemsLostItem(EventOutcomeConsequencesItemsGainedInner itemsLostItem) {
    if (this.itemsLost == null) {
      this.itemsLost = new ArrayList<>();
    }
    this.itemsLost.add(itemsLostItem);
    return this;
  }

  /**
   * Get itemsLost
   * @return itemsLost
   */
  @Valid 
  @Schema(name = "items_lost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items_lost")
  public List<@Valid EventOutcomeConsequencesItemsGainedInner> getItemsLost() {
    return itemsLost;
  }

  public void setItemsLost(List<@Valid EventOutcomeConsequencesItemsGainedInner> itemsLost) {
    this.itemsLost = itemsLost;
  }

  public EventOutcomeConsequences currencyChange(@Nullable EventOutcomeConsequencesCurrencyChange currencyChange) {
    this.currencyChange = currencyChange;
    return this;
  }

  /**
   * Get currencyChange
   * @return currencyChange
   */
  @Valid 
  @Schema(name = "currency_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currency_change")
  public @Nullable EventOutcomeConsequencesCurrencyChange getCurrencyChange() {
    return currencyChange;
  }

  public void setCurrencyChange(@Nullable EventOutcomeConsequencesCurrencyChange currencyChange) {
    this.currencyChange = currencyChange;
  }

  public EventOutcomeConsequences experienceGained(@Nullable Integer experienceGained) {
    this.experienceGained = experienceGained;
    return this;
  }

  /**
   * Get experienceGained
   * @return experienceGained
   */
  
  @Schema(name = "experience_gained", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_gained")
  public @Nullable Integer getExperienceGained() {
    return experienceGained;
  }

  public void setExperienceGained(@Nullable Integer experienceGained) {
    this.experienceGained = experienceGained;
  }

  public EventOutcomeConsequences unlocks(List<String> unlocks) {
    this.unlocks = unlocks;
    return this;
  }

  public EventOutcomeConsequences addUnlocksItem(String unlocksItem) {
    if (this.unlocks == null) {
      this.unlocks = new ArrayList<>();
    }
    this.unlocks.add(unlocksItem);
    return this;
  }

  /**
   * Квесты/локации/NPC
   * @return unlocks
   */
  
  @Schema(name = "unlocks", description = "Квесты/локации/NPC", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocks")
  public List<String> getUnlocks() {
    return unlocks;
  }

  public void setUnlocks(List<String> unlocks) {
    this.unlocks = unlocks;
  }

  public EventOutcomeConsequences followUpEvents(List<String> followUpEvents) {
    this.followUpEvents = followUpEvents;
    return this;
  }

  public EventOutcomeConsequences addFollowUpEventsItem(String followUpEventsItem) {
    if (this.followUpEvents == null) {
      this.followUpEvents = new ArrayList<>();
    }
    this.followUpEvents.add(followUpEventsItem);
    return this;
  }

  /**
   * События, которые могут появиться после
   * @return followUpEvents
   */
  
  @Schema(name = "follow_up_events", description = "События, которые могут появиться после", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("follow_up_events")
  public List<String> getFollowUpEvents() {
    return followUpEvents;
  }

  public void setFollowUpEvents(List<String> followUpEvents) {
    this.followUpEvents = followUpEvents;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EventOutcomeConsequences eventOutcomeConsequences = (EventOutcomeConsequences) o;
    return Objects.equals(this.reputationChanges, eventOutcomeConsequences.reputationChanges) &&
        Objects.equals(this.itemsGained, eventOutcomeConsequences.itemsGained) &&
        Objects.equals(this.itemsLost, eventOutcomeConsequences.itemsLost) &&
        Objects.equals(this.currencyChange, eventOutcomeConsequences.currencyChange) &&
        Objects.equals(this.experienceGained, eventOutcomeConsequences.experienceGained) &&
        Objects.equals(this.unlocks, eventOutcomeConsequences.unlocks) &&
        Objects.equals(this.followUpEvents, eventOutcomeConsequences.followUpEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reputationChanges, itemsGained, itemsLost, currencyChange, experienceGained, unlocks, followUpEvents);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EventOutcomeConsequences {\n");
    sb.append("    reputationChanges: ").append(toIndentedString(reputationChanges)).append("\n");
    sb.append("    itemsGained: ").append(toIndentedString(itemsGained)).append("\n");
    sb.append("    itemsLost: ").append(toIndentedString(itemsLost)).append("\n");
    sb.append("    currencyChange: ").append(toIndentedString(currencyChange)).append("\n");
    sb.append("    experienceGained: ").append(toIndentedString(experienceGained)).append("\n");
    sb.append("    unlocks: ").append(toIndentedString(unlocks)).append("\n");
    sb.append("    followUpEvents: ").append(toIndentedString(followUpEvents)).append("\n");
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

