package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.QuestNode;
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
 * AvailableQuestsResponseUpcomingQuestsInner
 */

@JsonTypeName("AvailableQuestsResponse_upcoming_quests_inner")

public class AvailableQuestsResponseUpcomingQuestsInner {

  private @Nullable QuestNode quest;

  @Valid
  private List<String> missingPrerequisites = new ArrayList<>();

  public AvailableQuestsResponseUpcomingQuestsInner quest(@Nullable QuestNode quest) {
    this.quest = quest;
    return this;
  }

  /**
   * Get quest
   * @return quest
   */
  @Valid 
  @Schema(name = "quest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quest")
  public @Nullable QuestNode getQuest() {
    return quest;
  }

  public void setQuest(@Nullable QuestNode quest) {
    this.quest = quest;
  }

  public AvailableQuestsResponseUpcomingQuestsInner missingPrerequisites(List<String> missingPrerequisites) {
    this.missingPrerequisites = missingPrerequisites;
    return this;
  }

  public AvailableQuestsResponseUpcomingQuestsInner addMissingPrerequisitesItem(String missingPrerequisitesItem) {
    if (this.missingPrerequisites == null) {
      this.missingPrerequisites = new ArrayList<>();
    }
    this.missingPrerequisites.add(missingPrerequisitesItem);
    return this;
  }

  /**
   * Get missingPrerequisites
   * @return missingPrerequisites
   */
  
  @Schema(name = "missing_prerequisites", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missing_prerequisites")
  public List<String> getMissingPrerequisites() {
    return missingPrerequisites;
  }

  public void setMissingPrerequisites(List<String> missingPrerequisites) {
    this.missingPrerequisites = missingPrerequisites;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AvailableQuestsResponseUpcomingQuestsInner availableQuestsResponseUpcomingQuestsInner = (AvailableQuestsResponseUpcomingQuestsInner) o;
    return Objects.equals(this.quest, availableQuestsResponseUpcomingQuestsInner.quest) &&
        Objects.equals(this.missingPrerequisites, availableQuestsResponseUpcomingQuestsInner.missingPrerequisites);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quest, missingPrerequisites);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvailableQuestsResponseUpcomingQuestsInner {\n");
    sb.append("    quest: ").append(toIndentedString(quest)).append("\n");
    sb.append("    missingPrerequisites: ").append(toIndentedString(missingPrerequisites)).append("\n");
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

