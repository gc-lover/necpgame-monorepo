package com.necpgame.partyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.partyservice.model.LootSettings;
import com.necpgame.partyservice.model.PartyMember;
import com.necpgame.partyservice.model.PartyQueueStatus;
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
 * PartyDetail
 */


public class PartyDetail {

  private @Nullable String partyId;

  private @Nullable String name;

  private @Nullable String leaderId;

  private @Nullable String mode;

  private @Nullable String visibility;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    IDLE("IDLE"),
    
    MATCHMAKING("MATCHMAKING"),
    
    IN_COMBAT("IN_COMBAT"),
    
    COMPLETED("COMPLETED");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable Integer maxMembers;

  private @Nullable LootSettings lootSettings;

  private @Nullable PartyQueueStatus queueStatus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @Valid
  private List<@Valid PartyMember> members = new ArrayList<>();

  public PartyDetail partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public PartyDetail name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public PartyDetail leaderId(@Nullable String leaderId) {
    this.leaderId = leaderId;
    return this;
  }

  /**
   * Get leaderId
   * @return leaderId
   */
  
  @Schema(name = "leaderId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leaderId")
  public @Nullable String getLeaderId() {
    return leaderId;
  }

  public void setLeaderId(@Nullable String leaderId) {
    this.leaderId = leaderId;
  }

  public PartyDetail mode(@Nullable String mode) {
    this.mode = mode;
    return this;
  }

  /**
   * Get mode
   * @return mode
   */
  
  @Schema(name = "mode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mode")
  public @Nullable String getMode() {
    return mode;
  }

  public void setMode(@Nullable String mode) {
    this.mode = mode;
  }

  public PartyDetail visibility(@Nullable String visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public @Nullable String getVisibility() {
    return visibility;
  }

  public void setVisibility(@Nullable String visibility) {
    this.visibility = visibility;
  }

  public PartyDetail status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public PartyDetail maxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
    return this;
  }

  /**
   * Get maxMembers
   * @return maxMembers
   */
  
  @Schema(name = "maxMembers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxMembers")
  public @Nullable Integer getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(@Nullable Integer maxMembers) {
    this.maxMembers = maxMembers;
  }

  public PartyDetail lootSettings(@Nullable LootSettings lootSettings) {
    this.lootSettings = lootSettings;
    return this;
  }

  /**
   * Get lootSettings
   * @return lootSettings
   */
  @Valid 
  @Schema(name = "lootSettings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lootSettings")
  public @Nullable LootSettings getLootSettings() {
    return lootSettings;
  }

  public void setLootSettings(@Nullable LootSettings lootSettings) {
    this.lootSettings = lootSettings;
  }

  public PartyDetail queueStatus(@Nullable PartyQueueStatus queueStatus) {
    this.queueStatus = queueStatus;
    return this;
  }

  /**
   * Get queueStatus
   * @return queueStatus
   */
  @Valid 
  @Schema(name = "queueStatus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueStatus")
  public @Nullable PartyQueueStatus getQueueStatus() {
    return queueStatus;
  }

  public void setQueueStatus(@Nullable PartyQueueStatus queueStatus) {
    this.queueStatus = queueStatus;
  }

  public PartyDetail createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public PartyDetail members(List<@Valid PartyMember> members) {
    this.members = members;
    return this;
  }

  public PartyDetail addMembersItem(PartyMember membersItem) {
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
  public List<@Valid PartyMember> getMembers() {
    return members;
  }

  public void setMembers(List<@Valid PartyMember> members) {
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
    PartyDetail partyDetail = (PartyDetail) o;
    return Objects.equals(this.partyId, partyDetail.partyId) &&
        Objects.equals(this.name, partyDetail.name) &&
        Objects.equals(this.leaderId, partyDetail.leaderId) &&
        Objects.equals(this.mode, partyDetail.mode) &&
        Objects.equals(this.visibility, partyDetail.visibility) &&
        Objects.equals(this.status, partyDetail.status) &&
        Objects.equals(this.maxMembers, partyDetail.maxMembers) &&
        Objects.equals(this.lootSettings, partyDetail.lootSettings) &&
        Objects.equals(this.queueStatus, partyDetail.queueStatus) &&
        Objects.equals(this.createdAt, partyDetail.createdAt) &&
        Objects.equals(this.members, partyDetail.members);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId, name, leaderId, mode, visibility, status, maxMembers, lootSettings, queueStatus, createdAt, members);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyDetail {\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    leaderId: ").append(toIndentedString(leaderId)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    maxMembers: ").append(toIndentedString(maxMembers)).append("\n");
    sb.append("    lootSettings: ").append(toIndentedString(lootSettings)).append("\n");
    sb.append("    queueStatus: ").append(toIndentedString(queueStatus)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

