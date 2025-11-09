package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ItemTransfer;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReservationRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ReservationRequest {

  /**
   * Gets or Sets context
   */
  public enum ContextEnum {
    TRADE("TRADE"),
    
    MAIL("MAIL"),
    
    QUEST("QUEST"),
    
    RAID("RAID"),
    
    OTHER("OTHER");

    private final String value;

    ContextEnum(String value) {
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
    public static ContextEnum fromValue(String value) {
      for (ContextEnum b : ContextEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ContextEnum context;

  private @Nullable String referenceId;

  @Valid
  private List<@Valid ItemTransfer> items = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public ReservationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReservationRequest(ContextEnum context, List<@Valid ItemTransfer> items) {
    this.context = context;
    this.items = items;
  }

  public ReservationRequest context(ContextEnum context) {
    this.context = context;
    return this;
  }

  /**
   * Get context
   * @return context
   */
  @NotNull 
  @Schema(name = "context", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("context")
  public ContextEnum getContext() {
    return context;
  }

  public void setContext(ContextEnum context) {
    this.context = context;
  }

  public ReservationRequest referenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
    return this;
  }

  /**
   * Get referenceId
   * @return referenceId
   */
  
  @Schema(name = "referenceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referenceId")
  public @Nullable String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
  }

  public ReservationRequest items(List<@Valid ItemTransfer> items) {
    this.items = items;
    return this;
  }

  public ReservationRequest addItemsItem(ItemTransfer itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
   */
  @NotNull @Valid 
  @Schema(name = "items", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("items")
  public List<@Valid ItemTransfer> getItems() {
    return items;
  }

  public void setItems(List<@Valid ItemTransfer> items) {
    this.items = items;
  }

  public ReservationRequest expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReservationRequest reservationRequest = (ReservationRequest) o;
    return Objects.equals(this.context, reservationRequest.context) &&
        Objects.equals(this.referenceId, reservationRequest.referenceId) &&
        Objects.equals(this.items, reservationRequest.items) &&
        Objects.equals(this.expiresAt, reservationRequest.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(context, referenceId, items, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReservationRequest {\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
    sb.append("    referenceId: ").append(toIndentedString(referenceId)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

