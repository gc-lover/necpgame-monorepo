package com.necpgame.characterservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.characterservice.model.Skill;
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
 * GetCharacterSkills200Response
 */

@JsonTypeName("getCharacterSkills_200_response")

public class GetCharacterSkills200Response {

  @Valid
  private List<@Valid Skill> skills = new ArrayList<>();

  public GetCharacterSkills200Response skills(List<@Valid Skill> skills) {
    this.skills = skills;
    return this;
  }

  public GetCharacterSkills200Response addSkillsItem(Skill skillsItem) {
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
    GetCharacterSkills200Response getCharacterSkills200Response = (GetCharacterSkills200Response) o;
    return Objects.equals(this.skills, getCharacterSkills200Response.skills);
  }

  @Override
  public int hashCode() {
    return Objects.hash(skills);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCharacterSkills200Response {\n");
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

