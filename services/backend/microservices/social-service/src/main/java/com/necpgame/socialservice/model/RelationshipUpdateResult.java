package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.MoodState;
import com.necpgame.socialservice.model.RelationshipSummary;
import com.necpgame.socialservice.model.WorldPulseLink;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RelationshipUpdateResult
 */


public class RelationshipUpdateResult {

  private RelationshipSummary relationship;

  private Float trustDelta;

  private @Nullable MoodState moodStateAfter;

  private WorldPulseLink worldPulseImpact;

  private @Nullable Boolean crisisAlertRaised;

  public RelationshipUpdateResult() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RelationshipUpdateResult(RelationshipSummary relationship, Float trustDelta, WorldPulseLink worldPulseImpact) {
    this.relationship = relationship;
    this.trustDelta = trustDelta;
    this.worldPulseImpact = worldPulseImpact;
  }

  public RelationshipUpdateResult relationship(RelationshipSummary relationship) {
    this.relationship = relationship;
    return this;
  }

  /**
   * Get relationship
   * @return relationship
   */
  @NotNull @Valid 
  @Schema(name = "relationship", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("relationship")
  public RelationshipSummary getRelationship() {
    return relationship;
  }

  public void setRelationship(RelationshipSummary relationship) {
    this.relationship = relationship;
  }

  public RelationshipUpdateResult trustDelta(Float trustDelta) {
    this.trustDelta = trustDelta;
    return this;
  }

  /**
   * Get trustDelta
   * @return trustDelta
   */
  @NotNull 
  @Schema(name = "trustDelta", example = "1.4", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trustDelta")
  public Float getTrustDelta() {
    return trustDelta;
  }

  public void setTrustDelta(Float trustDelta) {
    this.trustDelta = trustDelta;
  }

  public RelationshipUpdateResult moodStateAfter(@Nullable MoodState moodStateAfter) {
    this.moodStateAfter = moodStateAfter;
    return this;
  }

  /**
   * Get moodStateAfter
   * @return moodStateAfter
   */
  @Valid 
  @Schema(name = "moodStateAfter", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("moodStateAfter")
  public @Nullable MoodState getMoodStateAfter() {
    return moodStateAfter;
  }

  public void setMoodStateAfter(@Nullable MoodState moodStateAfter) {
    this.moodStateAfter = moodStateAfter;
  }

  public RelationshipUpdateResult worldPulseImpact(WorldPulseLink worldPulseImpact) {
    this.worldPulseImpact = worldPulseImpact;
    return this;
  }

  /**
   * Get worldPulseImpact
   * @return worldPulseImpact
   */
  @NotNull @Valid 
  @Schema(name = "worldPulseImpact", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("worldPulseImpact")
  public WorldPulseLink getWorldPulseImpact() {
    return worldPulseImpact;
  }

  public void setWorldPulseImpact(WorldPulseLink worldPulseImpact) {
    this.worldPulseImpact = worldPulseImpact;
  }

  public RelationshipUpdateResult crisisAlertRaised(@Nullable Boolean crisisAlertRaised) {
    this.crisisAlertRaised = crisisAlertRaised;
    return this;
  }

  /**
   * Get crisisAlertRaised
   * @return crisisAlertRaised
   */
  
  @Schema(name = "crisisAlertRaised", example = "false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("crisisAlertRaised")
  public @Nullable Boolean getCrisisAlertRaised() {
    return crisisAlertRaised;
  }

  public void setCrisisAlertRaised(@Nullable Boolean crisisAlertRaised) {
    this.crisisAlertRaised = crisisAlertRaised;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RelationshipUpdateResult relationshipUpdateResult = (RelationshipUpdateResult) o;
    return Objects.equals(this.relationship, relationshipUpdateResult.relationship) &&
        Objects.equals(this.trustDelta, relationshipUpdateResult.trustDelta) &&
        Objects.equals(this.moodStateAfter, relationshipUpdateResult.moodStateAfter) &&
        Objects.equals(this.worldPulseImpact, relationshipUpdateResult.worldPulseImpact) &&
        Objects.equals(this.crisisAlertRaised, relationshipUpdateResult.crisisAlertRaised);
  }

  @Override
  public int hashCode() {
    return Objects.hash(relationship, trustDelta, moodStateAfter, worldPulseImpact, crisisAlertRaised);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RelationshipUpdateResult {\n");
    sb.append("    relationship: ").append(toIndentedString(relationship)).append("\n");
    sb.append("    trustDelta: ").append(toIndentedString(trustDelta)).append("\n");
    sb.append("    moodStateAfter: ").append(toIndentedString(moodStateAfter)).append("\n");
    sb.append("    worldPulseImpact: ").append(toIndentedString(worldPulseImpact)).append("\n");
    sb.append("    crisisAlertRaised: ").append(toIndentedString(crisisAlertRaised)).append("\n");
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

