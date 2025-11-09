package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterSlotState;
import com.necpgame.backjava.model.CharacterSlotStateResponsePendingPaymentsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CharacterSlotStateResponse
 */


public class CharacterSlotStateResponse {

  private CharacterSlotState slots;

  @Valid
  private List<@Valid CharacterSlotStateResponsePendingPaymentsInner> pendingPayments = new ArrayList<>();

  public CharacterSlotStateResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSlotStateResponse(CharacterSlotState slots, List<@Valid CharacterSlotStateResponsePendingPaymentsInner> pendingPayments) {
    this.slots = slots;
    this.pendingPayments = pendingPayments;
  }

  public CharacterSlotStateResponse slots(CharacterSlotState slots) {
    this.slots = slots;
    return this;
  }

  /**
   * Get slots
   * @return slots
   */
  @NotNull @Valid 
  @Schema(name = "slots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slots")
  public CharacterSlotState getSlots() {
    return slots;
  }

  public void setSlots(CharacterSlotState slots) {
    this.slots = slots;
  }

  public CharacterSlotStateResponse pendingPayments(List<@Valid CharacterSlotStateResponsePendingPaymentsInner> pendingPayments) {
    this.pendingPayments = pendingPayments;
    return this;
  }

  public CharacterSlotStateResponse addPendingPaymentsItem(CharacterSlotStateResponsePendingPaymentsInner pendingPaymentsItem) {
    if (this.pendingPayments == null) {
      this.pendingPayments = new ArrayList<>();
    }
    this.pendingPayments.add(pendingPaymentsItem);
    return this;
  }

  /**
   * Get pendingPayments
   * @return pendingPayments
   */
  @NotNull @Valid 
  @Schema(name = "pendingPayments", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pendingPayments")
  public List<@Valid CharacterSlotStateResponsePendingPaymentsInner> getPendingPayments() {
    return pendingPayments;
  }

  public void setPendingPayments(List<@Valid CharacterSlotStateResponsePendingPaymentsInner> pendingPayments) {
    this.pendingPayments = pendingPayments;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSlotStateResponse characterSlotStateResponse = (CharacterSlotStateResponse) o;
    return Objects.equals(this.slots, characterSlotStateResponse.slots) &&
        Objects.equals(this.pendingPayments, characterSlotStateResponse.pendingPayments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(slots, pendingPayments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSlotStateResponse {\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
    sb.append("    pendingPayments: ").append(toIndentedString(pendingPayments)).append("\n");
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

