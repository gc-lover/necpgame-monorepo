package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * GetOwnPathInfo200Response
 */

@JsonTypeName("getOwnPathInfo_200_response")

public class GetOwnPathInfo200Response {

  private @Nullable String description;

  @Valid
  private List<String> advantages = new ArrayList<>();

  @Valid
  private List<String> disadvantages = new ArrayList<>();

  @Valid
  private List<String> skillTreeAccess = new ArrayList<>();

  public GetOwnPathInfo200Response description(@Nullable String description) {
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

  public GetOwnPathInfo200Response advantages(List<String> advantages) {
    this.advantages = advantages;
    return this;
  }

  public GetOwnPathInfo200Response addAdvantagesItem(String advantagesItem) {
    if (this.advantages == null) {
      this.advantages = new ArrayList<>();
    }
    this.advantages.add(advantagesItem);
    return this;
  }

  /**
   * Get advantages
   * @return advantages
   */
  
  @Schema(name = "advantages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("advantages")
  public List<String> getAdvantages() {
    return advantages;
  }

  public void setAdvantages(List<String> advantages) {
    this.advantages = advantages;
  }

  public GetOwnPathInfo200Response disadvantages(List<String> disadvantages) {
    this.disadvantages = disadvantages;
    return this;
  }

  public GetOwnPathInfo200Response addDisadvantagesItem(String disadvantagesItem) {
    if (this.disadvantages == null) {
      this.disadvantages = new ArrayList<>();
    }
    this.disadvantages.add(disadvantagesItem);
    return this;
  }

  /**
   * Get disadvantages
   * @return disadvantages
   */
  
  @Schema(name = "disadvantages", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("disadvantages")
  public List<String> getDisadvantages() {
    return disadvantages;
  }

  public void setDisadvantages(List<String> disadvantages) {
    this.disadvantages = disadvantages;
  }

  public GetOwnPathInfo200Response skillTreeAccess(List<String> skillTreeAccess) {
    this.skillTreeAccess = skillTreeAccess;
    return this;
  }

  public GetOwnPathInfo200Response addSkillTreeAccessItem(String skillTreeAccessItem) {
    if (this.skillTreeAccess == null) {
      this.skillTreeAccess = new ArrayList<>();
    }
    this.skillTreeAccess.add(skillTreeAccessItem);
    return this;
  }

  /**
   * Get skillTreeAccess
   * @return skillTreeAccess
   */
  
  @Schema(name = "skill_tree_access", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_tree_access")
  public List<String> getSkillTreeAccess() {
    return skillTreeAccess;
  }

  public void setSkillTreeAccess(List<String> skillTreeAccess) {
    this.skillTreeAccess = skillTreeAccess;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetOwnPathInfo200Response getOwnPathInfo200Response = (GetOwnPathInfo200Response) o;
    return Objects.equals(this.description, getOwnPathInfo200Response.description) &&
        Objects.equals(this.advantages, getOwnPathInfo200Response.advantages) &&
        Objects.equals(this.disadvantages, getOwnPathInfo200Response.disadvantages) &&
        Objects.equals(this.skillTreeAccess, getOwnPathInfo200Response.skillTreeAccess);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, advantages, disadvantages, skillTreeAccess);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetOwnPathInfo200Response {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    advantages: ").append(toIndentedString(advantages)).append("\n");
    sb.append("    disadvantages: ").append(toIndentedString(disadvantages)).append("\n");
    sb.append("    skillTreeAccess: ").append(toIndentedString(skillTreeAccess)).append("\n");
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

