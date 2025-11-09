package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
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
 * SupportTicket
 */


public class SupportTicket {

  private String ticketId;

  private String ticketNumber;

  /**
   * Gets or Sets source
   */
  public enum SourceEnum {
    PLAYER("PLAYER"),
    
    AGENT("AGENT"),
    
    SYSTEM("SYSTEM");

    private final String value;

    SourceEnum(String value) {
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
    public static SourceEnum fromValue(String value) {
      for (SourceEnum b : SourceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SourceEnum source;

  private @Nullable String playerId;

  private @Nullable String accountId;

  private @Nullable String category;

  private @Nullable String subject;

  private @Nullable String description;

  /**
   * Gets or Sets priority
   */
  public enum PriorityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    CRITICAL("critical");

    private final String value;

    PriorityEnum(String value) {
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
    public static PriorityEnum fromValue(String value) {
      for (PriorityEnum b : PriorityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PriorityEnum priority;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    OPEN("open"),
    
    PENDING("pending"),
    
    ON_HOLD("on_hold"),
    
    RESOLVED("resolved"),
    
    CLOSED("closed");

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

  private @Nullable String assignedTo;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime slaDueAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime updatedAt;

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public SupportTicket() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SupportTicket(String ticketId, String ticketNumber, PriorityEnum priority, StatusEnum status, OffsetDateTime createdAt) {
    this.ticketId = ticketId;
    this.ticketNumber = ticketNumber;
    this.priority = priority;
    this.status = status;
    this.createdAt = createdAt;
  }

  public SupportTicket ticketId(String ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  /**
   * Get ticketId
   * @return ticketId
   */
  @NotNull 
  @Schema(name = "ticketId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ticketId")
  public String getTicketId() {
    return ticketId;
  }

  public void setTicketId(String ticketId) {
    this.ticketId = ticketId;
  }

  public SupportTicket ticketNumber(String ticketNumber) {
    this.ticketNumber = ticketNumber;
    return this;
  }

  /**
   * Get ticketNumber
   * @return ticketNumber
   */
  @NotNull 
  @Schema(name = "ticketNumber", example = "NECPG-20251107-1024", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ticketNumber")
  public String getTicketNumber() {
    return ticketNumber;
  }

  public void setTicketNumber(String ticketNumber) {
    this.ticketNumber = ticketNumber;
  }

  public SupportTicket source(@Nullable SourceEnum source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable SourceEnum getSource() {
    return source;
  }

  public void setSource(@Nullable SourceEnum source) {
    this.source = source;
  }

  public SupportTicket playerId(@Nullable String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("playerId")
  public @Nullable String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(@Nullable String playerId) {
    this.playerId = playerId;
  }

  public SupportTicket accountId(@Nullable String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  
  @Schema(name = "accountId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountId")
  public @Nullable String getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable String accountId) {
    this.accountId = accountId;
  }

  public SupportTicket category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public SupportTicket subject(@Nullable String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * Get subject
   * @return subject
   */
  
  @Schema(name = "subject", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subject")
  public @Nullable String getSubject() {
    return subject;
  }

  public void setSubject(@Nullable String subject) {
    this.subject = subject;
  }

  public SupportTicket description(@Nullable String description) {
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

  public SupportTicket priority(PriorityEnum priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  @NotNull 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("priority")
  public PriorityEnum getPriority() {
    return priority;
  }

  public void setPriority(PriorityEnum priority) {
    this.priority = priority;
  }

  public SupportTicket status(StatusEnum status) {
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

  public SupportTicket assignedTo(@Nullable String assignedTo) {
    this.assignedTo = assignedTo;
    return this;
  }

  /**
   * Get assignedTo
   * @return assignedTo
   */
  
  @Schema(name = "assignedTo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assignedTo")
  public @Nullable String getAssignedTo() {
    return assignedTo;
  }

  public void setAssignedTo(@Nullable String assignedTo) {
    this.assignedTo = assignedTo;
  }

  public SupportTicket slaDueAt(@Nullable OffsetDateTime slaDueAt) {
    this.slaDueAt = slaDueAt;
    return this;
  }

  /**
   * Get slaDueAt
   * @return slaDueAt
   */
  @Valid 
  @Schema(name = "slaDueAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("slaDueAt")
  public @Nullable OffsetDateTime getSlaDueAt() {
    return slaDueAt;
  }

  public void setSlaDueAt(@Nullable OffsetDateTime slaDueAt) {
    this.slaDueAt = slaDueAt;
  }

  public SupportTicket createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public SupportTicket updatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  /**
   * Get updatedAt
   * @return updatedAt
   */
  @Valid 
  @Schema(name = "updatedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("updatedAt")
  public @Nullable OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(@Nullable OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public SupportTicket metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public SupportTicket putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SupportTicket supportTicket = (SupportTicket) o;
    return Objects.equals(this.ticketId, supportTicket.ticketId) &&
        Objects.equals(this.ticketNumber, supportTicket.ticketNumber) &&
        Objects.equals(this.source, supportTicket.source) &&
        Objects.equals(this.playerId, supportTicket.playerId) &&
        Objects.equals(this.accountId, supportTicket.accountId) &&
        Objects.equals(this.category, supportTicket.category) &&
        Objects.equals(this.subject, supportTicket.subject) &&
        Objects.equals(this.description, supportTicket.description) &&
        Objects.equals(this.priority, supportTicket.priority) &&
        Objects.equals(this.status, supportTicket.status) &&
        Objects.equals(this.assignedTo, supportTicket.assignedTo) &&
        Objects.equals(this.slaDueAt, supportTicket.slaDueAt) &&
        Objects.equals(this.createdAt, supportTicket.createdAt) &&
        Objects.equals(this.updatedAt, supportTicket.updatedAt) &&
        Objects.equals(this.metadata, supportTicket.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, ticketNumber, source, playerId, accountId, category, subject, description, priority, status, assignedTo, slaDueAt, createdAt, updatedAt, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SupportTicket {\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    ticketNumber: ").append(toIndentedString(ticketNumber)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    assignedTo: ").append(toIndentedString(assignedTo)).append("\n");
    sb.append("    slaDueAt: ").append(toIndentedString(slaDueAt)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

