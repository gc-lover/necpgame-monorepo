package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MentorAbility
 */


public class MentorAbility {

  private @Nullable String abilityId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable String type;

  private @Nullable Integer requiredLessons;

  private @Nullable Integer requiredBond;

  private @Nullable Boolean learned;

  private @Nullable Boolean unique;

  public MentorAbility abilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
    return this;
  }

  /**
   * Get abilityId
   * @return abilityId
   */
  
  @Schema(name = "ability_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ability_id")
  public @Nullable String getAbilityId() {
    return abilityId;
  }

  public void setAbilityId(@Nullable String abilityId) {
    this.abilityId = abilityId;
  }

  public MentorAbility name(@Nullable String name) {
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

  public MentorAbility description(@Nullable String description) {
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

  public MentorAbility type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public MentorAbility requiredLessons(@Nullable Integer requiredLessons) {
    this.requiredLessons = requiredLessons;
    return this;
  }

  /**
   * Get requiredLessons
   * @return requiredLessons
   */
  
  @Schema(name = "required_lessons", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_lessons")
  public @Nullable Integer getRequiredLessons() {
    return requiredLessons;
  }

  public void setRequiredLessons(@Nullable Integer requiredLessons) {
    this.requiredLessons = requiredLessons;
  }

  public MentorAbility requiredBond(@Nullable Integer requiredBond) {
    this.requiredBond = requiredBond;
    return this;
  }

  /**
   * Get requiredBond
   * @return requiredBond
   */
  
  @Schema(name = "required_bond", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_bond")
  public @Nullable Integer getRequiredBond() {
    return requiredBond;
  }

  public void setRequiredBond(@Nullable Integer requiredBond) {
    this.requiredBond = requiredBond;
  }

  public MentorAbility learned(@Nullable Boolean learned) {
    this.learned = learned;
    return this;
  }

  /**
   * Get learned
   * @return learned
   */
  
  @Schema(name = "learned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("learned")
  public @Nullable Boolean getLearned() {
    return learned;
  }

  public void setLearned(@Nullable Boolean learned) {
    this.learned = learned;
  }

  public MentorAbility unique(@Nullable Boolean unique) {
    this.unique = unique;
    return this;
  }

  /**
   * Уникальная способность mentor
   * @return unique
   */
  
  @Schema(name = "unique", description = "Уникальная способность mentor", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("unique")
  public @Nullable Boolean getUnique() {
    return unique;
  }

  public void setUnique(@Nullable Boolean unique) {
    this.unique = unique;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MentorAbility mentorAbility = (MentorAbility) o;
    return Objects.equals(this.abilityId, mentorAbility.abilityId) &&
        Objects.equals(this.name, mentorAbility.name) &&
        Objects.equals(this.description, mentorAbility.description) &&
        Objects.equals(this.type, mentorAbility.type) &&
        Objects.equals(this.requiredLessons, mentorAbility.requiredLessons) &&
        Objects.equals(this.requiredBond, mentorAbility.requiredBond) &&
        Objects.equals(this.learned, mentorAbility.learned) &&
        Objects.equals(this.unique, mentorAbility.unique);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilityId, name, description, type, requiredLessons, requiredBond, learned, unique);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MentorAbility {\n");
    sb.append("    abilityId: ").append(toIndentedString(abilityId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    requiredLessons: ").append(toIndentedString(requiredLessons)).append("\n");
    sb.append("    requiredBond: ").append(toIndentedString(requiredBond)).append("\n");
    sb.append("    learned: ").append(toIndentedString(learned)).append("\n");
    sb.append("    unique: ").append(toIndentedString(unique)).append("\n");
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

