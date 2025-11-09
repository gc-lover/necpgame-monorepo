package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * LoginResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LoginResponse {

  private String token;

  private UUID accountId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime expiresAt;

  public LoginResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoginResponse(String token, UUID accountId, OffsetDateTime expiresAt) {
    this.token = token;
    this.accountId = accountId;
    this.expiresAt = expiresAt;
  }

  public LoginResponse token(String token) {
    this.token = token;
    return this;
  }

  /**
   * JWT токен для аутентификации
   * @return token
   */
  @NotNull 
  @Schema(name = "token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", description = "JWT токен для аутентификации", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public LoginResponse accountId(UUID accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Идентификатор аккаунта
   * @return accountId
   */
  @NotNull @Valid 
  @Schema(name = "account_id", example = "550e8400-e29b-41d4-a716-446655440000", description = "Идентификатор аккаунта", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("account_id")
  public UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(UUID accountId) {
    this.accountId = accountId;
  }

  public LoginResponse expiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Дата истечения токена
   * @return expiresAt
   */
  @NotNull @Valid 
  @Schema(name = "expires_at", example = "2025-01-27T20:00Z", description = "Дата истечения токена", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expires_at")
  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginResponse loginResponse = (LoginResponse) o;
    return Objects.equals(this.token, loginResponse.token) &&
        Objects.equals(this.accountId, loginResponse.accountId) &&
        Objects.equals(this.expiresAt, loginResponse.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token, accountId, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginResponse {\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

