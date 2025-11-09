package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * ReconnectTokenResponse
 */


public class ReconnectTokenResponse {

  private String reconnectToken;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime expiresAt;

  private @Nullable Integer attemptsRemaining;

  public ReconnectTokenResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReconnectTokenResponse(String reconnectToken, OffsetDateTime expiresAt) {
    this.reconnectToken = reconnectToken;
    this.expiresAt = expiresAt;
  }

  public ReconnectTokenResponse reconnectToken(String reconnectToken) {
    this.reconnectToken = reconnectToken;
    return this;
  }

  /**
   * Get reconnectToken
   * @return reconnectToken
   */
  @NotNull 
  @Schema(name = "reconnectToken", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reconnectToken")
  public String getReconnectToken() {
    return reconnectToken;
  }

  public void setReconnectToken(String reconnectToken) {
    this.reconnectToken = reconnectToken;
  }

  public ReconnectTokenResponse expiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @NotNull @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expiresAt")
  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public ReconnectTokenResponse attemptsRemaining(@Nullable Integer attemptsRemaining) {
    this.attemptsRemaining = attemptsRemaining;
    return this;
  }

  /**
   * Get attemptsRemaining
   * minimum: 0
   * maximum: 3
   * @return attemptsRemaining
   */
  @Min(value = 0) @Max(value = 3) 
  @Schema(name = "attemptsRemaining", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attemptsRemaining")
  public @Nullable Integer getAttemptsRemaining() {
    return attemptsRemaining;
  }

  public void setAttemptsRemaining(@Nullable Integer attemptsRemaining) {
    this.attemptsRemaining = attemptsRemaining;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReconnectTokenResponse reconnectTokenResponse = (ReconnectTokenResponse) o;
    return Objects.equals(this.reconnectToken, reconnectTokenResponse.reconnectToken) &&
        Objects.equals(this.expiresAt, reconnectTokenResponse.expiresAt) &&
        Objects.equals(this.attemptsRemaining, reconnectTokenResponse.attemptsRemaining);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reconnectToken, expiresAt, attemptsRemaining);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReconnectTokenResponse {\n");
    sb.append("    reconnectToken: ").append(toIndentedString(reconnectToken)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    attemptsRemaining: ").append(toIndentedString(attemptsRemaining)).append("\n");
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

