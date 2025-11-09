package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SkillsMappingMappingsInnerMappedToInner
 */

@JsonTypeName("SkillsMapping_mappings_inner_mapped_to_inner")

public class SkillsMappingMappingsInnerMappedToInner {

  private @Nullable String id;

  private @Nullable String name;

  private @Nullable Integer requiredLevel;

  private @Nullable String bonusPerLevel;

  public SkillsMappingMappingsInnerMappedToInner id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public SkillsMappingMappingsInnerMappedToInner name(@Nullable String name) {
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

  public SkillsMappingMappingsInnerMappedToInner requiredLevel(@Nullable Integer requiredLevel) {
    this.requiredLevel = requiredLevel;
    return this;
  }

  /**
   * Get requiredLevel
   * @return requiredLevel
   */
  
  @Schema(name = "required_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_level")
  public @Nullable Integer getRequiredLevel() {
    return requiredLevel;
  }

  public void setRequiredLevel(@Nullable Integer requiredLevel) {
    this.requiredLevel = requiredLevel;
  }

  public SkillsMappingMappingsInnerMappedToInner bonusPerLevel(@Nullable String bonusPerLevel) {
    this.bonusPerLevel = bonusPerLevel;
    return this;
  }

  /**
   * Get bonusPerLevel
   * @return bonusPerLevel
   */
  
  @Schema(name = "bonus_per_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus_per_level")
  public @Nullable String getBonusPerLevel() {
    return bonusPerLevel;
  }

  public void setBonusPerLevel(@Nullable String bonusPerLevel) {
    this.bonusPerLevel = bonusPerLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillsMappingMappingsInnerMappedToInner skillsMappingMappingsInnerMappedToInner = (SkillsMappingMappingsInnerMappedToInner) o;
    return Objects.equals(this.id, skillsMappingMappingsInnerMappedToInner.id) &&
        Objects.equals(this.name, skillsMappingMappingsInnerMappedToInner.name) &&
        Objects.equals(this.requiredLevel, skillsMappingMappingsInnerMappedToInner.requiredLevel) &&
        Objects.equals(this.bonusPerLevel, skillsMappingMappingsInnerMappedToInner.bonusPerLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, requiredLevel, bonusPerLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillsMappingMappingsInnerMappedToInner {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    requiredLevel: ").append(toIndentedString(requiredLevel)).append("\n");
    sb.append("    bonusPerLevel: ").append(toIndentedString(bonusPerLevel)).append("\n");
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

