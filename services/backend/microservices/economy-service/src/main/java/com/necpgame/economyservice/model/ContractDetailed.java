package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.economyservice.model.ContractHistoryEntry;
import com.necpgame.economyservice.model.ContractTerms;
import com.necpgame.economyservice.model.Dispute;
import com.necpgame.economyservice.model.EscrowStatus;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * ContractDetailed
 */


public class ContractDetailed {

  private @Nullable UUID contractId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    EXCHANGE("EXCHANGE"),
    
    SERVICE("SERVICE"),
    
    COURIER("COURIER"),
    
    AUCTION("AUCTION");

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

  private @Nullable String title;

  private @Nullable UUID creatorId;

  private JsonNullable<UUID> executorId = JsonNullable.<UUID>undefined();

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    DRAFT("DRAFT"),
    
    PENDING("PENDING"),
    
    ACTIVE("ACTIVE"),
    
    COMPLETED("COMPLETED"),
    
    CANCELLED("CANCELLED"),
    
    DISPUTED("DISPUTED");

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

  private @Nullable Integer payment;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> deadline = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable String description;

  private @Nullable ContractTerms terms;

  private @Nullable EscrowStatus escrow;

  private JsonNullable<Object> completionProof = JsonNullable.<Object>undefined();

  private @Nullable Dispute dispute;

  @Valid
  private List<@Valid ContractHistoryEntry> history = new ArrayList<>();

  public ContractDetailed contractId(@Nullable UUID contractId) {
    this.contractId = contractId;
    return this;
  }

  /**
   * Get contractId
   * @return contractId
   */
  @Valid 
  @Schema(name = "contract_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("contract_id")
  public @Nullable UUID getContractId() {
    return contractId;
  }

  public void setContractId(@Nullable UUID contractId) {
    this.contractId = contractId;
  }

  public ContractDetailed type(@Nullable TypeEnum type) {
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

  public ContractDetailed title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public ContractDetailed creatorId(@Nullable UUID creatorId) {
    this.creatorId = creatorId;
    return this;
  }

  /**
   * Get creatorId
   * @return creatorId
   */
  @Valid 
  @Schema(name = "creator_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("creator_id")
  public @Nullable UUID getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(@Nullable UUID creatorId) {
    this.creatorId = creatorId;
  }

  public ContractDetailed executorId(UUID executorId) {
    this.executorId = JsonNullable.of(executorId);
    return this;
  }

  /**
   * Get executorId
   * @return executorId
   */
  @Valid 
  @Schema(name = "executor_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("executor_id")
  public JsonNullable<UUID> getExecutorId() {
    return executorId;
  }

  public void setExecutorId(JsonNullable<UUID> executorId) {
    this.executorId = executorId;
  }

  public ContractDetailed status(@Nullable StatusEnum status) {
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

  public ContractDetailed payment(@Nullable Integer payment) {
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

  public ContractDetailed createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ContractDetailed deadline(OffsetDateTime deadline) {
    this.deadline = JsonNullable.of(deadline);
    return this;
  }

  /**
   * Get deadline
   * @return deadline
   */
  @Valid 
  @Schema(name = "deadline", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deadline")
  public JsonNullable<OffsetDateTime> getDeadline() {
    return deadline;
  }

  public void setDeadline(JsonNullable<OffsetDateTime> deadline) {
    this.deadline = deadline;
  }

  public ContractDetailed description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public ContractDetailed terms(@Nullable ContractTerms terms) {
    this.terms = terms;
    return this;
  }

  /**
   * Get terms
   * @return terms
   */
  @Valid 
  @Schema(name = "terms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("terms")
  public @Nullable ContractTerms getTerms() {
    return terms;
  }

  public void setTerms(@Nullable ContractTerms terms) {
    this.terms = terms;
  }

  public ContractDetailed escrow(@Nullable EscrowStatus escrow) {
    this.escrow = escrow;
    return this;
  }

  /**
   * Get escrow
   * @return escrow
   */
  @Valid 
  @Schema(name = "escrow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escrow")
  public @Nullable EscrowStatus getEscrow() {
    return escrow;
  }

  public void setEscrow(@Nullable EscrowStatus escrow) {
    this.escrow = escrow;
  }

  public ContractDetailed completionProof(Object completionProof) {
    this.completionProof = JsonNullable.of(completionProof);
    return this;
  }

  /**
   * Get completionProof
   * @return completionProof
   */
  
  @Schema(name = "completion_proof", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completion_proof")
  public JsonNullable<Object> getCompletionProof() {
    return completionProof;
  }

  public void setCompletionProof(JsonNullable<Object> completionProof) {
    this.completionProof = completionProof;
  }

  public ContractDetailed dispute(@Nullable Dispute dispute) {
    this.dispute = dispute;
    return this;
  }

  /**
   * Get dispute
   * @return dispute
   */
  @Valid 
  @Schema(name = "dispute", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dispute")
  public @Nullable Dispute getDispute() {
    return dispute;
  }

  public void setDispute(@Nullable Dispute dispute) {
    this.dispute = dispute;
  }

  public ContractDetailed history(List<@Valid ContractHistoryEntry> history) {
    this.history = history;
    return this;
  }

  public ContractDetailed addHistoryItem(ContractHistoryEntry historyItem) {
    if (this.history == null) {
      this.history = new ArrayList<>();
    }
    this.history.add(historyItem);
    return this;
  }

  /**
   * Get history
   * @return history
   */
  @Valid 
  @Schema(name = "history", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("history")
  public List<@Valid ContractHistoryEntry> getHistory() {
    return history;
  }

  public void setHistory(List<@Valid ContractHistoryEntry> history) {
    this.history = history;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractDetailed contractDetailed = (ContractDetailed) o;
    return Objects.equals(this.contractId, contractDetailed.contractId) &&
        Objects.equals(this.type, contractDetailed.type) &&
        Objects.equals(this.title, contractDetailed.title) &&
        Objects.equals(this.creatorId, contractDetailed.creatorId) &&
        equalsNullable(this.executorId, contractDetailed.executorId) &&
        Objects.equals(this.status, contractDetailed.status) &&
        Objects.equals(this.payment, contractDetailed.payment) &&
        Objects.equals(this.createdAt, contractDetailed.createdAt) &&
        equalsNullable(this.deadline, contractDetailed.deadline) &&
        Objects.equals(this.description, contractDetailed.description) &&
        Objects.equals(this.terms, contractDetailed.terms) &&
        Objects.equals(this.escrow, contractDetailed.escrow) &&
        equalsNullable(this.completionProof, contractDetailed.completionProof) &&
        Objects.equals(this.dispute, contractDetailed.dispute) &&
        Objects.equals(this.history, contractDetailed.history);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(contractId, type, title, creatorId, hashCodeNullable(executorId), status, payment, createdAt, hashCodeNullable(deadline), description, terms, escrow, hashCodeNullable(completionProof), dispute, history);
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
    sb.append("class ContractDetailed {\n");
    sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    creatorId: ").append(toIndentedString(creatorId)).append("\n");
    sb.append("    executorId: ").append(toIndentedString(executorId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    payment: ").append(toIndentedString(payment)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    deadline: ").append(toIndentedString(deadline)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    terms: ").append(toIndentedString(terms)).append("\n");
    sb.append("    escrow: ").append(toIndentedString(escrow)).append("\n");
    sb.append("    completionProof: ").append(toIndentedString(completionProof)).append("\n");
    sb.append("    dispute: ").append(toIndentedString(dispute)).append("\n");
    sb.append("    history: ").append(toIndentedString(history)).append("\n");
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

