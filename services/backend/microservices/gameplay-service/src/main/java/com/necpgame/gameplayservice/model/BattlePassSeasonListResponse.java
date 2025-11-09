package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.BattlePassSeason;
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
 * BattlePassSeasonListResponse
 */


public class BattlePassSeasonListResponse {

  @Valid
  private List<@Valid BattlePassSeason> seasons = new ArrayList<>();

  public BattlePassSeasonListResponse seasons(List<@Valid BattlePassSeason> seasons) {
    this.seasons = seasons;
    return this;
  }

  public BattlePassSeasonListResponse addSeasonsItem(BattlePassSeason seasonsItem) {
    if (this.seasons == null) {
      this.seasons = new ArrayList<>();
    }
    this.seasons.add(seasonsItem);
    return this;
  }

  /**
   * Get seasons
   * @return seasons
   */
  @Valid 
  @Schema(name = "seasons", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasons")
  public List<@Valid BattlePassSeason> getSeasons() {
    return seasons;
  }

  public void setSeasons(List<@Valid BattlePassSeason> seasons) {
    this.seasons = seasons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BattlePassSeasonListResponse battlePassSeasonListResponse = (BattlePassSeasonListResponse) o;
    return Objects.equals(this.seasons, battlePassSeasonListResponse.seasons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seasons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BattlePassSeasonListResponse {\n");
    sb.append("    seasons: ").append(toIndentedString(seasons)).append("\n");
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

