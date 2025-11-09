package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.SessionInfo;
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
 * GetActiveSessions200Response
 */

@JsonTypeName("getActiveSessions_200_response")

public class GetActiveSessions200Response {

  @Valid
  private List<@Valid SessionInfo> sessions = new ArrayList<>();

  public GetActiveSessions200Response sessions(List<@Valid SessionInfo> sessions) {
    this.sessions = sessions;
    return this;
  }

  public GetActiveSessions200Response addSessionsItem(SessionInfo sessionsItem) {
    if (this.sessions == null) {
      this.sessions = new ArrayList<>();
    }
    this.sessions.add(sessionsItem);
    return this;
  }

  /**
   * Get sessions
   * @return sessions
   */
  @Valid 
  @Schema(name = "sessions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessions")
  public List<@Valid SessionInfo> getSessions() {
    return sessions;
  }

  public void setSessions(List<@Valid SessionInfo> sessions) {
    this.sessions = sessions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetActiveSessions200Response getActiveSessions200Response = (GetActiveSessions200Response) o;
    return Objects.equals(this.sessions, getActiveSessions200Response.sessions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetActiveSessions200Response {\n");
    sb.append("    sessions: ").append(toIndentedString(sessions)).append("\n");
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

