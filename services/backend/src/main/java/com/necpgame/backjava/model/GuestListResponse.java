package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.GuestInvite;
import com.necpgame.backjava.model.GuestLogEntry;
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
 * GuestListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuestListResponse {

  @Valid
  private List<@Valid GuestInvite> invites = new ArrayList<>();

  @Valid
  private List<@Valid GuestLogEntry> visitLog = new ArrayList<>();

  public GuestListResponse invites(List<@Valid GuestInvite> invites) {
    this.invites = invites;
    return this;
  }

  public GuestListResponse addInvitesItem(GuestInvite invitesItem) {
    if (this.invites == null) {
      this.invites = new ArrayList<>();
    }
    this.invites.add(invitesItem);
    return this;
  }

  /**
   * Get invites
   * @return invites
   */
  @Valid 
  @Schema(name = "invites", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("invites")
  public List<@Valid GuestInvite> getInvites() {
    return invites;
  }

  public void setInvites(List<@Valid GuestInvite> invites) {
    this.invites = invites;
  }

  public GuestListResponse visitLog(List<@Valid GuestLogEntry> visitLog) {
    this.visitLog = visitLog;
    return this;
  }

  public GuestListResponse addVisitLogItem(GuestLogEntry visitLogItem) {
    if (this.visitLog == null) {
      this.visitLog = new ArrayList<>();
    }
    this.visitLog.add(visitLogItem);
    return this;
  }

  /**
   * Get visitLog
   * @return visitLog
   */
  @Valid 
  @Schema(name = "visitLog", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visitLog")
  public List<@Valid GuestLogEntry> getVisitLog() {
    return visitLog;
  }

  public void setVisitLog(List<@Valid GuestLogEntry> visitLog) {
    this.visitLog = visitLog;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestListResponse guestListResponse = (GuestListResponse) o;
    return Objects.equals(this.invites, guestListResponse.invites) &&
        Objects.equals(this.visitLog, guestListResponse.visitLog);
  }

  @Override
  public int hashCode() {
    return Objects.hash(invites, visitLog);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestListResponse {\n");
    sb.append("    invites: ").append(toIndentedString(invites)).append("\n");
    sb.append("    visitLog: ").append(toIndentedString(visitLog)).append("\n");
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

