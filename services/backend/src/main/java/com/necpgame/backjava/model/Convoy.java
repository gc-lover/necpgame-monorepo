package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * Convoy
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Convoy {

  private @Nullable UUID convoyId;

  private @Nullable UUID leaderId;

  @Valid
  private List<UUID> members = new ArrayList<>();

  @Valid
  private List<UUID> shipments = new ArrayList<>();

  private @Nullable String status;

  private @Nullable BigDecimal riskReduction;

  public Convoy convoyId(@Nullable UUID convoyId) {
    this.convoyId = convoyId;
    return this;
  }

  /**
   * Get convoyId
   * @return convoyId
   */
  @Valid 
  @Schema(name = "convoy_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("convoy_id")
  public @Nullable UUID getConvoyId() {
    return convoyId;
  }

  public void setConvoyId(@Nullable UUID convoyId) {
    this.convoyId = convoyId;
  }

  public Convoy leaderId(@Nullable UUID leaderId) {
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

  public Convoy members(List<UUID> members) {
    this.members = members;
    return this;
  }

  public Convoy addMembersItem(UUID membersItem) {
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
  public List<UUID> getMembers() {
    return members;
  }

  public void setMembers(List<UUID> members) {
    this.members = members;
  }

  public Convoy shipments(List<UUID> shipments) {
    this.shipments = shipments;
    return this;
  }

  public Convoy addShipmentsItem(UUID shipmentsItem) {
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

  public Convoy status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public Convoy riskReduction(@Nullable BigDecimal riskReduction) {
    this.riskReduction = riskReduction;
    return this;
  }

  /**
   * Снижение риска в конвое
   * @return riskReduction
   */
  @Valid 
  @Schema(name = "risk_reduction", description = "Снижение риска в конвое", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("risk_reduction")
  public @Nullable BigDecimal getRiskReduction() {
    return riskReduction;
  }

  public void setRiskReduction(@Nullable BigDecimal riskReduction) {
    this.riskReduction = riskReduction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Convoy convoy = (Convoy) o;
    return Objects.equals(this.convoyId, convoy.convoyId) &&
        Objects.equals(this.leaderId, convoy.leaderId) &&
        Objects.equals(this.members, convoy.members) &&
        Objects.equals(this.shipments, convoy.shipments) &&
        Objects.equals(this.status, convoy.status) &&
        Objects.equals(this.riskReduction, convoy.riskReduction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(convoyId, leaderId, members, shipments, status, riskReduction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Convoy {\n");
    sb.append("    convoyId: ").append(toIndentedString(convoyId)).append("\n");
    sb.append("    leaderId: ").append(toIndentedString(leaderId)).append("\n");
    sb.append("    members: ").append(toIndentedString(members)).append("\n");
    sb.append("    shipments: ").append(toIndentedString(shipments)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    riskReduction: ").append(toIndentedString(riskReduction)).append("\n");
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

