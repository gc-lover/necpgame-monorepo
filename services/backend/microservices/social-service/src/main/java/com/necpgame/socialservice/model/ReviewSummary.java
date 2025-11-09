package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ReviewRatings;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
 * ReviewSummary
 */


public class ReviewSummary {

  private UUID playerId;

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

  private ReviewRatings average;

  private Integer count;

  @Valid
  private Map<String, Integer> flagsBreakdown = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdated;

  public ReviewSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewSummary(UUID playerId, RoleEnum role, ReviewRatings average, Integer count) {
    this.playerId = playerId;
    this.role = role;
    this.average = average;
    this.count = count;
  }

  public ReviewSummary playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public ReviewSummary role(RoleEnum role) {
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

  public ReviewSummary average(ReviewRatings average) {
    this.average = average;
    return this;
  }

  /**
   * Get average
   * @return average
   */
  @NotNull @Valid 
  @Schema(name = "average", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("average")
  public ReviewRatings getAverage() {
    return average;
  }

  public void setAverage(ReviewRatings average) {
    this.average = average;
  }

  public ReviewSummary count(Integer count) {
    this.count = count;
    return this;
  }

  /**
   * Get count
   * minimum: 0
   * @return count
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "count", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("count")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public ReviewSummary flagsBreakdown(Map<String, Integer> flagsBreakdown) {
    this.flagsBreakdown = flagsBreakdown;
    return this;
  }

  public ReviewSummary putFlagsBreakdownItem(String key, Integer flagsBreakdownItem) {
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

  public ReviewSummary lastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  /**
   * Get lastUpdated
   * @return lastUpdated
   */
  @Valid 
  @Schema(name = "lastUpdated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUpdated")
  public @Nullable OffsetDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewSummary reviewSummary = (ReviewSummary) o;
    return Objects.equals(this.playerId, reviewSummary.playerId) &&
        Objects.equals(this.role, reviewSummary.role) &&
        Objects.equals(this.average, reviewSummary.average) &&
        Objects.equals(this.count, reviewSummary.count) &&
        Objects.equals(this.flagsBreakdown, reviewSummary.flagsBreakdown) &&
        Objects.equals(this.lastUpdated, reviewSummary.lastUpdated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, role, average, count, flagsBreakdown, lastUpdated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewSummary {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    average: ").append(toIndentedString(average)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    flagsBreakdown: ").append(toIndentedString(flagsBreakdown)).append("\n");
    sb.append("    lastUpdated: ").append(toIndentedString(lastUpdated)).append("\n");
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

