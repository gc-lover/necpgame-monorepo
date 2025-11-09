package com.necpgame.narrativeservice.model;

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
 * StartBlackwallRaidRequest
 */

@JsonTypeName("startBlackwallRaid_request")

public class StartBlackwallRaidRequest {

  private String partyId;

  private String leaderId;

  private @Nullable String accessToken;

  public StartBlackwallRaidRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StartBlackwallRaidRequest(String partyId, String leaderId) {
    this.partyId = partyId;
    this.leaderId = leaderId;
  }

  public StartBlackwallRaidRequest partyId(String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  @NotNull 
  @Schema(name = "party_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("party_id")
  public String getPartyId() {
    return partyId;
  }

  public void setPartyId(String partyId) {
    this.partyId = partyId;
  }

  public StartBlackwallRaidRequest leaderId(String leaderId) {
    this.leaderId = leaderId;
    return this;
  }

  /**
   * Get leaderId
   * @return leaderId
   */
  @NotNull 
  @Schema(name = "leader_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("leader_id")
  public String getLeaderId() {
    return leaderId;
  }

  public void setLeaderId(String leaderId) {
    this.leaderId = leaderId;
  }

  public StartBlackwallRaidRequest accessToken(@Nullable String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * Специальный токен доступа
   * @return accessToken
   */
  
  @Schema(name = "access_token", description = "Специальный токен доступа", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_token")
  public @Nullable String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(@Nullable String accessToken) {
    this.accessToken = accessToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartBlackwallRaidRequest startBlackwallRaidRequest = (StartBlackwallRaidRequest) o;
    return Objects.equals(this.partyId, startBlackwallRaidRequest.partyId) &&
        Objects.equals(this.leaderId, startBlackwallRaidRequest.leaderId) &&
        Objects.equals(this.accessToken, startBlackwallRaidRequest.accessToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(partyId, leaderId, accessToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartBlackwallRaidRequest {\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    leaderId: ").append(toIndentedString(leaderId)).append("\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
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

