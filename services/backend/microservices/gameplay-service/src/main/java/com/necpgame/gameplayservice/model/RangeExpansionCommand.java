package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RangeExpansionCommand
 */


public class RangeExpansionCommand {

  private Integer newRatingRange;

  private Boolean expandLatency = false;

  private Boolean notifyPlayer = true;

  private @Nullable String comment;

  public RangeExpansionCommand() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RangeExpansionCommand(Integer newRatingRange) {
    this.newRatingRange = newRatingRange;
  }

  public RangeExpansionCommand newRatingRange(Integer newRatingRange) {
    this.newRatingRange = newRatingRange;
    return this;
  }

  /**
   * Get newRatingRange
   * minimum: 100
   * maximum: 1200
   * @return newRatingRange
   */
  @NotNull @Min(value = 100) @Max(value = 1200) 
  @Schema(name = "newRatingRange", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newRatingRange")
  public Integer getNewRatingRange() {
    return newRatingRange;
  }

  public void setNewRatingRange(Integer newRatingRange) {
    this.newRatingRange = newRatingRange;
  }

  public RangeExpansionCommand expandLatency(Boolean expandLatency) {
    this.expandLatency = expandLatency;
    return this;
  }

  /**
   * Get expandLatency
   * @return expandLatency
   */
  
  @Schema(name = "expandLatency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expandLatency")
  public Boolean getExpandLatency() {
    return expandLatency;
  }

  public void setExpandLatency(Boolean expandLatency) {
    this.expandLatency = expandLatency;
  }

  public RangeExpansionCommand notifyPlayer(Boolean notifyPlayer) {
    this.notifyPlayer = notifyPlayer;
    return this;
  }

  /**
   * Get notifyPlayer
   * @return notifyPlayer
   */
  
  @Schema(name = "notifyPlayer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyPlayer")
  public Boolean getNotifyPlayer() {
    return notifyPlayer;
  }

  public void setNotifyPlayer(Boolean notifyPlayer) {
    this.notifyPlayer = notifyPlayer;
  }

  public RangeExpansionCommand comment(@Nullable String comment) {
    this.comment = comment;
    return this;
  }

  /**
   * Get comment
   * @return comment
   */
  @Size(max = 200) 
  @Schema(name = "comment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("comment")
  public @Nullable String getComment() {
    return comment;
  }

  public void setComment(@Nullable String comment) {
    this.comment = comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RangeExpansionCommand rangeExpansionCommand = (RangeExpansionCommand) o;
    return Objects.equals(this.newRatingRange, rangeExpansionCommand.newRatingRange) &&
        Objects.equals(this.expandLatency, rangeExpansionCommand.expandLatency) &&
        Objects.equals(this.notifyPlayer, rangeExpansionCommand.notifyPlayer) &&
        Objects.equals(this.comment, rangeExpansionCommand.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newRatingRange, expandLatency, notifyPlayer, comment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RangeExpansionCommand {\n");
    sb.append("    newRatingRange: ").append(toIndentedString(newRatingRange)).append("\n");
    sb.append("    expandLatency: ").append(toIndentedString(expandLatency)).append("\n");
    sb.append("    notifyPlayer: ").append(toIndentedString(notifyPlayer)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
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

