package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.CharacterSlotState;
import java.net.URI;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
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
 * CharacterSlotPurchaseResponse
 */


public class CharacterSlotPurchaseResponse {

  private String transactionId;

  private CharacterSlotState slots;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    COMPLETED("completed");

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

  private JsonNullable<URI> economyLink = JsonNullable.<URI>undefined();

  public CharacterSlotPurchaseResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSlotPurchaseResponse(String transactionId, CharacterSlotState slots, StatusEnum status) {
    this.transactionId = transactionId;
    this.slots = slots;
    this.status = status;
  }

  public CharacterSlotPurchaseResponse transactionId(String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  /**
   * Get transactionId
   * @return transactionId
   */
  @NotNull 
  @Schema(name = "transactionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("transactionId")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  public CharacterSlotPurchaseResponse slots(CharacterSlotState slots) {
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

  public CharacterSlotPurchaseResponse status(StatusEnum status) {
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

  public CharacterSlotPurchaseResponse economyLink(URI economyLink) {
    this.economyLink = JsonNullable.of(economyLink);
    return this;
  }

  /**
   * Get economyLink
   * @return economyLink
   */
  @Valid 
  @Schema(name = "economyLink", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economyLink")
  public JsonNullable<URI> getEconomyLink() {
    return economyLink;
  }

  public void setEconomyLink(JsonNullable<URI> economyLink) {
    this.economyLink = economyLink;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSlotPurchaseResponse characterSlotPurchaseResponse = (CharacterSlotPurchaseResponse) o;
    return Objects.equals(this.transactionId, characterSlotPurchaseResponse.transactionId) &&
        Objects.equals(this.slots, characterSlotPurchaseResponse.slots) &&
        Objects.equals(this.status, characterSlotPurchaseResponse.status) &&
        equalsNullable(this.economyLink, characterSlotPurchaseResponse.economyLink);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, slots, status, hashCodeNullable(economyLink));
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
    sb.append("class CharacterSlotPurchaseResponse {\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    economyLink: ").append(toIndentedString(economyLink)).append("\n");
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

