package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * RequestEscortRequest
 */

@JsonTypeName("requestEscort_request")

public class RequestEscortRequest {

  private @Nullable UUID shipmentId;

  /**
   * Gets or Sets escortType
   */
  public enum EscortTypeEnum {
    NPC("NPC"),
    
    PLAYER("PLAYER");

    private final String value;

    EscortTypeEnum(String value) {
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
    public static EscortTypeEnum fromValue(String value) {
      for (EscortTypeEnum b : EscortTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable EscortTypeEnum escortType;

  private @Nullable Integer payment;

  public RequestEscortRequest shipmentId(@Nullable UUID shipmentId) {
    this.shipmentId = shipmentId;
    return this;
  }

  /**
   * Get shipmentId
   * @return shipmentId
   */
  @Valid 
  @Schema(name = "shipment_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("shipment_id")
  public @Nullable UUID getShipmentId() {
    return shipmentId;
  }

  public void setShipmentId(@Nullable UUID shipmentId) {
    this.shipmentId = shipmentId;
  }

  public RequestEscortRequest escortType(@Nullable EscortTypeEnum escortType) {
    this.escortType = escortType;
    return this;
  }

  /**
   * Get escortType
   * @return escortType
   */
  
  @Schema(name = "escort_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escort_type")
  public @Nullable EscortTypeEnum getEscortType() {
    return escortType;
  }

  public void setEscortType(@Nullable EscortTypeEnum escortType) {
    this.escortType = escortType;
  }

  public RequestEscortRequest payment(@Nullable Integer payment) {
    this.payment = payment;
    return this;
  }

  /**
   * Get payment
   * @return payment
   */
  
  @Schema(name = "payment", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("payment")
  public @Nullable Integer getPayment() {
    return payment;
  }

  public void setPayment(@Nullable Integer payment) {
    this.payment = payment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RequestEscortRequest requestEscortRequest = (RequestEscortRequest) o;
    return Objects.equals(this.shipmentId, requestEscortRequest.shipmentId) &&
        Objects.equals(this.escortType, requestEscortRequest.escortType) &&
        Objects.equals(this.payment, requestEscortRequest.payment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(shipmentId, escortType, payment);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RequestEscortRequest {\n");
    sb.append("    shipmentId: ").append(toIndentedString(shipmentId)).append("\n");
    sb.append("    escortType: ").append(toIndentedString(escortType)).append("\n");
    sb.append("    payment: ").append(toIndentedString(payment)).append("\n");
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

