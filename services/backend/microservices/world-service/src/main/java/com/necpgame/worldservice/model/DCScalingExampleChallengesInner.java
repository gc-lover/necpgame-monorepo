package com.necpgame.worldservice.model;

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
 * DCScalingExampleChallengesInner
 */

@JsonTypeName("DCScaling_example_challenges_inner")

public class DCScalingExampleChallengesInner {

  private @Nullable String challenge;

  private @Nullable Integer dc;

  public DCScalingExampleChallengesInner challenge(@Nullable String challenge) {
    this.challenge = challenge;
    return this;
  }

  /**
   * Get challenge
   * @return challenge
   */
  
  @Schema(name = "challenge", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("challenge")
  public @Nullable String getChallenge() {
    return challenge;
  }

  public void setChallenge(@Nullable String challenge) {
    this.challenge = challenge;
  }

  public DCScalingExampleChallengesInner dc(@Nullable Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Get dc
   * @return dc
   */
  
  @Schema(name = "dc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dc")
  public @Nullable Integer getDc() {
    return dc;
  }

  public void setDc(@Nullable Integer dc) {
    this.dc = dc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DCScalingExampleChallengesInner dcScalingExampleChallengesInner = (DCScalingExampleChallengesInner) o;
    return Objects.equals(this.challenge, dcScalingExampleChallengesInner.challenge) &&
        Objects.equals(this.dc, dcScalingExampleChallengesInner.dc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(challenge, dc);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DCScalingExampleChallengesInner {\n");
    sb.append("    challenge: ").append(toIndentedString(challenge)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
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

