package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.GameClassDetailsStartingBonuses;
import com.necpgame.gameplayservice.model.Subclass;
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
 * GameClassDetails
 */


public class GameClassDetails {

  private @Nullable String classId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String source;

  private @Nullable String role;

  private @Nullable String lore;

  private @Nullable GameClassDetailsStartingBonuses startingBonuses;

  @Valid
  private List<String> skillTreeAccess = new ArrayList<>();

  @Valid
  private List<String> uniqueAbilities = new ArrayList<>();

  @Valid
  private List<@Valid Subclass> subclasses = new ArrayList<>();

  public GameClassDetails classId(@Nullable String classId) {
    this.classId = classId;
    return this;
  }

  /**
   * Get classId
   * @return classId
   */
  
  @Schema(name = "class_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_id")
  public @Nullable String getClassId() {
    return classId;
  }

  public void setClassId(@Nullable String classId) {
    this.classId = classId;
  }

  public GameClassDetails name(@Nullable String name) {
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

  public GameClassDetails description(@Nullable String description) {
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

  public GameClassDetails source(@Nullable String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable String getSource() {
    return source;
  }

  public void setSource(@Nullable String source) {
    this.source = source;
  }

  public GameClassDetails role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public GameClassDetails lore(@Nullable String lore) {
    this.lore = lore;
    return this;
  }

  /**
   * Лор класса из Cyberpunk
   * @return lore
   */
  
  @Schema(name = "lore", description = "Лор класса из Cyberpunk", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lore")
  public @Nullable String getLore() {
    return lore;
  }

  public void setLore(@Nullable String lore) {
    this.lore = lore;
  }

  public GameClassDetails startingBonuses(@Nullable GameClassDetailsStartingBonuses startingBonuses) {
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
  public @Nullable GameClassDetailsStartingBonuses getStartingBonuses() {
    return startingBonuses;
  }

  public void setStartingBonuses(@Nullable GameClassDetailsStartingBonuses startingBonuses) {
    this.startingBonuses = startingBonuses;
  }

  public GameClassDetails skillTreeAccess(List<String> skillTreeAccess) {
    this.skillTreeAccess = skillTreeAccess;
    return this;
  }

  public GameClassDetails addSkillTreeAccessItem(String skillTreeAccessItem) {
    if (this.skillTreeAccess == null) {
      this.skillTreeAccess = new ArrayList<>();
    }
    this.skillTreeAccess.add(skillTreeAccessItem);
    return this;
  }

  /**
   * Доступные деревья навыков
   * @return skillTreeAccess
   */
  
  @Schema(name = "skill_tree_access", description = "Доступные деревья навыков", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_tree_access")
  public List<String> getSkillTreeAccess() {
    return skillTreeAccess;
  }

  public void setSkillTreeAccess(List<String> skillTreeAccess) {
    this.skillTreeAccess = skillTreeAccess;
  }

  public GameClassDetails uniqueAbilities(List<String> uniqueAbilities) {
    this.uniqueAbilities = uniqueAbilities;
    return this;
  }

  public GameClassDetails addUniqueAbilitiesItem(String uniqueAbilitiesItem) {
    if (this.uniqueAbilities == null) {
      this.uniqueAbilities = new ArrayList<>();
    }
    this.uniqueAbilities.add(uniqueAbilitiesItem);
    return this;
  }

  /**
   * Уникальные классовые способности
   * @return uniqueAbilities
   */
  
  @Schema(name = "unique_abilities", description = "Уникальные классовые способности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unique_abilities")
  public List<String> getUniqueAbilities() {
    return uniqueAbilities;
  }

  public void setUniqueAbilities(List<String> uniqueAbilities) {
    this.uniqueAbilities = uniqueAbilities;
  }

  public GameClassDetails subclasses(List<@Valid Subclass> subclasses) {
    this.subclasses = subclasses;
    return this;
  }

  public GameClassDetails addSubclassesItem(Subclass subclassesItem) {
    if (this.subclasses == null) {
      this.subclasses = new ArrayList<>();
    }
    this.subclasses.add(subclassesItem);
    return this;
  }

  /**
   * Get subclasses
   * @return subclasses
   */
  @Valid 
  @Schema(name = "subclasses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subclasses")
  public List<@Valid Subclass> getSubclasses() {
    return subclasses;
  }

  public void setSubclasses(List<@Valid Subclass> subclasses) {
    this.subclasses = subclasses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameClassDetails gameClassDetails = (GameClassDetails) o;
    return Objects.equals(this.classId, gameClassDetails.classId) &&
        Objects.equals(this.name, gameClassDetails.name) &&
        Objects.equals(this.description, gameClassDetails.description) &&
        Objects.equals(this.source, gameClassDetails.source) &&
        Objects.equals(this.role, gameClassDetails.role) &&
        Objects.equals(this.lore, gameClassDetails.lore) &&
        Objects.equals(this.startingBonuses, gameClassDetails.startingBonuses) &&
        Objects.equals(this.skillTreeAccess, gameClassDetails.skillTreeAccess) &&
        Objects.equals(this.uniqueAbilities, gameClassDetails.uniqueAbilities) &&
        Objects.equals(this.subclasses, gameClassDetails.subclasses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, name, description, source, role, lore, startingBonuses, skillTreeAccess, uniqueAbilities, subclasses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameClassDetails {\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    lore: ").append(toIndentedString(lore)).append("\n");
    sb.append("    startingBonuses: ").append(toIndentedString(startingBonuses)).append("\n");
    sb.append("    skillTreeAccess: ").append(toIndentedString(skillTreeAccess)).append("\n");
    sb.append("    uniqueAbilities: ").append(toIndentedString(uniqueAbilities)).append("\n");
    sb.append("    subclasses: ").append(toIndentedString(subclasses)).append("\n");
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

