package com.necpgame.backjava.model;

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
 * Party
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Party {

  private @Nullable String partyId;

  private @Nullable String leaderId;

  @Valid
  private List<String> members = new ArrayList<>();

  private @Nullable Integer maxMembers;

  private @Nullable String lootMode;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  public Party partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "party_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("party_id")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public Party leaderId(@Nullable String leaderId) {
    this.leaderId = leaderId;
    return this;
  }

  /**
   * Get leaderId
   * @return leaderId
   */
  
  @Schema(name = "leader_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leader_id")
  public @Nullable String getLeaderId() {
    return leaderId;
  }

  public void setLeaderId(@Nullable String leaderId) {
    this.leaderId = leaderId;
  }

  public Party members(List<String> members) {
    this.members = members;
    return this;
  }

  public Party addMembersItem(String membersItem) {
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
  
  @Schema(name = "members", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("members")
  public List<String> getMembers() {
    return members;
  }

  public void setMembers(List<String> members) {
    this.members = members;
  }

  public Party maxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
    return this;
  }

  /**
   * Get maxMembers
   * @return maxMembers
   */
  
  @Schema(name = "max_members", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_members")
  public @Nullable Integer getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
  }

  public Party lootMode(@Nullable String lootMode) {
    this.lootMode = lootMode;
    return this;
  }

  /**
   * Get lootMode
   * @return lootMode
   */
  
  @Schema(name = "loot_mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loot_mode")
  public @Nullable String getLootMode() {
    return lootMode;
  }

  public void setLootMode(@Nullable String lootMode) {
    this.lootMode = lootMode;
  }

  public Party createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Party party = (Party) o;
    return Objects.equals(this.partyId, party.partyId) &&
        Objects.equals(this.leaderId, party.leaderId) &&
        Objects.equals(this.members, party.members) &&
        Objects.equals(this.maxMembers, party.maxMembers) &&
        Objects.equals(this.lootMode, party.lootMode) &&
        Objects.equals(this.createdAt, party.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId, leaderId, members, maxMembers, lootMode, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Party {\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    leaderId: ").append(toIndentedString(leaderId)).append("\n");
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
    sb.append("    maxMembers: ").append(toIndentedString(maxMembers)).append("\n");
    sb.append("    lootMode: ").append(toIndentedString(lootMode)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

