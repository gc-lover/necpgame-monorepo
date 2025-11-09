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
 * MetaProgressHallOfFameInner
 */

@JsonTypeName("MetaProgress_hall_of_fame_inner")

public class MetaProgressHallOfFameInner {

  private @Nullable String leagueId;

  private @Nullable Integer rank;

  private @Nullable String category;

  public MetaProgressHallOfFameInner leagueId(@Nullable String leagueId) {
    this.leagueId = leagueId;
    return this;
  }

  /**
   * Get leagueId
   * @return leagueId
   */
  
  @Schema(name = "league_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("league_id")
  public @Nullable String getLeagueId() {
    return leagueId;
  }

  public void setLeagueId(@Nullable String leagueId) {
    this.leagueId = leagueId;
  }

  public MetaProgressHallOfFameInner rank(@Nullable Integer rank) {
    this.rank = rank;
    return this;
  }

  /**
   * Get rank
   * @return rank
   */
  
  @Schema(name = "rank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rank")
  public @Nullable Integer getRank() {
    return rank;
  }

  public void setRank(@Nullable Integer rank) {
    this.rank = rank;
  }

  public MetaProgressHallOfFameInner category(@Nullable String category) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaProgressHallOfFameInner metaProgressHallOfFameInner = (MetaProgressHallOfFameInner) o;
    return Objects.equals(this.leagueId, metaProgressHallOfFameInner.leagueId) &&
        Objects.equals(this.rank, metaProgressHallOfFameInner.rank) &&
        Objects.equals(this.category, metaProgressHallOfFameInner.category);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leagueId, rank, category);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetaProgressHallOfFameInner {\n");
    sb.append("    leagueId: ").append(toIndentedString(leagueId)).append("\n");
    sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
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

