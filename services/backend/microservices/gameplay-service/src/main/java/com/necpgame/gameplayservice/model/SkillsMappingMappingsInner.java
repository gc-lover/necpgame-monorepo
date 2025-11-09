package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.SkillsMappingMappingsInnerMappedToInner;
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
 * SkillsMappingMappingsInner
 */

@JsonTypeName("SkillsMapping_mappings_inner")

public class SkillsMappingMappingsInner {

  private @Nullable String skillId;

  private @Nullable String skillName;

  @Valid
  private List<@Valid SkillsMappingMappingsInnerMappedToInner> mappedTo = new ArrayList<>();

  public SkillsMappingMappingsInner skillId(@Nullable String skillId) {
    this.skillId = skillId;
    return this;
  }

  /**
   * Get skillId
   * @return skillId
   */
  
  @Schema(name = "skill_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_id")
  public @Nullable String getSkillId() {
    return skillId;
  }

  public void setSkillId(@Nullable String skillId) {
    this.skillId = skillId;
  }

  public SkillsMappingMappingsInner skillName(@Nullable String skillName) {
    this.skillName = skillName;
    return this;
  }

  /**
   * Get skillName
   * @return skillName
   */
  
  @Schema(name = "skill_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_name")
  public @Nullable String getSkillName() {
    return skillName;
  }

  public void setSkillName(@Nullable String skillName) {
    this.skillName = skillName;
  }

  public SkillsMappingMappingsInner mappedTo(List<@Valid SkillsMappingMappingsInnerMappedToInner> mappedTo) {
    this.mappedTo = mappedTo;
    return this;
  }

  public SkillsMappingMappingsInner addMappedToItem(SkillsMappingMappingsInnerMappedToInner mappedToItem) {
    if (this.mappedTo == null) {
      this.mappedTo = new ArrayList<>();
    }
    this.mappedTo.add(mappedToItem);
    return this;
  }

  /**
   * Get mappedTo
   * @return mappedTo
   */
  @Valid 
  @Schema(name = "mapped_to", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mapped_to")
  public List<@Valid SkillsMappingMappingsInnerMappedToInner> getMappedTo() {
    return mappedTo;
  }

  public void setMappedTo(List<@Valid SkillsMappingMappingsInnerMappedToInner> mappedTo) {
    this.mappedTo = mappedTo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillsMappingMappingsInner skillsMappingMappingsInner = (SkillsMappingMappingsInner) o;
    return Objects.equals(this.skillId, skillsMappingMappingsInner.skillId) &&
        Objects.equals(this.skillName, skillsMappingMappingsInner.skillName) &&
        Objects.equals(this.mappedTo, skillsMappingMappingsInner.mappedTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skillId, skillName, mappedTo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillsMappingMappingsInner {\n");
    sb.append("    skillId: ").append(toIndentedString(skillId)).append("\n");
    sb.append("    skillName: ").append(toIndentedString(skillName)).append("\n");
    sb.append("    mappedTo: ").append(toIndentedString(mappedTo)).append("\n");
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

