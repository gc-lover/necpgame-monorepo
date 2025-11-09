package com.necpgame.gameplayservice.model;

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
 * SuspendRequest
 */


public class SuspendRequest {

  /**
   * Gets or Sets reason
   */
  public enum ReasonEnum {
    ANTI_ABUSE("anti_abuse"),
    
    MAINTENANCE("maintenance"),
    
    PLAYER_REQUEST("player_request");

    private final String value;

    ReasonEnum(String value) {
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
    public static ReasonEnum fromValue(String value) {
      for (ReasonEnum b : ReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ReasonEnum reason;

  private @Nullable Integer durationHours;

  private @Nullable String initiatedBy;

  private @Nullable String notes;

  public SuspendRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SuspendRequest(ReasonEnum reason) {
    this.reason = reason;
  }

  public SuspendRequest reason(ReasonEnum reason) {
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
  public ReasonEnum getReason() {
    return reason;
  }

  public void setReason(ReasonEnum reason) {
    this.reason = reason;
  }

  public SuspendRequest durationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
    return this;
  }

  /**
   * Get durationHours
   * minimum: 1
   * maximum: 168
   * @return durationHours
   */
  @Min(value = 1) @Max(value = 168) 
  @Schema(name = "durationHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationHours")
  public @Nullable Integer getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
  }

  public SuspendRequest initiatedBy(@Nullable String initiatedBy) {
    this.initiatedBy = initiatedBy;
    return this;
  }

  /**
   * Get initiatedBy
   * @return initiatedBy
   */
  
  @Schema(name = "initiatedBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("initiatedBy")
  public @Nullable String getInitiatedBy() {
    return initiatedBy;
  }

  public void setInitiatedBy(@Nullable String initiatedBy) {
    this.initiatedBy = initiatedBy;
  }

  public SuspendRequest notes(@Nullable String notes) {
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
    SuspendRequest suspendRequest = (SuspendRequest) o;
    return Objects.equals(this.reason, suspendRequest.reason) &&
        Objects.equals(this.durationHours, suspendRequest.durationHours) &&
        Objects.equals(this.initiatedBy, suspendRequest.initiatedBy) &&
        Objects.equals(this.notes, suspendRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, durationHours, initiatedBy, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SuspendRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    durationHours: ").append(toIndentedString(durationHours)).append("\n");
    sb.append("    initiatedBy: ").append(toIndentedString(initiatedBy)).append("\n");
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

