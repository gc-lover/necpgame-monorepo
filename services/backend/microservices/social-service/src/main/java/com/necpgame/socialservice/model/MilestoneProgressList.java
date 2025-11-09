package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.MilestoneProgress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MilestoneProgressList
 */


public class MilestoneProgressList {

  private @Nullable String playerId;

  @Valid
  private List<@Valid MilestoneProgress> milestones = new ArrayList<>();

  public MilestoneProgressList playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public MilestoneProgressList milestones(List<@Valid MilestoneProgress> milestones) {
    this.milestones = milestones;
    return this;
  }

  public MilestoneProgressList addMilestonesItem(MilestoneProgress milestonesItem) {
    if (this.milestones == null) {
      this.milestones = new ArrayList<>();
    }
    this.milestones.add(milestonesItem);
    return this;
  }

  /**
   * Get milestones
   * @return milestones
   */
  @Valid 
  @Schema(name = "milestones", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("milestones")
  public List<@Valid MilestoneProgress> getMilestones() {
    return milestones;
  }

  public void setMilestones(List<@Valid MilestoneProgress> milestones) {
    this.milestones = milestones;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MilestoneProgressList milestoneProgressList = (MilestoneProgressList) o;
    return Objects.equals(this.playerId, milestoneProgressList.playerId) &&
        Objects.equals(this.milestones, milestoneProgressList.milestones);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, milestones);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MilestoneProgressList {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    milestones: ").append(toIndentedString(milestones)).append("\n");
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

