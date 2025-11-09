package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Skill;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterSkills
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CharacterSkills {

  private @Nullable UUID characterId;

  @Valid
  private List<@Valid Skill> skills = new ArrayList<>();

  public CharacterSkills characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public CharacterSkills skills(List<@Valid Skill> skills) {
    this.skills = skills;
    return this;
  }

  public CharacterSkills addSkillsItem(Skill skillsItem) {
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
    CharacterSkills characterSkills = (CharacterSkills) o;
    return Objects.equals(this.characterId, characterSkills.characterId) &&
        Objects.equals(this.skills, characterSkills.skills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, skills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSkills {\n");
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

