package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.TerritoryVulnerabilityWindow;
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
 * Territory
 */


public class Territory {

  private String territoryId;

  private String name;

  private String region;

  @Valid
  private List<String> bonuses = new ArrayList<>();

  private String currentOwner;

  /**
   * Gets or Sets contestStatus
   */
  public enum ContestStatusEnum {
    STABLE("stable"),
    
    CONTESTED("contested"),
    
    VULNERABLE("vulnerable");

    private final String value;

    ContestStatusEnum(String value) {
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
    public static ContestStatusEnum fromValue(String value) {
      for (ContestStatusEnum b : ContestStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ContestStatusEnum contestStatus;

  private @Nullable TerritoryVulnerabilityWindow vulnerabilityWindow;

  private @Nullable Integer fortifyLevel;

  public Territory() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Territory(String territoryId, String name, String region, String currentOwner) {
    this.territoryId = territoryId;
    this.name = name;
    this.region = region;
    this.currentOwner = currentOwner;
  }

  public Territory territoryId(String territoryId) {
    this.territoryId = territoryId;
    return this;
  }

  /**
   * Get territoryId
   * @return territoryId
   */
  @NotNull 
  @Schema(name = "territoryId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("territoryId")
  public String getTerritoryId() {
    return territoryId;
  }

  public void setTerritoryId(String territoryId) {
    this.territoryId = territoryId;
  }

  public Territory name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Territory region(String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  @NotNull 
  @Schema(name = "region", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("region")
  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public Territory bonuses(List<String> bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  public Territory addBonusesItem(String bonusesItem) {
    if (this.bonuses == null) {
      this.bonuses = new ArrayList<>();
    }
    this.bonuses.add(bonusesItem);
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public List<String> getBonuses() {
    return bonuses;
  }

  public void setBonuses(List<String> bonuses) {
    this.bonuses = bonuses;
  }

  public Territory currentOwner(String currentOwner) {
    this.currentOwner = currentOwner;
    return this;
  }

  /**
   * Get currentOwner
   * @return currentOwner
   */
  @NotNull 
  @Schema(name = "currentOwner", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentOwner")
  public String getCurrentOwner() {
    return currentOwner;
  }

  public void setCurrentOwner(String currentOwner) {
    this.currentOwner = currentOwner;
  }

  public Territory contestStatus(@Nullable ContestStatusEnum contestStatus) {
    this.contestStatus = contestStatus;
    return this;
  }

  /**
   * Get contestStatus
   * @return contestStatus
   */
  
  @Schema(name = "contestStatus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contestStatus")
  public @Nullable ContestStatusEnum getContestStatus() {
    return contestStatus;
  }

  public void setContestStatus(@Nullable ContestStatusEnum contestStatus) {
    this.contestStatus = contestStatus;
  }

  public Territory vulnerabilityWindow(@Nullable TerritoryVulnerabilityWindow vulnerabilityWindow) {
    this.vulnerabilityWindow = vulnerabilityWindow;
    return this;
  }

  /**
   * Get vulnerabilityWindow
   * @return vulnerabilityWindow
   */
  @Valid 
  @Schema(name = "vulnerabilityWindow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vulnerabilityWindow")
  public @Nullable TerritoryVulnerabilityWindow getVulnerabilityWindow() {
    return vulnerabilityWindow;
  }

  public void setVulnerabilityWindow(@Nullable TerritoryVulnerabilityWindow vulnerabilityWindow) {
    this.vulnerabilityWindow = vulnerabilityWindow;
  }

  public Territory fortifyLevel(@Nullable Integer fortifyLevel) {
    this.fortifyLevel = fortifyLevel;
    return this;
  }

  /**
   * Get fortifyLevel
   * minimum: 0
   * maximum: 5
   * @return fortifyLevel
   */
  @Min(value = 0) @Max(value = 5) 
  @Schema(name = "fortifyLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fortifyLevel")
  public @Nullable Integer getFortifyLevel() {
    return fortifyLevel;
  }

  public void setFortifyLevel(@Nullable Integer fortifyLevel) {
    this.fortifyLevel = fortifyLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Territory territory = (Territory) o;
    return Objects.equals(this.territoryId, territory.territoryId) &&
        Objects.equals(this.name, territory.name) &&
        Objects.equals(this.region, territory.region) &&
        Objects.equals(this.bonuses, territory.bonuses) &&
        Objects.equals(this.currentOwner, territory.currentOwner) &&
        Objects.equals(this.contestStatus, territory.contestStatus) &&
        Objects.equals(this.vulnerabilityWindow, territory.vulnerabilityWindow) &&
        Objects.equals(this.fortifyLevel, territory.fortifyLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(territoryId, name, region, bonuses, currentOwner, contestStatus, vulnerabilityWindow, fortifyLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Territory {\n");
    sb.append("    territoryId: ").append(toIndentedString(territoryId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    currentOwner: ").append(toIndentedString(currentOwner)).append("\n");
    sb.append("    contestStatus: ").append(toIndentedString(contestStatus)).append("\n");
    sb.append("    vulnerabilityWindow: ").append(toIndentedString(vulnerabilityWindow)).append("\n");
    sb.append("    fortifyLevel: ").append(toIndentedString(fortifyLevel)).append("\n");
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

