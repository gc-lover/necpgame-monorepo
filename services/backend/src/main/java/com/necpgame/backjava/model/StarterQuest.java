package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.StarterQuestRewards;
import java.util.Arrays;
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
 * StarterQuest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class StarterQuest {

  private @Nullable String questId;

  private @Nullable String title;

  private @Nullable String description;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    ORIGIN("ORIGIN"),
    
    propertyClass("CLASS"),
    
    FACTION("FACTION"),
    
    TUTORIAL("TUTORIAL"),
    
    MAIN_INTRO("MAIN_INTRO");

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

  private Integer requiredLevel = 1;

  private @Nullable Integer estimatedTimeMinutes;

  private @Nullable StarterQuestRewards rewards;

  private JsonNullable<String> nextQuest = JsonNullable.<String>undefined();

  public StarterQuest questId(@Nullable String questId) {
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

  public StarterQuest title(@Nullable String title) {
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

  public StarterQuest description(@Nullable String description) {
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

  public StarterQuest type(@Nullable TypeEnum type) {
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

  public StarterQuest requiredLevel(Integer requiredLevel) {
    this.requiredLevel = requiredLevel;
    return this;
  }

  /**
   * Get requiredLevel
   * @return requiredLevel
   */
  
  @Schema(name = "required_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_level")
  public Integer getRequiredLevel() {
    return requiredLevel;
  }

  public void setRequiredLevel(Integer requiredLevel) {
    this.requiredLevel = requiredLevel;
  }

  public StarterQuest estimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
    return this;
  }

  /**
   * Get estimatedTimeMinutes
   * @return estimatedTimeMinutes
   */
  
  @Schema(name = "estimated_time_minutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_time_minutes")
  public @Nullable Integer getEstimatedTimeMinutes() {
    return estimatedTimeMinutes;
  }

  public void setEstimatedTimeMinutes(@Nullable Integer estimatedTimeMinutes) {
    this.estimatedTimeMinutes = estimatedTimeMinutes;
  }

  public StarterQuest rewards(@Nullable StarterQuestRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable StarterQuestRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable StarterQuestRewards rewards) {
    this.rewards = rewards;
  }

  public StarterQuest nextQuest(String nextQuest) {
    this.nextQuest = JsonNullable.of(nextQuest);
    return this;
  }

  /**
   * Следующий квест в цепочке
   * @return nextQuest
   */
  
  @Schema(name = "next_quest", description = "Следующий квест в цепочке", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("next_quest")
  public JsonNullable<String> getNextQuest() {
    return nextQuest;
  }

  public void setNextQuest(JsonNullable<String> nextQuest) {
    this.nextQuest = nextQuest;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StarterQuest starterQuest = (StarterQuest) o;
    return Objects.equals(this.questId, starterQuest.questId) &&
        Objects.equals(this.title, starterQuest.title) &&
        Objects.equals(this.description, starterQuest.description) &&
        Objects.equals(this.type, starterQuest.type) &&
        Objects.equals(this.requiredLevel, starterQuest.requiredLevel) &&
        Objects.equals(this.estimatedTimeMinutes, starterQuest.estimatedTimeMinutes) &&
        Objects.equals(this.rewards, starterQuest.rewards) &&
        equalsNullable(this.nextQuest, starterQuest.nextQuest);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, title, description, type, requiredLevel, estimatedTimeMinutes, rewards, hashCodeNullable(nextQuest));
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
    sb.append("class StarterQuest {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    requiredLevel: ").append(toIndentedString(requiredLevel)).append("\n");
    sb.append("    estimatedTimeMinutes: ").append(toIndentedString(estimatedTimeMinutes)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    nextQuest: ").append(toIndentedString(nextQuest)).append("\n");
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

