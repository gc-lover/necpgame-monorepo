package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Skill;
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
 * SkillsResponse
 */


public class SkillsResponse {

  private @Nullable String characterId;

  @Valid
  private List<@Valid Skill> skills = new ArrayList<>();

  public SkillsResponse characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public SkillsResponse skills(List<@Valid Skill> skills) {
    this.skills = skills;
    return this;
  }

  public SkillsResponse addSkillsItem(Skill skillsItem) {
    if (this.skills == null) {
      this.skills = new ArrayList<>();
    }
    this.skills.add(skillsItem);
    return this;
  }

  /**
   * Get skills
   * @return skills
   */
  @Valid 
  @Schema(name = "skills", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skills")
  public List<@Valid Skill> getSkills() {
    return skills;
  }

  public void setSkills(List<@Valid Skill> skills) {
    this.skills = skills;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SkillsResponse skillsResponse = (SkillsResponse) o;
    return Objects.equals(this.characterId, skillsResponse.characterId) &&
        Objects.equals(this.skills, skillsResponse.skills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, skills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SkillsResponse {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    skills: ").append(toIndentedString(skills)).append("\n");
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

