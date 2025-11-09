package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.SupportTicket;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateTicketResponse
 */


public class CreateTicketResponse {

  private @Nullable SupportTicket ticket;

  private @Nullable String assignedTo;

  private @Nullable Integer slaSeconds;

  public CreateTicketResponse ticket(@Nullable SupportTicket ticket) {
    this.ticket = ticket;
    return this;
  }

  /**
   * Get ticket
   * @return ticket
   */
  @Valid 
  @Schema(name = "ticket", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ticket")
  public @Nullable SupportTicket getTicket() {
    return ticket;
  }

  public void setTicket(@Nullable SupportTicket ticket) {
    this.ticket = ticket;
  }

  public CreateTicketResponse assignedTo(@Nullable String assignedTo) {
    this.assignedTo = assignedTo;
    return this;
  }

  /**
   * Get assignedTo
   * @return assignedTo
   */
  
  @Schema(name = "assignedTo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assignedTo")
  public @Nullable String getAssignedTo() {
    return assignedTo;
  }

  public void setAssignedTo(@Nullable String assignedTo) {
    this.assignedTo = assignedTo;
  }

  public CreateTicketResponse slaSeconds(@Nullable Integer slaSeconds) {
    this.slaSeconds = slaSeconds;
    return this;
  }

  /**
   * Get slaSeconds
   * @return slaSeconds
   */
  
  @Schema(name = "slaSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slaSeconds")
  public @Nullable Integer getSlaSeconds() {
    return slaSeconds;
  }

  public void setSlaSeconds(@Nullable Integer slaSeconds) {
    this.slaSeconds = slaSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateTicketResponse createTicketResponse = (CreateTicketResponse) o;
    return Objects.equals(this.ticket, createTicketResponse.ticket) &&
        Objects.equals(this.assignedTo, createTicketResponse.assignedTo) &&
        Objects.equals(this.slaSeconds, createTicketResponse.slaSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticket, assignedTo, slaSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateTicketResponse {\n");
    sb.append("    ticket: ").append(toIndentedString(ticket)).append("\n");
    sb.append("    assignedTo: ").append(toIndentedString(assignedTo)).append("\n");
    sb.append("    slaSeconds: ").append(toIndentedString(slaSeconds)).append("\n");
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

