package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RatingCategoryConfig;
import com.necpgame.socialservice.model.RatingCategoryPatchRequestDecayDefaults;
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
 * RatingCategoryPatchRequest
 */


public class RatingCategoryPatchRequest {

  private String version;

  @Valid
  private List<@Valid RatingCategoryConfig> categories = new ArrayList<>();

  private @Nullable RatingCategoryPatchRequestDecayDefaults decayDefaults;

  private @Nullable UUID updatedBy;

  public RatingCategoryPatchRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingCategoryPatchRequest(String version, List<@Valid RatingCategoryConfig> categories) {
    this.version = version;
    this.categories = categories;
  }

  public RatingCategoryPatchRequest version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  @NotNull 
  @Schema(name = "version", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public RatingCategoryPatchRequest categories(List<@Valid RatingCategoryConfig> categories) {
    this.categories = categories;
    return this;
  }

  public RatingCategoryPatchRequest addCategoriesItem(RatingCategoryConfig categoriesItem) {
    if (this.categories == null) {
      this.categories = new ArrayList<>();
    }
    this.categories.add(categoriesItem);
    return this;
  }

  /**
   * Get categories
   * @return categories
   */
  @NotNull @Valid @Size(min = 1) 
  @Schema(name = "categories", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("categories")
  public List<@Valid RatingCategoryConfig> getCategories() {
    return categories;
  }

  public void setCategories(List<@Valid RatingCategoryConfig> categories) {
    this.categories = categories;
  }

  public RatingCategoryPatchRequest decayDefaults(@Nullable RatingCategoryPatchRequestDecayDefaults decayDefaults) {
    this.decayDefaults = decayDefaults;
    return this;
  }

  /**
   * Get decayDefaults
   * @return decayDefaults
   */
  @Valid 
  @Schema(name = "decayDefaults", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("decayDefaults")
  public @Nullable RatingCategoryPatchRequestDecayDefaults getDecayDefaults() {
    return decayDefaults;
  }

  public void setDecayDefaults(@Nullable RatingCategoryPatchRequestDecayDefaults decayDefaults) {
    this.decayDefaults = decayDefaults;
  }

  public RatingCategoryPatchRequest updatedBy(@Nullable UUID updatedBy) {
    this.updatedBy = updatedBy;
    return this;
  }

  /**
   * Get updatedBy
   * @return updatedBy
   */
  @Valid 
  @Schema(name = "updatedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedBy")
  public @Nullable UUID getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(@Nullable UUID updatedBy) {
    this.updatedBy = updatedBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingCategoryPatchRequest ratingCategoryPatchRequest = (RatingCategoryPatchRequest) o;
    return Objects.equals(this.version, ratingCategoryPatchRequest.version) &&
        Objects.equals(this.categories, ratingCategoryPatchRequest.categories) &&
        Objects.equals(this.decayDefaults, ratingCategoryPatchRequest.decayDefaults) &&
        Objects.equals(this.updatedBy, ratingCategoryPatchRequest.updatedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, categories, decayDefaults, updatedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingCategoryPatchRequest {\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    decayDefaults: ").append(toIndentedString(decayDefaults)).append("\n");
    sb.append("    updatedBy: ").append(toIndentedString(updatedBy)).append("\n");
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

