package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ResonanceDimension;
import com.necpgame.socialservice.model.WorldPulseLink;
import java.time.OffsetDateTime;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SocialIndexChangedEvent
 */


public class SocialIndexChangedEvent {

  private String eventId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime occurredAt;

  private Float trustIndex;

  private Float delta;

  private String reason;

  private ResonanceDimension source;

  private @Nullable WorldPulseLink worldPulse;

  private JsonNullable<String> triggeredByCampaign = JsonNullable.<String>undefined();

  private JsonNullable<String> triggeredByRelationship = JsonNullable.<String>undefined();

  public SocialIndexChangedEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SocialIndexChangedEvent(String eventId, OffsetDateTime occurredAt, Float trustIndex, Float delta, String reason, ResonanceDimension source) {
    this.eventId = eventId;
    this.occurredAt = occurredAt;
    this.trustIndex = trustIndex;
    this.delta = delta;
    this.reason = reason;
    this.source = source;
  }

  public SocialIndexChangedEvent eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull 
  @Schema(name = "eventId", example = "evt-res-8812", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  public SocialIndexChangedEvent occurredAt(OffsetDateTime occurredAt) {
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

  public SocialIndexChangedEvent trustIndex(Float trustIndex) {
    this.trustIndex = trustIndex;
    return this;
  }

  /**
   * Get trustIndex
   * @return trustIndex
   */
  @NotNull 
  @Schema(name = "trustIndex", example = "63.5", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trustIndex")
  public Float getTrustIndex() {
    return trustIndex;
  }

  public void setTrustIndex(Float trustIndex) {
    this.trustIndex = trustIndex;
  }

  public SocialIndexChangedEvent delta(Float delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  @NotNull 
  @Schema(name = "delta", example = "1.7", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Float getDelta() {
    return delta;
  }

  public void setDelta(Float delta) {
    this.delta = delta;
  }

  public SocialIndexChangedEvent reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", example = "Community outreach success", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public SocialIndexChangedEvent source(ResonanceDimension source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull @Valid 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public ResonanceDimension getSource() {
    return source;
  }

  public void setSource(ResonanceDimension source) {
    this.source = source;
  }

  public SocialIndexChangedEvent worldPulse(@Nullable WorldPulseLink worldPulse) {
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

  public SocialIndexChangedEvent triggeredByCampaign(String triggeredByCampaign) {
    this.triggeredByCampaign = JsonNullable.of(triggeredByCampaign);
    return this;
  }

  /**
   * Get triggeredByCampaign
   * @return triggeredByCampaign
   */
  
  @Schema(name = "triggeredByCampaign", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggeredByCampaign")
  public JsonNullable<String> getTriggeredByCampaign() {
    return triggeredByCampaign;
  }

  public void setTriggeredByCampaign(JsonNullable<String> triggeredByCampaign) {
    this.triggeredByCampaign = triggeredByCampaign;
  }

  public SocialIndexChangedEvent triggeredByRelationship(String triggeredByRelationship) {
    this.triggeredByRelationship = JsonNullable.of(triggeredByRelationship);
    return this;
  }

  /**
   * Get triggeredByRelationship
   * @return triggeredByRelationship
   */
  
  @Schema(name = "triggeredByRelationship", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("triggeredByRelationship")
  public JsonNullable<String> getTriggeredByRelationship() {
    return triggeredByRelationship;
  }

  public void setTriggeredByRelationship(JsonNullable<String> triggeredByRelationship) {
    this.triggeredByRelationship = triggeredByRelationship;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SocialIndexChangedEvent socialIndexChangedEvent = (SocialIndexChangedEvent) o;
    return Objects.equals(this.eventId, socialIndexChangedEvent.eventId) &&
        Objects.equals(this.occurredAt, socialIndexChangedEvent.occurredAt) &&
        Objects.equals(this.trustIndex, socialIndexChangedEvent.trustIndex) &&
        Objects.equals(this.delta, socialIndexChangedEvent.delta) &&
        Objects.equals(this.reason, socialIndexChangedEvent.reason) &&
        Objects.equals(this.source, socialIndexChangedEvent.source) &&
        Objects.equals(this.worldPulse, socialIndexChangedEvent.worldPulse) &&
        equalsNullable(this.triggeredByCampaign, socialIndexChangedEvent.triggeredByCampaign) &&
        equalsNullable(this.triggeredByRelationship, socialIndexChangedEvent.triggeredByRelationship);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, occurredAt, trustIndex, delta, reason, source, worldPulse, hashCodeNullable(triggeredByCampaign), hashCodeNullable(triggeredByRelationship));
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SocialIndexChangedEvent {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
    sb.append("    trustIndex: ").append(toIndentedString(trustIndex)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    worldPulse: ").append(toIndentedString(worldPulse)).append("\n");
    sb.append("    triggeredByCampaign: ").append(toIndentedString(triggeredByCampaign)).append("\n");
    sb.append("    triggeredByRelationship: ").append(toIndentedString(triggeredByRelationship)).append("\n");
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

