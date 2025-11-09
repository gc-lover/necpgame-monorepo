package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * SkillDetails
 */


public class SkillDetails {

  private @Nullable String skillId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String category;

  private @Nullable Integer maxRank;

  private @Nullable Object requirements;

  @Valid
  private List<Object> bonusesByRank = new ArrayList<>();

  public SkillDetails skillId(@Nullable String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  
  @Schema(name = "skill_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_id")
  public @Nullable String getSkillId() {
    return skillId;
  }

  public void setSkillId(@Nullable String skillId) {
    this.skillId = skillId;
  }

  public SkillDetails name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public SkillDetails description(@Nullable String description) {
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

  public SkillDetails category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public SkillDetails maxRank(@Nullable Integer maxRank) {
    this.maxRank = maxRank;
    return this;
  }

  /**
   * Get maxRank
   * @return maxRank
   */
  
  @Schema(name = "max_rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_rank")
  public @Nullable Integer getMaxRank() {
    return maxRank;
  }

  public void setMaxRank(@Nullable Integer maxRank) {
    this.maxRank = maxRank;
  }

  public SkillDetails requirements(@Nullable Object requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Требования для продвинутых навыков
   * @return requirements
   */
  
  @Schema(name = "requirements", description = "Требования для продвинутых навыков", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable Object getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable Object requirements) {
    this.requirements = requirements;
  }

  public SkillDetails bonusesByRank(List<Object> bonusesByRank) {
    this.bonusesByRank = bonusesByRank;
    return this;
  }

  public SkillDetails addBonusesByRankItem(Object bonusesByRankItem) {
    if (this.bonusesByRank == null) {
      this.bonusesByRank = new ArrayList<>();
    }
    this.bonusesByRank.add(bonusesByRankItem);
    return this;
  }

  /**
   * Бонусы на каждом ранге
   * @return bonusesByRank
   */
  
  @Schema(name = "bonuses_by_rank", description = "Бонусы на каждом ранге", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses_by_rank")
  public List<Object> getBonusesByRank() {
    return bonusesByRank;
  }

  public void setBonusesByRank(List<Object> bonusesByRank) {
    this.bonusesByRank = bonusesByRank;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillDetails skillDetails = (SkillDetails) o;
    return Objects.equals(this.skillId, skillDetails.skillId) &&
        Objects.equals(this.name, skillDetails.name) &&
        Objects.equals(this.description, skillDetails.description) &&
        Objects.equals(this.category, skillDetails.category) &&
        Objects.equals(this.maxRank, skillDetails.maxRank) &&
        Objects.equals(this.requirements, skillDetails.requirements) &&
        Objects.equals(this.bonusesByRank, skillDetails.bonusesByRank);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillId, name, description, category, maxRank, requirements, bonusesByRank);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillDetails {\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    maxRank: ").append(toIndentedString(maxRank)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    bonusesByRank: ").append(toIndentedString(bonusesByRank)).append("\n");
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

