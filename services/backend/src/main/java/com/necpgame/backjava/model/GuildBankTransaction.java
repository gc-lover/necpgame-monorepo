package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
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
 * GuildBankTransaction
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuildBankTransaction {

  private @Nullable String transactionId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    DEPOSIT("deposit"),
    
    WITHDRAW("withdraw"),
    
    PURCHASE("purchase");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable Integer amount;

  private @Nullable String itemId;

  private @Nullable String initiatorId;

  private @Nullable String approvedBy;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    APPROVED("approved"),
    
    REJECTED("rejected");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timestamp;

  private @Nullable String notes;

  public GuildBankTransaction transactionId(@Nullable String transactionId) {
    this.transactionId = transactionId;
    return this;
  }

  /**
   * Get transactionId
   * @return transactionId
   */
  
  @Schema(name = "transactionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("transactionId")
  public @Nullable String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(@Nullable String transactionId) {
    this.transactionId = transactionId;
  }

  public GuildBankTransaction type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public GuildBankTransaction amount(@Nullable Integer amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
   * @return amount
   */
  
  @Schema(name = "amount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public @Nullable Integer getAmount() {
    return amount;
  }

  public void setAmount(@Nullable Integer amount) {
    this.amount = amount;
  }

  public GuildBankTransaction itemId(@Nullable String itemId) {
    this.itemId = itemId;
    return this;
  }

  /**
   * Get itemId
   * @return itemId
   */
  
  @Schema(name = "itemId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("itemId")
  public @Nullable String getItemId() {
    return itemId;
  }

  public void setItemId(@Nullable String itemId) {
    this.itemId = itemId;
  }

  public GuildBankTransaction initiatorId(@Nullable String initiatorId) {
    this.initiatorId = initiatorId;
    return this;
  }

  /**
   * Get initiatorId
   * @return initiatorId
   */
  
  @Schema(name = "initiatorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("initiatorId")
  public @Nullable String getInitiatorId() {
    return initiatorId;
  }

  public void setInitiatorId(@Nullable String initiatorId) {
    this.initiatorId = initiatorId;
  }

  public GuildBankTransaction approvedBy(@Nullable String approvedBy) {
    this.approvedBy = approvedBy;
    return this;
  }

  /**
   * Get approvedBy
   * @return approvedBy
   */
  
  @Schema(name = "approvedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("approvedBy")
  public @Nullable String getApprovedBy() {
    return approvedBy;
  }

  public void setApprovedBy(@Nullable String approvedBy) {
    this.approvedBy = approvedBy;
  }

  public GuildBankTransaction status(@Nullable StatusEnum status) {
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

  public GuildBankTransaction timestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Get timestamp
   * @return timestamp
   */
  @Valid 
  @Schema(name = "timestamp", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public @Nullable OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(@Nullable OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public GuildBankTransaction notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildBankTransaction guildBankTransaction = (GuildBankTransaction) o;
    return Objects.equals(this.transactionId, guildBankTransaction.transactionId) &&
        Objects.equals(this.type, guildBankTransaction.type) &&
        Objects.equals(this.amount, guildBankTransaction.amount) &&
        Objects.equals(this.itemId, guildBankTransaction.itemId) &&
        Objects.equals(this.initiatorId, guildBankTransaction.initiatorId) &&
        Objects.equals(this.approvedBy, guildBankTransaction.approvedBy) &&
        Objects.equals(this.status, guildBankTransaction.status) &&
        Objects.equals(this.timestamp, guildBankTransaction.timestamp) &&
        Objects.equals(this.notes, guildBankTransaction.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(transactionId, type, amount, itemId, initiatorId, approvedBy, status, timestamp, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildBankTransaction {\n");
    sb.append("    transactionId: ").append(toIndentedString(transactionId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    itemId: ").append(toIndentedString(itemId)).append("\n");
    sb.append("    initiatorId: ").append(toIndentedString(initiatorId)).append("\n");
    sb.append("    approvedBy: ").append(toIndentedString(approvedBy)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

