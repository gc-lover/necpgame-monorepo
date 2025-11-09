package com.necpgame.backjava.model;

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
 * LevelUpResultUnlockedContent
 */

@JsonTypeName("LevelUpResult_unlocked_content")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LevelUpResultUnlockedContent {

  @Valid
  private List<String> quests = new ArrayList<>();

  @Valid
  private List<String> locations = new ArrayList<>();

  @Valid
  private List<String> abilities = new ArrayList<>();

  public LevelUpResultUnlockedContent quests(List<String> quests) {
    this.quests = quests;
    return this;
  }

  public LevelUpResultUnlockedContent addQuestsItem(String questsItem) {
    if (this.quests == null) {
      this.quests = new ArrayList<>();
    }
    this.quests.add(questsItem);
    return this;
  }

  /**
   * Get quests
   * @return quests
   */
  
  @Schema(name = "quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quests")
  public List<String> getQuests() {
    return quests;
  }

  public void setQuests(List<String> quests) {
    this.quests = quests;
  }

  public LevelUpResultUnlockedContent locations(List<String> locations) {
    this.locations = locations;
    return this;
  }

  public LevelUpResultUnlockedContent addLocationsItem(String locationsItem) {
    if (this.locations == null) {
      this.locations = new ArrayList<>();
    }
    this.locations.add(locationsItem);
    return this;
  }

  /**
   * Get locations
   * @return locations
   */
  
  @Schema(name = "locations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locations")
  public List<String> getLocations() {
    return locations;
  }

  public void setLocations(List<String> locations) {
    this.locations = locations;
  }

  public LevelUpResultUnlockedContent abilities(List<String> abilities) {
    this.abilities = abilities;
    return this;
  }

  public LevelUpResultUnlockedContent addAbilitiesItem(String abilitiesItem) {
    if (this.abilities == null) {
      this.abilities = new ArrayList<>();
    }
    this.abilities.add(abilitiesItem);
    return this;
  }

  /**
   * Get abilities
   * @return abilities
   */
  
  @Schema(name = "abilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("abilities")
  public List<String> getAbilities() {
    return abilities;
  }

  public void setAbilities(List<String> abilities) {
    this.abilities = abilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LevelUpResultUnlockedContent levelUpResultUnlockedContent = (LevelUpResultUnlockedContent) o;
    return Objects.equals(this.quests, levelUpResultUnlockedContent.quests) &&
        Objects.equals(this.locations, levelUpResultUnlockedContent.locations) &&
        Objects.equals(this.abilities, levelUpResultUnlockedContent.abilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quests, locations, abilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LevelUpResultUnlockedContent {\n");
    sb.append("    quests: ").append(toIndentedString(quests)).append("\n");
    sb.append("    locations: ").append(toIndentedString(locations)).append("\n");
    sb.append("    abilities: ").append(toIndentedString(abilities)).append("\n");
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

