package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * EraInfo
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class EraInfo {

  private @Nullable String era;

  private @Nullable String name;

  private @Nullable String description;

  @Valid
  private List<String> keyFeatures = new ArrayList<>();

  @Valid
  private List<String> majorFactions = new ArrayList<>();

  private @Nullable Integer technologyLevel;

  /**
   * Gets or Sets dangerLevel
   */
  public enum DangerLevelEnum {
    LOW("LOW"),
    
    MEDIUM("MEDIUM"),
    
    HIGH("HIGH"),
    
    EXTREME("EXTREME");

    private final String value;

    DangerLevelEnum(String value) {
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
    public static DangerLevelEnum fromValue(String value) {
      for (DangerLevelEnum b : DangerLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable DangerLevelEnum dangerLevel;

  private @Nullable Integer activeEventsCount;

  public EraInfo era(@Nullable String era) {
    this.era = era;
    return this;
  }

  /**
   * Get era
   * @return era
   */
  
  @Schema(name = "era", example = "2060-2077", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("era")
  public @Nullable String getEra() {
    return era;
  }

  public void setEra(@Nullable String era) {
    this.era = era;
  }

  public EraInfo name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Corporate Control", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public EraInfo description(@Nullable String description) {
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

  public EraInfo keyFeatures(List<String> keyFeatures) {
    this.keyFeatures = keyFeatures;
    return this;
  }

  public EraInfo addKeyFeaturesItem(String keyFeaturesItem) {
    if (this.keyFeatures == null) {
      this.keyFeatures = new ArrayList<>();
    }
    this.keyFeatures.add(keyFeaturesItem);
    return this;
  }

  /**
   * Get keyFeatures
   * @return keyFeatures
   */
  
  @Schema(name = "key_features", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("key_features")
  public List<String> getKeyFeatures() {
    return keyFeatures;
  }

  public void setKeyFeatures(List<String> keyFeatures) {
    this.keyFeatures = keyFeatures;
  }

  public EraInfo majorFactions(List<String> majorFactions) {
    this.majorFactions = majorFactions;
    return this;
  }

  public EraInfo addMajorFactionsItem(String majorFactionsItem) {
    if (this.majorFactions == null) {
      this.majorFactions = new ArrayList<>();
    }
    this.majorFactions.add(majorFactionsItem);
    return this;
  }

  /**
   * Get majorFactions
   * @return majorFactions
   */
  
  @Schema(name = "major_factions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("major_factions")
  public List<String> getMajorFactions() {
    return majorFactions;
  }

  public void setMajorFactions(List<String> majorFactions) {
    this.majorFactions = majorFactions;
  }

  public EraInfo technologyLevel(@Nullable Integer technologyLevel) {
    this.technologyLevel = technologyLevel;
    return this;
  }

  /**
   * Get technologyLevel
   * minimum: 1
   * maximum: 10
   * @return technologyLevel
   */
  @Min(value = 1) @Max(value = 10) 
  @Schema(name = "technology_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("technology_level")
  public @Nullable Integer getTechnologyLevel() {
    return technologyLevel;
  }

  public void setTechnologyLevel(@Nullable Integer technologyLevel) {
    this.technologyLevel = technologyLevel;
  }

  public EraInfo dangerLevel(@Nullable DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
    return this;
  }

  /**
   * Get dangerLevel
   * @return dangerLevel
   */
  
  @Schema(name = "danger_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("danger_level")
  public @Nullable DangerLevelEnum getDangerLevel() {
    return dangerLevel;
  }

  public void setDangerLevel(@Nullable DangerLevelEnum dangerLevel) {
    this.dangerLevel = dangerLevel;
  }

  public EraInfo activeEventsCount(@Nullable Integer activeEventsCount) {
    this.activeEventsCount = activeEventsCount;
    return this;
  }

  /**
   * Get activeEventsCount
   * @return activeEventsCount
   */
  
  @Schema(name = "active_events_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_events_count")
  public @Nullable Integer getActiveEventsCount() {
    return activeEventsCount;
  }

  public void setActiveEventsCount(@Nullable Integer activeEventsCount) {
    this.activeEventsCount = activeEventsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EraInfo eraInfo = (EraInfo) o;
    return Objects.equals(this.era, eraInfo.era) &&
        Objects.equals(this.name, eraInfo.name) &&
        Objects.equals(this.description, eraInfo.description) &&
        Objects.equals(this.keyFeatures, eraInfo.keyFeatures) &&
        Objects.equals(this.majorFactions, eraInfo.majorFactions) &&
        Objects.equals(this.technologyLevel, eraInfo.technologyLevel) &&
        Objects.equals(this.dangerLevel, eraInfo.dangerLevel) &&
        Objects.equals(this.activeEventsCount, eraInfo.activeEventsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(era, name, description, keyFeatures, majorFactions, technologyLevel, dangerLevel, activeEventsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EraInfo {\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    keyFeatures: ").append(toIndentedString(keyFeatures)).append("\n");
    sb.append("    majorFactions: ").append(toIndentedString(majorFactions)).append("\n");
    sb.append("    technologyLevel: ").append(toIndentedString(technologyLevel)).append("\n");
    sb.append("    dangerLevel: ").append(toIndentedString(dangerLevel)).append("\n");
    sb.append("    activeEventsCount: ").append(toIndentedString(activeEventsCount)).append("\n");
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

