package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * ValidatePlayerOrderFactionsRequest
 */

@JsonTypeName("validatePlayerOrderFactions_request")

public class ValidatePlayerOrderFactionsRequest {

  private UUID orderId;

  @Valid
  private List<String> invitees = new ArrayList<>();

  public ValidatePlayerOrderFactionsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ValidatePlayerOrderFactionsRequest(UUID orderId, List<String> invitees) {
    this.orderId = orderId;
    this.invitees = invitees;
  }

  public ValidatePlayerOrderFactionsRequest orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  /**
   * Get orderId
   * @return orderId
   */
  @NotNull @Valid 
  @Schema(name = "orderId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("orderId")
  public UUID getOrderId() {
    return orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public ValidatePlayerOrderFactionsRequest invitees(List<String> invitees) {
    this.invitees = invitees;
    return this;
  }

  public ValidatePlayerOrderFactionsRequest addInviteesItem(String inviteesItem) {
    if (this.invitees == null) {
      this.invitees = new ArrayList<>();
    }
    this.invitees.add(inviteesItem);
    return this;
  }

  /**
   * Get invitees
   * @return invitees
   */
  @NotNull 
  @Schema(name = "invitees", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("invitees")
  public List<String> getInvitees() {
    return invitees;
  }

  public void setInvitees(List<String> invitees) {
    this.invitees = invitees;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidatePlayerOrderFactionsRequest validatePlayerOrderFactionsRequest = (ValidatePlayerOrderFactionsRequest) o;
    return Objects.equals(this.orderId, validatePlayerOrderFactionsRequest.orderId) &&
        Objects.equals(this.invitees, validatePlayerOrderFactionsRequest.invitees);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, invitees);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidatePlayerOrderFactionsRequest {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    invitees: ").append(toIndentedString(invitees)).append("\n");
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

