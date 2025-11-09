package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * Требования для выполнения действия (если enabled&#x3D;false)
 */

@Schema(name = "LocationAction_requirements", description = "Требования для выполнения действия (если enabled=false)")
@JsonTypeName("LocationAction_requirements")

public class LocationActionRequirements {

  private @Nullable Integer minLevel;

  @Valid
  private List<String> requiredSkills = new ArrayList<>();

  @Valid
  private List<String> requiredItems = new ArrayList<>();

  @Valid
  private List<String> requiredQuests = new ArrayList<>();

  public LocationActionRequirements minLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Минимальный уровень персонажа
   * @return minLevel
   */
  
  @Schema(name = "minLevel", description = "Минимальный уровень персонажа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minLevel")
  public @Nullable Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
  }

  public LocationActionRequirements requiredSkills(List<String> requiredSkills) {
    this.requiredSkills = requiredSkills;
    return this;
  }

  public LocationActionRequirements addRequiredSkillsItem(String requiredSkillsItem) {
    if (this.requiredSkills == null) {
      this.requiredSkills = new ArrayList<>();
    }
    this.requiredSkills.add(requiredSkillsItem);
    return this;
  }

  /**
   * Требуемые навыки
   * @return requiredSkills
   */
  
  @Schema(name = "requiredSkills", description = "Требуемые навыки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredSkills")
  public List<String> getRequiredSkills() {
    return requiredSkills;
  }

  public void setRequiredSkills(List<String> requiredSkills) {
    this.requiredSkills = requiredSkills;
  }

  public LocationActionRequirements requiredItems(List<String> requiredItems) {
    this.requiredItems = requiredItems;
    return this;
  }

  public LocationActionRequirements addRequiredItemsItem(String requiredItemsItem) {
    if (this.requiredItems == null) {
      this.requiredItems = new ArrayList<>();
    }
    this.requiredItems.add(requiredItemsItem);
    return this;
  }

  /**
   * Требуемые предметы
   * @return requiredItems
   */
  
  @Schema(name = "requiredItems", description = "Требуемые предметы", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredItems")
  public List<String> getRequiredItems() {
    return requiredItems;
  }

  public void setRequiredItems(List<String> requiredItems) {
    this.requiredItems = requiredItems;
  }

  public LocationActionRequirements requiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
    return this;
  }

  public LocationActionRequirements addRequiredQuestsItem(String requiredQuestsItem) {
    if (this.requiredQuests == null) {
      this.requiredQuests = new ArrayList<>();
    }
    this.requiredQuests.add(requiredQuestsItem);
    return this;
  }

  /**
   * Требуемые завершенные квесты
   * @return requiredQuests
   */
  
  @Schema(name = "requiredQuests", description = "Требуемые завершенные квесты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredQuests")
  public List<String> getRequiredQuests() {
    return requiredQuests;
  }

  public void setRequiredQuests(List<String> requiredQuests) {
    this.requiredQuests = requiredQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocationActionRequirements locationActionRequirements = (LocationActionRequirements) o;
    return Objects.equals(this.minLevel, locationActionRequirements.minLevel) &&
        Objects.equals(this.requiredSkills, locationActionRequirements.requiredSkills) &&
        Objects.equals(this.requiredItems, locationActionRequirements.requiredItems) &&
        Objects.equals(this.requiredQuests, locationActionRequirements.requiredQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minLevel, requiredSkills, requiredItems, requiredQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LocationActionRequirements {\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    requiredSkills: ").append(toIndentedString(requiredSkills)).append("\n");
    sb.append("    requiredItems: ").append(toIndentedString(requiredItems)).append("\n");
    sb.append("    requiredQuests: ").append(toIndentedString(requiredQuests)).append("\n");
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

