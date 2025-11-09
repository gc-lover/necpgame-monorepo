package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
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
 * MatchSearchTicket
 */


public class MatchSearchTicket {

  private UUID ticketId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    QUEUED("QUEUED"),
    
    BUILDING("BUILDING"),
    
    MATCH_FOUND("MATCH_FOUND");

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

  private Integer estimatedWaitSeconds;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  @Valid
  private List<UUID> queueIds = new ArrayList<>();

  public MatchSearchTicket() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MatchSearchTicket(UUID ticketId, StatusEnum status, Integer estimatedWaitSeconds) {
    this.ticketId = ticketId;
    this.status = status;
    this.estimatedWaitSeconds = estimatedWaitSeconds;
  }

  public MatchSearchTicket ticketId(UUID ticketId) {
    this.ticketId = ticketId;
    return this;
  }

  /**
   * Get ticketId
   * @return ticketId
   */
  @NotNull @Valid 
  @Schema(name = "ticketId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ticketId")
  public UUID getTicketId() {
    return ticketId;
  }

  public void setTicketId(UUID ticketId) {
    this.ticketId = ticketId;
  }

  public MatchSearchTicket status(StatusEnum status) {
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

  public MatchSearchTicket estimatedWaitSeconds(Integer estimatedWaitSeconds) {
    this.estimatedWaitSeconds = estimatedWaitSeconds;
    return this;
  }

  /**
   * Get estimatedWaitSeconds
   * minimum: 0
   * @return estimatedWaitSeconds
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "estimatedWaitSeconds", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("estimatedWaitSeconds")
  public Integer getEstimatedWaitSeconds() {
    return estimatedWaitSeconds;
  }

  public void setEstimatedWaitSeconds(Integer estimatedWaitSeconds) {
    this.estimatedWaitSeconds = estimatedWaitSeconds;
  }

  public MatchSearchTicket expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public MatchSearchTicket queueIds(List<UUID> queueIds) {
    this.queueIds = queueIds;
    return this;
  }

  public MatchSearchTicket addQueueIdsItem(UUID queueIdsItem) {
    if (this.queueIds == null) {
      this.queueIds = new ArrayList<>();
    }
    this.queueIds.add(queueIdsItem);
    return this;
  }

  /**
   * Get queueIds
   * @return queueIds
   */
  @Valid 
  @Schema(name = "queueIds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("queueIds")
  public List<UUID> getQueueIds() {
    return queueIds;
  }

  public void setQueueIds(List<UUID> queueIds) {
    this.queueIds = queueIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchSearchTicket matchSearchTicket = (MatchSearchTicket) o;
    return Objects.equals(this.ticketId, matchSearchTicket.ticketId) &&
        Objects.equals(this.status, matchSearchTicket.status) &&
        Objects.equals(this.estimatedWaitSeconds, matchSearchTicket.estimatedWaitSeconds) &&
        Objects.equals(this.expiresAt, matchSearchTicket.expiresAt) &&
        Objects.equals(this.queueIds, matchSearchTicket.queueIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticketId, status, estimatedWaitSeconds, expiresAt, queueIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchSearchTicket {\n");
    sb.append("    ticketId: ").append(toIndentedString(ticketId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    estimatedWaitSeconds: ").append(toIndentedString(estimatedWaitSeconds)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    queueIds: ").append(toIndentedString(queueIds)).append("\n");
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

