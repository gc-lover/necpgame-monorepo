package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TokenVerifyResponse
 */


public class TokenVerifyResponse {

  private @Nullable Boolean valid;

  private JsonNullable<UUID> accountId = JsonNullable.<UUID>undefined();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime issuedAt;

  @Valid
  private List<String> roles = new ArrayList<>();

  private @Nullable String reason;

  public TokenVerifyResponse valid(@Nullable Boolean valid) {
    this.valid = valid;
    return this;
  }

  /**
   * Get valid
   * @return valid
   */
  
  @Schema(name = "valid", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("valid")
  public @Nullable Boolean getValid() {
    return valid;
  }

  public void setValid(@Nullable Boolean valid) {
    this.valid = valid;
  }

  public TokenVerifyResponse accountId(UUID accountId) {
    this.accountId = JsonNullable.of(accountId);
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  @Valid 
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account_id")
  public JsonNullable<UUID> getAccountId() {
    return accountId;
  }

  public void setAccountId(JsonNullable<UUID> accountId) {
    this.accountId = accountId;
  }

  public TokenVerifyResponse expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expires_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_at")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public TokenVerifyResponse issuedAt(@Nullable OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
    return this;
  }

  /**
   * Get issuedAt
   * @return issuedAt
   */
  @Valid 
  @Schema(name = "issued_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("issued_at")
  public @Nullable OffsetDateTime getIssuedAt() {
    return issuedAt;
  }

  public void setIssuedAt(@Nullable OffsetDateTime issuedAt) {
    this.issuedAt = issuedAt;
  }

  public TokenVerifyResponse roles(List<String> roles) {
    this.roles = roles;
    return this;
  }

  public TokenVerifyResponse addRolesItem(String rolesItem) {
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

  public TokenVerifyResponse reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Причина невалидности (если valid=false)
   * @return reason
   */
  
  @Schema(name = "reason", description = "Причина невалидности (если valid=false)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    TokenVerifyResponse tokenVerifyResponse = (TokenVerifyResponse) o;
    return Objects.equals(this.valid, tokenVerifyResponse.valid) &&
        equalsNullable(this.accountId, tokenVerifyResponse.accountId) &&
        Objects.equals(this.expiresAt, tokenVerifyResponse.expiresAt) &&
        Objects.equals(this.issuedAt, tokenVerifyResponse.issuedAt) &&
        Objects.equals(this.roles, tokenVerifyResponse.roles) &&
        Objects.equals(this.reason, tokenVerifyResponse.reason);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(valid, hashCodeNullable(accountId), expiresAt, issuedAt, roles, reason);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TokenVerifyResponse {\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    issuedAt: ").append(toIndentedString(issuedAt)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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

