package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ConversationSnapshotRecommendations
 */

@JsonTypeName("ConversationSnapshot_recommendations")

public class ConversationSnapshotRecommendations {

  @Valid
  private List<String> suggestedOrder = new ArrayList<>();

  private @Nullable String followUpQuest;

  @Valid
  private List<String> uiModules = new ArrayList<>();

  public ConversationSnapshotRecommendations suggestedOrder(List<String> suggestedOrder) {
    this.suggestedOrder = suggestedOrder;
    return this;
  }

  public ConversationSnapshotRecommendations addSuggestedOrderItem(String suggestedOrderItem) {
    if (this.suggestedOrder == null) {
      this.suggestedOrder = new ArrayList<>();
    }
    this.suggestedOrder.add(suggestedOrderItem);
    return this;
  }

  /**
   * Get suggestedOrder
   * @return suggestedOrder
   */
  
  @Schema(name = "suggestedOrder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("suggestedOrder")
  public List<String> getSuggestedOrder() {
    return suggestedOrder;
  }

  public void setSuggestedOrder(List<String> suggestedOrder) {
    this.suggestedOrder = suggestedOrder;
  }

  public ConversationSnapshotRecommendations followUpQuest(@Nullable String followUpQuest) {
    this.followUpQuest = followUpQuest;
    return this;
  }

  /**
   * Get followUpQuest
   * @return followUpQuest
   */
  
  @Schema(name = "followUpQuest", example = "quest-main-002-choose-path", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("followUpQuest")
  public @Nullable String getFollowUpQuest() {
    return followUpQuest;
  }

  public void setFollowUpQuest(@Nullable String followUpQuest) {
    this.followUpQuest = followUpQuest;
  }

  public ConversationSnapshotRecommendations uiModules(List<String> uiModules) {
    this.uiModules = uiModules;
    return this;
  }

  public ConversationSnapshotRecommendations addUiModulesItem(String uiModulesItem) {
    if (this.uiModules == null) {
      this.uiModules = new ArrayList<>();
    }
    this.uiModules.add(uiModulesItem);
    return this;
  }

  /**
   * Get uiModules
   * @return uiModules
   */
  
  @Schema(name = "uiModules", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uiModules")
  public List<String> getUiModules() {
    return uiModules;
  }

  public void setUiModules(List<String> uiModules) {
    this.uiModules = uiModules;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConversationSnapshotRecommendations conversationSnapshotRecommendations = (ConversationSnapshotRecommendations) o;
    return Objects.equals(this.suggestedOrder, conversationSnapshotRecommendations.suggestedOrder) &&
        Objects.equals(this.followUpQuest, conversationSnapshotRecommendations.followUpQuest) &&
        Objects.equals(this.uiModules, conversationSnapshotRecommendations.uiModules);
  }

  @Override
  public int hashCode() {
    return Objects.hash(suggestedOrder, followUpQuest, uiModules);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConversationSnapshotRecommendations {\n");
    sb.append("    suggestedOrder: ").append(toIndentedString(suggestedOrder)).append("\n");
    sb.append("    followUpQuest: ").append(toIndentedString(followUpQuest)).append("\n");
    sb.append("    uiModules: ").append(toIndentedString(uiModules)).append("\n");
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

