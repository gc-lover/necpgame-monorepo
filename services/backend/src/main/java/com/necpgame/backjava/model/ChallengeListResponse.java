package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.ChallengeWithProgress;
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
 * ChallengeListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ChallengeListResponse {

  @Valid
  private List<@Valid ChallengeWithProgress> challenges = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime refreshAt;

  public ChallengeListResponse challenges(List<@Valid ChallengeWithProgress> challenges) {
    this.challenges = challenges;
    return this;
  }

  public ChallengeListResponse addChallengesItem(ChallengeWithProgress challengesItem) {
    if (this.challenges == null) {
      this.challenges = new ArrayList<>();
    }
    this.challenges.add(challengesItem);
    return this;
  }

  /**
   * Get challenges
   * @return challenges
   */
  @Valid 
  @Schema(name = "challenges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("challenges")
  public List<@Valid ChallengeWithProgress> getChallenges() {
    return challenges;
  }

  public void setChallenges(List<@Valid ChallengeWithProgress> challenges) {
    this.challenges = challenges;
  }

  public ChallengeListResponse refreshAt(@Nullable OffsetDateTime refreshAt) {
    this.refreshAt = refreshAt;
    return this;
  }

  /**
   * Get refreshAt
   * @return refreshAt
   */
  @Valid 
  @Schema(name = "refreshAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refreshAt")
  public @Nullable OffsetDateTime getRefreshAt() {
    return refreshAt;
  }

  public void setRefreshAt(@Nullable OffsetDateTime refreshAt) {
    this.refreshAt = refreshAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChallengeListResponse challengeListResponse = (ChallengeListResponse) o;
    return Objects.equals(this.challenges, challengeListResponse.challenges) &&
        Objects.equals(this.refreshAt, challengeListResponse.refreshAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(challenges, refreshAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeListResponse {\n");
    sb.append("    challenges: ").append(toIndentedString(challenges)).append("\n");
    sb.append("    refreshAt: ").append(toIndentedString(refreshAt)).append("\n");
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

