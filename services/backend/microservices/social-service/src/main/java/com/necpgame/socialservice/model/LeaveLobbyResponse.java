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
 * LeaveLobbyResponse
 */


public class LeaveLobbyResponse {

  private @Nullable String lobbyId;

  private @Nullable Integer remainingParticipants;

  private @Nullable String freedRole;

  public LeaveLobbyResponse lobbyId(@Nullable String lobbyId) {
    this.lobbyId = lobbyId;
    return this;
  }

  /**
   * Get lobbyId
   * @return lobbyId
   */
  
  @Schema(name = "lobbyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lobbyId")
  public @Nullable String getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(@Nullable String lobbyId) {
    this.lobbyId = lobbyId;
  }

  public LeaveLobbyResponse remainingParticipants(@Nullable Integer remainingParticipants) {
    this.remainingParticipants = remainingParticipants;
    return this;
  }

  /**
   * Get remainingParticipants
   * @return remainingParticipants
   */
  
  @Schema(name = "remainingParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("remainingParticipants")
  public @Nullable Integer getRemainingParticipants() {
    return remainingParticipants;
  }

  public void setRemainingParticipants(@Nullable Integer remainingParticipants) {
    this.remainingParticipants = remainingParticipants;
  }

  public LeaveLobbyResponse freedRole(@Nullable String freedRole) {
    this.freedRole = freedRole;
    return this;
  }

  /**
   * Get freedRole
   * @return freedRole
   */
  
  @Schema(name = "freedRole", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("freedRole")
  public @Nullable String getFreedRole() {
    return freedRole;
  }

  public void setFreedRole(@Nullable String freedRole) {
    this.freedRole = freedRole;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeaveLobbyResponse leaveLobbyResponse = (LeaveLobbyResponse) o;
    return Objects.equals(this.lobbyId, leaveLobbyResponse.lobbyId) &&
        Objects.equals(this.remainingParticipants, leaveLobbyResponse.remainingParticipants) &&
        Objects.equals(this.freedRole, leaveLobbyResponse.freedRole);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lobbyId, remainingParticipants, freedRole);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeaveLobbyResponse {\n");
    sb.append("    lobbyId: ").append(toIndentedString(lobbyId)).append("\n");
    sb.append("    remainingParticipants: ").append(toIndentedString(remainingParticipants)).append("\n");
    sb.append("    freedRole: ").append(toIndentedString(freedRole)).append("\n");
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

