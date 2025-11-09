package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RatingCategoriesResponseDecayDefaults;
import com.necpgame.socialservice.model.RatingCategoryConfig;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RatingCategoriesResponse
 */


public class RatingCategoriesResponse {

  @Valid
  private List<@Valid RatingCategoryConfig> data = new ArrayList<>();

  private String version;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  private @Nullable RatingCategoriesResponseDecayDefaults decayDefaults;

  public RatingCategoriesResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RatingCategoriesResponse(List<@Valid RatingCategoryConfig> data, String version) {
    this.data = data;
    this.version = version;
  }

  public RatingCategoriesResponse data(List<@Valid RatingCategoryConfig> data) {
    this.data = data;
    return this;
  }

  public RatingCategoriesResponse addDataItem(RatingCategoryConfig dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @NotNull @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<@Valid RatingCategoryConfig> getData() {
    return data;
  }

  public void setData(List<@Valid RatingCategoryConfig> data) {
    this.data = data;
  }

  public RatingCategoriesResponse version(String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  @NotNull 
  @Schema(name = "version", example = "2077.Q4", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public RatingCategoriesResponse updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public RatingCategoriesResponse decayDefaults(@Nullable RatingCategoriesResponseDecayDefaults decayDefaults) {
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
  public @Nullable RatingCategoriesResponseDecayDefaults getDecayDefaults() {
    return decayDefaults;
  }

  public void setDecayDefaults(@Nullable RatingCategoriesResponseDecayDefaults decayDefaults) {
    this.decayDefaults = decayDefaults;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RatingCategoriesResponse ratingCategoriesResponse = (RatingCategoriesResponse) o;
    return Objects.equals(this.data, ratingCategoriesResponse.data) &&
        Objects.equals(this.version, ratingCategoriesResponse.version) &&
        Objects.equals(this.updatedAt, ratingCategoriesResponse.updatedAt) &&
        Objects.equals(this.decayDefaults, ratingCategoriesResponse.decayDefaults);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, version, updatedAt, decayDefaults);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RatingCategoriesResponse {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    decayDefaults: ").append(toIndentedString(decayDefaults)).append("\n");
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

