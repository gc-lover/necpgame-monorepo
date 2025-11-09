package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * WorldQuest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class WorldQuest {

  private @Nullable String questId;

  private @Nullable String title;

  private @Nullable String description;

  private JsonNullable<String> faction = JsonNullable.<String>undefined();

  @Valid
  private List<String> availableInRegions = new ArrayList<>();

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    FACTION("FACTION"),
    
    EVENT("EVENT"),
    
    SEASONAL("SEASONAL"),
    
    SPECIAL("SPECIAL");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private JsonNullable<Object> requiredReputation = JsonNullable.<Object>undefined();

  private @Nullable Object rewards;

  private JsonNullable<Integer> durationDays = JsonNullable.<Integer>undefined();

  public WorldQuest questId(@Nullable String questId) {
    this.questId = questId;
    return this;
  }

  /**
   * Get questId
   * @return questId
   */
  
  @Schema(name = "quest_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_id")
  public @Nullable String getQuestId() {
    return questId;
  }

  public void setQuestId(@Nullable String questId) {
    this.questId = questId;
  }

  public WorldQuest title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public WorldQuest description(@Nullable String description) {
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

  public WorldQuest faction(String faction) {
    this.faction = JsonNullable.of(faction);
    return this;
  }

  /**
   * Get faction
   * @return faction
   */
  
  @Schema(name = "faction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction")
  public JsonNullable<String> getFaction() {
    return faction;
  }

  public void setFaction(JsonNullable<String> faction) {
    this.faction = faction;
  }

  public WorldQuest availableInRegions(List<String> availableInRegions) {
    this.availableInRegions = availableInRegions;
    return this;
  }

  public WorldQuest addAvailableInRegionsItem(String availableInRegionsItem) {
    if (this.availableInRegions == null) {
      this.availableInRegions = new ArrayList<>();
    }
    this.availableInRegions.add(availableInRegionsItem);
    return this;
  }

  /**
   * Get availableInRegions
   * @return availableInRegions
   */
  
  @Schema(name = "available_in_regions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_in_regions")
  public List<String> getAvailableInRegions() {
    return availableInRegions;
  }

  public void setAvailableInRegions(List<String> availableInRegions) {
    this.availableInRegions = availableInRegions;
  }

  public WorldQuest type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public WorldQuest requiredReputation(Object requiredReputation) {
    this.requiredReputation = JsonNullable.of(requiredReputation);
    return this;
  }

  /**
   * Get requiredReputation
   * @return requiredReputation
   */
  
  @Schema(name = "required_reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_reputation")
  public JsonNullable<Object> getRequiredReputation() {
    return requiredReputation;
  }

  public void setRequiredReputation(JsonNullable<Object> requiredReputation) {
    this.requiredReputation = requiredReputation;
  }

  public WorldQuest rewards(@Nullable Object rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable Object getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable Object rewards) {
    this.rewards = rewards;
  }

  public WorldQuest durationDays(Integer durationDays) {
    this.durationDays = JsonNullable.of(durationDays);
    return this;
  }

  /**
   * Для временных квестов
   * @return durationDays
   */
  
  @Schema(name = "duration_days", description = "Для временных квестов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration_days")
  public JsonNullable<Integer> getDurationDays() {
    return durationDays;
  }

  public void setDurationDays(JsonNullable<Integer> durationDays) {
    this.durationDays = durationDays;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldQuest worldQuest = (WorldQuest) o;
    return Objects.equals(this.questId, worldQuest.questId) &&
        Objects.equals(this.title, worldQuest.title) &&
        Objects.equals(this.description, worldQuest.description) &&
        equalsNullable(this.faction, worldQuest.faction) &&
        Objects.equals(this.availableInRegions, worldQuest.availableInRegions) &&
        Objects.equals(this.type, worldQuest.type) &&
        equalsNullable(this.requiredReputation, worldQuest.requiredReputation) &&
        Objects.equals(this.rewards, worldQuest.rewards) &&
        equalsNullable(this.durationDays, worldQuest.durationDays);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, title, description, hashCodeNullable(faction), availableInRegions, type, hashCodeNullable(requiredReputation), rewards, hashCodeNullable(durationDays));
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
    sb.append("class WorldQuest {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    faction: ").append(toIndentedString(faction)).append("\n");
    sb.append("    availableInRegions: ").append(toIndentedString(availableInRegions)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    requiredReputation: ").append(toIndentedString(requiredReputation)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    durationDays: ").append(toIndentedString(durationDays)).append("\n");
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

