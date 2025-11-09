package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * LoginResponse
 */


public class LoginResponse {

  private @Nullable String accessToken;

  private @Nullable String refreshToken;

  private @Nullable String tokenType;

  private @Nullable Integer expiresIn;

  private @Nullable UUID accountId;

  @Valid
  private List<String> roles = new ArrayList<>();

  private @Nullable Boolean twoFactorRequired;

  public LoginResponse accessToken(@Nullable String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * Get accessToken
   * @return accessToken
   */
  
  @Schema(name = "access_token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("access_token")
  public @Nullable String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(@Nullable String accessToken) {
    this.accessToken = accessToken;
  }

  public LoginResponse refreshToken(@Nullable String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  /**
   * Get refreshToken
   * @return refreshToken
   */
  
  @Schema(name = "refresh_token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("refresh_token")
  public @Nullable String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(@Nullable String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public LoginResponse tokenType(@Nullable String tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  /**
   * Get tokenType
   * @return tokenType
   */
  
  @Schema(name = "token_type", example = "Bearer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("token_type")
  public @Nullable String getTokenType() {
    return tokenType;
  }

  public void setTokenType(@Nullable String tokenType) {
    this.tokenType = tokenType;
  }

  public LoginResponse expiresIn(@Nullable Integer expiresIn) {
    this.expiresIn = expiresIn;
    return this;
  }

  /**
   * Время жизни access token в секундах
   * @return expiresIn
   */
  
  @Schema(name = "expires_in", description = "Время жизни access token в секундах", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_in")
  public @Nullable Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(@Nullable Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  public LoginResponse accountId(@Nullable UUID accountId) {
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

  public LoginResponse roles(List<String> roles) {
    this.roles = roles;
    return this;
  }

  public LoginResponse addRolesItem(String rolesItem) {
    if (this.roles == null) {
      this.roles = new ArrayList<>();
    }
    this.roles.add(rolesItem);
    return this;
  }

  /**
   * Get roles
   * @return roles
   */
  
  @Schema(name = "roles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roles")
  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

  public LoginResponse twoFactorRequired(@Nullable Boolean twoFactorRequired) {
    this.twoFactorRequired = twoFactorRequired;
    return this;
  }

  /**
   * true, если требуется подтверждение 2FA перед выдачей токенов
   * @return twoFactorRequired
   */
  
  @Schema(name = "two_factor_required", description = "true, если требуется подтверждение 2FA перед выдачей токенов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("two_factor_required")
  public @Nullable Boolean getTwoFactorRequired() {
    return twoFactorRequired;
  }

  public void setTwoFactorRequired(@Nullable Boolean twoFactorRequired) {
    this.twoFactorRequired = twoFactorRequired;
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
    return Objects.equals(this.accessToken, loginResponse.accessToken) &&
        Objects.equals(this.refreshToken, loginResponse.refreshToken) &&
        Objects.equals(this.tokenType, loginResponse.tokenType) &&
        Objects.equals(this.expiresIn, loginResponse.expiresIn) &&
        Objects.equals(this.accountId, loginResponse.accountId) &&
        Objects.equals(this.roles, loginResponse.roles) &&
        Objects.equals(this.twoFactorRequired, loginResponse.twoFactorRequired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, refreshToken, tokenType, expiresIn, accountId, roles, twoFactorRequired);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginResponse {\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    refreshToken: ").append(toIndentedString(refreshToken)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
    sb.append("    twoFactorRequired: ").append(toIndentedString(twoFactorRequired)).append("\n");
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

