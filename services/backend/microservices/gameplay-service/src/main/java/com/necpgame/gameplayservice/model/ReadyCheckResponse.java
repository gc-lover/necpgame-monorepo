package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
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
 * ReadyCheckResponse
 */


public class ReadyCheckResponse {

  private UUID playerId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACCEPTED("ACCEPTED"),
    
    DECLINED("DECLINED"),
    
    TIMEOUT("TIMEOUT");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime respondedAt;

  private @Nullable String reason;

  public ReadyCheckResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReadyCheckResponse(UUID playerId, StatusEnum status) {
    this.playerId = playerId;
    this.status = status;
  }

  public ReadyCheckResponse playerId(UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull @Valid 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public UUID getPlayerId() {
    return playerId;
  }

  public void setPlayerId(UUID playerId) {
    this.playerId = playerId;
  }

  public ReadyCheckResponse status(StatusEnum status) {
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

  public ReadyCheckResponse respondedAt(@Nullable OffsetDateTime respondedAt) {
    this.respondedAt = respondedAt;
    return this;
  }

  /**
   * Get respondedAt
   * @return respondedAt
   */
  @Valid 
  @Schema(name = "respondedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("respondedAt")
  public @Nullable OffsetDateTime getRespondedAt() {
    return respondedAt;
  }

  public void setRespondedAt(@Nullable OffsetDateTime respondedAt) {
    this.respondedAt = respondedAt;
  }

  public ReadyCheckResponse reason(@Nullable String reason) {
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
    ReadyCheckResponse readyCheckResponse = (ReadyCheckResponse) o;
    return Objects.equals(this.playerId, readyCheckResponse.playerId) &&
        Objects.equals(this.status, readyCheckResponse.status) &&
        Objects.equals(this.respondedAt, readyCheckResponse.respondedAt) &&
        Objects.equals(this.reason, readyCheckResponse.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, status, respondedAt, reason);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReadyCheckResponse {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    respondedAt: ").append(toIndentedString(respondedAt)).append("\n");
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

