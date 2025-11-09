package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * LootReleaseRequest
 */


public class LootReleaseRequest {

  private UUID reservationId;

  private @Nullable String reason;

  public LootReleaseRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootReleaseRequest(UUID reservationId) {
    this.reservationId = reservationId;
  }

  public LootReleaseRequest reservationId(UUID reservationId) {
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

  public LootReleaseRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootReleaseRequest lootReleaseRequest = (LootReleaseRequest) o;
    return Objects.equals(this.reservationId, lootReleaseRequest.reservationId) &&
        Objects.equals(this.reason, lootReleaseRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reservationId, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootReleaseRequest {\n");
    sb.append("    reservationId: ").append(toIndentedString(reservationId)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

