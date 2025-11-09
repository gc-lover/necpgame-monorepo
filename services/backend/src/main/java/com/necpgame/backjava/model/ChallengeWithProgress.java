package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.Challenge;
import com.necpgame.backjava.model.ChallengeProgress;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ChallengeWithProgress
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ChallengeWithProgress {

  private @Nullable Challenge challenge;

  private @Nullable ChallengeProgress progress;

  public ChallengeWithProgress challenge(@Nullable Challenge challenge) {
    this.challenge = challenge;
    return this;
  }

  /**
   * Get challenge
   * @return challenge
   */
  @Valid 
  @Schema(name = "challenge", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("challenge")
  public @Nullable Challenge getChallenge() {
    return challenge;
  }

  public void setChallenge(@Nullable Challenge challenge) {
    this.challenge = challenge;
  }

  public ChallengeWithProgress progress(@Nullable ChallengeProgress progress) {
    this.progress = progress;
    return this;
  }

  /**
   * Get progress
   * @return progress
   */
  @Valid 
  @Schema(name = "progress", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progress")
  public @Nullable ChallengeProgress getProgress() {
    return progress;
  }

  public void setProgress(@Nullable ChallengeProgress progress) {
    this.progress = progress;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChallengeWithProgress challengeWithProgress = (ChallengeWithProgress) o;
    return Objects.equals(this.challenge, challengeWithProgress.challenge) &&
        Objects.equals(this.progress, challengeWithProgress.progress);
  }

  @Override
  public int hashCode() {
    return Objects.hash(challenge, progress);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChallengeWithProgress {\n");
    sb.append("    challenge: ").append(toIndentedString(challenge)).append("\n");
    sb.append("    progress: ").append(toIndentedString(progress)).append("\n");
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

