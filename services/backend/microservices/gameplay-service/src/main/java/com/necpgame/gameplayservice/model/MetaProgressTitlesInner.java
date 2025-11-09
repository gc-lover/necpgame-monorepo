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
 * MetaProgressTitlesInner
 */

@JsonTypeName("MetaProgress_titles_inner")

public class MetaProgressTitlesInner {

  private @Nullable String titleId;

  private @Nullable String name;

  private @Nullable String leagueEarned;

  public MetaProgressTitlesInner titleId(@Nullable String titleId) {
    this.titleId = titleId;
    return this;
  }

  /**
   * Get titleId
   * @return titleId
   */
  
  @Schema(name = "title_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title_id")
  public @Nullable String getTitleId() {
    return titleId;
  }

  public void setTitleId(@Nullable String titleId) {
    this.titleId = titleId;
  }

  public MetaProgressTitlesInner name(@Nullable String name) {
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

  public MetaProgressTitlesInner leagueEarned(@Nullable String leagueEarned) {
    this.leagueEarned = leagueEarned;
    return this;
  }

  /**
   * Get leagueEarned
   * @return leagueEarned
   */
  
  @Schema(name = "league_earned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("league_earned")
  public @Nullable String getLeagueEarned() {
    return leagueEarned;
  }

  public void setLeagueEarned(@Nullable String leagueEarned) {
    this.leagueEarned = leagueEarned;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MetaProgressTitlesInner metaProgressTitlesInner = (MetaProgressTitlesInner) o;
    return Objects.equals(this.titleId, metaProgressTitlesInner.titleId) &&
        Objects.equals(this.name, metaProgressTitlesInner.name) &&
        Objects.equals(this.leagueEarned, metaProgressTitlesInner.leagueEarned);
  }

  @Override
  public int hashCode() {
    return Objects.hash(titleId, name, leagueEarned);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MetaProgressTitlesInner {\n");
    sb.append("    titleId: ").append(toIndentedString(titleId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    leagueEarned: ").append(toIndentedString(leagueEarned)).append("\n");
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

