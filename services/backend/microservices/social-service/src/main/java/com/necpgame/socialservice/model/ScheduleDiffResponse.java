package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.ScheduleDiff;
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
 * ScheduleDiffResponse
 */


public class ScheduleDiffResponse {

  private UUID npcId;

  private ScheduleDiff diff;

  public ScheduleDiffResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ScheduleDiffResponse(UUID npcId, ScheduleDiff diff) {
    this.npcId = npcId;
    this.diff = diff;
  }

  public ScheduleDiffResponse npcId(UUID npcId) {
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

  public ScheduleDiffResponse diff(ScheduleDiff diff) {
    this.diff = diff;
    return this;
  }

  /**
   * Get diff
   * @return diff
   */
  @NotNull @Valid 
  @Schema(name = "diff", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("diff")
  public ScheduleDiff getDiff() {
    return diff;
  }

  public void setDiff(ScheduleDiff diff) {
    this.diff = diff;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleDiffResponse scheduleDiffResponse = (ScheduleDiffResponse) o;
    return Objects.equals(this.npcId, scheduleDiffResponse.npcId) &&
        Objects.equals(this.diff, scheduleDiffResponse.diff);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcId, diff);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleDiffResponse {\n");
    sb.append("    npcId: ").append(toIndentedString(npcId)).append("\n");
    sb.append("    diff: ").append(toIndentedString(diff)).append("\n");
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

