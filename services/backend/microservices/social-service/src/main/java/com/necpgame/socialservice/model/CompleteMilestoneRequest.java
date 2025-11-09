package com.necpgame.socialservice.model;

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
 * CompleteMilestoneRequest
 */


public class CompleteMilestoneRequest {

  private String milestoneId;

  private @Nullable String note;

  public CompleteMilestoneRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CompleteMilestoneRequest(String milestoneId) {
    this.milestoneId = milestoneId;
  }

  public CompleteMilestoneRequest milestoneId(String milestoneId) {
    this.milestoneId = milestoneId;
    return this;
  }

  /**
   * Get milestoneId
   * @return milestoneId
   */
  @NotNull 
  @Schema(name = "milestoneId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("milestoneId")
  public String getMilestoneId() {
    return milestoneId;
  }

  public void setMilestoneId(String milestoneId) {
    this.milestoneId = milestoneId;
  }

  public CompleteMilestoneRequest note(@Nullable String note) {
    this.note = note;
    return this;
  }

  /**
   * Get note
   * @return note
   */
  
  @Schema(name = "note", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("note")
  public @Nullable String getNote() {
    return note;
  }

  public void setNote(@Nullable String note) {
    this.note = note;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompleteMilestoneRequest completeMilestoneRequest = (CompleteMilestoneRequest) o;
    return Objects.equals(this.milestoneId, completeMilestoneRequest.milestoneId) &&
        Objects.equals(this.note, completeMilestoneRequest.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(milestoneId, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompleteMilestoneRequest {\n");
    sb.append("    milestoneId: ").append(toIndentedString(milestoneId)).append("\n");
    sb.append("    note: ").append(toIndentedString(note)).append("\n");
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

