package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.Arrays;
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
 * Dispute
 */


public class Dispute {

  private @Nullable UUID disputeId;

  private @Nullable UUID contractId;

  private @Nullable UUID openedBy;

  private @Nullable String reason;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    OPEN("OPEN"),
    
    UNDER_REVIEW("UNDER_REVIEW"),
    
    RESOLVED("RESOLVED"),
    
    REJECTED("REJECTED");

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

  private JsonNullable<String> resolution = JsonNullable.<String>undefined();

  /**
   * Gets or Sets resolvedBy
   */
  public enum ResolvedByEnum {
    CREATOR("CREATOR"),
    
    EXECUTOR("EXECUTOR"),
    
    ARBITRATOR("ARBITRATOR"),
    
    AUTO("AUTO");

    private final String value;

    ResolvedByEnum(String value) {
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
    public static ResolvedByEnum fromValue(String value) {
      for (ResolvedByEnum b : ResolvedByEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      return null;
    }
  }

  private JsonNullable<ResolvedByEnum> resolvedBy = JsonNullable.<ResolvedByEnum>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime openedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> resolvedAt = JsonNullable.<OffsetDateTime>undefined();

  public Dispute disputeId(@Nullable UUID disputeId) {
    this.disputeId = disputeId;
    return this;
  }

  /**
   * Get disputeId
   * @return disputeId
   */
  @Valid 
  @Schema(name = "dispute_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dispute_id")
  public @Nullable UUID getDisputeId() {
    return disputeId;
  }

  public void setDisputeId(@Nullable UUID disputeId) {
    this.disputeId = disputeId;
  }

  public Dispute contractId(@Nullable UUID contractId) {
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

  public Dispute openedBy(@Nullable UUID openedBy) {
    this.openedBy = openedBy;
    return this;
  }

  /**
   * Get openedBy
   * @return openedBy
   */
  @Valid 
  @Schema(name = "opened_by", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opened_by")
  public @Nullable UUID getOpenedBy() {
    return openedBy;
  }

  public void setOpenedBy(@Nullable UUID openedBy) {
    this.openedBy = openedBy;
  }

  public Dispute reason(@Nullable String reason) {
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

  public Dispute status(@Nullable StatusEnum status) {
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

  public Dispute resolution(String resolution) {
    this.resolution = JsonNullable.of(resolution);
    return this;
  }

  /**
   * Get resolution
   * @return resolution
   */
  
  @Schema(name = "resolution", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolution")
  public JsonNullable<String> getResolution() {
    return resolution;
  }

  public void setResolution(JsonNullable<String> resolution) {
    this.resolution = resolution;
  }

  public Dispute resolvedBy(ResolvedByEnum resolvedBy) {
    this.resolvedBy = JsonNullable.of(resolvedBy);
    return this;
  }

  /**
   * Get resolvedBy
   * @return resolvedBy
   */
  
  @Schema(name = "resolved_by", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolved_by")
  public JsonNullable<ResolvedByEnum> getResolvedBy() {
    return resolvedBy;
  }

  public void setResolvedBy(JsonNullable<ResolvedByEnum> resolvedBy) {
    this.resolvedBy = resolvedBy;
  }

  public Dispute openedAt(@Nullable OffsetDateTime openedAt) {
    this.openedAt = openedAt;
    return this;
  }

  /**
   * Get openedAt
   * @return openedAt
   */
  @Valid 
  @Schema(name = "opened_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("opened_at")
  public @Nullable OffsetDateTime getOpenedAt() {
    return openedAt;
  }

  public void setOpenedAt(@Nullable OffsetDateTime openedAt) {
    this.openedAt = openedAt;
  }

  public Dispute resolvedAt(OffsetDateTime resolvedAt) {
    this.resolvedAt = JsonNullable.of(resolvedAt);
    return this;
  }

  /**
   * Get resolvedAt
   * @return resolvedAt
   */
  @Valid 
  @Schema(name = "resolved_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("resolved_at")
  public JsonNullable<OffsetDateTime> getResolvedAt() {
    return resolvedAt;
  }

  public void setResolvedAt(JsonNullable<OffsetDateTime> resolvedAt) {
    this.resolvedAt = resolvedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Dispute dispute = (Dispute) o;
    return Objects.equals(this.disputeId, dispute.disputeId) &&
        Objects.equals(this.contractId, dispute.contractId) &&
        Objects.equals(this.openedBy, dispute.openedBy) &&
        Objects.equals(this.reason, dispute.reason) &&
        Objects.equals(this.status, dispute.status) &&
        equalsNullable(this.resolution, dispute.resolution) &&
        equalsNullable(this.resolvedBy, dispute.resolvedBy) &&
        Objects.equals(this.openedAt, dispute.openedAt) &&
        equalsNullable(this.resolvedAt, dispute.resolvedAt);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(disputeId, contractId, openedBy, reason, status, hashCodeNullable(resolution), hashCodeNullable(resolvedBy), openedAt, hashCodeNullable(resolvedAt));
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
    sb.append("class Dispute {\n");
    sb.append("    disputeId: ").append(toIndentedString(disputeId)).append("\n");
    sb.append("    contractId: ").append(toIndentedString(contractId)).append("\n");
    sb.append("    openedBy: ").append(toIndentedString(openedBy)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    resolution: ").append(toIndentedString(resolution)).append("\n");
    sb.append("    resolvedBy: ").append(toIndentedString(resolvedBy)).append("\n");
    sb.append("    openedAt: ").append(toIndentedString(openedAt)).append("\n");
    sb.append("    resolvedAt: ").append(toIndentedString(resolvedAt)).append("\n");
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

