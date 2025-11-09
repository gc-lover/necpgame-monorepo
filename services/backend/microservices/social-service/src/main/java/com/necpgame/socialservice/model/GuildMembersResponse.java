package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.socialservice.model.GuildMember;
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
 * GuildMembersResponse
 */


public class GuildMembersResponse {

  private @Nullable String guildId;

  @Valid
  private List<@Valid GuildMember> members = new ArrayList<>();

  public GuildMembersResponse guildId(@Nullable String guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  
  @Schema(name = "guildId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildId")
  public @Nullable String getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable String guildId) {
    this.guildId = guildId;
  }

  public GuildMembersResponse members(List<@Valid GuildMember> members) {
    this.members = members;
    return this;
  }

  public GuildMembersResponse addMembersItem(GuildMember membersItem) {
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
  public List<@Valid GuildMember> getMembers() {
    return members;
  }

  public void setMembers(List<@Valid GuildMember> members) {
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
    GuildMembersResponse guildMembersResponse = (GuildMembersResponse) o;
    return Objects.equals(this.guildId, guildMembersResponse.guildId) &&
        Objects.equals(this.members, guildMembersResponse.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guildId, members);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildMembersResponse {\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
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

