package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.RelationshipSummary;
import com.necpgame.socialservice.model.WorldPulseLink;
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
 * SocialRelationshipUpdatedEvent
 */


public class SocialRelationshipUpdatedEvent {

  private String eventId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime occurredAt;

  private RelationshipSummary relationship;

  private Float delta;

  private @Nullable WorldPulseLink worldPulse;

  private @Nullable Boolean crisisAlertRaised;

  public SocialRelationshipUpdatedEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialRelationshipUpdatedEvent(String eventId, OffsetDateTime occurredAt, RelationshipSummary relationship, Float delta) {
    this.eventId = eventId;
    this.occurredAt = occurredAt;
    this.relationship = relationship;
    this.delta = delta;
  }

  public SocialRelationshipUpdatedEvent eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public SocialRelationshipUpdatedEvent occurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @NotNull @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("occurredAt")
  public OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  public SocialRelationshipUpdatedEvent relationship(RelationshipSummary relationship) {
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

  public SocialRelationshipUpdatedEvent delta(Float delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  @NotNull 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Float getDelta() {
    return delta;
  }

  public void setDelta(Float delta) {
    this.delta = delta;
  }

  public SocialRelationshipUpdatedEvent worldPulse(@Nullable WorldPulseLink worldPulse) {
    this.worldPulse = worldPulse;
    return this;
  }

  /**
   * Get worldPulse
   * @return worldPulse
   */
  @Valid 
  @Schema(name = "worldPulse", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worldPulse")
  public @Nullable WorldPulseLink getWorldPulse() {
    return worldPulse;
  }

  public void setWorldPulse(@Nullable WorldPulseLink worldPulse) {
    this.worldPulse = worldPulse;
  }

  public SocialRelationshipUpdatedEvent crisisAlertRaised(@Nullable Boolean crisisAlertRaised) {
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
    SocialRelationshipUpdatedEvent socialRelationshipUpdatedEvent = (SocialRelationshipUpdatedEvent) o;
    return Objects.equals(this.eventId, socialRelationshipUpdatedEvent.eventId) &&
        Objects.equals(this.occurredAt, socialRelationshipUpdatedEvent.occurredAt) &&
        Objects.equals(this.relationship, socialRelationshipUpdatedEvent.relationship) &&
        Objects.equals(this.delta, socialRelationshipUpdatedEvent.delta) &&
        Objects.equals(this.worldPulse, socialRelationshipUpdatedEvent.worldPulse) &&
        Objects.equals(this.crisisAlertRaised, socialRelationshipUpdatedEvent.crisisAlertRaised);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, occurredAt, relationship, delta, worldPulse, crisisAlertRaised);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialRelationshipUpdatedEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
    sb.append("    relationship: ").append(toIndentedString(relationship)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    worldPulse: ").append(toIndentedString(worldPulse)).append("\n");
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

