package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RegisterResponse
 */


public class RegisterResponse {

  private @Nullable UUID accountId;

  private @Nullable String email;

  private @Nullable String accessToken;

  private @Nullable String refreshToken;

  private @Nullable Boolean verificationRequired;

  public RegisterResponse accountId(@Nullable UUID accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  @Valid 
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account_id")
  public @Nullable UUID getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable UUID accountId) {
    this.accountId = accountId;
  }

  public RegisterResponse email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public RegisterResponse accessToken(@Nullable String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * JWT access token (15 минут)
   * @return accessToken
   */
  
  @Schema(name = "access_token", description = "JWT access token (15 минут)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_token")
  public @Nullable String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(@Nullable String accessToken) {
    this.accessToken = accessToken;
  }

  public RegisterResponse refreshToken(@Nullable String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  /**
   * Refresh token (7 дней)
   * @return refreshToken
   */
  
  @Schema(name = "refresh_token", description = "Refresh token (7 дней)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refresh_token")
  public @Nullable String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(@Nullable String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public RegisterResponse verificationRequired(@Nullable Boolean verificationRequired) {
    this.verificationRequired = verificationRequired;
    return this;
  }

  /**
   * Нужно ли подтвердить email перед входом в игру
   * @return verificationRequired
   */
  
  @Schema(name = "verification_required", description = "Нужно ли подтвердить email перед входом в игру", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("verification_required")
  public @Nullable Boolean getVerificationRequired() {
    return verificationRequired;
  }

  public void setVerificationRequired(@Nullable Boolean verificationRequired) {
    this.verificationRequired = verificationRequired;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterResponse registerResponse = (RegisterResponse) o;
    return Objects.equals(this.accountId, registerResponse.accountId) &&
        Objects.equals(this.email, registerResponse.email) &&
        Objects.equals(this.accessToken, registerResponse.accessToken) &&
        Objects.equals(this.refreshToken, registerResponse.refreshToken) &&
        Objects.equals(this.verificationRequired, registerResponse.verificationRequired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, email, accessToken, refreshToken, verificationRequired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterResponse {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
    sb.append("    verificationRequired: ").append(toIndentedString(verificationRequired)).append("\n");
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

