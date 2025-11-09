package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.AvailableQuestsResponseUpcomingQuestsInner;
import com.necpgame.narrativeservice.model.QuestNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AvailableQuestsResponse
 */


public class AvailableQuestsResponse {

  private @Nullable String playerId;

  private @Nullable Integer currentLevel;

  @Valid
  private List<@Valid QuestNode> availableQuests = new ArrayList<>();

  @Valid
  private List<@Valid QuestNode> recommendedQuests = new ArrayList<>();

  @Valid
  private List<@Valid AvailableQuestsResponseUpcomingQuestsInner> upcomingQuests = new ArrayList<>();

  private @Nullable Integer totalAvailable;

  @Valid
  private Map<String, Object> filtersApplied = new HashMap<>();

  public AvailableQuestsResponse playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "player_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("player_id")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public AvailableQuestsResponse currentLevel(@Nullable Integer currentLevel) {
    this.currentLevel = currentLevel;
    return this;
  }

  /**
   * Get currentLevel
   * @return currentLevel
   */
  
  @Schema(name = "current_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_level")
  public @Nullable Integer getCurrentLevel() {
    return currentLevel;
  }

  public void setCurrentLevel(@Nullable Integer currentLevel) {
    this.currentLevel = currentLevel;
  }

  public AvailableQuestsResponse availableQuests(List<@Valid QuestNode> availableQuests) {
    this.availableQuests = availableQuests;
    return this;
  }

  public AvailableQuestsResponse addAvailableQuestsItem(QuestNode availableQuestsItem) {
    if (this.availableQuests == null) {
      this.availableQuests = new ArrayList<>();
    }
    this.availableQuests.add(availableQuestsItem);
    return this;
  }

  /**
   * Квесты доступные сейчас
   * @return availableQuests
   */
  @Valid 
  @Schema(name = "available_quests", description = "Квесты доступные сейчас", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available_quests")
  public List<@Valid QuestNode> getAvailableQuests() {
    return availableQuests;
  }

  public void setAvailableQuests(List<@Valid QuestNode> availableQuests) {
    this.availableQuests = availableQuests;
  }

  public AvailableQuestsResponse recommendedQuests(List<@Valid QuestNode> recommendedQuests) {
    this.recommendedQuests = recommendedQuests;
    return this;
  }

  public AvailableQuestsResponse addRecommendedQuestsItem(QuestNode recommendedQuestsItem) {
    if (this.recommendedQuests == null) {
      this.recommendedQuests = new ArrayList<>();
    }
    this.recommendedQuests.add(recommendedQuestsItem);
    return this;
  }

  /**
   * Рекомендуемые квесты
   * @return recommendedQuests
   */
  @Valid 
  @Schema(name = "recommended_quests", description = "Рекомендуемые квесты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recommended_quests")
  public List<@Valid QuestNode> getRecommendedQuests() {
    return recommendedQuests;
  }

  public void setRecommendedQuests(List<@Valid QuestNode> recommendedQuests) {
    this.recommendedQuests = recommendedQuests;
  }

  public AvailableQuestsResponse upcomingQuests(List<@Valid AvailableQuestsResponseUpcomingQuestsInner> upcomingQuests) {
    this.upcomingQuests = upcomingQuests;
    return this;
  }

  public AvailableQuestsResponse addUpcomingQuestsItem(AvailableQuestsResponseUpcomingQuestsInner upcomingQuestsItem) {
    if (this.upcomingQuests == null) {
      this.upcomingQuests = new ArrayList<>();
    }
    this.upcomingQuests.add(upcomingQuestsItem);
    return this;
  }

  /**
   * Квесты скоро доступные
   * @return upcomingQuests
   */
  @Valid 
  @Schema(name = "upcoming_quests", description = "Квесты скоро доступные", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upcoming_quests")
  public List<@Valid AvailableQuestsResponseUpcomingQuestsInner> getUpcomingQuests() {
    return upcomingQuests;
  }

  public void setUpcomingQuests(List<@Valid AvailableQuestsResponseUpcomingQuestsInner> upcomingQuests) {
    this.upcomingQuests = upcomingQuests;
  }

  public AvailableQuestsResponse totalAvailable(@Nullable Integer totalAvailable) {
    this.totalAvailable = totalAvailable;
    return this;
  }

  /**
   * Get totalAvailable
   * @return totalAvailable
   */
  
  @Schema(name = "total_available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_available")
  public @Nullable Integer getTotalAvailable() {
    return totalAvailable;
  }

  public void setTotalAvailable(@Nullable Integer totalAvailable) {
    this.totalAvailable = totalAvailable;
  }

  public AvailableQuestsResponse filtersApplied(Map<String, Object> filtersApplied) {
    this.filtersApplied = filtersApplied;
    return this;
  }

  public AvailableQuestsResponse putFiltersAppliedItem(String key, Object filtersAppliedItem) {
    if (this.filtersApplied == null) {
      this.filtersApplied = new HashMap<>();
    }
    this.filtersApplied.put(key, filtersAppliedItem);
    return this;
  }

  /**
   * Get filtersApplied
   * @return filtersApplied
   */
  
  @Schema(name = "filters_applied", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filters_applied")
  public Map<String, Object> getFiltersApplied() {
    return filtersApplied;
  }

  public void setFiltersApplied(Map<String, Object> filtersApplied) {
    this.filtersApplied = filtersApplied;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AvailableQuestsResponse availableQuestsResponse = (AvailableQuestsResponse) o;
    return Objects.equals(this.playerId, availableQuestsResponse.playerId) &&
        Objects.equals(this.currentLevel, availableQuestsResponse.currentLevel) &&
        Objects.equals(this.availableQuests, availableQuestsResponse.availableQuests) &&
        Objects.equals(this.recommendedQuests, availableQuestsResponse.recommendedQuests) &&
        Objects.equals(this.upcomingQuests, availableQuestsResponse.upcomingQuests) &&
        Objects.equals(this.totalAvailable, availableQuestsResponse.totalAvailable) &&
        Objects.equals(this.filtersApplied, availableQuestsResponse.filtersApplied);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, currentLevel, availableQuests, recommendedQuests, upcomingQuests, totalAvailable, filtersApplied);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvailableQuestsResponse {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    currentLevel: ").append(toIndentedString(currentLevel)).append("\n");
    sb.append("    availableQuests: ").append(toIndentedString(availableQuests)).append("\n");
    sb.append("    recommendedQuests: ").append(toIndentedString(recommendedQuests)).append("\n");
    sb.append("    upcomingQuests: ").append(toIndentedString(upcomingQuests)).append("\n");
    sb.append("    totalAvailable: ").append(toIndentedString(totalAvailable)).append("\n");
    sb.append("    filtersApplied: ").append(toIndentedString(filtersApplied)).append("\n");
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

