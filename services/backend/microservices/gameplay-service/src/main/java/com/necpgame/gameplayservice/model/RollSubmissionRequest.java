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
 * RollSubmissionRequest
 */


public class RollSubmissionRequest {

  /**
   * Gets or Sets rollType
   */
  public enum RollTypeEnum {
    NEED("NEED"),
    
    GREED("GREED"),
    
    PASS("PASS");

    private final String value;

    RollTypeEnum(String value) {
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
    public static RollTypeEnum fromValue(String value) {
      for (RollTypeEnum b : RollTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RollTypeEnum rollType;

  private @Nullable Boolean autoAccept;

  private @Nullable String reason;

  public RollSubmissionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RollSubmissionRequest(RollTypeEnum rollType) {
    this.rollType = rollType;
  }

  public RollSubmissionRequest rollType(RollTypeEnum rollType) {
    this.rollType = rollType;
    return this;
  }

  /**
   * Get rollType
   * @return rollType
   */
  @NotNull 
  @Schema(name = "rollType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rollType")
  public RollTypeEnum getRollType() {
    return rollType;
  }

  public void setRollType(RollTypeEnum rollType) {
    this.rollType = rollType;
  }

  public RollSubmissionRequest autoAccept(@Nullable Boolean autoAccept) {
    this.autoAccept = autoAccept;
    return this;
  }

  /**
   * Get autoAccept
   * @return autoAccept
   */
  
  @Schema(name = "autoAccept", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoAccept")
  public @Nullable Boolean getAutoAccept() {
    return autoAccept;
  }

  public void setAutoAccept(@Nullable Boolean autoAccept) {
    this.autoAccept = autoAccept;
  }

  public RollSubmissionRequest reason(@Nullable String reason) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RollSubmissionRequest rollSubmissionRequest = (RollSubmissionRequest) o;
    return Objects.equals(this.rollType, rollSubmissionRequest.rollType) &&
        Objects.equals(this.autoAccept, rollSubmissionRequest.autoAccept) &&
        Objects.equals(this.reason, rollSubmissionRequest.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rollType, autoAccept, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollSubmissionRequest {\n");
    sb.append("    rollType: ").append(toIndentedString(rollType)).append("\n");
    sb.append("    autoAccept: ").append(toIndentedString(autoAccept)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

