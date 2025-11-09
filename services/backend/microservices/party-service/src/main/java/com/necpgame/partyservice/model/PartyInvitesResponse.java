package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.partyservice.model.PartyInvite;
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
 * PartyInvitesResponse
 */


public class PartyInvitesResponse {

  @Valid
  private List<@Valid PartyInvite> invites = new ArrayList<>();

  public PartyInvitesResponse invites(List<@Valid PartyInvite> invites) {
    this.invites = invites;
    return this;
  }

  public PartyInvitesResponse addInvitesItem(PartyInvite invitesItem) {
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
  public List<@Valid PartyInvite> getInvites() {
    return invites;
  }

  public void setInvites(List<@Valid PartyInvite> invites) {
    this.invites = invites;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyInvitesResponse partyInvitesResponse = (PartyInvitesResponse) o;
    return Objects.equals(this.invites, partyInvitesResponse.invites);
  }

  @Override
  public int hashCode() {
    return Objects.hash(invites);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyInvitesResponse {\n");
    sb.append("    invites: ").append(toIndentedString(invites)).append("\n");
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

