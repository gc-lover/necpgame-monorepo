package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ValidationStatus;
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
 * ToxicityCheckResult
 */


public class ToxicityCheckResult {

  private ValidationStatus status;

  private Float toxicityScore;

  @Valid
  private List<String> flaggedPhrases = new ArrayList<>();

  @Valid
  private List<String> categories = new ArrayList<>();

  public ToxicityCheckResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ToxicityCheckResult(ValidationStatus status, Float toxicityScore) {
    this.status = status;
    this.toxicityScore = toxicityScore;
  }

  public ToxicityCheckResult status(ValidationStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public ValidationStatus getStatus() {
    return status;
  }

  public void setStatus(ValidationStatus status) {
    this.status = status;
  }

  public ToxicityCheckResult toxicityScore(Float toxicityScore) {
    this.toxicityScore = toxicityScore;
    return this;
  }

  /**
   * Get toxicityScore
   * minimum: 0
   * maximum: 1
   * @return toxicityScore
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "toxicityScore", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("toxicityScore")
  public Float getToxicityScore() {
    return toxicityScore;
  }

  public void setToxicityScore(Float toxicityScore) {
    this.toxicityScore = toxicityScore;
  }

  public ToxicityCheckResult flaggedPhrases(List<String> flaggedPhrases) {
    this.flaggedPhrases = flaggedPhrases;
    return this;
  }

  public ToxicityCheckResult addFlaggedPhrasesItem(String flaggedPhrasesItem) {
    if (this.flaggedPhrases == null) {
      this.flaggedPhrases = new ArrayList<>();
    }
    this.flaggedPhrases.add(flaggedPhrasesItem);
    return this;
  }

  /**
   * Get flaggedPhrases
   * @return flaggedPhrases
   */
  
  @Schema(name = "flaggedPhrases", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flaggedPhrases")
  public List<String> getFlaggedPhrases() {
    return flaggedPhrases;
  }

  public void setFlaggedPhrases(List<String> flaggedPhrases) {
    this.flaggedPhrases = flaggedPhrases;
  }

  public ToxicityCheckResult categories(List<String> categories) {
    this.categories = categories;
    return this;
  }

  public ToxicityCheckResult addCategoriesItem(String categoriesItem) {
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
  
  @Schema(name = "categories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categories")
  public List<String> getCategories() {
    return categories;
  }

  public void setCategories(List<String> categories) {
    this.categories = categories;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ToxicityCheckResult toxicityCheckResult = (ToxicityCheckResult) o;
    return Objects.equals(this.status, toxicityCheckResult.status) &&
        Objects.equals(this.toxicityScore, toxicityCheckResult.toxicityScore) &&
        Objects.equals(this.flaggedPhrases, toxicityCheckResult.flaggedPhrases) &&
        Objects.equals(this.categories, toxicityCheckResult.categories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, toxicityScore, flaggedPhrases, categories);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ToxicityCheckResult {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    toxicityScore: ").append(toIndentedString(toxicityScore)).append("\n");
    sb.append("    flaggedPhrases: ").append(toIndentedString(flaggedPhrases)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
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

