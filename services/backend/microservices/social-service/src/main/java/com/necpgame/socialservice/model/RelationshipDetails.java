package com.necpgame.socialservice.model;

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
 * RelationshipDetails
 */


public class RelationshipDetails {

  private @Nullable String relationshipId;

  private @Nullable String characterId;

  private @Nullable String targetId;

  private @Nullable String targetName;

  private @Nullable String targetType;

  private @Nullable String relationshipType;

  private @Nullable BigDecimal level;

  private @Nullable String stage;

  @Valid
  private List<Object> history = new ArrayList<>();

  @Valid
  private List<String> unlockedContent = new ArrayList<>();

  @Valid
  private List<Object> activeBonuses = new ArrayList<>();

  public RelationshipDetails relationshipId(@Nullable String relationshipId) {
    this.relationshipId = relationshipId;
    return this;
  }

  /**
   * Get relationshipId
   * @return relationshipId
   */
  
  @Schema(name = "relationship_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_id")
  public @Nullable String getRelationshipId() {
    return relationshipId;
  }

  public void setRelationshipId(@Nullable String relationshipId) {
    this.relationshipId = relationshipId;
  }

  public RelationshipDetails characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public RelationshipDetails targetId(@Nullable String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "target_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_id")
  public @Nullable String getTargetId() {
    return targetId;
  }

  public void setTargetId(@Nullable String targetId) {
    this.targetId = targetId;
  }

  public RelationshipDetails targetName(@Nullable String targetName) {
    this.targetName = targetName;
    return this;
  }

  /**
   * Get targetName
   * @return targetName
   */
  
  @Schema(name = "target_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_name")
  public @Nullable String getTargetName() {
    return targetName;
  }

  public void setTargetName(@Nullable String targetName) {
    this.targetName = targetName;
  }

  public RelationshipDetails targetType(@Nullable String targetType) {
    this.targetType = targetType;
    return this;
  }

  /**
   * Get targetType
   * @return targetType
   */
  
  @Schema(name = "target_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("target_type")
  public @Nullable String getTargetType() {
    return targetType;
  }

  public void setTargetType(@Nullable String targetType) {
    this.targetType = targetType;
  }

  public RelationshipDetails relationshipType(@Nullable String relationshipType) {
    this.relationshipType = relationshipType;
    return this;
  }

  /**
   * Get relationshipType
   * @return relationshipType
   */
  
  @Schema(name = "relationship_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationship_type")
  public @Nullable String getRelationshipType() {
    return relationshipType;
  }

  public void setRelationshipType(@Nullable String relationshipType) {
    this.relationshipType = relationshipType;
  }

  public RelationshipDetails level(@Nullable BigDecimal level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  @Valid 
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable BigDecimal getLevel() {
    return level;
  }

  public void setLevel(@Nullable BigDecimal level) {
    this.level = level;
  }

  public RelationshipDetails stage(@Nullable String stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Get stage
   * @return stage
   */
  
  @Schema(name = "stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage")
  public @Nullable String getStage() {
    return stage;
  }

  public void setStage(@Nullable String stage) {
    this.stage = stage;
  }

  public RelationshipDetails history(List<Object> history) {
    this.history = history;
    return this;
  }

  public RelationshipDetails addHistoryItem(Object historyItem) {
    if (this.history == null) {
      this.history = new ArrayList<>();
    }
    this.history.add(historyItem);
    return this;
  }

  /**
   * История взаимодействий
   * @return history
   */
  
  @Schema(name = "history", description = "История взаимодействий", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public List<Object> getHistory() {
    return history;
  }

  public void setHistory(List<Object> history) {
    this.history = history;
  }

  public RelationshipDetails unlockedContent(List<String> unlockedContent) {
    this.unlockedContent = unlockedContent;
    return this;
  }

  public RelationshipDetails addUnlockedContentItem(String unlockedContentItem) {
    if (this.unlockedContent == null) {
      this.unlockedContent = new ArrayList<>();
    }
    this.unlockedContent.add(unlockedContentItem);
    return this;
  }

  /**
   * Разблокированный контент
   * @return unlockedContent
   */
  
  @Schema(name = "unlocked_content", description = "Разблокированный контент", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_content")
  public List<String> getUnlockedContent() {
    return unlockedContent;
  }

  public void setUnlockedContent(List<String> unlockedContent) {
    this.unlockedContent = unlockedContent;
  }

  public RelationshipDetails activeBonuses(List<Object> activeBonuses) {
    this.activeBonuses = activeBonuses;
    return this;
  }

  public RelationshipDetails addActiveBonusesItem(Object activeBonusesItem) {
    if (this.activeBonuses == null) {
      this.activeBonuses = new ArrayList<>();
    }
    this.activeBonuses.add(activeBonusesItem);
    return this;
  }

  /**
   * Активные бонусы от отношений
   * @return activeBonuses
   */
  
  @Schema(name = "active_bonuses", description = "Активные бонусы от отношений", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_bonuses")
  public List<Object> getActiveBonuses() {
    return activeBonuses;
  }

  public void setActiveBonuses(List<Object> activeBonuses) {
    this.activeBonuses = activeBonuses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelationshipDetails relationshipDetails = (RelationshipDetails) o;
    return Objects.equals(this.relationshipId, relationshipDetails.relationshipId) &&
        Objects.equals(this.characterId, relationshipDetails.characterId) &&
        Objects.equals(this.targetId, relationshipDetails.targetId) &&
        Objects.equals(this.targetName, relationshipDetails.targetName) &&
        Objects.equals(this.targetType, relationshipDetails.targetType) &&
        Objects.equals(this.relationshipType, relationshipDetails.relationshipType) &&
        Objects.equals(this.level, relationshipDetails.level) &&
        Objects.equals(this.stage, relationshipDetails.stage) &&
        Objects.equals(this.history, relationshipDetails.history) &&
        Objects.equals(this.unlockedContent, relationshipDetails.unlockedContent) &&
        Objects.equals(this.activeBonuses, relationshipDetails.activeBonuses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, characterId, targetId, targetName, targetType, relationshipType, level, stage, history, unlockedContent, activeBonuses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelationshipDetails {\n");
    sb.append("    relationshipId: ").append(toIndentedString(relationshipId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    targetName: ").append(toIndentedString(targetName)).append("\n");
    sb.append("    targetType: ").append(toIndentedString(targetType)).append("\n");
    sb.append("    relationshipType: ").append(toIndentedString(relationshipType)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
    sb.append("    unlockedContent: ").append(toIndentedString(unlockedContent)).append("\n");
    sb.append("    activeBonuses: ").append(toIndentedString(activeBonuses)).append("\n");
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

