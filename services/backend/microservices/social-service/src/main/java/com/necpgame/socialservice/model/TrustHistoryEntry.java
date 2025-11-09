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
 * TrustHistoryEntry
 */


public class TrustHistoryEntry {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime timestamp;

  private Float trustIndex;

  private Float delta;

  private String reason;

  private ResonanceDimension source;

  private JsonNullable<String> campaignId = JsonNullable.<String>undefined();

  private JsonNullable<String> relationshipId = JsonNullable.<String>undefined();

  private @Nullable WorldPulseLink worldPulseSnapshot;

  public TrustHistoryEntry() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TrustHistoryEntry(OffsetDateTime timestamp, Float trustIndex, Float delta, String reason, ResonanceDimension source) {
    this.timestamp = timestamp;
    this.trustIndex = trustIndex;
    this.delta = delta;
    this.reason = reason;
    this.source = source;
  }

  public TrustHistoryEntry timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @NotNull @Valid 
  @Schema(name = "timestamp", example = "2077-05-18T11:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public TrustHistoryEntry trustIndex(Float trustIndex) {
    this.trustIndex = trustIndex;
    return this;
  }

  /**
   * Get trustIndex
   * minimum: 0
   * maximum: 100
   * @return trustIndex
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "trustIndex", example = "63.0", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trustIndex")
  public Float getTrustIndex() {
    return trustIndex;
  }

  public void setTrustIndex(Float trustIndex) {
    this.trustIndex = trustIndex;
  }

  public TrustHistoryEntry delta(Float delta) {
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

  public TrustHistoryEntry reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public TrustHistoryEntry source(ResonanceDimension source) {
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

  public TrustHistoryEntry campaignId(String campaignId) {
    this.campaignId = JsonNullable.of(campaignId);
    return this;
  }

  /**
   * Get campaignId
   * @return campaignId
   */
  
  @Schema(name = "campaignId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("campaignId")
  public JsonNullable<String> getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(JsonNullable<String> campaignId) {
    this.campaignId = campaignId;
  }

  public TrustHistoryEntry relationshipId(String relationshipId) {
    this.relationshipId = JsonNullable.of(relationshipId);
    return this;
  }

  /**
   * Get relationshipId
   * @return relationshipId
   */
  
  @Schema(name = "relationshipId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("relationshipId")
  public JsonNullable<String> getRelationshipId() {
    return relationshipId;
  }

  public void setRelationshipId(JsonNullable<String> relationshipId) {
    this.relationshipId = relationshipId;
  }

  public TrustHistoryEntry worldPulseSnapshot(@Nullable WorldPulseLink worldPulseSnapshot) {
    this.worldPulseSnapshot = worldPulseSnapshot;
    return this;
  }

  /**
   * Get worldPulseSnapshot
   * @return worldPulseSnapshot
   */
  @Valid 
  @Schema(name = "worldPulseSnapshot", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worldPulseSnapshot")
  public @Nullable WorldPulseLink getWorldPulseSnapshot() {
    return worldPulseSnapshot;
  }

  public void setWorldPulseSnapshot(@Nullable WorldPulseLink worldPulseSnapshot) {
    this.worldPulseSnapshot = worldPulseSnapshot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrustHistoryEntry trustHistoryEntry = (TrustHistoryEntry) o;
    return Objects.equals(this.timestamp, trustHistoryEntry.timestamp) &&
        Objects.equals(this.trustIndex, trustHistoryEntry.trustIndex) &&
        Objects.equals(this.delta, trustHistoryEntry.delta) &&
        Objects.equals(this.reason, trustHistoryEntry.reason) &&
        Objects.equals(this.source, trustHistoryEntry.source) &&
        equalsNullable(this.campaignId, trustHistoryEntry.campaignId) &&
        equalsNullable(this.relationshipId, trustHistoryEntry.relationshipId) &&
        Objects.equals(this.worldPulseSnapshot, trustHistoryEntry.worldPulseSnapshot);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, trustIndex, delta, reason, source, hashCodeNullable(campaignId), hashCodeNullable(relationshipId), worldPulseSnapshot);
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
    sb.append("class TrustHistoryEntry {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    trustIndex: ").append(toIndentedString(trustIndex)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    campaignId: ").append(toIndentedString(campaignId)).append("\n");
    sb.append("    relationshipId: ").append(toIndentedString(relationshipId)).append("\n");
    sb.append("    worldPulseSnapshot: ").append(toIndentedString(worldPulseSnapshot)).append("\n");
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

