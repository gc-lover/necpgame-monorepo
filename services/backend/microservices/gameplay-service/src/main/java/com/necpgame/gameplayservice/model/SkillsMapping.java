package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.SkillsMappingMappingsInner;
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
 * SkillsMapping
 */


public class SkillsMapping {

  /**
   * Gets or Sets mappingType
   */
  public enum MappingTypeEnum {
    TO_ITEMS("TO_ITEMS"),
    
    TO_IMPLANTS("TO_IMPLANTS"),
    
    TO_CLASSES("TO_CLASSES");

    private final String value;

    MappingTypeEnum(String value) {
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
    public static MappingTypeEnum fromValue(String value) {
      for (MappingTypeEnum b : MappingTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MappingTypeEnum mappingType;

  @Valid
  private List<@Valid SkillsMappingMappingsInner> mappings = new ArrayList<>();

  public SkillsMapping mappingType(@Nullable MappingTypeEnum mappingType) {
    this.mappingType = mappingType;
    return this;
  }

  /**
   * Get mappingType
   * @return mappingType
   */
  
  @Schema(name = "mapping_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mapping_type")
  public @Nullable MappingTypeEnum getMappingType() {
    return mappingType;
  }

  public void setMappingType(@Nullable MappingTypeEnum mappingType) {
    this.mappingType = mappingType;
  }

  public SkillsMapping mappings(List<@Valid SkillsMappingMappingsInner> mappings) {
    this.mappings = mappings;
    return this;
  }

  public SkillsMapping addMappingsItem(SkillsMappingMappingsInner mappingsItem) {
    if (this.mappings == null) {
      this.mappings = new ArrayList<>();
    }
    this.mappings.add(mappingsItem);
    return this;
  }

  /**
   * Get mappings
   * @return mappings
   */
  @Valid 
  @Schema(name = "mappings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mappings")
  public List<@Valid SkillsMappingMappingsInner> getMappings() {
    return mappings;
  }

  public void setMappings(List<@Valid SkillsMappingMappingsInner> mappings) {
    this.mappings = mappings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillsMapping skillsMapping = (SkillsMapping) o;
    return Objects.equals(this.mappingType, skillsMapping.mappingType) &&
        Objects.equals(this.mappings, skillsMapping.mappings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mappingType, mappings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillsMapping {\n");
    sb.append("    mappingType: ").append(toIndentedString(mappingType)).append("\n");
    sb.append("    mappings: ").append(toIndentedString(mappings)).append("\n");
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

