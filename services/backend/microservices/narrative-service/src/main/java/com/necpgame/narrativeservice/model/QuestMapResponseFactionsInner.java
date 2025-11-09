package com.necpgame.narrativeservice.model;

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
 * QuestMapResponseFactionsInner
 */

@JsonTypeName("QuestMapResponse_factions_inner")

public class QuestMapResponseFactionsInner {

  private @Nullable String factionId;

  private @Nullable String factionName;

  private @Nullable Integer questChains;

  private @Nullable Integer totalQuests;

  public QuestMapResponseFactionsInner factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public QuestMapResponseFactionsInner factionName(@Nullable String factionName) {
    this.factionName = factionName;
    return this;
  }

  /**
   * Get factionName
   * @return factionName
   */
  
  @Schema(name = "faction_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_name")
  public @Nullable String getFactionName() {
    return factionName;
  }

  public void setFactionName(@Nullable String factionName) {
    this.factionName = factionName;
  }

  public QuestMapResponseFactionsInner questChains(@Nullable Integer questChains) {
    this.questChains = questChains;
    return this;
  }

  /**
   * Get questChains
   * @return questChains
   */
  
  @Schema(name = "quest_chains", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest_chains")
  public @Nullable Integer getQuestChains() {
    return questChains;
  }

  public void setQuestChains(@Nullable Integer questChains) {
    this.questChains = questChains;
  }

  public QuestMapResponseFactionsInner totalQuests(@Nullable Integer totalQuests) {
    this.totalQuests = totalQuests;
    return this;
  }

  /**
   * Get totalQuests
   * @return totalQuests
   */
  
  @Schema(name = "total_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_quests")
  public @Nullable Integer getTotalQuests() {
    return totalQuests;
  }

  public void setTotalQuests(@Nullable Integer totalQuests) {
    this.totalQuests = totalQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestMapResponseFactionsInner questMapResponseFactionsInner = (QuestMapResponseFactionsInner) o;
    return Objects.equals(this.factionId, questMapResponseFactionsInner.factionId) &&
        Objects.equals(this.factionName, questMapResponseFactionsInner.factionName) &&
        Objects.equals(this.questChains, questMapResponseFactionsInner.questChains) &&
        Objects.equals(this.totalQuests, questMapResponseFactionsInner.totalQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, factionName, questChains, totalQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestMapResponseFactionsInner {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    factionName: ").append(toIndentedString(factionName)).append("\n");
    sb.append("    questChains: ").append(toIndentedString(questChains)).append("\n");
    sb.append("    totalQuests: ").append(toIndentedString(totalQuests)).append("\n");
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

