package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.MentorRequirements;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Mentor
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Mentor {

  private @Nullable String npcId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    COMBAT("COMBAT"),
    
    TECH("TECH"),
    
    NETRUNNING("NETRUNNING"),
    
    SOCIAL("SOCIAL"),
    
    ECONOMY("ECONOMY"),
    
    MEDICAL("MEDICAL");

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

  private @Nullable String specialization;

  private @Nullable Integer skillLevel;

  private @Nullable Integer reputation;

  private @Nullable Integer maxStudents;

  private @Nullable Integer currentStudents;

  private @Nullable Integer teachingCost;

  private @Nullable MentorRequirements requirements;

  public Mentor npcId(@Nullable String npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  
  @Schema(name = "npc_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_id")
  public @Nullable String getNpcId() {
    return npcId;
  }

  public void setNpcId(@Nullable String npcId) {
    this.npcId = npcId;
  }

  public Mentor name(@Nullable String name) {
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

  public Mentor type(@Nullable TypeEnum type) {
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

  public Mentor specialization(@Nullable String specialization) {
    this.specialization = specialization;
    return this;
  }

  /**
   * Get specialization
   * @return specialization
   */
  
  @Schema(name = "specialization", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("specialization")
  public @Nullable String getSpecialization() {
    return specialization;
  }

  public void setSpecialization(@Nullable String specialization) {
    this.specialization = specialization;
  }

  public Mentor skillLevel(@Nullable Integer skillLevel) {
    this.skillLevel = skillLevel;
    return this;
  }

  /**
   * Get skillLevel
   * minimum: 1
   * maximum: 20
   * @return skillLevel
   */
  @Min(value = 1) @Max(value = 20) 
  @Schema(name = "skill_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_level")
  public @Nullable Integer getSkillLevel() {
    return skillLevel;
  }

  public void setSkillLevel(@Nullable Integer skillLevel) {
    this.skillLevel = skillLevel;
  }

  public Mentor reputation(@Nullable Integer reputation) {
    this.reputation = reputation;
    return this;
  }

  /**
   * Get reputation
   * @return reputation
   */
  
  @Schema(name = "reputation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputation")
  public @Nullable Integer getReputation() {
    return reputation;
  }

  public void setReputation(@Nullable Integer reputation) {
    this.reputation = reputation;
  }

  public Mentor maxStudents(@Nullable Integer maxStudents) {
    this.maxStudents = maxStudents;
    return this;
  }

  /**
   * Get maxStudents
   * @return maxStudents
   */
  
  @Schema(name = "max_students", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_students")
  public @Nullable Integer getMaxStudents() {
    return maxStudents;
  }

  public void setMaxStudents(@Nullable Integer maxStudents) {
    this.maxStudents = maxStudents;
  }

  public Mentor currentStudents(@Nullable Integer currentStudents) {
    this.currentStudents = currentStudents;
    return this;
  }

  /**
   * Get currentStudents
   * @return currentStudents
   */
  
  @Schema(name = "current_students", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_students")
  public @Nullable Integer getCurrentStudents() {
    return currentStudents;
  }

  public void setCurrentStudents(@Nullable Integer currentStudents) {
    this.currentStudents = currentStudents;
  }

  public Mentor teachingCost(@Nullable Integer teachingCost) {
    this.teachingCost = teachingCost;
    return this;
  }

  /**
   * Стоимость обучения
   * @return teachingCost
   */
  
  @Schema(name = "teaching_cost", description = "Стоимость обучения", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("teaching_cost")
  public @Nullable Integer getTeachingCost() {
    return teachingCost;
  }

  public void setTeachingCost(@Nullable Integer teachingCost) {
    this.teachingCost = teachingCost;
  }

  public Mentor requirements(@Nullable MentorRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable MentorRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable MentorRequirements requirements) {
    this.requirements = requirements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Mentor mentor = (Mentor) o;
    return Objects.equals(this.npcId, mentor.npcId) &&
        Objects.equals(this.name, mentor.name) &&
        Objects.equals(this.type, mentor.type) &&
        Objects.equals(this.specialization, mentor.specialization) &&
        Objects.equals(this.skillLevel, mentor.skillLevel) &&
        Objects.equals(this.reputation, mentor.reputation) &&
        Objects.equals(this.maxStudents, mentor.maxStudents) &&
        Objects.equals(this.currentStudents, mentor.currentStudents) &&
        Objects.equals(this.teachingCost, mentor.teachingCost) &&
        Objects.equals(this.requirements, mentor.requirements);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, name, type, specialization, skillLevel, reputation, maxStudents, currentStudents, teachingCost, requirements);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Mentor {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    specialization: ").append(toIndentedString(specialization)).append("\n");
    sb.append("    skillLevel: ").append(toIndentedString(skillLevel)).append("\n");
    sb.append("    reputation: ").append(toIndentedString(reputation)).append("\n");
    sb.append("    maxStudents: ").append(toIndentedString(maxStudents)).append("\n");
    sb.append("    currentStudents: ").append(toIndentedString(currentStudents)).append("\n");
    sb.append("    teachingCost: ").append(toIndentedString(teachingCost)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
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

