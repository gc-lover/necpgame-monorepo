package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.UUID;
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
 * ControlShiftEvent
 */


public class ControlShiftEvent {

  private UUID regionId;

  private UUID previousOwner;

  private UUID newOwner;

  private String trigger;

  private UUID timelineEntryId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime effectiveAt;

  private @Nullable Integer controlScore;

  public ControlShiftEvent() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ControlShiftEvent(UUID regionId, UUID previousOwner, UUID newOwner, String trigger, UUID timelineEntryId, OffsetDateTime effectiveAt) {
    this.regionId = regionId;
    this.previousOwner = previousOwner;
    this.newOwner = newOwner;
    this.trigger = trigger;
    this.timelineEntryId = timelineEntryId;
    this.effectiveAt = effectiveAt;
  }

  public ControlShiftEvent regionId(UUID regionId) {
    this.regionId = regionId;
    return this;
  }

  /**
   * Get regionId
   * @return regionId
   */
  @NotNull @Valid 
  @Schema(name = "regionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("regionId")
  public UUID getRegionId() {
    return regionId;
  }

  public void setRegionId(UUID regionId) {
    this.regionId = regionId;
  }

  public ControlShiftEvent previousOwner(UUID previousOwner) {
    this.previousOwner = previousOwner;
    return this;
  }

  /**
   * Get previousOwner
   * @return previousOwner
   */
  @NotNull @Valid 
  @Schema(name = "previousOwner", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("previousOwner")
  public UUID getPreviousOwner() {
    return previousOwner;
  }

  public void setPreviousOwner(UUID previousOwner) {
    this.previousOwner = previousOwner;
  }

  public ControlShiftEvent newOwner(UUID newOwner) {
    this.newOwner = newOwner;
    return this;
  }

  /**
   * Get newOwner
   * @return newOwner
   */
  @NotNull @Valid 
  @Schema(name = "newOwner", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newOwner")
  public UUID getNewOwner() {
    return newOwner;
  }

  public void setNewOwner(UUID newOwner) {
    this.newOwner = newOwner;
  }

  public ControlShiftEvent trigger(String trigger) {
    this.trigger = trigger;
    return this;
  }

  /**
   * Get trigger
   * @return trigger
   */
  @NotNull 
  @Schema(name = "trigger", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("trigger")
  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(String trigger) {
    this.trigger = trigger;
  }

  public ControlShiftEvent timelineEntryId(UUID timelineEntryId) {
    this.timelineEntryId = timelineEntryId;
    return this;
  }

  /**
   * Get timelineEntryId
   * @return timelineEntryId
   */
  @NotNull @Valid 
  @Schema(name = "timelineEntryId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timelineEntryId")
  public UUID getTimelineEntryId() {
    return timelineEntryId;
  }

  public void setTimelineEntryId(UUID timelineEntryId) {
    this.timelineEntryId = timelineEntryId;
  }

  public ControlShiftEvent effectiveAt(OffsetDateTime effectiveAt) {
    this.effectiveAt = effectiveAt;
    return this;
  }

  /**
   * Get effectiveAt
   * @return effectiveAt
   */
  @NotNull @Valid 
  @Schema(name = "effectiveAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("effectiveAt")
  public OffsetDateTime getEffectiveAt() {
    return effectiveAt;
  }

  public void setEffectiveAt(OffsetDateTime effectiveAt) {
    this.effectiveAt = effectiveAt;
  }

  public ControlShiftEvent controlScore(@Nullable Integer controlScore) {
    this.controlScore = controlScore;
    return this;
  }

  /**
   * Get controlScore
   * @return controlScore
   */
  
  @Schema(name = "controlScore", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("controlScore")
  public @Nullable Integer getControlScore() {
    return controlScore;
  }

  public void setControlScore(@Nullable Integer controlScore) {
    this.controlScore = controlScore;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ControlShiftEvent controlShiftEvent = (ControlShiftEvent) o;
    return Objects.equals(this.regionId, controlShiftEvent.regionId) &&
        Objects.equals(this.previousOwner, controlShiftEvent.previousOwner) &&
        Objects.equals(this.newOwner, controlShiftEvent.newOwner) &&
        Objects.equals(this.trigger, controlShiftEvent.trigger) &&
        Objects.equals(this.timelineEntryId, controlShiftEvent.timelineEntryId) &&
        Objects.equals(this.effectiveAt, controlShiftEvent.effectiveAt) &&
        Objects.equals(this.controlScore, controlShiftEvent.controlScore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(regionId, previousOwner, newOwner, trigger, timelineEntryId, effectiveAt, controlScore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ControlShiftEvent {\n");
    sb.append("    regionId: ").append(toIndentedString(regionId)).append("\n");
    sb.append("    previousOwner: ").append(toIndentedString(previousOwner)).append("\n");
    sb.append("    newOwner: ").append(toIndentedString(newOwner)).append("\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
    sb.append("    timelineEntryId: ").append(toIndentedString(timelineEntryId)).append("\n");
    sb.append("    effectiveAt: ").append(toIndentedString(effectiveAt)).append("\n");
    sb.append("    controlScore: ").append(toIndentedString(controlScore)).append("\n");
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

