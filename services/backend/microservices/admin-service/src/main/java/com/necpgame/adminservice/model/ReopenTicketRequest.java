package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReopenTicketRequest
 */


public class ReopenTicketRequest {

  private String reason;

  /**
   * Gets or Sets initiatedBy
   */
  public enum InitiatedByEnum {
    PLAYER("PLAYER"),
    
    AGENT("AGENT"),
    
    SUPERVISOR("SUPERVISOR");

    private final String value;

    InitiatedByEnum(String value) {
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
    public static InitiatedByEnum fromValue(String value) {
      for (InitiatedByEnum b : InitiatedByEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable InitiatedByEnum initiatedBy;

  public ReopenTicketRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReopenTicketRequest(String reason) {
    this.reason = reason;
  }

  public ReopenTicketRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public ReopenTicketRequest initiatedBy(@Nullable InitiatedByEnum initiatedBy) {
    this.initiatedBy = initiatedBy;
    return this;
  }

  /**
   * Get initiatedBy
   * @return initiatedBy
   */
  
  @Schema(name = "initiatedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("initiatedBy")
  public @Nullable InitiatedByEnum getInitiatedBy() {
    return initiatedBy;
  }

  public void setInitiatedBy(@Nullable InitiatedByEnum initiatedBy) {
    this.initiatedBy = initiatedBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReopenTicketRequest reopenTicketRequest = (ReopenTicketRequest) o;
    return Objects.equals(this.reason, reopenTicketRequest.reason) &&
        Objects.equals(this.initiatedBy, reopenTicketRequest.initiatedBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, initiatedBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReopenTicketRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    initiatedBy: ").append(toIndentedString(initiatedBy)).append("\n");
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

