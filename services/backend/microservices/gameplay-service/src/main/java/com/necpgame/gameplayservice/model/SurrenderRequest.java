package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.SurrenderRequestVotesInner;
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
 * SurrenderRequest
 */


public class SurrenderRequest {

  private @Nullable String teamId;

  @Valid
  private List<@Valid SurrenderRequestVotesInner> votes = new ArrayList<>();

  public SurrenderRequest teamId(@Nullable String teamId) {
    this.teamId = teamId;
    return this;
  }

  /**
   * Get teamId
   * @return teamId
   */
  
  @Schema(name = "teamId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("teamId")
  public @Nullable String getTeamId() {
    return teamId;
  }

  public void setTeamId(@Nullable String teamId) {
    this.teamId = teamId;
  }

  public SurrenderRequest votes(List<@Valid SurrenderRequestVotesInner> votes) {
    this.votes = votes;
    return this;
  }

  public SurrenderRequest addVotesItem(SurrenderRequestVotesInner votesItem) {
    if (this.votes == null) {
      this.votes = new ArrayList<>();
    }
    this.votes.add(votesItem);
    return this;
  }

  /**
   * Get votes
   * @return votes
   */
  @Valid 
  @Schema(name = "votes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("votes")
  public List<@Valid SurrenderRequestVotesInner> getVotes() {
    return votes;
  }

  public void setVotes(List<@Valid SurrenderRequestVotesInner> votes) {
    this.votes = votes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SurrenderRequest surrenderRequest = (SurrenderRequest) o;
    return Objects.equals(this.teamId, surrenderRequest.teamId) &&
        Objects.equals(this.votes, surrenderRequest.votes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(teamId, votes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SurrenderRequest {\n");
    sb.append("    teamId: ").append(toIndentedString(teamId)).append("\n");
    sb.append("    votes: ").append(toIndentedString(votes)).append("\n");
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

