package com.necpgame.partyservice.model;

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
 * ReadyCheckResponseRequest
 */


public class ReadyCheckResponseRequest {

  private Boolean ready;

  private @Nullable String note;

  public ReadyCheckResponseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReadyCheckResponseRequest(Boolean ready) {
    this.ready = ready;
  }

  public ReadyCheckResponseRequest ready(Boolean ready) {
    this.ready = ready;
    return this;
  }

  /**
   * Get ready
   * @return ready
   */
  @NotNull 
  @Schema(name = "ready", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ready")
  public Boolean getReady() {
    return ready;
  }

  public void setReady(Boolean ready) {
    this.ready = ready;
  }

  public ReadyCheckResponseRequest note(@Nullable String note) {
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
    ReadyCheckResponseRequest readyCheckResponseRequest = (ReadyCheckResponseRequest) o;
    return Objects.equals(this.ready, readyCheckResponseRequest.ready) &&
        Objects.equals(this.note, readyCheckResponseRequest.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ready, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadyCheckResponseRequest {\n");
    sb.append("    ready: ").append(toIndentedString(ready)).append("\n");
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

