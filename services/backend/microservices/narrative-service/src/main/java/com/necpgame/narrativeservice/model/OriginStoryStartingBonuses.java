package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OriginStoryStartingBonuses
 */

@JsonTypeName("OriginStory_starting_bonuses")

public class OriginStoryStartingBonuses {

  @Valid
  private Map<String, Integer> attributes = new HashMap<>();

  @Valid
  private Map<String, Integer> skills = new HashMap<>();

  @Valid
  private List<UUID> items = new ArrayList<>();

  @Valid
  private Map<String, Integer> reputation = new HashMap<>();

  public OriginStoryStartingBonuses attributes(Map<String, Integer> attributes) {
    this.attributes = attributes;
    return this;
  }

  public OriginStoryStartingBonuses putAttributesItem(String key, Integer attributesItem) {
    if (this.attributes == null) {
      this.attributes = new HashMap<>();
    }
    this.attributes.put(key, attributesItem);
    return this;
  }

  /**
   * Get attributes
   * @return attributes
   */
  
  @Schema(name = "attributes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attributes")
  public Map<String, Integer> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, Integer> attributes) {
    this.attributes = attributes;
  }

  public OriginStoryStartingBonuses skills(Map<String, Integer> skills) {
    this.skills = skills;
    return this;
  }

  public OriginStoryStartingBonuses putSkillsItem(String key, Integer skillsItem) {
    if (this.skills == null) {
      this.skills = new HashMap<>();
    }
    this.skills.put(key, skillsItem);
    return this;
  }

  /**
   * Get skills
   * @return skills
   */
  
  @Schema(name = "skills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skills")
  public Map<String, Integer> getSkills() {
    return skills;
  }

  public void setSkills(Map<String, Integer> skills) {
    this.skills = skills;
  }

  public OriginStoryStartingBonuses items(List<UUID> items) {
    this.items = items;
    return this;
  }

  public OriginStoryStartingBonuses addItemsItem(UUID itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items")
  public List<UUID> getItems() {
    return items;
  }

  public void setItems(List<UUID> items) {
    this.items = items;
  }

  public OriginStoryStartingBonuses reputation(Map<String, Integer> reputation) {
    this.reputation = reputation;
    return this;
  }

  public OriginStoryStartingBonuses putReputationItem(String key, Integer reputationItem) {
    if (this.reputation == null) {
      this.reputation = new HashMap<>();
    }
    this.reputation.put(key, reputationItem);
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public Map<String, Integer> getReputation() {
    return reputation;
  }

  public void setReputation(Map<String, Integer> reputation) {
    this.reputation = reputation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OriginStoryStartingBonuses originStoryStartingBonuses = (OriginStoryStartingBonuses) o;
    return Objects.equals(this.attributes, originStoryStartingBonuses.attributes) &&
        Objects.equals(this.skills, originStoryStartingBonuses.skills) &&
        Objects.equals(this.items, originStoryStartingBonuses.items) &&
        Objects.equals(this.reputation, originStoryStartingBonuses.reputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributes, skills, items, reputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OriginStoryStartingBonuses {\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    skills: ").append(toIndentedString(skills)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
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

