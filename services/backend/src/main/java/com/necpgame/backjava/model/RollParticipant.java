package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.RollParticipantBonuses;
import com.necpgame.backjava.model.RollSubmission;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RollParticipant
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RollParticipant {

  private @Nullable String playerId;

  private @Nullable String propertyClass;

  private @Nullable Boolean eligible;

  private @Nullable RollParticipantBonuses bonuses;

  private @Nullable RollSubmission submission;

  public RollParticipant playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public RollParticipant propertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
    return this;
  }

  /**
   * Get propertyClass
   * @return propertyClass
   */
  
  @Schema(name = "class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class")
  public @Nullable String getPropertyClass() {
    return propertyClass;
  }

  public void setPropertyClass(@Nullable String propertyClass) {
    this.propertyClass = propertyClass;
  }

  public RollParticipant eligible(@Nullable Boolean eligible) {
    this.eligible = eligible;
    return this;
  }

  /**
   * Get eligible
   * @return eligible
   */
  
  @Schema(name = "eligible", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eligible")
  public @Nullable Boolean getEligible() {
    return eligible;
  }

  public void setEligible(@Nullable Boolean eligible) {
    this.eligible = eligible;
  }

  public RollParticipant bonuses(@Nullable RollParticipantBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable RollParticipantBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable RollParticipantBonuses bonuses) {
    this.bonuses = bonuses;
  }

  public RollParticipant submission(@Nullable RollSubmission submission) {
    this.submission = submission;
    return this;
  }

  /**
   * Get submission
   * @return submission
   */
  @Valid 
  @Schema(name = "submission", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("submission")
  public @Nullable RollSubmission getSubmission() {
    return submission;
  }

  public void setSubmission(@Nullable RollSubmission submission) {
    this.submission = submission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RollParticipant rollParticipant = (RollParticipant) o;
    return Objects.equals(this.playerId, rollParticipant.playerId) &&
        Objects.equals(this.propertyClass, rollParticipant.propertyClass) &&
        Objects.equals(this.eligible, rollParticipant.eligible) &&
        Objects.equals(this.bonuses, rollParticipant.bonuses) &&
        Objects.equals(this.submission, rollParticipant.submission);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, propertyClass, eligible, bonuses, submission);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollParticipant {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    propertyClass: ").append(toIndentedString(propertyClass)).append("\n");
    sb.append("    eligible: ").append(toIndentedString(eligible)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    submission: ").append(toIndentedString(submission)).append("\n");
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

