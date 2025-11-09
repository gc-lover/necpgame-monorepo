package com.necpgame.backjava.model;

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
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * OriginStoryBonuses
 */

@JsonTypeName("OriginStory_bonuses")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class OriginStoryBonuses {

  @Valid
  private Map<String, Integer> attributes = new HashMap<>();

  @Valid
  private Map<String, Integer> skills = new HashMap<>();

  @Valid
  private List<String> startingItems = new ArrayList<>();

  public OriginStoryBonuses attributes(Map<String, Integer> attributes) {
    this.attributes = attributes;
    return this;
  }

  public OriginStoryBonuses putAttributesItem(String key, Integer attributesItem) {
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

  public OriginStoryBonuses skills(Map<String, Integer> skills) {
    this.skills = skills;
    return this;
  }

  public OriginStoryBonuses putSkillsItem(String key, Integer skillsItem) {
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

  public OriginStoryBonuses startingItems(List<String> startingItems) {
    this.startingItems = startingItems;
    return this;
  }

  public OriginStoryBonuses addStartingItemsItem(String startingItemsItem) {
    if (this.startingItems == null) {
      this.startingItems = new ArrayList<>();
    }
    this.startingItems.add(startingItemsItem);
    return this;
  }

  /**
   * Get startingItems
   * @return startingItems
   */
  
  @Schema(name = "starting_items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("starting_items")
  public List<String> getStartingItems() {
    return startingItems;
  }

  public void setStartingItems(List<String> startingItems) {
    this.startingItems = startingItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OriginStoryBonuses originStoryBonuses = (OriginStoryBonuses) o;
    return Objects.equals(this.attributes, originStoryBonuses.attributes) &&
        Objects.equals(this.skills, originStoryBonuses.skills) &&
        Objects.equals(this.startingItems, originStoryBonuses.startingItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributes, skills, startingItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OriginStoryBonuses {\n");
    sb.append("    attributes: ").append(toIndentedString(attributes)).append("\n");
    sb.append("    skills: ").append(toIndentedString(skills)).append("\n");
    sb.append("    startingItems: ").append(toIndentedString(startingItems)).append("\n");
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

