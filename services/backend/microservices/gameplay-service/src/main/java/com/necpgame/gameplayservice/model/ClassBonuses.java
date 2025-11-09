package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * ClassBonuses
 */


public class ClassBonuses {

  private @Nullable String classId;

  private @Nullable String className;

  @Valid
  private Map<String, Integer> attributeBonuses = new HashMap<>();

  @Valid
  private Map<String, BigDecimal> skillBonuses = new HashMap<>();

  @Valid
  private List<String> specialAbilities = new ArrayList<>();

  @Valid
  private List<String> startingPerks = new ArrayList<>();

  public ClassBonuses classId(@Nullable String classId) {
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

  public ClassBonuses className(@Nullable String className) {
    this.className = className;
    return this;
  }

  /**
   * Get className
   * @return className
   */
  
  @Schema(name = "class_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_name")
  public @Nullable String getClassName() {
    return className;
  }

  public void setClassName(@Nullable String className) {
    this.className = className;
  }

  public ClassBonuses attributeBonuses(Map<String, Integer> attributeBonuses) {
    this.attributeBonuses = attributeBonuses;
    return this;
  }

  public ClassBonuses putAttributeBonusesItem(String key, Integer attributeBonusesItem) {
    if (this.attributeBonuses == null) {
      this.attributeBonuses = new HashMap<>();
    }
    this.attributeBonuses.put(key, attributeBonusesItem);
    return this;
  }

  /**
   * Бонусы к атрибутам
   * @return attributeBonuses
   */
  
  @Schema(name = "attribute_bonuses", description = "Бонусы к атрибутам", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_bonuses")
  public Map<String, Integer> getAttributeBonuses() {
    return attributeBonuses;
  }

  public void setAttributeBonuses(Map<String, Integer> attributeBonuses) {
    this.attributeBonuses = attributeBonuses;
  }

  public ClassBonuses skillBonuses(Map<String, BigDecimal> skillBonuses) {
    this.skillBonuses = skillBonuses;
    return this;
  }

  public ClassBonuses putSkillBonusesItem(String key, BigDecimal skillBonusesItem) {
    if (this.skillBonuses == null) {
      this.skillBonuses = new HashMap<>();
    }
    this.skillBonuses.put(key, skillBonusesItem);
    return this;
  }

  /**
   * Бонусы к навыкам
   * @return skillBonuses
   */
  @Valid 
  @Schema(name = "skill_bonuses", description = "Бонусы к навыкам", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_bonuses")
  public Map<String, BigDecimal> getSkillBonuses() {
    return skillBonuses;
  }

  public void setSkillBonuses(Map<String, BigDecimal> skillBonuses) {
    this.skillBonuses = skillBonuses;
  }

  public ClassBonuses specialAbilities(List<String> specialAbilities) {
    this.specialAbilities = specialAbilities;
    return this;
  }

  public ClassBonuses addSpecialAbilitiesItem(String specialAbilitiesItem) {
    if (this.specialAbilities == null) {
      this.specialAbilities = new ArrayList<>();
    }
    this.specialAbilities.add(specialAbilitiesItem);
    return this;
  }

  /**
   * Get specialAbilities
   * @return specialAbilities
   */
  
  @Schema(name = "special_abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("special_abilities")
  public List<String> getSpecialAbilities() {
    return specialAbilities;
  }

  public void setSpecialAbilities(List<String> specialAbilities) {
    this.specialAbilities = specialAbilities;
  }

  public ClassBonuses startingPerks(List<String> startingPerks) {
    this.startingPerks = startingPerks;
    return this;
  }

  public ClassBonuses addStartingPerksItem(String startingPerksItem) {
    if (this.startingPerks == null) {
      this.startingPerks = new ArrayList<>();
    }
    this.startingPerks.add(startingPerksItem);
    return this;
  }

  /**
   * Get startingPerks
   * @return startingPerks
   */
  
  @Schema(name = "starting_perks", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starting_perks")
  public List<String> getStartingPerks() {
    return startingPerks;
  }

  public void setStartingPerks(List<String> startingPerks) {
    this.startingPerks = startingPerks;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ClassBonuses classBonuses = (ClassBonuses) o;
    return Objects.equals(this.classId, classBonuses.classId) &&
        Objects.equals(this.className, classBonuses.className) &&
        Objects.equals(this.attributeBonuses, classBonuses.attributeBonuses) &&
        Objects.equals(this.skillBonuses, classBonuses.skillBonuses) &&
        Objects.equals(this.specialAbilities, classBonuses.specialAbilities) &&
        Objects.equals(this.startingPerks, classBonuses.startingPerks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classId, className, attributeBonuses, skillBonuses, specialAbilities, startingPerks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ClassBonuses {\n");
    sb.append("    classId: ").append(toIndentedString(classId)).append("\n");
    sb.append("    className: ").append(toIndentedString(className)).append("\n");
    sb.append("    attributeBonuses: ").append(toIndentedString(attributeBonuses)).append("\n");
    sb.append("    skillBonuses: ").append(toIndentedString(skillBonuses)).append("\n");
    sb.append("    specialAbilities: ").append(toIndentedString(specialAbilities)).append("\n");
    sb.append("    startingPerks: ").append(toIndentedString(startingPerks)).append("\n");
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

