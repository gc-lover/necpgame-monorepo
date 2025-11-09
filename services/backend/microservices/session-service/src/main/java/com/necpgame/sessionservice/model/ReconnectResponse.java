package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReconnectResponse
 */


public class ReconnectResponse {

  private @Nullable String sessionId;

  private @Nullable String status;

  private @Nullable String reconnectToken;

  /**
   * Gets or Sets warnings
   */
  public enum WarningsEnum {
    ATTEMPTS_LIMIT_NEAR("ATTEMPTS_LIMIT_NEAR"),
    
    TOKEN_EXPIRING("TOKEN_EXPIRING");

    private final String value;

    WarningsEnum(String value) {
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
    public static WarningsEnum fromValue(String value) {
      for (WarningsEnum b : WarningsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<WarningsEnum> warnings = new ArrayList<>();

  public ReconnectResponse sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sessionId")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public ReconnectResponse status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public ReconnectResponse reconnectToken(@Nullable String reconnectToken) {
    this.reconnectToken = reconnectToken;
    return this;
  }

  /**
   * Get reconnectToken
   * @return reconnectToken
   */
  
  @Schema(name = "reconnectToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reconnectToken")
  public @Nullable String getReconnectToken() {
    return reconnectToken;
  }

  public void setReconnectToken(@Nullable String reconnectToken) {
    this.reconnectToken = reconnectToken;
  }

  public ReconnectResponse warnings(List<WarningsEnum> warnings) {
    this.warnings = warnings;
    return this;
  }

  public ReconnectResponse addWarningsItem(WarningsEnum warningsItem) {
    if (this.warnings == null) {
      this.warnings = new ArrayList<>();
    }
    this.warnings.add(warningsItem);
    return this;
  }

  /**
   * Get warnings
   * @return warnings
   */
  
  @Schema(name = "warnings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warnings")
  public List<WarningsEnum> getWarnings() {
    return warnings;
  }

  public void setWarnings(List<WarningsEnum> warnings) {
    this.warnings = warnings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReconnectResponse reconnectResponse = (ReconnectResponse) o;
    return Objects.equals(this.sessionId, reconnectResponse.sessionId) &&
        Objects.equals(this.status, reconnectResponse.status) &&
        Objects.equals(this.reconnectToken, reconnectResponse.reconnectToken) &&
        Objects.equals(this.warnings, reconnectResponse.warnings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, status, reconnectToken, warnings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReconnectResponse {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    reconnectToken: ").append(toIndentedString(reconnectToken)).append("\n");
    sb.append("    warnings: ").append(toIndentedString(warnings)).append("\n");
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

