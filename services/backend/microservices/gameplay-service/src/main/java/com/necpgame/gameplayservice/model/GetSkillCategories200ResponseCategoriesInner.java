package com.necpgame.gameplayservice.model;

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
 * GetSkillCategories200ResponseCategoriesInner
 */

@JsonTypeName("getSkillCategories_200_response_categories_inner")

public class GetSkillCategories200ResponseCategoriesInner {

  private @Nullable String categoryId;

  private @Nullable String name;

  private @Nullable Integer skillsCount;

  public GetSkillCategories200ResponseCategoriesInner categoryId(@Nullable String categoryId) {
    this.categoryId = categoryId;
    return this;
  }

  /**
   * Get categoryId
   * @return categoryId
   */
  
  @Schema(name = "category_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category_id")
  public @Nullable String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(@Nullable String categoryId) {
    this.categoryId = categoryId;
  }

  public GetSkillCategories200ResponseCategoriesInner name(@Nullable String name) {
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

  public GetSkillCategories200ResponseCategoriesInner skillsCount(@Nullable Integer skillsCount) {
    this.skillsCount = skillsCount;
    return this;
  }

  /**
   * Get skillsCount
   * @return skillsCount
   */
  
  @Schema(name = "skills_count", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("skills_count")
  public @Nullable Integer getSkillsCount() {
    return skillsCount;
  }

  public void setSkillsCount(@Nullable Integer skillsCount) {
    this.skillsCount = skillsCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSkillCategories200ResponseCategoriesInner getSkillCategories200ResponseCategoriesInner = (GetSkillCategories200ResponseCategoriesInner) o;
    return Objects.equals(this.categoryId, getSkillCategories200ResponseCategoriesInner.categoryId) &&
        Objects.equals(this.name, getSkillCategories200ResponseCategoriesInner.name) &&
        Objects.equals(this.skillsCount, getSkillCategories200ResponseCategoriesInner.skillsCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(categoryId, name, skillsCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSkillCategories200ResponseCategoriesInner {\n");
    sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    skillsCount: ").append(toIndentedString(skillsCount)).append("\n");
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

