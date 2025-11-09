package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * TriggerConditions
 */


public class TriggerConditions {

  private JsonNullable<Integer> minLevel = JsonNullable.<Integer>undefined();

  private JsonNullable<Integer> maxLevel = JsonNullable.<Integer>undefined();

  @Valid
  private JsonNullable<Map<String, Integer>> requiredReputation = JsonNullable.<Map<String, Integer>>undefined();

  @Valid
  private JsonNullable<List<String>> requiredItems = JsonNullable.<List<String>>undefined();

  @Valid
  private JsonNullable<List<String>> previousEvents = JsonNullable.<List<String>>undefined();

  @Valid
  private JsonNullable<List<String>> mutuallyExclusiveWith = JsonNullable.<List<String>>undefined();

  public TriggerConditions minLevel(Integer minLevel) {
    this.minLevel = JsonNullable.of(minLevel);
    return this;
  }

  /**
   * Get minLevel
   * @return minLevel
   */
  
  @Schema(name = "min_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_level")
  public JsonNullable<Integer> getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(JsonNullable<Integer> minLevel) {
    this.minLevel = minLevel;
  }

  public TriggerConditions maxLevel(Integer maxLevel) {
    this.maxLevel = JsonNullable.of(maxLevel);
    return this;
  }

  /**
   * Get maxLevel
   * @return maxLevel
   */
  
  @Schema(name = "max_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_level")
  public JsonNullable<Integer> getMaxLevel() {
    return maxLevel;
  }

  public void setMaxLevel(JsonNullable<Integer> maxLevel) {
    this.maxLevel = maxLevel;
  }

  public TriggerConditions requiredReputation(Map<String, Integer> requiredReputation) {
    this.requiredReputation = JsonNullable.of(requiredReputation);
    return this;
  }

  public TriggerConditions putRequiredReputationItem(String key, Integer requiredReputationItem) {
    if (this.requiredReputation == null || !this.requiredReputation.isPresent()) {
      this.requiredReputation = JsonNullable.of(new HashMap<>());
    }
    this.requiredReputation.get().put(key, requiredReputationItem);
    return this;
  }

  /**
   * Get requiredReputation
   * @return requiredReputation
   */
  
  @Schema(name = "required_reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_reputation")
  public JsonNullable<Map<String, Integer>> getRequiredReputation() {
    return requiredReputation;
  }

  public void setRequiredReputation(JsonNullable<Map<String, Integer>> requiredReputation) {
    this.requiredReputation = requiredReputation;
  }

  public TriggerConditions requiredItems(List<String> requiredItems) {
    this.requiredItems = JsonNullable.of(requiredItems);
    return this;
  }

  public TriggerConditions addRequiredItemsItem(String requiredItemsItem) {
    if (this.requiredItems == null || !this.requiredItems.isPresent()) {
      this.requiredItems = JsonNullable.of(new ArrayList<>());
    }
    this.requiredItems.get().add(requiredItemsItem);
    return this;
  }

  /**
   * Get requiredItems
   * @return requiredItems
   */
  
  @Schema(name = "required_items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_items")
  public JsonNullable<List<String>> getRequiredItems() {
    return requiredItems;
  }

  public void setRequiredItems(JsonNullable<List<String>> requiredItems) {
    this.requiredItems = requiredItems;
  }

  public TriggerConditions previousEvents(List<String> previousEvents) {
    this.previousEvents = JsonNullable.of(previousEvents);
    return this;
  }

  public TriggerConditions addPreviousEventsItem(String previousEventsItem) {
    if (this.previousEvents == null || !this.previousEvents.isPresent()) {
      this.previousEvents = JsonNullable.of(new ArrayList<>());
    }
    this.previousEvents.get().add(previousEventsItem);
    return this;
  }

  /**
   * События, которые должны произойти раньше
   * @return previousEvents
   */
  
  @Schema(name = "previous_events", description = "События, которые должны произойти раньше", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_events")
  public JsonNullable<List<String>> getPreviousEvents() {
    return previousEvents;
  }

  public void setPreviousEvents(JsonNullable<List<String>> previousEvents) {
    this.previousEvents = previousEvents;
  }

  public TriggerConditions mutuallyExclusiveWith(List<String> mutuallyExclusiveWith) {
    this.mutuallyExclusiveWith = JsonNullable.of(mutuallyExclusiveWith);
    return this;
  }

  public TriggerConditions addMutuallyExclusiveWithItem(String mutuallyExclusiveWithItem) {
    if (this.mutuallyExclusiveWith == null || !this.mutuallyExclusiveWith.isPresent()) {
      this.mutuallyExclusiveWith = JsonNullable.of(new ArrayList<>());
    }
    this.mutuallyExclusiveWith.get().add(mutuallyExclusiveWithItem);
    return this;
  }

  /**
   * События, с которыми несовместимо
   * @return mutuallyExclusiveWith
   */
  
  @Schema(name = "mutually_exclusive_with", description = "События, с которыми несовместимо", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mutually_exclusive_with")
  public JsonNullable<List<String>> getMutuallyExclusiveWith() {
    return mutuallyExclusiveWith;
  }

  public void setMutuallyExclusiveWith(JsonNullable<List<String>> mutuallyExclusiveWith) {
    this.mutuallyExclusiveWith = mutuallyExclusiveWith;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerConditions triggerConditions = (TriggerConditions) o;
    return equalsNullable(this.minLevel, triggerConditions.minLevel) &&
        equalsNullable(this.maxLevel, triggerConditions.maxLevel) &&
        equalsNullable(this.requiredReputation, triggerConditions.requiredReputation) &&
        equalsNullable(this.requiredItems, triggerConditions.requiredItems) &&
        equalsNullable(this.previousEvents, triggerConditions.previousEvents) &&
        equalsNullable(this.mutuallyExclusiveWith, triggerConditions.mutuallyExclusiveWith);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(hashCodeNullable(minLevel), hashCodeNullable(maxLevel), hashCodeNullable(requiredReputation), hashCodeNullable(requiredItems), hashCodeNullable(previousEvents), hashCodeNullable(mutuallyExclusiveWith));
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
    sb.append("class TriggerConditions {\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    maxLevel: ").append(toIndentedString(maxLevel)).append("\n");
    sb.append("    requiredReputation: ").append(toIndentedString(requiredReputation)).append("\n");
    sb.append("    requiredItems: ").append(toIndentedString(requiredItems)).append("\n");
    sb.append("    previousEvents: ").append(toIndentedString(previousEvents)).append("\n");
    sb.append("    mutuallyExclusiveWith: ").append(toIndentedString(mutuallyExclusiveWith)).append("\n");
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

