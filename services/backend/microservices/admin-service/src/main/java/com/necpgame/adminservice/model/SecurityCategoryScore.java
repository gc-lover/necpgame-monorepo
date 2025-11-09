package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * SecurityCategoryScore
 */


public class SecurityCategoryScore {

  private @Nullable Integer score;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    EXCELLENT("excellent"),
    
    GOOD("good"),
    
    FAIR("fair"),
    
    POOR("poor");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @Valid
  private List<String> issues = new ArrayList<>();

  public SecurityCategoryScore score(@Nullable Integer score) {
    this.score = score;
    return this;
  }

  /**
   * Get score
   * minimum: 0
   * maximum: 100
   * @return score
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "score", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("score")
  public @Nullable Integer getScore() {
    return score;
  }

  public void setScore(@Nullable Integer score) {
    this.score = score;
  }

  public SecurityCategoryScore status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public SecurityCategoryScore issues(List<String> issues) {
    this.issues = issues;
    return this;
  }

  public SecurityCategoryScore addIssuesItem(String issuesItem) {
    if (this.issues == null) {
      this.issues = new ArrayList<>();
    }
    this.issues.add(issuesItem);
    return this;
  }

  /**
   * Get issues
   * @return issues
   */
  
  @Schema(name = "issues", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issues")
  public List<String> getIssues() {
    return issues;
  }

  public void setIssues(List<String> issues) {
    this.issues = issues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecurityCategoryScore securityCategoryScore = (SecurityCategoryScore) o;
    return Objects.equals(this.score, securityCategoryScore.score) &&
        Objects.equals(this.status, securityCategoryScore.status) &&
        Objects.equals(this.issues, securityCategoryScore.issues);
  }

  @Override
  public int hashCode() {
    return Objects.hash(score, status, issues);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecurityCategoryScore {\n");
    sb.append("    score: ").append(toIndentedString(score)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    issues: ").append(toIndentedString(issues)).append("\n");
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

