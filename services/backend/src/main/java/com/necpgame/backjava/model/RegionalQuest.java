package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RegionalQuest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RegionalQuest {

  private @Nullable String questId;

  private @Nullable String title;

  /**
   * Gets or Sets region
   */
  public enum RegionEnum {
    AFRICA("AFRICA"),
    
    AMERICA("AMERICA"),
    
    ASIA("ASIA"),
    
    CIS("CIS"),
    
    EUROPE("EUROPE"),
    
    MIDDLE_EAST("MIDDLE_EAST"),
    
    OCEANIA("OCEANIA"),
    
    NIGHT_CITY("NIGHT_CITY");

    private final String value;

    RegionEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RegionEnum fromValue(String value) {
      for (RegionEnum b : RegionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RegionEnum region;

  private @Nullable String location;

  private @Nullable String description;

  private @Nullable Integer levelRequirement;

  private @Nullable String culturalContext;

  private @Nullable Boolean localFlavor;

  private @Nullable Object rewards;

  private @Nullable Boolean repeatable;

  public RegionalQuest questId(@Nullable String questId) {
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

  public RegionalQuest title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public RegionalQuest region(@Nullable RegionEnum region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable RegionEnum getRegion() {
    return region;
  }

  public void setRegion(@Nullable RegionEnum region) {
    this.region = region;
  }

  public RegionalQuest location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Конкретная локация в регионе
   * @return location
   */
  
  @Schema(name = "location", description = "Конкретная локация в регионе", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public RegionalQuest description(@Nullable String description) {
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

  public RegionalQuest levelRequirement(@Nullable Integer levelRequirement) {
    this.levelRequirement = levelRequirement;
    return this;
  }

  /**
   * Get levelRequirement
   * @return levelRequirement
   */
  
  @Schema(name = "level_requirement", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_requirement")
  public @Nullable Integer getLevelRequirement() {
    return levelRequirement;
  }

  public void setLevelRequirement(@Nullable Integer levelRequirement) {
    this.levelRequirement = levelRequirement;
  }

  public RegionalQuest culturalContext(@Nullable String culturalContext) {
    this.culturalContext = culturalContext;
    return this;
  }

  /**
   * Культурный контекст квеста
   * @return culturalContext
   */
  
  @Schema(name = "cultural_context", description = "Культурный контекст квеста", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cultural_context")
  public @Nullable String getCulturalContext() {
    return culturalContext;
  }

  public void setCulturalContext(@Nullable String culturalContext) {
    this.culturalContext = culturalContext;
  }

  public RegionalQuest localFlavor(@Nullable Boolean localFlavor) {
    this.localFlavor = localFlavor;
    return this;
  }

  /**
   * Содержит локальные особенности
   * @return localFlavor
   */
  
  @Schema(name = "local_flavor", description = "Содержит локальные особенности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("local_flavor")
  public @Nullable Boolean getLocalFlavor() {
    return localFlavor;
  }

  public void setLocalFlavor(@Nullable Boolean localFlavor) {
    this.localFlavor = localFlavor;
  }

  public RegionalQuest rewards(@Nullable Object rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable Object getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable Object rewards) {
    this.rewards = rewards;
  }

  public RegionalQuest repeatable(@Nullable Boolean repeatable) {
    this.repeatable = repeatable;
    return this;
  }

  /**
   * Get repeatable
   * @return repeatable
   */
  
  @Schema(name = "repeatable", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("repeatable")
  public @Nullable Boolean getRepeatable() {
    return repeatable;
  }

  public void setRepeatable(@Nullable Boolean repeatable) {
    this.repeatable = repeatable;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegionalQuest regionalQuest = (RegionalQuest) o;
    return Objects.equals(this.questId, regionalQuest.questId) &&
        Objects.equals(this.title, regionalQuest.title) &&
        Objects.equals(this.region, regionalQuest.region) &&
        Objects.equals(this.location, regionalQuest.location) &&
        Objects.equals(this.description, regionalQuest.description) &&
        Objects.equals(this.levelRequirement, regionalQuest.levelRequirement) &&
        Objects.equals(this.culturalContext, regionalQuest.culturalContext) &&
        Objects.equals(this.localFlavor, regionalQuest.localFlavor) &&
        Objects.equals(this.rewards, regionalQuest.rewards) &&
        Objects.equals(this.repeatable, regionalQuest.repeatable);
  }

  @Override
  public int hashCode() {
    return Objects.hash(questId, title, region, location, description, levelRequirement, culturalContext, localFlavor, rewards, repeatable);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegionalQuest {\n");
    sb.append("    questId: ").append(toIndentedString(questId)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    levelRequirement: ").append(toIndentedString(levelRequirement)).append("\n");
    sb.append("    culturalContext: ").append(toIndentedString(culturalContext)).append("\n");
    sb.append("    localFlavor: ").append(toIndentedString(localFlavor)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    repeatable: ").append(toIndentedString(repeatable)).append("\n");
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

