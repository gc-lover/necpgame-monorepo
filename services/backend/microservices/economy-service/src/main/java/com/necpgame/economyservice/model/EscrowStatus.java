package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.EscrowStatusHeldItemsInner;
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
 * EscrowStatus
 */


public class EscrowStatus {

  private @Nullable UUID escrowId;

  private @Nullable Integer amountHeld;

  private @Nullable Integer collateralHeld;

  @Valid
  private List<String> releaseConditions = new ArrayList<>();

  @Valid
  private List<@Valid EscrowStatusHeldItemsInner> heldItems = new ArrayList<>();

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    HOLDING("HOLDING"),
    
    RELEASING("RELEASING"),
    
    RELEASED("RELEASED");

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

  public EscrowStatus escrowId(@Nullable UUID escrowId) {
    this.escrowId = escrowId;
    return this;
  }

  /**
   * Get escrowId
   * @return escrowId
   */
  @Valid 
  @Schema(name = "escrow_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escrow_id")
  public @Nullable UUID getEscrowId() {
    return escrowId;
  }

  public void setEscrowId(@Nullable UUID escrowId) {
    this.escrowId = escrowId;
  }

  public EscrowStatus amountHeld(@Nullable Integer amountHeld) {
    this.amountHeld = amountHeld;
    return this;
  }

  /**
   * Get amountHeld
   * @return amountHeld
   */
  
  @Schema(name = "amount_held", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount_held")
  public @Nullable Integer getAmountHeld() {
    return amountHeld;
  }

  public void setAmountHeld(@Nullable Integer amountHeld) {
    this.amountHeld = amountHeld;
  }

  public EscrowStatus collateralHeld(@Nullable Integer collateralHeld) {
    this.collateralHeld = collateralHeld;
    return this;
  }

  /**
   * Get collateralHeld
   * @return collateralHeld
   */
  
  @Schema(name = "collateral_held", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("collateral_held")
  public @Nullable Integer getCollateralHeld() {
    return collateralHeld;
  }

  public void setCollateralHeld(@Nullable Integer collateralHeld) {
    this.collateralHeld = collateralHeld;
  }

  public EscrowStatus releaseConditions(List<String> releaseConditions) {
    this.releaseConditions = releaseConditions;
    return this;
  }

  public EscrowStatus addReleaseConditionsItem(String releaseConditionsItem) {
    if (this.releaseConditions == null) {
      this.releaseConditions = new ArrayList<>();
    }
    this.releaseConditions.add(releaseConditionsItem);
    return this;
  }

  /**
   * Get releaseConditions
   * @return releaseConditions
   */
  
  @Schema(name = "release_conditions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("release_conditions")
  public List<String> getReleaseConditions() {
    return releaseConditions;
  }

  public void setReleaseConditions(List<String> releaseConditions) {
    this.releaseConditions = releaseConditions;
  }

  public EscrowStatus heldItems(List<@Valid EscrowStatusHeldItemsInner> heldItems) {
    this.heldItems = heldItems;
    return this;
  }

  public EscrowStatus addHeldItemsItem(EscrowStatusHeldItemsInner heldItemsItem) {
    if (this.heldItems == null) {
      this.heldItems = new ArrayList<>();
    }
    this.heldItems.add(heldItemsItem);
    return this;
  }

  /**
   * Get heldItems
   * @return heldItems
   */
  @Valid 
  @Schema(name = "held_items", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("held_items")
  public List<@Valid EscrowStatusHeldItemsInner> getHeldItems() {
    return heldItems;
  }

  public void setHeldItems(List<@Valid EscrowStatusHeldItemsInner> heldItems) {
    this.heldItems = heldItems;
  }

  public EscrowStatus status(@Nullable StatusEnum status) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EscrowStatus escrowStatus = (EscrowStatus) o;
    return Objects.equals(this.escrowId, escrowStatus.escrowId) &&
        Objects.equals(this.amountHeld, escrowStatus.amountHeld) &&
        Objects.equals(this.collateralHeld, escrowStatus.collateralHeld) &&
        Objects.equals(this.releaseConditions, escrowStatus.releaseConditions) &&
        Objects.equals(this.heldItems, escrowStatus.heldItems) &&
        Objects.equals(this.status, escrowStatus.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(escrowId, amountHeld, collateralHeld, releaseConditions, heldItems, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EscrowStatus {\n");
    sb.append("    escrowId: ").append(toIndentedString(escrowId)).append("\n");
    sb.append("    amountHeld: ").append(toIndentedString(amountHeld)).append("\n");
    sb.append("    collateralHeld: ").append(toIndentedString(collateralHeld)).append("\n");
    sb.append("    releaseConditions: ").append(toIndentedString(releaseConditions)).append("\n");
    sb.append("    heldItems: ").append(toIndentedString(heldItems)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

