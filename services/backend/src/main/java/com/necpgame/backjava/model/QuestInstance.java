package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.QuestInstanceProgressValue;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
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
 * QuestInstance
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class QuestInstance {

  private @Nullable UUID id;

  private @Nullable UUID characterId;

  private @Nullable String questTemplateId;

  private @Nullable String questName;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    COMPLETED("COMPLETED"),
    
    FAILED("FAILED"),
    
    ABANDONED("ABANDONED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private JsonNullable<String> currentBranchId = JsonNullable.<String>undefined();

  private JsonNullable<String> currentDialogueNodeId = JsonNullable.<String>undefined();

  @Valid
  private Map<String, QuestInstanceProgressValue> progress = new HashMap<>();

  @Valid
  private Map<String, Object> flags = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime startedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> completedAt = JsonNullable.<OffsetDateTime>undefined();

  public QuestInstance id(@Nullable UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable UUID getId() {
    return id;
  }

  public void setId(@Nullable UUID id) {
    this.id = id;
  }

  public QuestInstance characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public QuestInstance questTemplateId(@Nullable String questTemplateId) {
    this.questTemplateId = questTemplateId;
    return this;
  }

  /**
   * Get questTemplateId
   * @return questTemplateId
   */
  
  @Schema(name = "quest_template_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_template_id")
  public @Nullable String getQuestTemplateId() {
    return questTemplateId;
  }

  public void setQuestTemplateId(@Nullable String questTemplateId) {
    this.questTemplateId = questTemplateId;
  }

  public QuestInstance questName(@Nullable String questName) {
    this.questName = questName;
    return this;
  }

  /**
   * Get questName
   * @return questName
   */
  
  @Schema(name = "quest_name", example = "First Contact", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_name")
  public @Nullable String getQuestName() {
    return questName;
  }

  public void setQuestName(@Nullable String questName) {
    this.questName = questName;
  }

  public QuestInstance status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public QuestInstance currentBranchId(String currentBranchId) {
    this.currentBranchId = JsonNullable.of(currentBranchId);
    return this;
  }

  /**
   * Get currentBranchId
   * @return currentBranchId
   */
  
  @Schema(name = "current_branch_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_branch_id")
  public JsonNullable<String> getCurrentBranchId() {
    return currentBranchId;
  }

  public void setCurrentBranchId(JsonNullable<String> currentBranchId) {
    this.currentBranchId = currentBranchId;
  }

  public QuestInstance currentDialogueNodeId(String currentDialogueNodeId) {
    this.currentDialogueNodeId = JsonNullable.of(currentDialogueNodeId);
    return this;
  }

  /**
   * Get currentDialogueNodeId
   * @return currentDialogueNodeId
   */
  
  @Schema(name = "current_dialogue_node_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_dialogue_node_id")
  public JsonNullable<String> getCurrentDialogueNodeId() {
    return currentDialogueNodeId;
  }

  public void setCurrentDialogueNodeId(JsonNullable<String> currentDialogueNodeId) {
    this.currentDialogueNodeId = currentDialogueNodeId;
  }

  public QuestInstance progress(Map<String, QuestInstanceProgressValue> progress) {
    this.progress = progress;
    return this;
  }

  public QuestInstance putProgressItem(String key, QuestInstanceProgressValue progressItem) {
    if (this.progress == null) {
      this.progress = new HashMap<>();
    }
    this.progress.put(key, progressItem);
    return this;
  }

  /**
   * Прогресс по objectives
   * @return progress
   */
  @Valid 
  @Schema(name = "progress", description = "Прогресс по objectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  public Map<String, QuestInstanceProgressValue> getProgress() {
    return progress;
  }

  public void setProgress(Map<String, QuestInstanceProgressValue> progress) {
    this.progress = progress;
  }

  public QuestInstance flags(Map<String, Object> flags) {
    this.flags = flags;
    return this;
  }

  public QuestInstance putFlagsItem(String key, Object flagsItem) {
    if (this.flags == null) {
      this.flags = new HashMap<>();
    }
    this.flags.put(key, flagsItem);
    return this;
  }

  /**
   * Флаги квеста для условий
   * @return flags
   */
  
  @Schema(name = "flags", description = "Флаги квеста для условий", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flags")
  public Map<String, Object> getFlags() {
    return flags;
  }

  public void setFlags(Map<String, Object> flags) {
    this.flags = flags;
  }

  public QuestInstance startedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
    return this;
  }

  /**
   * Get startedAt
   * @return startedAt
   */
  @Valid 
  @Schema(name = "started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("started_at")
  public @Nullable OffsetDateTime getStartedAt() {
    return startedAt;
  }

  public void setStartedAt(@Nullable OffsetDateTime startedAt) {
    this.startedAt = startedAt;
  }

  public QuestInstance completedAt(OffsetDateTime completedAt) {
    this.completedAt = JsonNullable.of(completedAt);
    return this;
  }

  /**
   * Get completedAt
   * @return completedAt
   */
  @Valid 
  @Schema(name = "completed_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completed_at")
  public JsonNullable<OffsetDateTime> getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(JsonNullable<OffsetDateTime> completedAt) {
    this.completedAt = completedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestInstance questInstance = (QuestInstance) o;
    return Objects.equals(this.id, questInstance.id) &&
        Objects.equals(this.characterId, questInstance.characterId) &&
        Objects.equals(this.questTemplateId, questInstance.questTemplateId) &&
        Objects.equals(this.questName, questInstance.questName) &&
        Objects.equals(this.status, questInstance.status) &&
        equalsNullable(this.currentBranchId, questInstance.currentBranchId) &&
        equalsNullable(this.currentDialogueNodeId, questInstance.currentDialogueNodeId) &&
        Objects.equals(this.progress, questInstance.progress) &&
        Objects.equals(this.flags, questInstance.flags) &&
        Objects.equals(this.startedAt, questInstance.startedAt) &&
        equalsNullable(this.completedAt, questInstance.completedAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, characterId, questTemplateId, questName, status, hashCodeNullable(currentBranchId), hashCodeNullable(currentDialogueNodeId), progress, flags, startedAt, hashCodeNullable(completedAt));
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
    sb.append("class QuestInstance {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    questTemplateId: ").append(toIndentedString(questTemplateId)).append("\n");
    sb.append("    questName: ").append(toIndentedString(questName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    currentBranchId: ").append(toIndentedString(currentBranchId)).append("\n");
    sb.append("    currentDialogueNodeId: ").append(toIndentedString(currentDialogueNodeId)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
    sb.append("    flags: ").append(toIndentedString(flags)).append("\n");
    sb.append("    startedAt: ").append(toIndentedString(startedAt)).append("\n");
    sb.append("    completedAt: ").append(toIndentedString(completedAt)).append("\n");
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

