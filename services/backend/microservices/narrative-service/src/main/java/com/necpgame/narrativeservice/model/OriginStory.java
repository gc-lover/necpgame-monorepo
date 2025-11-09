package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.narrativeservice.model.OriginStoryStartingBonuses;
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
 * OriginStory
 */


public class OriginStory {

  private @Nullable String originId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String backstory;

  private @Nullable OriginStoryStartingBonuses startingBonuses;

  @Valid
  private List<String> compatibleClasses = new ArrayList<>();

  public OriginStory originId(@Nullable String originId) {
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

  public OriginStory name(@Nullable String name) {
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

  public OriginStory description(@Nullable String description) {
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

  public OriginStory backstory(@Nullable String backstory) {
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

  public OriginStory startingBonuses(@Nullable OriginStoryStartingBonuses startingBonuses) {
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

  public OriginStory compatibleClasses(List<String> compatibleClasses) {
    this.compatibleClasses = compatibleClasses;
    return this;
  }

  public OriginStory addCompatibleClassesItem(String compatibleClassesItem) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OriginStory originStory = (OriginStory) o;
    return Objects.equals(this.originId, originStory.originId) &&
        Objects.equals(this.name, originStory.name) &&
        Objects.equals(this.description, originStory.description) &&
        Objects.equals(this.backstory, originStory.backstory) &&
        Objects.equals(this.startingBonuses, originStory.startingBonuses) &&
        Objects.equals(this.compatibleClasses, originStory.compatibleClasses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(originId, name, description, backstory, startingBonuses, compatibleClasses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OriginStory {\n");
    sb.append("    originId: ").append(toIndentedString(originId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    backstory: ").append(toIndentedString(backstory)).append("\n");
    sb.append("    startingBonuses: ").append(toIndentedString(startingBonuses)).append("\n");
    sb.append("    compatibleClasses: ").append(toIndentedString(compatibleClasses)).append("\n");
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

