package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * CreateConvoyRequest
 */


public class CreateConvoyRequest {

  private @Nullable UUID leaderCharacterId;

  @Valid
  private List<UUID> shipments = new ArrayList<>();

  private Integer maxMembers = 4;

  public CreateConvoyRequest leaderCharacterId(@Nullable UUID leaderCharacterId) {
    this.leaderCharacterId = leaderCharacterId;
    return this;
  }

  /**
   * Get leaderCharacterId
   * @return leaderCharacterId
   */
  @Valid 
  @Schema(name = "leader_character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leader_character_id")
  public @Nullable UUID getLeaderCharacterId() {
    return leaderCharacterId;
  }

  public void setLeaderCharacterId(@Nullable UUID leaderCharacterId) {
    this.leaderCharacterId = leaderCharacterId;
  }

  public CreateConvoyRequest shipments(List<UUID> shipments) {
    this.shipments = shipments;
    return this;
  }

  public CreateConvoyRequest addShipmentsItem(UUID shipmentsItem) {
    if (this.shipments == null) {
      this.shipments = new ArrayList<>();
    }
    this.shipments.add(shipmentsItem);
    return this;
  }

  /**
   * Get shipments
   * @return shipments
   */
  @Valid 
  @Schema(name = "shipments", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shipments")
  public List<UUID> getShipments() {
    return shipments;
  }

  public void setShipments(List<UUID> shipments) {
    this.shipments = shipments;
  }

  public CreateConvoyRequest maxMembers(Integer maxMembers) {
    this.maxMembers = maxMembers;
    return this;
  }

  /**
   * Get maxMembers
   * @return maxMembers
   */
  
  @Schema(name = "max_members", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_members")
  public Integer getMaxMembers() {
    return maxMembers;
  }

  public void setMaxMembers(Integer maxMembers) {
    this.maxMembers = maxMembers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateConvoyRequest createConvoyRequest = (CreateConvoyRequest) o;
    return Objects.equals(this.leaderCharacterId, createConvoyRequest.leaderCharacterId) &&
        Objects.equals(this.shipments, createConvoyRequest.shipments) &&
        Objects.equals(this.maxMembers, createConvoyRequest.maxMembers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(leaderCharacterId, shipments, maxMembers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateConvoyRequest {\n");
    sb.append("    leaderCharacterId: ").append(toIndentedString(leaderCharacterId)).append("\n");
    sb.append("    shipments: ").append(toIndentedString(shipments)).append("\n");
    sb.append("    maxMembers: ").append(toIndentedString(maxMembers)).append("\n");
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

