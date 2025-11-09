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
 * GuildMemberUpdateRequest
 */


public class GuildMemberUpdateRequest {

  private @Nullable String rankId;

  private @Nullable String role;

  private @Nullable String notes;

  private @Nullable String status;

  public GuildMemberUpdateRequest rankId(@Nullable String rankId) {
    this.rankId = rankId;
    return this;
  }

  /**
   * Get rankId
   * @return rankId
   */
  
  @Schema(name = "rankId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rankId")
  public @Nullable String getRankId() {
    return rankId;
  }

  public void setRankId(@Nullable String rankId) {
    this.rankId = rankId;
  }

  public GuildMemberUpdateRequest role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public GuildMemberUpdateRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  public GuildMemberUpdateRequest status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildMemberUpdateRequest guildMemberUpdateRequest = (GuildMemberUpdateRequest) o;
    return Objects.equals(this.rankId, guildMemberUpdateRequest.rankId) &&
        Objects.equals(this.role, guildMemberUpdateRequest.role) &&
        Objects.equals(this.notes, guildMemberUpdateRequest.notes) &&
        Objects.equals(this.status, guildMemberUpdateRequest.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rankId, role, notes, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildMemberUpdateRequest {\n");
    sb.append("    rankId: ").append(toIndentedString(rankId)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

