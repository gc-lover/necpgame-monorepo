package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetStarterProgression200ResponseRecommendedOrderInner
 */

@JsonTypeName("getStarterProgression_200_response_recommended_order_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetStarterProgression200ResponseRecommendedOrderInner {

  private @Nullable Integer step;

  private @Nullable String questId;

  private @Nullable String questName;

  private @Nullable Integer estimatedLevel;

  public GetStarterProgression200ResponseRecommendedOrderInner step(@Nullable Integer step) {
    this.step = step;
    return this;
  }

  /**
   * Get step
   * @return step
   */
  
  @Schema(name = "step", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("step")
  public @Nullable Integer getStep() {
    return step;
  }

  public void setStep(@Nullable Integer step) {
    this.step = step;
  }

  public GetStarterProgression200ResponseRecommendedOrderInner questId(@Nullable String questId) {
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

  public GetStarterProgression200ResponseRecommendedOrderInner questName(@Nullable String questName) {
    this.questName = questName;
    return this;
  }

  /**
   * Get questName
   * @return questName
   */
  
  @Schema(name = "quest_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_name")
  public @Nullable String getQuestName() {
    return questName;
  }

  public void setQuestName(@Nullable String questName) {
    this.questName = questName;
  }

  public GetStarterProgression200ResponseRecommendedOrderInner estimatedLevel(@Nullable Integer estimatedLevel) {
    this.estimatedLevel = estimatedLevel;
    return this;
  }

  /**
   * Get estimatedLevel
   * @return estimatedLevel
   */
  
  @Schema(name = "estimated_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_level")
  public @Nullable Integer getEstimatedLevel() {
    return estimatedLevel;
  }

  public void setEstimatedLevel(@Nullable Integer estimatedLevel) {
    this.estimatedLevel = estimatedLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetStarterProgression200ResponseRecommendedOrderInner getStarterProgression200ResponseRecommendedOrderInner = (GetStarterProgression200ResponseRecommendedOrderInner) o;
    return Objects.equals(this.step, getStarterProgression200ResponseRecommendedOrderInner.step) &&
        Objects.equals(this.questId, getStarterProgression200ResponseRecommendedOrderInner.questId) &&
        Objects.equals(this.questName, getStarterProgression200ResponseRecommendedOrderInner.questName) &&
        Objects.equals(this.estimatedLevel, getStarterProgression200ResponseRecommendedOrderInner.estimatedLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(step, questId, questName, estimatedLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetStarterProgression200ResponseRecommendedOrderInner {\n");
    sb.append("    step: ").append(toIndentedString(step)).append("\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    questName: ").append(toIndentedString(questName)).append("\n");
    sb.append("    estimatedLevel: ").append(toIndentedString(estimatedLevel)).append("\n");
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

