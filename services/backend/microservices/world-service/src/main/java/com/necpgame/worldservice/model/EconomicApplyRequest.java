package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.worldservice.model.EconomicModifier;
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
 * EconomicApplyRequest
 */


public class EconomicApplyRequest {

  private UUID eventId;

  private EconomicModifier modifier;

  private @Nullable UUID appliedBy;

  public EconomicApplyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EconomicApplyRequest(UUID eventId, EconomicModifier modifier) {
    this.eventId = eventId;
    this.modifier = modifier;
  }

  public EconomicApplyRequest eventId(UUID eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * Get eventId
   * @return eventId
   */
  @NotNull @Valid 
  @Schema(name = "eventId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("eventId")
  public UUID getEventId() {
    return eventId;
  }

  public void setEventId(UUID eventId) {
    this.eventId = eventId;
  }

  public EconomicApplyRequest modifier(EconomicModifier modifier) {
    this.modifier = modifier;
    return this;
  }

  /**
   * Get modifier
   * @return modifier
   */
  @NotNull @Valid 
  @Schema(name = "modifier", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("modifier")
  public EconomicModifier getModifier() {
    return modifier;
  }

  public void setModifier(EconomicModifier modifier) {
    this.modifier = modifier;
  }

  public EconomicApplyRequest appliedBy(@Nullable UUID appliedBy) {
    this.appliedBy = appliedBy;
    return this;
  }

  /**
   * Get appliedBy
   * @return appliedBy
   */
  @Valid 
  @Schema(name = "appliedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appliedBy")
  public @Nullable UUID getAppliedBy() {
    return appliedBy;
  }

  public void setAppliedBy(@Nullable UUID appliedBy) {
    this.appliedBy = appliedBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EconomicApplyRequest economicApplyRequest = (EconomicApplyRequest) o;
    return Objects.equals(this.eventId, economicApplyRequest.eventId) &&
        Objects.equals(this.modifier, economicApplyRequest.modifier) &&
        Objects.equals(this.appliedBy, economicApplyRequest.appliedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eventId, modifier, appliedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EconomicApplyRequest {\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    modifier: ").append(toIndentedString(modifier)).append("\n");
    sb.append("    appliedBy: ").append(toIndentedString(appliedBy)).append("\n");
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

