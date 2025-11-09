package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderBrief1;
import com.necpgame.socialservice.model.PlayerOrderBudgetEstimate;
import com.necpgame.socialservice.model.PlayerOrderDraftStatus;
import com.necpgame.socialservice.model.PlayerOrderGuaranteeSelection;
import com.necpgame.socialservice.model.PlayerOrderPublicationInfo;
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
 * PlayerOrderSummary
 */


public class PlayerOrderSummary {

  private UUID orderId;

  private PlayerOrderDraftStatus status;

  private PlayerOrderBrief1 brief;

  private PlayerOrderBudgetEstimate budget;

  private PlayerOrderGuaranteeSelection guarantees;

  private @Nullable PlayerOrderPublicationInfo publication;

  public PlayerOrderSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderSummary(UUID orderId, PlayerOrderDraftStatus status, PlayerOrderBrief1 brief, PlayerOrderBudgetEstimate budget, PlayerOrderGuaranteeSelection guarantees) {
    this.orderId = orderId;
    this.status = status;
    this.brief = brief;
    this.budget = budget;
    this.guarantees = guarantees;
  }

  public PlayerOrderSummary orderId(UUID orderId) {
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

  public PlayerOrderSummary status(PlayerOrderDraftStatus status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull @Valid 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public PlayerOrderDraftStatus getStatus() {
    return status;
  }

  public void setStatus(PlayerOrderDraftStatus status) {
    this.status = status;
  }

  public PlayerOrderSummary brief(PlayerOrderBrief1 brief) {
    this.brief = brief;
    return this;
  }

  /**
   * Get brief
   * @return brief
   */
  @NotNull @Valid 
  @Schema(name = "brief", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("brief")
  public PlayerOrderBrief1 getBrief() {
    return brief;
  }

  public void setBrief(PlayerOrderBrief1 brief) {
    this.brief = brief;
  }

  public PlayerOrderSummary budget(PlayerOrderBudgetEstimate budget) {
    this.budget = budget;
    return this;
  }

  /**
   * Get budget
   * @return budget
   */
  @NotNull @Valid 
  @Schema(name = "budget", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("budget")
  public PlayerOrderBudgetEstimate getBudget() {
    return budget;
  }

  public void setBudget(PlayerOrderBudgetEstimate budget) {
    this.budget = budget;
  }

  public PlayerOrderSummary guarantees(PlayerOrderGuaranteeSelection guarantees) {
    this.guarantees = guarantees;
    return this;
  }

  /**
   * Get guarantees
   * @return guarantees
   */
  @NotNull @Valid 
  @Schema(name = "guarantees", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("guarantees")
  public PlayerOrderGuaranteeSelection getGuarantees() {
    return guarantees;
  }

  public void setGuarantees(PlayerOrderGuaranteeSelection guarantees) {
    this.guarantees = guarantees;
  }

  public PlayerOrderSummary publication(@Nullable PlayerOrderPublicationInfo publication) {
    this.publication = publication;
    return this;
  }

  /**
   * Get publication
   * @return publication
   */
  @Valid 
  @Schema(name = "publication", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("publication")
  public @Nullable PlayerOrderPublicationInfo getPublication() {
    return publication;
  }

  public void setPublication(@Nullable PlayerOrderPublicationInfo publication) {
    this.publication = publication;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderSummary playerOrderSummary = (PlayerOrderSummary) o;
    return Objects.equals(this.orderId, playerOrderSummary.orderId) &&
        Objects.equals(this.status, playerOrderSummary.status) &&
        Objects.equals(this.brief, playerOrderSummary.brief) &&
        Objects.equals(this.budget, playerOrderSummary.budget) &&
        Objects.equals(this.guarantees, playerOrderSummary.guarantees) &&
        Objects.equals(this.publication, playerOrderSummary.publication);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, status, brief, budget, guarantees, publication);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderSummary {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    brief: ").append(toIndentedString(brief)).append("\n");
    sb.append("    budget: ").append(toIndentedString(budget)).append("\n");
    sb.append("    guarantees: ").append(toIndentedString(guarantees)).append("\n");
    sb.append("    publication: ").append(toIndentedString(publication)).append("\n");
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

