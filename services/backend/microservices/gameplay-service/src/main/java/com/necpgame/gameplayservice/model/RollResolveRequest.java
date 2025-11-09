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
 * RollResolveRequest
 */


public class RollResolveRequest {

  private @Nullable String forceWinnerId;

  private @Nullable String note;

  public RollResolveRequest forceWinnerId(@Nullable String forceWinnerId) {
    this.forceWinnerId = forceWinnerId;
    return this;
  }

  /**
   * Get forceWinnerId
   * @return forceWinnerId
   */
  
  @Schema(name = "forceWinnerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("forceWinnerId")
  public @Nullable String getForceWinnerId() {
    return forceWinnerId;
  }

  public void setForceWinnerId(@Nullable String forceWinnerId) {
    this.forceWinnerId = forceWinnerId;
  }

  public RollResolveRequest note(@Nullable String note) {
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
    RollResolveRequest rollResolveRequest = (RollResolveRequest) o;
    return Objects.equals(this.forceWinnerId, rollResolveRequest.forceWinnerId) &&
        Objects.equals(this.note, rollResolveRequest.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(forceWinnerId, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollResolveRequest {\n");
    sb.append("    forceWinnerId: ").append(toIndentedString(forceWinnerId)).append("\n");
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

