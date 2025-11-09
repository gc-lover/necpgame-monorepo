package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderBrief1;
import com.necpgame.socialservice.model.PlayerOrderBudgetEstimate;
import com.necpgame.socialservice.model.PlayerOrderDraftStatus;
import com.necpgame.socialservice.model.PlayerOrderValidationSummary;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderDraft
 */


public class PlayerOrderDraft {

  private UUID orderId;

  private UUID ownerId;

  private PlayerOrderDraftStatus status;

  private PlayerOrderBrief1 brief;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updatedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> lastValidatedAt = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable PlayerOrderValidationSummary validationSummary;

  private @Nullable PlayerOrderBudgetEstimate budget;

  public PlayerOrderDraft() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderDraft(UUID orderId, UUID ownerId, PlayerOrderDraftStatus status, PlayerOrderBrief1 brief, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
    this.orderId = orderId;
    this.ownerId = ownerId;
    this.status = status;
    this.brief = brief;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public PlayerOrderDraft orderId(UUID orderId) {
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

  public PlayerOrderDraft ownerId(UUID ownerId) {
    this.ownerId = ownerId;
    return this;
  }

  /**
   * Идентификатор владельца заказа.
   * @return ownerId
   */
  @NotNull @Valid 
  @Schema(name = "ownerId", description = "Идентификатор владельца заказа.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ownerId")
  public UUID getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(UUID ownerId) {
    this.ownerId = ownerId;
  }

  public PlayerOrderDraft status(PlayerOrderDraftStatus status) {
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

  public PlayerOrderDraft brief(PlayerOrderBrief1 brief) {
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

  public PlayerOrderDraft createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public PlayerOrderDraft updatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @NotNull @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("updatedAt")
  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public PlayerOrderDraft lastValidatedAt(OffsetDateTime lastValidatedAt) {
    this.lastValidatedAt = JsonNullable.of(lastValidatedAt);
    return this;
  }

  /**
   * Get lastValidatedAt
   * @return lastValidatedAt
   */
  @Valid 
  @Schema(name = "lastValidatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastValidatedAt")
  public JsonNullable<OffsetDateTime> getLastValidatedAt() {
    return lastValidatedAt;
  }

  public void setLastValidatedAt(JsonNullable<OffsetDateTime> lastValidatedAt) {
    this.lastValidatedAt = lastValidatedAt;
  }

  public PlayerOrderDraft validationSummary(@Nullable PlayerOrderValidationSummary validationSummary) {
    this.validationSummary = validationSummary;
    return this;
  }

  /**
   * Get validationSummary
   * @return validationSummary
   */
  @Valid 
  @Schema(name = "validationSummary", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("validationSummary")
  public @Nullable PlayerOrderValidationSummary getValidationSummary() {
    return validationSummary;
  }

  public void setValidationSummary(@Nullable PlayerOrderValidationSummary validationSummary) {
    this.validationSummary = validationSummary;
  }

  public PlayerOrderDraft budget(@Nullable PlayerOrderBudgetEstimate budget) {
    this.budget = budget;
    return this;
  }

  /**
   * Get budget
   * @return budget
   */
  @Valid 
  @Schema(name = "budget", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("budget")
  public @Nullable PlayerOrderBudgetEstimate getBudget() {
    return budget;
  }

  public void setBudget(@Nullable PlayerOrderBudgetEstimate budget) {
    this.budget = budget;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderDraft playerOrderDraft = (PlayerOrderDraft) o;
    return Objects.equals(this.orderId, playerOrderDraft.orderId) &&
        Objects.equals(this.ownerId, playerOrderDraft.ownerId) &&
        Objects.equals(this.status, playerOrderDraft.status) &&
        Objects.equals(this.brief, playerOrderDraft.brief) &&
        Objects.equals(this.createdAt, playerOrderDraft.createdAt) &&
        Objects.equals(this.updatedAt, playerOrderDraft.updatedAt) &&
        equalsNullable(this.lastValidatedAt, playerOrderDraft.lastValidatedAt) &&
        Objects.equals(this.validationSummary, playerOrderDraft.validationSummary) &&
        Objects.equals(this.budget, playerOrderDraft.budget);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderId, ownerId, status, brief, createdAt, updatedAt, hashCodeNullable(lastValidatedAt), validationSummary, budget);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderDraft {\n");
    sb.append("    orderId: ").append(toIndentedString(orderId)).append("\n");
    sb.append("    ownerId: ").append(toIndentedString(ownerId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    brief: ").append(toIndentedString(brief)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    lastValidatedAt: ").append(toIndentedString(lastValidatedAt)).append("\n");
    sb.append("    validationSummary: ").append(toIndentedString(validationSummary)).append("\n");
    sb.append("    budget: ").append(toIndentedString(budget)).append("\n");
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

