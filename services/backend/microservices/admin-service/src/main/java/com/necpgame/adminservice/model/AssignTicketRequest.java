package com.necpgame.adminservice.model;

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
 * AssignTicketRequest
 */


public class AssignTicketRequest {

  private String agentId;

  private Boolean notify = true;

  private @Nullable String note;

  public AssignTicketRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public AssignTicketRequest(String agentId) {
    this.agentId = agentId;
  }

  public AssignTicketRequest agentId(String agentId) {
    this.agentId = agentId;
    return this;
  }

  /**
   * Get agentId
   * @return agentId
   */
  @NotNull 
  @Schema(name = "agentId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("agentId")
  public String getAgentId() {
    return agentId;
  }

  public void setAgentId(String agentId) {
    this.agentId = agentId;
  }

  public AssignTicketRequest notify(Boolean notify) {
    this.notify = notify;
    return this;
  }

  /**
   * Get notify
   * @return notify
   */
  
  @Schema(name = "notify", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notify")
  public Boolean getNotify() {
    return notify;
  }

  public void setNotify(Boolean notify) {
    this.notify = notify;
  }

  public AssignTicketRequest note(@Nullable String note) {
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
    AssignTicketRequest assignTicketRequest = (AssignTicketRequest) o;
    return Objects.equals(this.agentId, assignTicketRequest.agentId) &&
        Objects.equals(this.notify, assignTicketRequest.notify) &&
        Objects.equals(this.note, assignTicketRequest.note);
  }

  @Override
  public int hashCode() {
    return Objects.hash(agentId, notify, note);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AssignTicketRequest {\n");
    sb.append("    agentId: ").append(toIndentedString(agentId)).append("\n");
    sb.append("    notify: ").append(toIndentedString(notify)).append("\n");
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

