package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.FactionDetailedAllOfLeadership;
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
 * FactionDetailed
 */


public class FactionDetailed {

  private @Nullable String factionId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CORPORATION("CORPORATION"),
    
    GANG("GANG"),
    
    ORGANIZATION("ORGANIZATION"),
    
    GOVERNMENT("GOVERNMENT");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String region;

  private @Nullable Integer powerLevel;

  private @Nullable String descriptionShort;

  private @Nullable String fullDescription;

  private @Nullable String history;

  @Valid
  private List<String> goals = new ArrayList<>();

  @Valid
  private List<@Valid FactionDetailedAllOfLeadership> leadership = new ArrayList<>();

  @Valid
  private List<String> territories = new ArrayList<>();

  @Valid
  private List<String> allies = new ArrayList<>();

  @Valid
  private List<String> enemies = new ArrayList<>();

  @Valid
  private List<String> resources = new ArrayList<>();

  public FactionDetailed factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public FactionDetailed name(@Nullable String name) {
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

  public FactionDetailed type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public FactionDetailed region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public FactionDetailed powerLevel(@Nullable Integer powerLevel) {
    this.powerLevel = powerLevel;
    return this;
  }

  /**
   * Get powerLevel
   * minimum: 1
   * maximum: 10
   * @return powerLevel
   */
  @Min(value = 1) @Max(value = 10) 
  @Schema(name = "power_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("power_level")
  public @Nullable Integer getPowerLevel() {
    return powerLevel;
  }

  public void setPowerLevel(@Nullable Integer powerLevel) {
    this.powerLevel = powerLevel;
  }

  public FactionDetailed descriptionShort(@Nullable String descriptionShort) {
    this.descriptionShort = descriptionShort;
    return this;
  }

  /**
   * Get descriptionShort
   * @return descriptionShort
   */
  
  @Schema(name = "description_short", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description_short")
  public @Nullable String getDescriptionShort() {
    return descriptionShort;
  }

  public void setDescriptionShort(@Nullable String descriptionShort) {
    this.descriptionShort = descriptionShort;
  }

  public FactionDetailed fullDescription(@Nullable String fullDescription) {
    this.fullDescription = fullDescription;
    return this;
  }

  /**
   * Get fullDescription
   * @return fullDescription
   */
  
  @Schema(name = "full_description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("full_description")
  public @Nullable String getFullDescription() {
    return fullDescription;
  }

  public void setFullDescription(@Nullable String fullDescription) {
    this.fullDescription = fullDescription;
  }

  public FactionDetailed history(@Nullable String history) {
    this.history = history;
    return this;
  }

  /**
   * Get history
   * @return history
   */
  
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public @Nullable String getHistory() {
    return history;
  }

  public void setHistory(@Nullable String history) {
    this.history = history;
  }

  public FactionDetailed goals(List<String> goals) {
    this.goals = goals;
    return this;
  }

  public FactionDetailed addGoalsItem(String goalsItem) {
    if (this.goals == null) {
      this.goals = new ArrayList<>();
    }
    this.goals.add(goalsItem);
    return this;
  }

  /**
   * Get goals
   * @return goals
   */
  
  @Schema(name = "goals", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("goals")
  public List<String> getGoals() {
    return goals;
  }

  public void setGoals(List<String> goals) {
    this.goals = goals;
  }

  public FactionDetailed leadership(List<@Valid FactionDetailedAllOfLeadership> leadership) {
    this.leadership = leadership;
    return this;
  }

  public FactionDetailed addLeadershipItem(FactionDetailedAllOfLeadership leadershipItem) {
    if (this.leadership == null) {
      this.leadership = new ArrayList<>();
    }
    this.leadership.add(leadershipItem);
    return this;
  }

  /**
   * Get leadership
   * @return leadership
   */
  @Valid 
  @Schema(name = "leadership", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leadership")
  public List<@Valid FactionDetailedAllOfLeadership> getLeadership() {
    return leadership;
  }

  public void setLeadership(List<@Valid FactionDetailedAllOfLeadership> leadership) {
    this.leadership = leadership;
  }

  public FactionDetailed territories(List<String> territories) {
    this.territories = territories;
    return this;
  }

  public FactionDetailed addTerritoriesItem(String territoriesItem) {
    if (this.territories == null) {
      this.territories = new ArrayList<>();
    }
    this.territories.add(territoriesItem);
    return this;
  }

  /**
   * Get territories
   * @return territories
   */
  
  @Schema(name = "territories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territories")
  public List<String> getTerritories() {
    return territories;
  }

  public void setTerritories(List<String> territories) {
    this.territories = territories;
  }

  public FactionDetailed allies(List<String> allies) {
    this.allies = allies;
    return this;
  }

  public FactionDetailed addAlliesItem(String alliesItem) {
    if (this.allies == null) {
      this.allies = new ArrayList<>();
    }
    this.allies.add(alliesItem);
    return this;
  }

  /**
   * Get allies
   * @return allies
   */
  
  @Schema(name = "allies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allies")
  public List<String> getAllies() {
    return allies;
  }

  public void setAllies(List<String> allies) {
    this.allies = allies;
  }

  public FactionDetailed enemies(List<String> enemies) {
    this.enemies = enemies;
    return this;
  }

  public FactionDetailed addEnemiesItem(String enemiesItem) {
    if (this.enemies == null) {
      this.enemies = new ArrayList<>();
    }
    this.enemies.add(enemiesItem);
    return this;
  }

  /**
   * Get enemies
   * @return enemies
   */
  
  @Schema(name = "enemies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enemies")
  public List<String> getEnemies() {
    return enemies;
  }

  public void setEnemies(List<String> enemies) {
    this.enemies = enemies;
  }

  public FactionDetailed resources(List<String> resources) {
    this.resources = resources;
    return this;
  }

  public FactionDetailed addResourcesItem(String resourcesItem) {
    if (this.resources == null) {
      this.resources = new ArrayList<>();
    }
    this.resources.add(resourcesItem);
    return this;
  }

  /**
   * Get resources
   * @return resources
   */
  
  @Schema(name = "resources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resources")
  public List<String> getResources() {
    return resources;
  }

  public void setResources(List<String> resources) {
    this.resources = resources;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FactionDetailed factionDetailed = (FactionDetailed) o;
    return Objects.equals(this.factionId, factionDetailed.factionId) &&
        Objects.equals(this.name, factionDetailed.name) &&
        Objects.equals(this.type, factionDetailed.type) &&
        Objects.equals(this.region, factionDetailed.region) &&
        Objects.equals(this.powerLevel, factionDetailed.powerLevel) &&
        Objects.equals(this.descriptionShort, factionDetailed.descriptionShort) &&
        Objects.equals(this.fullDescription, factionDetailed.fullDescription) &&
        Objects.equals(this.history, factionDetailed.history) &&
        Objects.equals(this.goals, factionDetailed.goals) &&
        Objects.equals(this.leadership, factionDetailed.leadership) &&
        Objects.equals(this.territories, factionDetailed.territories) &&
        Objects.equals(this.allies, factionDetailed.allies) &&
        Objects.equals(this.enemies, factionDetailed.enemies) &&
        Objects.equals(this.resources, factionDetailed.resources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, name, type, region, powerLevel, descriptionShort, fullDescription, history, goals, leadership, territories, allies, enemies, resources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FactionDetailed {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    powerLevel: ").append(toIndentedString(powerLevel)).append("\n");
    sb.append("    descriptionShort: ").append(toIndentedString(descriptionShort)).append("\n");
    sb.append("    fullDescription: ").append(toIndentedString(fullDescription)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
    sb.append("    goals: ").append(toIndentedString(goals)).append("\n");
    sb.append("    leadership: ").append(toIndentedString(leadership)).append("\n");
    sb.append("    territories: ").append(toIndentedString(territories)).append("\n");
    sb.append("    allies: ").append(toIndentedString(allies)).append("\n");
    sb.append("    enemies: ").append(toIndentedString(enemies)).append("\n");
    sb.append("    resources: ").append(toIndentedString(resources)).append("\n");
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

