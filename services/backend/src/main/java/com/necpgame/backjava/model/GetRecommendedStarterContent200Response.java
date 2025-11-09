package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.MainStoryQuest;
import com.necpgame.backjava.model.StarterQuest;
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
 * GetRecommendedStarterContent200Response
 */

@JsonTypeName("getRecommendedStarterContent_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetRecommendedStarterContent200Response {

  private @Nullable StarterQuest originQuest;

  @Valid
  private List<@Valid StarterQuest> classQuests = new ArrayList<>();

  private @Nullable MainStoryQuest mainStoryEntry;

  @Valid
  private List<@Valid StarterQuest> tutorialQuests = new ArrayList<>();

  public GetRecommendedStarterContent200Response originQuest(@Nullable StarterQuest originQuest) {
    this.originQuest = originQuest;
    return this;
  }

  /**
   * Get originQuest
   * @return originQuest
   */
  @Valid 
  @Schema(name = "origin_quest", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("origin_quest")
  public @Nullable StarterQuest getOriginQuest() {
    return originQuest;
  }

  public void setOriginQuest(@Nullable StarterQuest originQuest) {
    this.originQuest = originQuest;
  }

  public GetRecommendedStarterContent200Response classQuests(List<@Valid StarterQuest> classQuests) {
    this.classQuests = classQuests;
    return this;
  }

  public GetRecommendedStarterContent200Response addClassQuestsItem(StarterQuest classQuestsItem) {
    if (this.classQuests == null) {
      this.classQuests = new ArrayList<>();
    }
    this.classQuests.add(classQuestsItem);
    return this;
  }

  /**
   * Get classQuests
   * @return classQuests
   */
  @Valid 
  @Schema(name = "class_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_quests")
  public List<@Valid StarterQuest> getClassQuests() {
    return classQuests;
  }

  public void setClassQuests(List<@Valid StarterQuest> classQuests) {
    this.classQuests = classQuests;
  }

  public GetRecommendedStarterContent200Response mainStoryEntry(@Nullable MainStoryQuest mainStoryEntry) {
    this.mainStoryEntry = mainStoryEntry;
    return this;
  }

  /**
   * Get mainStoryEntry
   * @return mainStoryEntry
   */
  @Valid 
  @Schema(name = "main_story_entry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("main_story_entry")
  public @Nullable MainStoryQuest getMainStoryEntry() {
    return mainStoryEntry;
  }

  public void setMainStoryEntry(@Nullable MainStoryQuest mainStoryEntry) {
    this.mainStoryEntry = mainStoryEntry;
  }

  public GetRecommendedStarterContent200Response tutorialQuests(List<@Valid StarterQuest> tutorialQuests) {
    this.tutorialQuests = tutorialQuests;
    return this;
  }

  public GetRecommendedStarterContent200Response addTutorialQuestsItem(StarterQuest tutorialQuestsItem) {
    if (this.tutorialQuests == null) {
      this.tutorialQuests = new ArrayList<>();
    }
    this.tutorialQuests.add(tutorialQuestsItem);
    return this;
  }

  /**
   * Get tutorialQuests
   * @return tutorialQuests
   */
  @Valid 
  @Schema(name = "tutorial_quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tutorial_quests")
  public List<@Valid StarterQuest> getTutorialQuests() {
    return tutorialQuests;
  }

  public void setTutorialQuests(List<@Valid StarterQuest> tutorialQuests) {
    this.tutorialQuests = tutorialQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetRecommendedStarterContent200Response getRecommendedStarterContent200Response = (GetRecommendedStarterContent200Response) o;
    return Objects.equals(this.originQuest, getRecommendedStarterContent200Response.originQuest) &&
        Objects.equals(this.classQuests, getRecommendedStarterContent200Response.classQuests) &&
        Objects.equals(this.mainStoryEntry, getRecommendedStarterContent200Response.mainStoryEntry) &&
        Objects.equals(this.tutorialQuests, getRecommendedStarterContent200Response.tutorialQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(originQuest, classQuests, mainStoryEntry, tutorialQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRecommendedStarterContent200Response {\n");
    sb.append("    originQuest: ").append(toIndentedString(originQuest)).append("\n");
    sb.append("    classQuests: ").append(toIndentedString(classQuests)).append("\n");
    sb.append("    mainStoryEntry: ").append(toIndentedString(mainStoryEntry)).append("\n");
    sb.append("    tutorialQuests: ").append(toIndentedString(tutorialQuests)).append("\n");
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

