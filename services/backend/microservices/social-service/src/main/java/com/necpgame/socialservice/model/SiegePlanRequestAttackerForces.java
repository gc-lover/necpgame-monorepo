package com.necpgame.socialservice.model;

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
 * SiegePlanRequestAttackerForces
 */

@JsonTypeName("SiegePlanRequest_attackerForces")

public class SiegePlanRequestAttackerForces {

  private @Nullable Integer maxParticipants;

  private @Nullable Integer requiredGearScore;

  public SiegePlanRequestAttackerForces maxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
    return this;
  }

  /**
   * Get maxParticipants
   * @return maxParticipants
   */
  
  @Schema(name = "maxParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxParticipants")
  public @Nullable Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public SiegePlanRequestAttackerForces requiredGearScore(@Nullable Integer requiredGearScore) {
    this.requiredGearScore = requiredGearScore;
    return this;
  }

  /**
   * Get requiredGearScore
   * @return requiredGearScore
   */
  
  @Schema(name = "requiredGearScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requiredGearScore")
  public @Nullable Integer getRequiredGearScore() {
    return requiredGearScore;
  }

  public void setRequiredGearScore(@Nullable Integer requiredGearScore) {
    this.requiredGearScore = requiredGearScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SiegePlanRequestAttackerForces siegePlanRequestAttackerForces = (SiegePlanRequestAttackerForces) o;
    return Objects.equals(this.maxParticipants, siegePlanRequestAttackerForces.maxParticipants) &&
        Objects.equals(this.requiredGearScore, siegePlanRequestAttackerForces.requiredGearScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxParticipants, requiredGearScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SiegePlanRequestAttackerForces {\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    requiredGearScore: ").append(toIndentedString(requiredGearScore)).append("\n");
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

