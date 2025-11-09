package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.OriginStoryStartingBonuses;
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
 * OriginStoryDetailed
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class OriginStoryDetailed {

  private @Nullable String originId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String backstory;

  private @Nullable OriginStoryStartingBonuses startingBonuses;

  @Valid
  private List<String> compatibleClasses = new ArrayList<>();

  private @Nullable String fullBackstory;

  private @Nullable StarterQuest originQuest;

  @Valid
  private List<String> uniqueDialogueOptions = new ArrayList<>();

  @Valid
  private List<String> worldReactions = new ArrayList<>();

  public OriginStoryDetailed originId(@Nullable String originId) {
    this.originId = originId;
    return this;
  }

  /**
   * Get originId
   * @return originId
   */
  
  @Schema(name = "origin_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("origin_id")
  public @Nullable String getOriginId() {
    return originId;
  }

  public void setOriginId(@Nullable String originId) {
    this.originId = originId;
  }

  public OriginStoryDetailed name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Solo - Military Veteran", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public OriginStoryDetailed description(@Nullable String description) {
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

  public OriginStoryDetailed backstory(@Nullable String backstory) {
    this.backstory = backstory;
    return this;
  }

  /**
   * Get backstory
   * @return backstory
   */
  
  @Schema(name = "backstory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("backstory")
  public @Nullable String getBackstory() {
    return backstory;
  }

  public void setBackstory(@Nullable String backstory) {
    this.backstory = backstory;
  }

  public OriginStoryDetailed startingBonuses(@Nullable OriginStoryStartingBonuses startingBonuses) {
    this.startingBonuses = startingBonuses;
    return this;
  }

  /**
   * Get startingBonuses
   * @return startingBonuses
   */
  @Valid 
  @Schema(name = "starting_bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starting_bonuses")
  public @Nullable OriginStoryStartingBonuses getStartingBonuses() {
    return startingBonuses;
  }

  public void setStartingBonuses(@Nullable OriginStoryStartingBonuses startingBonuses) {
    this.startingBonuses = startingBonuses;
  }

  public OriginStoryDetailed compatibleClasses(List<String> compatibleClasses) {
    this.compatibleClasses = compatibleClasses;
    return this;
  }

  public OriginStoryDetailed addCompatibleClassesItem(String compatibleClassesItem) {
    if (this.compatibleClasses == null) {
      this.compatibleClasses = new ArrayList<>();
    }
    this.compatibleClasses.add(compatibleClassesItem);
    return this;
  }

  /**
   * Get compatibleClasses
   * @return compatibleClasses
   */
  
  @Schema(name = "compatible_classes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("compatible_classes")
  public List<String> getCompatibleClasses() {
    return compatibleClasses;
  }

  public void setCompatibleClasses(List<String> compatibleClasses) {
    this.compatibleClasses = compatibleClasses;
  }

  public OriginStoryDetailed fullBackstory(@Nullable String fullBackstory) {
    this.fullBackstory = fullBackstory;
    return this;
  }

  /**
   * Get fullBackstory
   * @return fullBackstory
   */
  
  @Schema(name = "full_backstory", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("full_backstory")
  public @Nullable String getFullBackstory() {
    return fullBackstory;
  }

  public void setFullBackstory(@Nullable String fullBackstory) {
    this.fullBackstory = fullBackstory;
  }

  public OriginStoryDetailed originQuest(@Nullable StarterQuest originQuest) {
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

  public OriginStoryDetailed uniqueDialogueOptions(List<String> uniqueDialogueOptions) {
    this.uniqueDialogueOptions = uniqueDialogueOptions;
    return this;
  }

  public OriginStoryDetailed addUniqueDialogueOptionsItem(String uniqueDialogueOptionsItem) {
    if (this.uniqueDialogueOptions == null) {
      this.uniqueDialogueOptions = new ArrayList<>();
    }
    this.uniqueDialogueOptions.add(uniqueDialogueOptionsItem);
    return this;
  }

  /**
   * Get uniqueDialogueOptions
   * @return uniqueDialogueOptions
   */
  
  @Schema(name = "unique_dialogue_options", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unique_dialogue_options")
  public List<String> getUniqueDialogueOptions() {
    return uniqueDialogueOptions;
  }

  public void setUniqueDialogueOptions(List<String> uniqueDialogueOptions) {
    this.uniqueDialogueOptions = uniqueDialogueOptions;
  }

  public OriginStoryDetailed worldReactions(List<String> worldReactions) {
    this.worldReactions = worldReactions;
    return this;
  }

  public OriginStoryDetailed addWorldReactionsItem(String worldReactionsItem) {
    if (this.worldReactions == null) {
      this.worldReactions = new ArrayList<>();
    }
    this.worldReactions.add(worldReactionsItem);
    return this;
  }

  /**
   * Как мир реагирует на origin
   * @return worldReactions
   */
  
  @Schema(name = "world_reactions", description = "Как мир реагирует на origin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("world_reactions")
  public List<String> getWorldReactions() {
    return worldReactions;
  }

  public void setWorldReactions(List<String> worldReactions) {
    this.worldReactions = worldReactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OriginStoryDetailed originStoryDetailed = (OriginStoryDetailed) o;
    return Objects.equals(this.originId, originStoryDetailed.originId) &&
        Objects.equals(this.name, originStoryDetailed.name) &&
        Objects.equals(this.description, originStoryDetailed.description) &&
        Objects.equals(this.backstory, originStoryDetailed.backstory) &&
        Objects.equals(this.startingBonuses, originStoryDetailed.startingBonuses) &&
        Objects.equals(this.compatibleClasses, originStoryDetailed.compatibleClasses) &&
        Objects.equals(this.fullBackstory, originStoryDetailed.fullBackstory) &&
        Objects.equals(this.originQuest, originStoryDetailed.originQuest) &&
        Objects.equals(this.uniqueDialogueOptions, originStoryDetailed.uniqueDialogueOptions) &&
        Objects.equals(this.worldReactions, originStoryDetailed.worldReactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(originId, name, description, backstory, startingBonuses, compatibleClasses, fullBackstory, originQuest, uniqueDialogueOptions, worldReactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OriginStoryDetailed {\n");
    sb.append("    originId: ").append(toIndentedString(originId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    backstory: ").append(toIndentedString(backstory)).append("\n");
    sb.append("    startingBonuses: ").append(toIndentedString(startingBonuses)).append("\n");
    sb.append("    compatibleClasses: ").append(toIndentedString(compatibleClasses)).append("\n");
    sb.append("    fullBackstory: ").append(toIndentedString(fullBackstory)).append("\n");
    sb.append("    originQuest: ").append(toIndentedString(originQuest)).append("\n");
    sb.append("    uniqueDialogueOptions: ").append(toIndentedString(uniqueDialogueOptions)).append("\n");
    sb.append("    worldReactions: ").append(toIndentedString(worldReactions)).append("\n");
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

