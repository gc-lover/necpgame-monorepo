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
 * InviteToGuildRequest
 */

@JsonTypeName("inviteToGuild_request")

public class InviteToGuildRequest {

  private String inviterId;

  private String inviteeCharacterName;

  public InviteToGuildRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InviteToGuildRequest(String inviterId, String inviteeCharacterName) {
    this.inviterId = inviterId;
    this.inviteeCharacterName = inviteeCharacterName;
  }

  public InviteToGuildRequest inviterId(String inviterId) {
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

  public InviteToGuildRequest inviteeCharacterName(String inviteeCharacterName) {
    this.inviteeCharacterName = inviteeCharacterName;
    return this;
  }

  /**
   * Get inviteeCharacterName
   * @return inviteeCharacterName
   */
  @NotNull 
  @Schema(name = "invitee_character_name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("invitee_character_name")
  public String getInviteeCharacterName() {
    return inviteeCharacterName;
  }

  public void setInviteeCharacterName(String inviteeCharacterName) {
    this.inviteeCharacterName = inviteeCharacterName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InviteToGuildRequest inviteToGuildRequest = (InviteToGuildRequest) o;
    return Objects.equals(this.inviterId, inviteToGuildRequest.inviterId) &&
        Objects.equals(this.inviteeCharacterName, inviteToGuildRequest.inviteeCharacterName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inviterId, inviteeCharacterName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InviteToGuildRequest {\n");
    sb.append("    inviterId: ").append(toIndentedString(inviterId)).append("\n");
    sb.append("    inviteeCharacterName: ").append(toIndentedString(inviteeCharacterName)).append("\n");
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

