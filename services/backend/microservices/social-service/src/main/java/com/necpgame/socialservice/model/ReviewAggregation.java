package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ReviewRatings;
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
 * ReviewAggregation
 */


public class ReviewAggregation {

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    EXECUTOR("executor"),
    
    CLIENT("client");

    private final String value;

    RoleEnum(String value) {
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
    public static RoleEnum fromValue(String value) {
      for (RoleEnum b : RoleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RoleEnum role;

  @Valid
  private Map<String, Integer> totals = new HashMap<>();

  private ReviewRatings averages;

  @Valid
  private Map<String, Integer> flagsBreakdown = new HashMap<>();

  private @Nullable Float sentimentScore;

  public ReviewAggregation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewAggregation(RoleEnum role, Map<String, Integer> totals, ReviewRatings averages) {
    this.role = role;
    this.totals = totals;
    this.averages = averages;
  }

  public ReviewAggregation role(RoleEnum role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public RoleEnum getRole() {
    return role;
  }

  public void setRole(RoleEnum role) {
    this.role = role;
  }

  public ReviewAggregation totals(Map<String, Integer> totals) {
    this.totals = totals;
    return this;
  }

  public ReviewAggregation putTotalsItem(String key, Integer totalsItem) {
    if (this.totals == null) {
      this.totals = new HashMap<>();
    }
    this.totals.put(key, totalsItem);
    return this;
  }

  /**
   * Get totals
   * @return totals
   */
  @NotNull 
  @Schema(name = "totals", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totals")
  public Map<String, Integer> getTotals() {
    return totals;
  }

  public void setTotals(Map<String, Integer> totals) {
    this.totals = totals;
  }

  public ReviewAggregation averages(ReviewRatings averages) {
    this.averages = averages;
    return this;
  }

  /**
   * Get averages
   * @return averages
   */
  @NotNull @Valid 
  @Schema(name = "averages", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("averages")
  public ReviewRatings getAverages() {
    return averages;
  }

  public void setAverages(ReviewRatings averages) {
    this.averages = averages;
  }

  public ReviewAggregation flagsBreakdown(Map<String, Integer> flagsBreakdown) {
    this.flagsBreakdown = flagsBreakdown;
    return this;
  }

  public ReviewAggregation putFlagsBreakdownItem(String key, Integer flagsBreakdownItem) {
    if (this.flagsBreakdown == null) {
      this.flagsBreakdown = new HashMap<>();
    }
    this.flagsBreakdown.put(key, flagsBreakdownItem);
    return this;
  }

  /**
   * Get flagsBreakdown
   * @return flagsBreakdown
   */
  
  @Schema(name = "flagsBreakdown", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flagsBreakdown")
  public Map<String, Integer> getFlagsBreakdown() {
    return flagsBreakdown;
  }

  public void setFlagsBreakdown(Map<String, Integer> flagsBreakdown) {
    this.flagsBreakdown = flagsBreakdown;
  }

  public ReviewAggregation sentimentScore(@Nullable Float sentimentScore) {
    this.sentimentScore = sentimentScore;
    return this;
  }

  /**
   * Get sentimentScore
   * @return sentimentScore
   */
  
  @Schema(name = "sentimentScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sentimentScore")
  public @Nullable Float getSentimentScore() {
    return sentimentScore;
  }

  public void setSentimentScore(@Nullable Float sentimentScore) {
    this.sentimentScore = sentimentScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewAggregation reviewAggregation = (ReviewAggregation) o;
    return Objects.equals(this.role, reviewAggregation.role) &&
        Objects.equals(this.totals, reviewAggregation.totals) &&
        Objects.equals(this.averages, reviewAggregation.averages) &&
        Objects.equals(this.flagsBreakdown, reviewAggregation.flagsBreakdown) &&
        Objects.equals(this.sentimentScore, reviewAggregation.sentimentScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(role, totals, averages, flagsBreakdown, sentimentScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewAggregation {\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    totals: ").append(toIndentedString(totals)).append("\n");
    sb.append("    averages: ").append(toIndentedString(averages)).append("\n");
    sb.append("    flagsBreakdown: ").append(toIndentedString(flagsBreakdown)).append("\n");
    sb.append("    sentimentScore: ").append(toIndentedString(sentimentScore)).append("\n");
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

