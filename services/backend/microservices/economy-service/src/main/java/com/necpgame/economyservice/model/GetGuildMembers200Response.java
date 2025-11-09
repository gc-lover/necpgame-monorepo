package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.GuildMember;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetGuildMembers200Response
 */

@JsonTypeName("getGuildMembers_200_response")

public class GetGuildMembers200Response {

  @Valid
  private List<@Valid GuildMember> members = new ArrayList<>();

  @Valid
  private Map<String, Integer> rolesDistribution = new HashMap<>();

  public GetGuildMembers200Response members(List<@Valid GuildMember> members) {
    this.members = members;
    return this;
  }

  public GetGuildMembers200Response addMembersItem(GuildMember membersItem) {
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

  public GetGuildMembers200Response rolesDistribution(Map<String, Integer> rolesDistribution) {
    this.rolesDistribution = rolesDistribution;
    return this;
  }

  public GetGuildMembers200Response putRolesDistributionItem(String key, Integer rolesDistributionItem) {
    if (this.rolesDistribution == null) {
      this.rolesDistribution = new HashMap<>();
    }
    this.rolesDistribution.put(key, rolesDistributionItem);
    return this;
  }

  /**
   * Get rolesDistribution
   * @return rolesDistribution
   */
  
  @Schema(name = "roles_distribution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roles_distribution")
  public Map<String, Integer> getRolesDistribution() {
    return rolesDistribution;
  }

  public void setRolesDistribution(Map<String, Integer> rolesDistribution) {
    this.rolesDistribution = rolesDistribution;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetGuildMembers200Response getGuildMembers200Response = (GetGuildMembers200Response) o;
    return Objects.equals(this.members, getGuildMembers200Response.members) &&
        Objects.equals(this.rolesDistribution, getGuildMembers200Response.rolesDistribution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(members, rolesDistribution);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetGuildMembers200Response {\n");
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
    sb.append("    rolesDistribution: ").append(toIndentedString(rolesDistribution)).append("\n");
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

