package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.AchievementDefinitionRequirements;
import com.necpgame.gameplayservice.model.AchievementRewards;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AchievementDefinition
 */


public class AchievementDefinition {

  private @Nullable UUID id;

  private @Nullable String name;

  private @Nullable String description;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    COMBAT("COMBAT"),
    
    SOCIAL("SOCIAL"),
    
    ECONOMY("ECONOMY"),
    
    EXPLORATION("EXPLORATION"),
    
    STORY("STORY"),
    
    PROGRESSION("PROGRESSION"),
    
    WORLD("WORLD"),
    
    HIDDEN("HIDDEN");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CategoryEnum category;

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    COMMON("COMMON"),
    
    RARE("RARE"),
    
    EPIC("EPIC"),
    
    LEGENDARY("LEGENDARY"),
    
    SECRET("SECRET");

    private final String value;

    RarityEnum(String value) {
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
    public static RarityEnum fromValue(String value) {
      for (RarityEnum b : RarityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RarityEnum rarity;

  private @Nullable URI icon;

  private @Nullable AchievementDefinitionRequirements requirements;

  private @Nullable AchievementRewards rewards;

  private @Nullable Integer points;

  private @Nullable Boolean isHidden;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public AchievementDefinition id(@Nullable UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable UUID getId() {
    return id;
  }

  public void setId(@Nullable UUID id) {
    this.id = id;
  }

  public AchievementDefinition name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "First Blood", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public AchievementDefinition description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", example = "Kill your first enemy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public AchievementDefinition category(@Nullable CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(@Nullable CategoryEnum category) {
    this.category = category;
  }

  public AchievementDefinition rarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable RarityEnum getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
  }

  public AchievementDefinition icon(@Nullable URI icon) {
    this.icon = icon;
    return this;
  }

  /**
   * Get icon
   * @return icon
   */
  @Valid 
  @Schema(name = "icon", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("icon")
  public @Nullable URI getIcon() {
    return icon;
  }

  public void setIcon(@Nullable URI icon) {
    this.icon = icon;
  }

  public AchievementDefinition requirements(@Nullable AchievementDefinitionRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable AchievementDefinitionRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable AchievementDefinitionRequirements requirements) {
    this.requirements = requirements;
  }

  public AchievementDefinition rewards(@Nullable AchievementRewards rewards) {
    this.rewards = rewards;
    return this;
  }

  /**
   * Get rewards
   * @return rewards
   */
  @Valid 
  @Schema(name = "rewards", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewards")
  public @Nullable AchievementRewards getRewards() {
    return rewards;
  }

  public void setRewards(@Nullable AchievementRewards rewards) {
    this.rewards = rewards;
  }

  public AchievementDefinition points(@Nullable Integer points) {
    this.points = points;
    return this;
  }

  /**
   * Achievement points
   * @return points
   */
  
  @Schema(name = "points", example = "10", description = "Achievement points", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("points")
  public @Nullable Integer getPoints() {
    return points;
  }

  public void setPoints(@Nullable Integer points) {
    this.points = points;
  }

  public AchievementDefinition isHidden(@Nullable Boolean isHidden) {
    this.isHidden = isHidden;
    return this;
  }

  /**
   * Скрыто до разблокировки
   * @return isHidden
   */
  
  @Schema(name = "is_hidden", description = "Скрыто до разблокировки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("is_hidden")
  public @Nullable Boolean getIsHidden() {
    return isHidden;
  }

  public void setIsHidden(@Nullable Boolean isHidden) {
    this.isHidden = isHidden;
  }

  public AchievementDefinition createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AchievementDefinition achievementDefinition = (AchievementDefinition) o;
    return Objects.equals(this.id, achievementDefinition.id) &&
        Objects.equals(this.name, achievementDefinition.name) &&
        Objects.equals(this.description, achievementDefinition.description) &&
        Objects.equals(this.category, achievementDefinition.category) &&
        Objects.equals(this.rarity, achievementDefinition.rarity) &&
        Objects.equals(this.icon, achievementDefinition.icon) &&
        Objects.equals(this.requirements, achievementDefinition.requirements) &&
        Objects.equals(this.rewards, achievementDefinition.rewards) &&
        Objects.equals(this.points, achievementDefinition.points) &&
        Objects.equals(this.isHidden, achievementDefinition.isHidden) &&
        Objects.equals(this.createdAt, achievementDefinition.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, category, rarity, icon, requirements, rewards, points, isHidden, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AchievementDefinition {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    icon: ").append(toIndentedString(icon)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    rewards: ").append(toIndentedString(rewards)).append("\n");
    sb.append("    points: ").append(toIndentedString(points)).append("\n");
    sb.append("    isHidden: ").append(toIndentedString(isHidden)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

