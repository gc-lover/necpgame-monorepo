package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.MainGameUIDataPartyMembersInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * MainGameUIDataParty
 */

@JsonTypeName("MainGameUIData_party")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class MainGameUIDataParty {

  private @Nullable UUID leaderId;

  @Valid
  private List<@Valid MainGameUIDataPartyMembersInner> members = new ArrayList<>();

  public MainGameUIDataParty leaderId(@Nullable UUID leaderId) {
    this.leaderId = leaderId;
    return this;
  }

  /**
   * Get leaderId
   * @return leaderId
   */
  @Valid 
  @Schema(name = "leader_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leader_id")
  public @Nullable UUID getLeaderId() {
    return leaderId;
  }

  public void setLeaderId(@Nullable UUID leaderId) {
    this.leaderId = leaderId;
  }

  public MainGameUIDataParty members(List<@Valid MainGameUIDataPartyMembersInner> members) {
    this.members = members;
    return this;
  }

  public MainGameUIDataParty addMembersItem(MainGameUIDataPartyMembersInner membersItem) {
    if (this.members == null) {
      this.members = new ArrayList<>();
    }
    this.members.add(membersItem);
    return this;
  }

  /**
   * Get members
   * @return members
   */
  @Valid 
  @Schema(name = "members", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("members")
  public List<@Valid MainGameUIDataPartyMembersInner> getMembers() {
    return members;
  }

  public void setMembers(List<@Valid MainGameUIDataPartyMembersInner> members) {
    this.members = members;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MainGameUIDataParty mainGameUIDataParty = (MainGameUIDataParty) o;
    return Objects.equals(this.leaderId, mainGameUIDataParty.leaderId) &&
        Objects.equals(this.members, mainGameUIDataParty.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leaderId, members);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MainGameUIDataParty {\n");
    sb.append("    leaderId: ").append(toIndentedString(leaderId)).append("\n");
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
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

