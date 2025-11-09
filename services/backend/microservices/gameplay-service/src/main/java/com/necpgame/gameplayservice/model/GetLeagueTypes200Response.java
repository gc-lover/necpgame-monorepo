package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.LeagueType;
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
 * GetLeagueTypes200Response
 */

@JsonTypeName("getLeagueTypes_200_response")

public class GetLeagueTypes200Response {

  @Valid
  private List<@Valid LeagueType> leagueTypes = new ArrayList<>();

  public GetLeagueTypes200Response leagueTypes(List<@Valid LeagueType> leagueTypes) {
    this.leagueTypes = leagueTypes;
    return this;
  }

  public GetLeagueTypes200Response addLeagueTypesItem(LeagueType leagueTypesItem) {
    if (this.leagueTypes == null) {
      this.leagueTypes = new ArrayList<>();
    }
    this.leagueTypes.add(leagueTypesItem);
    return this;
  }

  /**
   * Get leagueTypes
   * @return leagueTypes
   */
  @Valid 
  @Schema(name = "league_types", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("league_types")
  public List<@Valid LeagueType> getLeagueTypes() {
    return leagueTypes;
  }

  public void setLeagueTypes(List<@Valid LeagueType> leagueTypes) {
    this.leagueTypes = leagueTypes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetLeagueTypes200Response getLeagueTypes200Response = (GetLeagueTypes200Response) o;
    return Objects.equals(this.leagueTypes, getLeagueTypes200Response.leagueTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leagueTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetLeagueTypes200Response {\n");
    sb.append("    leagueTypes: ").append(toIndentedString(leagueTypes)).append("\n");
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

