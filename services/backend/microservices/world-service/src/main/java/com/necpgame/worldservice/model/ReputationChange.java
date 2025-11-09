package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ReputationImpactType;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReputationChange
 */


public class ReputationChange {

  private UUID factionId;

  private Integer delta;

  private @Nullable ReputationImpactType type;

  private @Nullable String reason;

  private @Nullable Integer capMin;

  private @Nullable Integer capMax;

  public ReputationChange() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReputationChange(UUID factionId, Integer delta) {
    this.factionId = factionId;
    this.delta = delta;
  }

  public ReputationChange factionId(UUID factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  @NotNull @Valid 
  @Schema(name = "factionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factionId")
  public UUID getFactionId() {
    return factionId;
  }

  public void setFactionId(UUID factionId) {
    this.factionId = factionId;
  }

  public ReputationChange delta(Integer delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * minimum: -200
   * maximum: 200
   * @return delta
   */
  @NotNull @Min(value = -200) @Max(value = 200) 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Integer getDelta() {
    return delta;
  }

  public void setDelta(Integer delta) {
    this.delta = delta;
  }

  public ReputationChange type(@Nullable ReputationImpactType type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @Valid 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable ReputationImpactType getType() {
    return type;
  }

  public void setType(@Nullable ReputationImpactType type) {
    this.type = type;
  }

  public ReputationChange reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public ReputationChange capMin(@Nullable Integer capMin) {
    this.capMin = capMin;
    return this;
  }

  /**
   * Get capMin
   * minimum: -1000
   * maximum: 1000
   * @return capMin
   */
  @Min(value = -1000) @Max(value = 1000) 
  @Schema(name = "capMin", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capMin")
  public @Nullable Integer getCapMin() {
    return capMin;
  }

  public void setCapMin(@Nullable Integer capMin) {
    this.capMin = capMin;
  }

  public ReputationChange capMax(@Nullable Integer capMax) {
    this.capMax = capMax;
    return this;
  }

  /**
   * Get capMax
   * minimum: -1000
   * maximum: 1000
   * @return capMax
   */
  @Min(value = -1000) @Max(value = 1000) 
  @Schema(name = "capMax", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capMax")
  public @Nullable Integer getCapMax() {
    return capMax;
  }

  public void setCapMax(@Nullable Integer capMax) {
    this.capMax = capMax;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReputationChange reputationChange = (ReputationChange) o;
    return Objects.equals(this.factionId, reputationChange.factionId) &&
        Objects.equals(this.delta, reputationChange.delta) &&
        Objects.equals(this.type, reputationChange.type) &&
        Objects.equals(this.reason, reputationChange.reason) &&
        Objects.equals(this.capMin, reputationChange.capMin) &&
        Objects.equals(this.capMax, reputationChange.capMax);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, delta, type, reason, capMin, capMax);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReputationChange {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    capMin: ").append(toIndentedString(capMin)).append("\n");
    sb.append("    capMax: ").append(toIndentedString(capMax)).append("\n");
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

