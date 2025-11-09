package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * OutcomeEffectReputationChangesInner
 */

@JsonTypeName("OutcomeEffect_reputationChanges_inner")

public class OutcomeEffectReputationChangesInner {

  private @Nullable UUID factionId;

  private @Nullable Integer delta;

  public OutcomeEffectReputationChangesInner factionId(@Nullable UUID factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  @Valid 
  @Schema(name = "factionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factionId")
  public @Nullable UUID getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable UUID factionId) {
    this.factionId = factionId;
  }

  public OutcomeEffectReputationChangesInner delta(@Nullable Integer delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * minimum: -200
   * maximum: 200
   * @return delta
   */
  @Min(value = -200) @Max(value = 200) 
  @Schema(name = "delta", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("delta")
  public @Nullable Integer getDelta() {
    return delta;
  }

  public void setDelta(@Nullable Integer delta) {
    this.delta = delta;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OutcomeEffectReputationChangesInner outcomeEffectReputationChangesInner = (OutcomeEffectReputationChangesInner) o;
    return Objects.equals(this.factionId, outcomeEffectReputationChangesInner.factionId) &&
        Objects.equals(this.delta, outcomeEffectReputationChangesInner.delta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, delta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class OutcomeEffectReputationChangesInner {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
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

