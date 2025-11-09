package com.necpgame.backjava.model;

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
 * MentorRequirements
 */

@JsonTypeName("Mentor_requirements")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MentorRequirements {

  private @Nullable Integer minLevel;

  private @Nullable Integer minSkill;

  private @Nullable Object requiredReputation;

  public MentorRequirements minLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
    return this;
  }

  /**
   * Get minLevel
   * @return minLevel
   */
  
  @Schema(name = "min_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_level")
  public @Nullable Integer getMinLevel() {
    return minLevel;
  }

  public void setMinLevel(@Nullable Integer minLevel) {
    this.minLevel = minLevel;
  }

  public MentorRequirements minSkill(@Nullable Integer minSkill) {
    this.minSkill = minSkill;
    return this;
  }

  /**
   * Get minSkill
   * @return minSkill
   */
  
  @Schema(name = "min_skill", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_skill")
  public @Nullable Integer getMinSkill() {
    return minSkill;
  }

  public void setMinSkill(@Nullable Integer minSkill) {
    this.minSkill = minSkill;
  }

  public MentorRequirements requiredReputation(@Nullable Object requiredReputation) {
    this.requiredReputation = requiredReputation;
    return this;
  }

  /**
   * Get requiredReputation
   * @return requiredReputation
   */
  
  @Schema(name = "required_reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_reputation")
  public @Nullable Object getRequiredReputation() {
    return requiredReputation;
  }

  public void setRequiredReputation(@Nullable Object requiredReputation) {
    this.requiredReputation = requiredReputation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MentorRequirements mentorRequirements = (MentorRequirements) o;
    return Objects.equals(this.minLevel, mentorRequirements.minLevel) &&
        Objects.equals(this.minSkill, mentorRequirements.minSkill) &&
        Objects.equals(this.requiredReputation, mentorRequirements.requiredReputation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(minLevel, minSkill, requiredReputation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MentorRequirements {\n");
    sb.append("    minLevel: ").append(toIndentedString(minLevel)).append("\n");
    sb.append("    minSkill: ").append(toIndentedString(minSkill)).append("\n");
    sb.append("    requiredReputation: ").append(toIndentedString(requiredReputation)).append("\n");
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

