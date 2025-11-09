package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * InviteToPartyRequest
 */

@JsonTypeName("inviteToParty_request")

public class InviteToPartyRequest {

  private String inviterId;

  private String inviteeId;

  public InviteToPartyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InviteToPartyRequest(String inviterId, String inviteeId) {
    this.inviterId = inviterId;
    this.inviteeId = inviteeId;
  }

  public InviteToPartyRequest inviterId(String inviterId) {
    this.inviterId = inviterId;
    return this;
  }

  /**
   * Get inviterId
   * @return inviterId
   */
  @NotNull 
  @Schema(name = "inviter_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("inviter_id")
  public String getInviterId() {
    return inviterId;
  }

  public void setInviterId(String inviterId) {
    this.inviterId = inviterId;
  }

  public InviteToPartyRequest inviteeId(String inviteeId) {
    this.inviteeId = inviteeId;
    return this;
  }

  /**
   * Get inviteeId
   * @return inviteeId
   */
  @NotNull 
  @Schema(name = "invitee_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("invitee_id")
  public String getInviteeId() {
    return inviteeId;
  }

  public void setInviteeId(String inviteeId) {
    this.inviteeId = inviteeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InviteToPartyRequest inviteToPartyRequest = (InviteToPartyRequest) o;
    return Objects.equals(this.inviterId, inviteToPartyRequest.inviterId) &&
        Objects.equals(this.inviteeId, inviteToPartyRequest.inviteeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inviterId, inviteeId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InviteToPartyRequest {\n");
    sb.append("    inviterId: ").append(toIndentedString(inviterId)).append("\n");
    sb.append("    inviteeId: ").append(toIndentedString(inviteeId)).append("\n");
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

