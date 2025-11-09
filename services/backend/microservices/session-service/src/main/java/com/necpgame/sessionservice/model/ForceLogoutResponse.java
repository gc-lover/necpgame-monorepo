package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ForceLogoutResponse
 */


public class ForceLogoutResponse {

  @Valid
  private List<String> affectedSessions = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime scheduledAt;

  public ForceLogoutResponse affectedSessions(List<String> affectedSessions) {
    this.affectedSessions = affectedSessions;
    return this;
  }

  public ForceLogoutResponse addAffectedSessionsItem(String affectedSessionsItem) {
    if (this.affectedSessions == null) {
      this.affectedSessions = new ArrayList<>();
    }
    this.affectedSessions.add(affectedSessionsItem);
    return this;
  }

  /**
   * Get affectedSessions
   * @return affectedSessions
   */
  
  @Schema(name = "affectedSessions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affectedSessions")
  public List<String> getAffectedSessions() {
    return affectedSessions;
  }

  public void setAffectedSessions(List<String> affectedSessions) {
    this.affectedSessions = affectedSessions;
  }

  public ForceLogoutResponse scheduledAt(@Nullable OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
    return this;
  }

  /**
   * Get scheduledAt
   * @return scheduledAt
   */
  @Valid 
  @Schema(name = "scheduledAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledAt")
  public @Nullable OffsetDateTime getScheduledAt() {
    return scheduledAt;
  }

  public void setScheduledAt(@Nullable OffsetDateTime scheduledAt) {
    this.scheduledAt = scheduledAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ForceLogoutResponse forceLogoutResponse = (ForceLogoutResponse) o;
    return Objects.equals(this.affectedSessions, forceLogoutResponse.affectedSessions) &&
        Objects.equals(this.scheduledAt, forceLogoutResponse.scheduledAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(affectedSessions, scheduledAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ForceLogoutResponse {\n");
    sb.append("    affectedSessions: ").append(toIndentedString(affectedSessions)).append("\n");
    sb.append("    scheduledAt: ").append(toIndentedString(scheduledAt)).append("\n");
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

