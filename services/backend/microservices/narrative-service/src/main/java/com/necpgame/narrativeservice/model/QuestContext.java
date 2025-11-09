package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * QuestContext
 */


public class QuestContext {

  private @Nullable String questState;

  @Valid
  private List<String> activeFlags = new ArrayList<>();

  @Valid
  private List<String> clearedFlags = new ArrayList<>();

  @Valid
  private List<String> activeEvents = new ArrayList<>();

  @Valid
  private List<String> gear = new ArrayList<>();

  @Valid
  private List<String> implants = new ArrayList<>();

  /**
   * Gets or Sets difficulty
   */
  public enum DifficultyEnum {
    STORY("story"),
    
    DEFAULT("default"),
    
    HARDCORE("hardcore");

    private final String value;

    DifficultyEnum(String value) {
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
    public static DifficultyEnum fromValue(String value) {
      for (DifficultyEnum b : DifficultyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DifficultyEnum difficulty;

  private @Nullable String fatigueState;

  public QuestContext questState(@Nullable String questState) {
    this.questState = questState;
    return this;
  }

  /**
   * Get questState
   * @return questState
   */
  
  @Schema(name = "questState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("questState")
  public @Nullable String getQuestState() {
    return questState;
  }

  public void setQuestState(@Nullable String questState) {
    this.questState = questState;
  }

  public QuestContext activeFlags(List<String> activeFlags) {
    this.activeFlags = activeFlags;
    return this;
  }

  public QuestContext addActiveFlagsItem(String activeFlagsItem) {
    if (this.activeFlags == null) {
      this.activeFlags = new ArrayList<>();
    }
    this.activeFlags.add(activeFlagsItem);
    return this;
  }

  /**
   * Get activeFlags
   * @return activeFlags
   */
  
  @Schema(name = "activeFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeFlags")
  public List<String> getActiveFlags() {
    return activeFlags;
  }

  public void setActiveFlags(List<String> activeFlags) {
    this.activeFlags = activeFlags;
  }

  public QuestContext clearedFlags(List<String> clearedFlags) {
    this.clearedFlags = clearedFlags;
    return this;
  }

  public QuestContext addClearedFlagsItem(String clearedFlagsItem) {
    if (this.clearedFlags == null) {
      this.clearedFlags = new ArrayList<>();
    }
    this.clearedFlags.add(clearedFlagsItem);
    return this;
  }

  /**
   * Get clearedFlags
   * @return clearedFlags
   */
  
  @Schema(name = "clearedFlags", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("clearedFlags")
  public List<String> getClearedFlags() {
    return clearedFlags;
  }

  public void setClearedFlags(List<String> clearedFlags) {
    this.clearedFlags = clearedFlags;
  }

  public QuestContext activeEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
    return this;
  }

  public QuestContext addActiveEventsItem(String activeEventsItem) {
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
  
  @Schema(name = "activeEvents", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeEvents")
  public List<String> getActiveEvents() {
    return activeEvents;
  }

  public void setActiveEvents(List<String> activeEvents) {
    this.activeEvents = activeEvents;
  }

  public QuestContext gear(List<String> gear) {
    this.gear = gear;
    return this;
  }

  public QuestContext addGearItem(String gearItem) {
    if (this.gear == null) {
      this.gear = new ArrayList<>();
    }
    this.gear.add(gearItem);
    return this;
  }

  /**
   * Get gear
   * @return gear
   */
  
  @Schema(name = "gear", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gear")
  public List<String> getGear() {
    return gear;
  }

  public void setGear(List<String> gear) {
    this.gear = gear;
  }

  public QuestContext implants(List<String> implants) {
    this.implants = implants;
    return this;
  }

  public QuestContext addImplantsItem(String implantsItem) {
    if (this.implants == null) {
      this.implants = new ArrayList<>();
    }
    this.implants.add(implantsItem);
    return this;
  }

  /**
   * Get implants
   * @return implants
   */
  
  @Schema(name = "implants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implants")
  public List<String> getImplants() {
    return implants;
  }

  public void setImplants(List<String> implants) {
    this.implants = implants;
  }

  public QuestContext difficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
    return this;
  }

  /**
   * Get difficulty
   * @return difficulty
   */
  
  @Schema(name = "difficulty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("difficulty")
  public @Nullable DifficultyEnum getDifficulty() {
    return difficulty;
  }

  public void setDifficulty(@Nullable DifficultyEnum difficulty) {
    this.difficulty = difficulty;
  }

  public QuestContext fatigueState(@Nullable String fatigueState) {
    this.fatigueState = fatigueState;
    return this;
  }

  /**
   * Get fatigueState
   * @return fatigueState
   */
  
  @Schema(name = "fatigueState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fatigueState")
  public @Nullable String getFatigueState() {
    return fatigueState;
  }

  public void setFatigueState(@Nullable String fatigueState) {
    this.fatigueState = fatigueState;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestContext questContext = (QuestContext) o;
    return Objects.equals(this.questState, questContext.questState) &&
        Objects.equals(this.activeFlags, questContext.activeFlags) &&
        Objects.equals(this.clearedFlags, questContext.clearedFlags) &&
        Objects.equals(this.activeEvents, questContext.activeEvents) &&
        Objects.equals(this.gear, questContext.gear) &&
        Objects.equals(this.implants, questContext.implants) &&
        Objects.equals(this.difficulty, questContext.difficulty) &&
        Objects.equals(this.fatigueState, questContext.fatigueState);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questState, activeFlags, clearedFlags, activeEvents, gear, implants, difficulty, fatigueState);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestContext {\n");
    sb.append("    questState: ").append(toIndentedString(questState)).append("\n");
    sb.append("    activeFlags: ").append(toIndentedString(activeFlags)).append("\n");
    sb.append("    clearedFlags: ").append(toIndentedString(clearedFlags)).append("\n");
    sb.append("    activeEvents: ").append(toIndentedString(activeEvents)).append("\n");
    sb.append("    gear: ").append(toIndentedString(gear)).append("\n");
    sb.append("    implants: ").append(toIndentedString(implants)).append("\n");
    sb.append("    difficulty: ").append(toIndentedString(difficulty)).append("\n");
    sb.append("    fatigueState: ").append(toIndentedString(fatigueState)).append("\n");
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

