package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.NpcSchedule;
import com.necpgame.socialservice.model.ScheduleSlotChange;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ScheduleDiff
 */


public class ScheduleDiff {

  private UUID npcId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime baselineTimestamp;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime currentTimestamp;

  private NpcSchedule baselineSchedule;

  private NpcSchedule currentSchedule;

  @Valid
  private List<@Valid ScheduleSlotChange> changedSlots = new ArrayList<>();

  public ScheduleDiff() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleDiff(UUID npcId, NpcSchedule baselineSchedule, NpcSchedule currentSchedule) {
    this.npcId = npcId;
    this.baselineSchedule = baselineSchedule;
    this.currentSchedule = currentSchedule;
  }

  public ScheduleDiff npcId(UUID npcId) {
    this.npcId = npcId;
    return this;
  }

  /**
   * Get npcId
   * @return npcId
   */
  @NotNull @Valid 
  @Schema(name = "npcId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("npcId")
  public UUID getNpcId() {
    return npcId;
  }

  public void setNpcId(UUID npcId) {
    this.npcId = npcId;
  }

  public ScheduleDiff baselineTimestamp(@Nullable OffsetDateTime baselineTimestamp) {
    this.baselineTimestamp = baselineTimestamp;
    return this;
  }

  /**
   * Get baselineTimestamp
   * @return baselineTimestamp
   */
  @Valid 
  @Schema(name = "baselineTimestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("baselineTimestamp")
  public @Nullable OffsetDateTime getBaselineTimestamp() {
    return baselineTimestamp;
  }

  public void setBaselineTimestamp(@Nullable OffsetDateTime baselineTimestamp) {
    this.baselineTimestamp = baselineTimestamp;
  }

  public ScheduleDiff currentTimestamp(@Nullable OffsetDateTime currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
    return this;
  }

  /**
   * Get currentTimestamp
   * @return currentTimestamp
   */
  @Valid 
  @Schema(name = "currentTimestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentTimestamp")
  public @Nullable OffsetDateTime getCurrentTimestamp() {
    return currentTimestamp;
  }

  public void setCurrentTimestamp(@Nullable OffsetDateTime currentTimestamp) {
    this.currentTimestamp = currentTimestamp;
  }

  public ScheduleDiff baselineSchedule(NpcSchedule baselineSchedule) {
    this.baselineSchedule = baselineSchedule;
    return this;
  }

  /**
   * Get baselineSchedule
   * @return baselineSchedule
   */
  @NotNull @Valid 
  @Schema(name = "baselineSchedule", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("baselineSchedule")
  public NpcSchedule getBaselineSchedule() {
    return baselineSchedule;
  }

  public void setBaselineSchedule(NpcSchedule baselineSchedule) {
    this.baselineSchedule = baselineSchedule;
  }

  public ScheduleDiff currentSchedule(NpcSchedule currentSchedule) {
    this.currentSchedule = currentSchedule;
    return this;
  }

  /**
   * Get currentSchedule
   * @return currentSchedule
   */
  @NotNull @Valid 
  @Schema(name = "currentSchedule", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentSchedule")
  public NpcSchedule getCurrentSchedule() {
    return currentSchedule;
  }

  public void setCurrentSchedule(NpcSchedule currentSchedule) {
    this.currentSchedule = currentSchedule;
  }

  public ScheduleDiff changedSlots(List<@Valid ScheduleSlotChange> changedSlots) {
    this.changedSlots = changedSlots;
    return this;
  }

  public ScheduleDiff addChangedSlotsItem(ScheduleSlotChange changedSlotsItem) {
    if (this.changedSlots == null) {
      this.changedSlots = new ArrayList<>();
    }
    this.changedSlots.add(changedSlotsItem);
    return this;
  }

  /**
   * Get changedSlots
   * @return changedSlots
   */
  @Valid 
  @Schema(name = "changedSlots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("changedSlots")
  public List<@Valid ScheduleSlotChange> getChangedSlots() {
    return changedSlots;
  }

  public void setChangedSlots(List<@Valid ScheduleSlotChange> changedSlots) {
    this.changedSlots = changedSlots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleDiff scheduleDiff = (ScheduleDiff) o;
    return Objects.equals(this.npcId, scheduleDiff.npcId) &&
        Objects.equals(this.baselineTimestamp, scheduleDiff.baselineTimestamp) &&
        Objects.equals(this.currentTimestamp, scheduleDiff.currentTimestamp) &&
        Objects.equals(this.baselineSchedule, scheduleDiff.baselineSchedule) &&
        Objects.equals(this.currentSchedule, scheduleDiff.currentSchedule) &&
        Objects.equals(this.changedSlots, scheduleDiff.changedSlots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, baselineTimestamp, currentTimestamp, baselineSchedule, currentSchedule, changedSlots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleDiff {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    baselineTimestamp: ").append(toIndentedString(baselineTimestamp)).append("\n");
    sb.append("    currentTimestamp: ").append(toIndentedString(currentTimestamp)).append("\n");
    sb.append("    baselineSchedule: ").append(toIndentedString(baselineSchedule)).append("\n");
    sb.append("    currentSchedule: ").append(toIndentedString(currentSchedule)).append("\n");
    sb.append("    changedSlots: ").append(toIndentedString(changedSlots)).append("\n");
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

