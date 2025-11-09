package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ControlShiftRequestEvidence
 */

@JsonTypeName("ControlShiftRequest_evidence")

public class ControlShiftRequestEvidence {

  @Valid
  private List<UUID> timelineEntries = new ArrayList<>();

  private @Nullable BigDecimal supplyIndex;

  private @Nullable Integer raidVictories;

  private @Nullable BigDecimal reputationShift;

  public ControlShiftRequestEvidence timelineEntries(List<UUID> timelineEntries) {
    this.timelineEntries = timelineEntries;
    return this;
  }

  public ControlShiftRequestEvidence addTimelineEntriesItem(UUID timelineEntriesItem) {
    if (this.timelineEntries == null) {
      this.timelineEntries = new ArrayList<>();
    }
    this.timelineEntries.add(timelineEntriesItem);
    return this;
  }

  /**
   * Get timelineEntries
   * @return timelineEntries
   */
  @Valid 
  @Schema(name = "timelineEntries", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timelineEntries")
  public List<UUID> getTimelineEntries() {
    return timelineEntries;
  }

  public void setTimelineEntries(List<UUID> timelineEntries) {
    this.timelineEntries = timelineEntries;
  }

  public ControlShiftRequestEvidence supplyIndex(@Nullable BigDecimal supplyIndex) {
    this.supplyIndex = supplyIndex;
    return this;
  }

  /**
   * Get supplyIndex
   * minimum: 0
   * @return supplyIndex
   */
  @Valid @DecimalMin(value = "0") 
  @Schema(name = "supplyIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("supplyIndex")
  public @Nullable BigDecimal getSupplyIndex() {
    return supplyIndex;
  }

  public void setSupplyIndex(@Nullable BigDecimal supplyIndex) {
    this.supplyIndex = supplyIndex;
  }

  public ControlShiftRequestEvidence raidVictories(@Nullable Integer raidVictories) {
    this.raidVictories = raidVictories;
    return this;
  }

  /**
   * Get raidVictories
   * minimum: 0
   * @return raidVictories
   */
  @Min(value = 0) 
  @Schema(name = "raidVictories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("raidVictories")
  public @Nullable Integer getRaidVictories() {
    return raidVictories;
  }

  public void setRaidVictories(@Nullable Integer raidVictories) {
    this.raidVictories = raidVictories;
  }

  public ControlShiftRequestEvidence reputationShift(@Nullable BigDecimal reputationShift) {
    this.reputationShift = reputationShift;
    return this;
  }

  /**
   * Get reputationShift
   * @return reputationShift
   */
  @Valid 
  @Schema(name = "reputationShift", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reputationShift")
  public @Nullable BigDecimal getReputationShift() {
    return reputationShift;
  }

  public void setReputationShift(@Nullable BigDecimal reputationShift) {
    this.reputationShift = reputationShift;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ControlShiftRequestEvidence controlShiftRequestEvidence = (ControlShiftRequestEvidence) o;
    return Objects.equals(this.timelineEntries, controlShiftRequestEvidence.timelineEntries) &&
        Objects.equals(this.supplyIndex, controlShiftRequestEvidence.supplyIndex) &&
        Objects.equals(this.raidVictories, controlShiftRequestEvidence.raidVictories) &&
        Objects.equals(this.reputationShift, controlShiftRequestEvidence.reputationShift);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timelineEntries, supplyIndex, raidVictories, reputationShift);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ControlShiftRequestEvidence {\n");
    sb.append("    timelineEntries: ").append(toIndentedString(timelineEntries)).append("\n");
    sb.append("    supplyIndex: ").append(toIndentedString(supplyIndex)).append("\n");
    sb.append("    raidVictories: ").append(toIndentedString(raidVictories)).append("\n");
    sb.append("    reputationShift: ").append(toIndentedString(reputationShift)).append("\n");
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

