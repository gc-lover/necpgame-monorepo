package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.LeaderboardEntry;
import com.necpgame.backjava.model.Season;
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
 * GetSeasonalLeaderboard200Response
 */

@JsonTypeName("getSeasonalLeaderboard_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetSeasonalLeaderboard200Response {

  private @Nullable String category;

  /**
   * Gets or Sets leaderboardType
   */
  public enum LeaderboardTypeEnum {
    GLOBAL("GLOBAL"),
    
    SEASONAL("SEASONAL"),
    
    FRIENDS("FRIENDS");

    private final String value;

    LeaderboardTypeEnum(String value) {
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
    public static LeaderboardTypeEnum fromValue(String value) {
      for (LeaderboardTypeEnum b : LeaderboardTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LeaderboardTypeEnum leaderboardType;

  @Valid
  private List<@Valid LeaderboardEntry> entries = new ArrayList<>();

  private @Nullable Integer totalEntries;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  private @Nullable Season season;

  public GetSeasonalLeaderboard200Response category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public GetSeasonalLeaderboard200Response leaderboardType(@Nullable LeaderboardTypeEnum leaderboardType) {
    this.leaderboardType = leaderboardType;
    return this;
  }

  /**
   * Get leaderboardType
   * @return leaderboardType
   */
  
  @Schema(name = "leaderboard_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leaderboard_type")
  public @Nullable LeaderboardTypeEnum getLeaderboardType() {
    return leaderboardType;
  }

  public void setLeaderboardType(@Nullable LeaderboardTypeEnum leaderboardType) {
    this.leaderboardType = leaderboardType;
  }

  public GetSeasonalLeaderboard200Response entries(List<@Valid LeaderboardEntry> entries) {
    this.entries = entries;
    return this;
  }

  public GetSeasonalLeaderboard200Response addEntriesItem(LeaderboardEntry entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

  /**
   * Get entries
   * @return entries
   */
  @Valid 
  @Schema(name = "entries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entries")
  public List<@Valid LeaderboardEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<@Valid LeaderboardEntry> entries) {
    this.entries = entries;
  }

  public GetSeasonalLeaderboard200Response totalEntries(@Nullable Integer totalEntries) {
    this.totalEntries = totalEntries;
    return this;
  }

  /**
   * Get totalEntries
   * @return totalEntries
   */
  
  @Schema(name = "total_entries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_entries")
  public @Nullable Integer getTotalEntries() {
    return totalEntries;
  }

  public void setTotalEntries(@Nullable Integer totalEntries) {
    this.totalEntries = totalEntries;
  }

  public GetSeasonalLeaderboard200Response updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updated_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updated_at")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public GetSeasonalLeaderboard200Response season(@Nullable Season season) {
    this.season = season;
    return this;
  }

  /**
   * Get season
   * @return season
   */
  @Valid 
  @Schema(name = "season", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("season")
  public @Nullable Season getSeason() {
    return season;
  }

  public void setSeason(@Nullable Season season) {
    this.season = season;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSeasonalLeaderboard200Response getSeasonalLeaderboard200Response = (GetSeasonalLeaderboard200Response) o;
    return Objects.equals(this.category, getSeasonalLeaderboard200Response.category) &&
        Objects.equals(this.leaderboardType, getSeasonalLeaderboard200Response.leaderboardType) &&
        Objects.equals(this.entries, getSeasonalLeaderboard200Response.entries) &&
        Objects.equals(this.totalEntries, getSeasonalLeaderboard200Response.totalEntries) &&
        Objects.equals(this.updatedAt, getSeasonalLeaderboard200Response.updatedAt) &&
        Objects.equals(this.season, getSeasonalLeaderboard200Response.season);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, leaderboardType, entries, totalEntries, updatedAt, season);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSeasonalLeaderboard200Response {\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    leaderboardType: ").append(toIndentedString(leaderboardType)).append("\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
    sb.append("    totalEntries: ").append(toIndentedString(totalEntries)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    season: ").append(toIndentedString(season)).append("\n");
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

