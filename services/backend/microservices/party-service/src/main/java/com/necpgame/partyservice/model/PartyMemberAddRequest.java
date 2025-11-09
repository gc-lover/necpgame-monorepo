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
 * PartyMemberAddRequest
 */


public class PartyMemberAddRequest {

  private @Nullable String inviteCode;

  private @Nullable String playerId;

  private @Nullable Boolean autoAssignRole;

  private @Nullable String idempotencyKey;

  public PartyMemberAddRequest inviteCode(@Nullable String inviteCode) {
    this.inviteCode = inviteCode;
    return this;
  }

  /**
   * Get inviteCode
   * @return inviteCode
   */
  
  @Schema(name = "inviteCode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inviteCode")
  public @Nullable String getInviteCode() {
    return inviteCode;
  }

  public void setInviteCode(@Nullable String inviteCode) {
    this.inviteCode = inviteCode;
  }

  public PartyMemberAddRequest playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public PartyMemberAddRequest autoAssignRole(@Nullable Boolean autoAssignRole) {
    this.autoAssignRole = autoAssignRole;
    return this;
  }

  /**
   * Get autoAssignRole
   * @return autoAssignRole
   */
  
  @Schema(name = "autoAssignRole", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoAssignRole")
  public @Nullable Boolean getAutoAssignRole() {
    return autoAssignRole;
  }

  public void setAutoAssignRole(@Nullable Boolean autoAssignRole) {
    this.autoAssignRole = autoAssignRole;
  }

  public PartyMemberAddRequest idempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
    return this;
  }

  /**
   * Get idempotencyKey
   * @return idempotencyKey
   */
  
  @Schema(name = "idempotencyKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("idempotencyKey")
  public @Nullable String getIdempotencyKey() {
    return idempotencyKey;
  }

  public void setIdempotencyKey(@Nullable String idempotencyKey) {
    this.idempotencyKey = idempotencyKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyMemberAddRequest partyMemberAddRequest = (PartyMemberAddRequest) o;
    return Objects.equals(this.inviteCode, partyMemberAddRequest.inviteCode) &&
        Objects.equals(this.playerId, partyMemberAddRequest.playerId) &&
        Objects.equals(this.autoAssignRole, partyMemberAddRequest.autoAssignRole) &&
        Objects.equals(this.idempotencyKey, partyMemberAddRequest.idempotencyKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inviteCode, playerId, autoAssignRole, idempotencyKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyMemberAddRequest {\n");
    sb.append("    inviteCode: ").append(toIndentedString(inviteCode)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    autoAssignRole: ").append(toIndentedString(autoAssignRole)).append("\n");
    sb.append("    idempotencyKey: ").append(toIndentedString(idempotencyKey)).append("\n");
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

