package com.necpgame.economyservice.model;

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
 * InitiateTradeRequest
 */

@JsonTypeName("initiateTrade_request")

public class InitiateTradeRequest {

  private String initiatorCharacterId;

  private String receiverCharacterId;

  public InitiateTradeRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public InitiateTradeRequest(String initiatorCharacterId, String receiverCharacterId) {
    this.initiatorCharacterId = initiatorCharacterId;
    this.receiverCharacterId = receiverCharacterId;
  }

  public InitiateTradeRequest initiatorCharacterId(String initiatorCharacterId) {
    this.initiatorCharacterId = initiatorCharacterId;
    return this;
  }

  /**
   * Get initiatorCharacterId
   * @return initiatorCharacterId
   */
  @NotNull 
  @Schema(name = "initiator_character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("initiator_character_id")
  public String getInitiatorCharacterId() {
    return initiatorCharacterId;
  }

  public void setInitiatorCharacterId(String initiatorCharacterId) {
    this.initiatorCharacterId = initiatorCharacterId;
  }

  public InitiateTradeRequest receiverCharacterId(String receiverCharacterId) {
    this.receiverCharacterId = receiverCharacterId;
    return this;
  }

  /**
   * Get receiverCharacterId
   * @return receiverCharacterId
   */
  @NotNull 
  @Schema(name = "receiver_character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("receiver_character_id")
  public String getReceiverCharacterId() {
    return receiverCharacterId;
  }

  public void setReceiverCharacterId(String receiverCharacterId) {
    this.receiverCharacterId = receiverCharacterId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InitiateTradeRequest initiateTradeRequest = (InitiateTradeRequest) o;
    return Objects.equals(this.initiatorCharacterId, initiateTradeRequest.initiatorCharacterId) &&
        Objects.equals(this.receiverCharacterId, initiateTradeRequest.receiverCharacterId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(initiatorCharacterId, receiverCharacterId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InitiateTradeRequest {\n");
    sb.append("    initiatorCharacterId: ").append(toIndentedString(initiatorCharacterId)).append("\n");
    sb.append("    receiverCharacterId: ").append(toIndentedString(receiverCharacterId)).append("\n");
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

