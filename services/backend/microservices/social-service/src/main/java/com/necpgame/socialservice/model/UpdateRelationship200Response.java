package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * UpdateRelationship200Response
 */

@JsonTypeName("updateRelationship_200_response")

public class UpdateRelationship200Response {

  private @Nullable String relationshipId;

  private @Nullable BigDecimal previousLevel;

  private @Nullable BigDecimal newLevel;

  private @Nullable BigDecimal levelChange;

  private @Nullable Boolean stageChanged;

  private @Nullable String newStage;

  @Valid
  private List<String> unlockedContent = new ArrayList<>();

  public UpdateRelationship200Response relationshipId(@Nullable String relationshipId) {
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

  public UpdateRelationship200Response previousLevel(@Nullable BigDecimal previousLevel) {
    this.previousLevel = previousLevel;
    return this;
  }

  /**
   * Get previousLevel
   * @return previousLevel
   */
  @Valid 
  @Schema(name = "previous_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_level")
  public @Nullable BigDecimal getPreviousLevel() {
    return previousLevel;
  }

  public void setPreviousLevel(@Nullable BigDecimal previousLevel) {
    this.previousLevel = previousLevel;
  }

  public UpdateRelationship200Response newLevel(@Nullable BigDecimal newLevel) {
    this.newLevel = newLevel;
    return this;
  }

  /**
   * Get newLevel
   * @return newLevel
   */
  @Valid 
  @Schema(name = "new_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_level")
  public @Nullable BigDecimal getNewLevel() {
    return newLevel;
  }

  public void setNewLevel(@Nullable BigDecimal newLevel) {
    this.newLevel = newLevel;
  }

  public UpdateRelationship200Response levelChange(@Nullable BigDecimal levelChange) {
    this.levelChange = levelChange;
    return this;
  }

  /**
   * Get levelChange
   * @return levelChange
   */
  @Valid 
  @Schema(name = "level_change", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_change")
  public @Nullable BigDecimal getLevelChange() {
    return levelChange;
  }

  public void setLevelChange(@Nullable BigDecimal levelChange) {
    this.levelChange = levelChange;
  }

  public UpdateRelationship200Response stageChanged(@Nullable Boolean stageChanged) {
    this.stageChanged = stageChanged;
    return this;
  }

  /**
   * Get stageChanged
   * @return stageChanged
   */
  
  @Schema(name = "stage_changed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage_changed")
  public @Nullable Boolean getStageChanged() {
    return stageChanged;
  }

  public void setStageChanged(@Nullable Boolean stageChanged) {
    this.stageChanged = stageChanged;
  }

  public UpdateRelationship200Response newStage(@Nullable String newStage) {
    this.newStage = newStage;
    return this;
  }

  /**
   * Get newStage
   * @return newStage
   */
  
  @Schema(name = "new_stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_stage")
  public @Nullable String getNewStage() {
    return newStage;
  }

  public void setNewStage(@Nullable String newStage) {
    this.newStage = newStage;
  }

  public UpdateRelationship200Response unlockedContent(List<String> unlockedContent) {
    this.unlockedContent = unlockedContent;
    return this;
  }

  public UpdateRelationship200Response addUnlockedContentItem(String unlockedContentItem) {
    if (this.unlockedContent == null) {
      this.unlockedContent = new ArrayList<>();
    }
    this.unlockedContent.add(unlockedContentItem);
    return this;
  }

  /**
   * Get unlockedContent
   * @return unlockedContent
   */
  
  @Schema(name = "unlocked_content", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unlocked_content")
  public List<String> getUnlockedContent() {
    return unlockedContent;
  }

  public void setUnlockedContent(List<String> unlockedContent) {
    this.unlockedContent = unlockedContent;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateRelationship200Response updateRelationship200Response = (UpdateRelationship200Response) o;
    return Objects.equals(this.relationshipId, updateRelationship200Response.relationshipId) &&
        Objects.equals(this.previousLevel, updateRelationship200Response.previousLevel) &&
        Objects.equals(this.newLevel, updateRelationship200Response.newLevel) &&
        Objects.equals(this.levelChange, updateRelationship200Response.levelChange) &&
        Objects.equals(this.stageChanged, updateRelationship200Response.stageChanged) &&
        Objects.equals(this.newStage, updateRelationship200Response.newStage) &&
        Objects.equals(this.unlockedContent, updateRelationship200Response.unlockedContent);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationshipId, previousLevel, newLevel, levelChange, stageChanged, newStage, unlockedContent);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateRelationship200Response {\n");
    sb.append("    relationshipId: ").append(toIndentedString(relationshipId)).append("\n");
    sb.append("    previousLevel: ").append(toIndentedString(previousLevel)).append("\n");
    sb.append("    newLevel: ").append(toIndentedString(newLevel)).append("\n");
    sb.append("    levelChange: ").append(toIndentedString(levelChange)).append("\n");
    sb.append("    stageChanged: ").append(toIndentedString(stageChanged)).append("\n");
    sb.append("    newStage: ").append(toIndentedString(newStage)).append("\n");
    sb.append("    unlockedContent: ").append(toIndentedString(unlockedContent)).append("\n");
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

