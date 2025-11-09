package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
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
 * AdvanceBlackwallPhase200Response
 */

@JsonTypeName("advanceBlackwallPhase_200_response")

public class AdvanceBlackwallPhase200Response {

  private @Nullable String raidId;

  private @Nullable String previousPhase;

  private @Nullable String newPhase;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime phaseStartedAt;

  public AdvanceBlackwallPhase200Response raidId(@Nullable String raidId) {
    this.raidId = raidId;
    return this;
  }

  /**
   * Get raidId
   * @return raidId
   */
  
  @Schema(name = "raid_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("raid_id")
  public @Nullable String getRaidId() {
    return raidId;
  }

  public void setRaidId(@Nullable String raidId) {
    this.raidId = raidId;
  }

  public AdvanceBlackwallPhase200Response previousPhase(@Nullable String previousPhase) {
    this.previousPhase = previousPhase;
    return this;
  }

  /**
   * Get previousPhase
   * @return previousPhase
   */
  
  @Schema(name = "previous_phase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previous_phase")
  public @Nullable String getPreviousPhase() {
    return previousPhase;
  }

  public void setPreviousPhase(@Nullable String previousPhase) {
    this.previousPhase = previousPhase;
  }

  public AdvanceBlackwallPhase200Response newPhase(@Nullable String newPhase) {
    this.newPhase = newPhase;
    return this;
  }

  /**
   * Get newPhase
   * @return newPhase
   */
  
  @Schema(name = "new_phase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("new_phase")
  public @Nullable String getNewPhase() {
    return newPhase;
  }

  public void setNewPhase(@Nullable String newPhase) {
    this.newPhase = newPhase;
  }

  public AdvanceBlackwallPhase200Response phaseStartedAt(@Nullable OffsetDateTime phaseStartedAt) {
    this.phaseStartedAt = phaseStartedAt;
    return this;
  }

  /**
   * Get phaseStartedAt
   * @return phaseStartedAt
   */
  @Valid 
  @Schema(name = "phase_started_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase_started_at")
  public @Nullable OffsetDateTime getPhaseStartedAt() {
    return phaseStartedAt;
  }

  public void setPhaseStartedAt(@Nullable OffsetDateTime phaseStartedAt) {
    this.phaseStartedAt = phaseStartedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdvanceBlackwallPhase200Response advanceBlackwallPhase200Response = (AdvanceBlackwallPhase200Response) o;
    return Objects.equals(this.raidId, advanceBlackwallPhase200Response.raidId) &&
        Objects.equals(this.previousPhase, advanceBlackwallPhase200Response.previousPhase) &&
        Objects.equals(this.newPhase, advanceBlackwallPhase200Response.newPhase) &&
        Objects.equals(this.phaseStartedAt, advanceBlackwallPhase200Response.phaseStartedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(raidId, previousPhase, newPhase, phaseStartedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdvanceBlackwallPhase200Response {\n");
    sb.append("    raidId: ").append(toIndentedString(raidId)).append("\n");
    sb.append("    previousPhase: ").append(toIndentedString(previousPhase)).append("\n");
    sb.append("    newPhase: ").append(toIndentedString(newPhase)).append("\n");
    sb.append("    phaseStartedAt: ").append(toIndentedString(phaseStartedAt)).append("\n");
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

