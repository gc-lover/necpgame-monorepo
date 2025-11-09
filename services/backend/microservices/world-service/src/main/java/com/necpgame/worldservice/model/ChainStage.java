package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.BranchOption;
import com.necpgame.worldservice.model.StageObjective;
import com.necpgame.worldservice.model.StageType;
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
 * ChainStage
 */


public class ChainStage {

  private String stageId;

  private String title;

  private Integer order;

  private StageType type;

  private @Nullable String description;

  @Valid
  private List<@Valid StageObjective> objectives = new ArrayList<>();

  @Valid
  private List<@Valid BranchOption> branchOptions = new ArrayList<>();

  private @Nullable Integer recommendedPowerScore;

  @Valid
  private List<String> worldFlagUpdates = new ArrayList<>();

  private @Nullable Integer timerSeconds;

  public ChainStage() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChainStage(String stageId, String title, Integer order, StageType type) {
    this.stageId = stageId;
    this.title = title;
    this.order = order;
    this.type = type;
  }

  public ChainStage stageId(String stageId) {
    this.stageId = stageId;
    return this;
  }

  /**
   * Get stageId
   * @return stageId
   */
  @NotNull 
  @Schema(name = "stageId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("stageId")
  public String getStageId() {
    return stageId;
  }

  public void setStageId(String stageId) {
    this.stageId = stageId;
  }

  public ChainStage title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  @NotNull 
  @Schema(name = "title", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ChainStage order(Integer order) {
    this.order = order;
    return this;
  }

  /**
   * Get order
   * minimum: 1
   * @return order
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "order", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("order")
  public Integer getOrder() {
    return order;
  }

  public void setOrder(Integer order) {
    this.order = order;
  }

  public ChainStage type(StageType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull @Valid 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public StageType getType() {
    return type;
  }

  public void setType(StageType type) {
    this.type = type;
  }

  public ChainStage description(@Nullable String description) {
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

  public ChainStage objectives(List<@Valid StageObjective> objectives) {
    this.objectives = objectives;
    return this;
  }

  public ChainStage addObjectivesItem(StageObjective objectivesItem) {
    if (this.objectives == null) {
      this.objectives = new ArrayList<>();
    }
    this.objectives.add(objectivesItem);
    return this;
  }

  /**
   * Get objectives
   * @return objectives
   */
  @Valid 
  @Schema(name = "objectives", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("objectives")
  public List<@Valid StageObjective> getObjectives() {
    return objectives;
  }

  public void setObjectives(List<@Valid StageObjective> objectives) {
    this.objectives = objectives;
  }

  public ChainStage branchOptions(List<@Valid BranchOption> branchOptions) {
    this.branchOptions = branchOptions;
    return this;
  }

  public ChainStage addBranchOptionsItem(BranchOption branchOptionsItem) {
    if (this.branchOptions == null) {
      this.branchOptions = new ArrayList<>();
    }
    this.branchOptions.add(branchOptionsItem);
    return this;
  }

  /**
   * Get branchOptions
   * @return branchOptions
   */
  @Valid 
  @Schema(name = "branchOptions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("branchOptions")
  public List<@Valid BranchOption> getBranchOptions() {
    return branchOptions;
  }

  public void setBranchOptions(List<@Valid BranchOption> branchOptions) {
    this.branchOptions = branchOptions;
  }

  public ChainStage recommendedPowerScore(@Nullable Integer recommendedPowerScore) {
    this.recommendedPowerScore = recommendedPowerScore;
    return this;
  }

  /**
   * Get recommendedPowerScore
   * minimum: 0
   * @return recommendedPowerScore
   */
  @Min(value = 0) 
  @Schema(name = "recommendedPowerScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommendedPowerScore")
  public @Nullable Integer getRecommendedPowerScore() {
    return recommendedPowerScore;
  }

  public void setRecommendedPowerScore(@Nullable Integer recommendedPowerScore) {
    this.recommendedPowerScore = recommendedPowerScore;
  }

  public ChainStage worldFlagUpdates(List<String> worldFlagUpdates) {
    this.worldFlagUpdates = worldFlagUpdates;
    return this;
  }

  public ChainStage addWorldFlagUpdatesItem(String worldFlagUpdatesItem) {
    if (this.worldFlagUpdates == null) {
      this.worldFlagUpdates = new ArrayList<>();
    }
    this.worldFlagUpdates.add(worldFlagUpdatesItem);
    return this;
  }

  /**
   * Get worldFlagUpdates
   * @return worldFlagUpdates
   */
  
  @Schema(name = "worldFlagUpdates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worldFlagUpdates")
  public List<String> getWorldFlagUpdates() {
    return worldFlagUpdates;
  }

  public void setWorldFlagUpdates(List<String> worldFlagUpdates) {
    this.worldFlagUpdates = worldFlagUpdates;
  }

  public ChainStage timerSeconds(@Nullable Integer timerSeconds) {
    this.timerSeconds = timerSeconds;
    return this;
  }

  /**
   * Get timerSeconds
   * minimum: 0
   * @return timerSeconds
   */
  @Min(value = 0) 
  @Schema(name = "timerSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timerSeconds")
  public @Nullable Integer getTimerSeconds() {
    return timerSeconds;
  }

  public void setTimerSeconds(@Nullable Integer timerSeconds) {
    this.timerSeconds = timerSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChainStage chainStage = (ChainStage) o;
    return Objects.equals(this.stageId, chainStage.stageId) &&
        Objects.equals(this.title, chainStage.title) &&
        Objects.equals(this.order, chainStage.order) &&
        Objects.equals(this.type, chainStage.type) &&
        Objects.equals(this.description, chainStage.description) &&
        Objects.equals(this.objectives, chainStage.objectives) &&
        Objects.equals(this.branchOptions, chainStage.branchOptions) &&
        Objects.equals(this.recommendedPowerScore, chainStage.recommendedPowerScore) &&
        Objects.equals(this.worldFlagUpdates, chainStage.worldFlagUpdates) &&
        Objects.equals(this.timerSeconds, chainStage.timerSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stageId, title, order, type, description, objectives, branchOptions, recommendedPowerScore, worldFlagUpdates, timerSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChainStage {\n");
    sb.append("    stageId: ").append(toIndentedString(stageId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    order: ").append(toIndentedString(order)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    objectives: ").append(toIndentedString(objectives)).append("\n");
    sb.append("    branchOptions: ").append(toIndentedString(branchOptions)).append("\n");
    sb.append("    recommendedPowerScore: ").append(toIndentedString(recommendedPowerScore)).append("\n");
    sb.append("    worldFlagUpdates: ").append(toIndentedString(worldFlagUpdates)).append("\n");
    sb.append("    timerSeconds: ").append(toIndentedString(timerSeconds)).append("\n");
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

