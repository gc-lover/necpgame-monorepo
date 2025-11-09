package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * TwoFactorSetupResponse
 */


public class TwoFactorSetupResponse {

  private @Nullable String secret;

  private @Nullable URI otpauthUrl;

  @Valid
  private List<String> recoveryCodes = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public TwoFactorSetupResponse secret(@Nullable String secret) {
    this.secret = secret;
    return this;
  }

  /**
   * Base32 секрет для генерации TOTP
   * @return secret
   */
  
  @Schema(name = "secret", description = "Base32 секрет для генерации TOTP", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("secret")
  public @Nullable String getSecret() {
    return secret;
  }

  public void setSecret(@Nullable String secret) {
    this.secret = secret;
  }

  public TwoFactorSetupResponse otpauthUrl(@Nullable URI otpauthUrl) {
    this.otpauthUrl = otpauthUrl;
    return this;
  }

  /**
   * Get otpauthUrl
   * @return otpauthUrl
   */
  @Valid 
  @Schema(name = "otpauth_url", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("otpauth_url")
  public @Nullable URI getOtpauthUrl() {
    return otpauthUrl;
  }

  public void setOtpauthUrl(@Nullable URI otpauthUrl) {
    this.otpauthUrl = otpauthUrl;
  }

  public TwoFactorSetupResponse recoveryCodes(List<String> recoveryCodes) {
    this.recoveryCodes = recoveryCodes;
    return this;
  }

  public TwoFactorSetupResponse addRecoveryCodesItem(String recoveryCodesItem) {
    if (this.recoveryCodes == null) {
      this.recoveryCodes = new ArrayList<>();
    }
    this.recoveryCodes.add(recoveryCodesItem);
    return this;
  }

  /**
   * Get recoveryCodes
   * @return recoveryCodes
   */
  
  @Schema(name = "recovery_codes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recovery_codes")
  public List<String> getRecoveryCodes() {
    return recoveryCodes;
  }

  public void setRecoveryCodes(List<String> recoveryCodes) {
    this.recoveryCodes = recoveryCodes;
  }

  public TwoFactorSetupResponse expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Время истечения окна активации (обычно 10 минут)
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", description = "Время истечения окна активации (обычно 10 минут)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
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
    TwoFactorSetupResponse twoFactorSetupResponse = (TwoFactorSetupResponse) o;
    return Objects.equals(this.secret, twoFactorSetupResponse.secret) &&
        Objects.equals(this.otpauthUrl, twoFactorSetupResponse.otpauthUrl) &&
        Objects.equals(this.recoveryCodes, twoFactorSetupResponse.recoveryCodes) &&
        Objects.equals(this.expiresAt, twoFactorSetupResponse.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(secret, otpauthUrl, recoveryCodes, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TwoFactorSetupResponse {\n");
    sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
    sb.append("    otpauthUrl: ").append(toIndentedString(otpauthUrl)).append("\n");
    sb.append("    recoveryCodes: ").append(toIndentedString(recoveryCodes)).append("\n");
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

