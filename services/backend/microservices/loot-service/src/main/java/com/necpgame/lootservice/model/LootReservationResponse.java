package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.lootservice.model.LootItem;
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
 * LootReservationResponse
 */


public class LootReservationResponse {

  private UUID reservationId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    RELEASED("RELEASED"),
    
    EXPIRED("EXPIRED");

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

  private StatusEnum status;

  private @Nullable LootItem reservedItem;

  public LootReservationResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootReservationResponse(UUID reservationId, StatusEnum status) {
    this.reservationId = reservationId;
    this.status = status;
  }

  public LootReservationResponse reservationId(UUID reservationId) {
    this.reservationId = reservationId;
    return this;
  }

  /**
   * Get reservationId
   * @return reservationId
   */
  @NotNull @Valid 
  @Schema(name = "reservationId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reservationId")
  public UUID getReservationId() {
    return reservationId;
  }

  public void setReservationId(UUID reservationId) {
    this.reservationId = reservationId;
  }

  public LootReservationResponse status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public LootReservationResponse reservedItem(@Nullable LootItem reservedItem) {
    this.reservedItem = reservedItem;
    return this;
  }

  /**
   * Get reservedItem
   * @return reservedItem
   */
  @Valid 
  @Schema(name = "reservedItem", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reservedItem")
  public @Nullable LootItem getReservedItem() {
    return reservedItem;
  }

  public void setReservedItem(@Nullable LootItem reservedItem) {
    this.reservedItem = reservedItem;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootReservationResponse lootReservationResponse = (LootReservationResponse) o;
    return Objects.equals(this.reservationId, lootReservationResponse.reservationId) &&
        Objects.equals(this.status, lootReservationResponse.status) &&
        Objects.equals(this.reservedItem, lootReservationResponse.reservedItem);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reservationId, status, reservedItem);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootReservationResponse {\n");
    sb.append("    reservationId: ").append(toIndentedString(reservationId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    reservedItem: ").append(toIndentedString(reservedItem)).append("\n");
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

