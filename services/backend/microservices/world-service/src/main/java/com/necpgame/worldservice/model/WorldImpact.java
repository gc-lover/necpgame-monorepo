package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
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
 * WorldImpact
 */


public class WorldImpact {

  private String characterId;

  /**
   * Gets or Sets impactType
   */
  public enum ImpactTypeEnum {
    QUEST_COMPLETION("quest_completion"),
    
    COMBAT_ACTION("combat_action"),
    
    ECONOMIC_ACTION("economic_action"),
    
    SOCIAL_ACTION("social_action"),
    
    TERRITORY_CAPTURE("territory_capture");

    private final String value;

    ImpactTypeEnum(String value) {
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
    public static ImpactTypeEnum fromValue(String value) {
      for (ImpactTypeEnum b : ImpactTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ImpactTypeEnum impactType;

  /**
   * Gets or Sets impactCategory
   */
  public enum ImpactCategoryEnum {
    TERRITORY_CONTROL("territory_control"),
    
    FACTION_POWER("faction_power"),
    
    ECONOMIC_STATE("economic_state"),
    
    TECHNOLOGY_LEVEL("technology_level"),
    
    SOCIAL_STRUCTURE("social_structure"),
    
    ENVIRONMENTAL("environmental");

    private final String value;

    ImpactCategoryEnum(String value) {
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
    public static ImpactCategoryEnum fromValue(String value) {
      for (ImpactCategoryEnum b : ImpactCategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ImpactCategoryEnum impactCategory;

  private @Nullable BigDecimal magnitude;

  @Valid
  private List<String> affectedEntities = new ArrayList<>();

  private @Nullable Object metadata;

  public WorldImpact() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WorldImpact(String characterId, ImpactTypeEnum impactType, ImpactCategoryEnum impactCategory) {
    this.characterId = characterId;
    this.impactType = impactType;
    this.impactCategory = impactCategory;
  }

  public WorldImpact characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public WorldImpact impactType(ImpactTypeEnum impactType) {
    this.impactType = impactType;
    return this;
  }

  /**
   * Get impactType
   * @return impactType
   */
  @NotNull 
  @Schema(name = "impact_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("impact_type")
  public ImpactTypeEnum getImpactType() {
    return impactType;
  }

  public void setImpactType(ImpactTypeEnum impactType) {
    this.impactType = impactType;
  }

  public WorldImpact impactCategory(ImpactCategoryEnum impactCategory) {
    this.impactCategory = impactCategory;
    return this;
  }

  /**
   * Get impactCategory
   * @return impactCategory
   */
  @NotNull 
  @Schema(name = "impact_category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("impact_category")
  public ImpactCategoryEnum getImpactCategory() {
    return impactCategory;
  }

  public void setImpactCategory(ImpactCategoryEnum impactCategory) {
    this.impactCategory = impactCategory;
  }

  public WorldImpact magnitude(@Nullable BigDecimal magnitude) {
    this.magnitude = magnitude;
    return this;
  }

  /**
   * Величина влияния
   * @return magnitude
   */
  @Valid 
  @Schema(name = "magnitude", description = "Величина влияния", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("magnitude")
  public @Nullable BigDecimal getMagnitude() {
    return magnitude;
  }

  public void setMagnitude(@Nullable BigDecimal magnitude) {
    this.magnitude = magnitude;
  }

  public WorldImpact affectedEntities(List<String> affectedEntities) {
    this.affectedEntities = affectedEntities;
    return this;
  }

  public WorldImpact addAffectedEntitiesItem(String affectedEntitiesItem) {
    if (this.affectedEntities == null) {
      this.affectedEntities = new ArrayList<>();
    }
    this.affectedEntities.add(affectedEntitiesItem);
    return this;
  }

  /**
   * Затронутые сущности (регионы, фракции)
   * @return affectedEntities
   */
  
  @Schema(name = "affected_entities", description = "Затронутые сущности (регионы, фракции)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_entities")
  public List<String> getAffectedEntities() {
    return affectedEntities;
  }

  public void setAffectedEntities(List<String> affectedEntities) {
    this.affectedEntities = affectedEntities;
  }

  public WorldImpact metadata(@Nullable Object metadata) {
    this.metadata = metadata;
    return this;
  }

  /**
   * Дополнительные данные о влиянии
   * @return metadata
   */
  
  @Schema(name = "metadata", description = "Дополнительные данные о влиянии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public @Nullable Object getMetadata() {
    return metadata;
  }

  public void setMetadata(@Nullable Object metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldImpact worldImpact = (WorldImpact) o;
    return Objects.equals(this.characterId, worldImpact.characterId) &&
        Objects.equals(this.impactType, worldImpact.impactType) &&
        Objects.equals(this.impactCategory, worldImpact.impactCategory) &&
        Objects.equals(this.magnitude, worldImpact.magnitude) &&
        Objects.equals(this.affectedEntities, worldImpact.affectedEntities) &&
        Objects.equals(this.metadata, worldImpact.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, impactType, impactCategory, magnitude, affectedEntities, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldImpact {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    impactType: ").append(toIndentedString(impactType)).append("\n");
    sb.append("    impactCategory: ").append(toIndentedString(impactCategory)).append("\n");
    sb.append("    magnitude: ").append(toIndentedString(magnitude)).append("\n");
    sb.append("    affectedEntities: ").append(toIndentedString(affectedEntities)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

