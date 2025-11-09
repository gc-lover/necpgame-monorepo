package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetCaps200Response
 */

@JsonTypeName("getCaps_200_response")

public class GetCaps200Response {

  @Valid
  private Map<String, Integer> attributeCaps = new HashMap<>();

  @Valid
  private Map<String, Integer> skillCaps = new HashMap<>();

  private @Nullable Integer levelCap;

  public GetCaps200Response attributeCaps(Map<String, Integer> attributeCaps) {
    this.attributeCaps = attributeCaps;
    return this;
  }

  public GetCaps200Response putAttributeCapsItem(String key, Integer attributeCapsItem) {
    if (this.attributeCaps == null) {
      this.attributeCaps = new HashMap<>();
    }
    this.attributeCaps.put(key, attributeCapsItem);
    return this;
  }

  /**
   * Get attributeCaps
   * @return attributeCaps
   */
  
  @Schema(name = "attribute_caps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attribute_caps")
  public Map<String, Integer> getAttributeCaps() {
    return attributeCaps;
  }

  public void setAttributeCaps(Map<String, Integer> attributeCaps) {
    this.attributeCaps = attributeCaps;
  }

  public GetCaps200Response skillCaps(Map<String, Integer> skillCaps) {
    this.skillCaps = skillCaps;
    return this;
  }

  public GetCaps200Response putSkillCapsItem(String key, Integer skillCapsItem) {
    if (this.skillCaps == null) {
      this.skillCaps = new HashMap<>();
    }
    this.skillCaps.put(key, skillCapsItem);
    return this;
  }

  /**
   * Get skillCaps
   * @return skillCaps
   */
  
  @Schema(name = "skill_caps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skill_caps")
  public Map<String, Integer> getSkillCaps() {
    return skillCaps;
  }

  public void setSkillCaps(Map<String, Integer> skillCaps) {
    this.skillCaps = skillCaps;
  }

  public GetCaps200Response levelCap(@Nullable Integer levelCap) {
    this.levelCap = levelCap;
    return this;
  }

  /**
   * Get levelCap
   * @return levelCap
   */
  
  @Schema(name = "level_cap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_cap")
  public @Nullable Integer getLevelCap() {
    return levelCap;
  }

  public void setLevelCap(@Nullable Integer levelCap) {
    this.levelCap = levelCap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCaps200Response getCaps200Response = (GetCaps200Response) o;
    return Objects.equals(this.attributeCaps, getCaps200Response.attributeCaps) &&
        Objects.equals(this.skillCaps, getCaps200Response.skillCaps) &&
        Objects.equals(this.levelCap, getCaps200Response.levelCap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attributeCaps, skillCaps, levelCap);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCaps200Response {\n");
    sb.append("    attributeCaps: ").append(toIndentedString(attributeCaps)).append("\n");
    sb.append("    skillCaps: ").append(toIndentedString(skillCaps)).append("\n");
    sb.append("    levelCap: ").append(toIndentedString(levelCap)).append("\n");
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

