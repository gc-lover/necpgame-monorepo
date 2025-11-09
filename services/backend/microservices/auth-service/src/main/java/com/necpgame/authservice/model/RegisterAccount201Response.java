package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RegisterAccount201Response
 */

@JsonTypeName("registerAccount_201_response")

public class RegisterAccount201Response {

  private @Nullable String accountId;

  private @Nullable String email;

  private @Nullable Boolean verificationRequired;

  private @Nullable String message;

  public RegisterAccount201Response accountId(@Nullable String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account_id")
  public @Nullable String getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable String accountId) {
    this.accountId = accountId;
  }

  public RegisterAccount201Response email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public RegisterAccount201Response verificationRequired(@Nullable Boolean verificationRequired) {
    this.verificationRequired = verificationRequired;
    return this;
  }

  /**
   * Get verificationRequired
   * @return verificationRequired
   */
  
  @Schema(name = "verification_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("verification_required")
  public @Nullable Boolean getVerificationRequired() {
    return verificationRequired;
  }

  public void setVerificationRequired(@Nullable Boolean verificationRequired) {
    this.verificationRequired = verificationRequired;
  }

  public RegisterAccount201Response message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterAccount201Response registerAccount201Response = (RegisterAccount201Response) o;
    return Objects.equals(this.accountId, registerAccount201Response.accountId) &&
        Objects.equals(this.email, registerAccount201Response.email) &&
        Objects.equals(this.verificationRequired, registerAccount201Response.verificationRequired) &&
        Objects.equals(this.message, registerAccount201Response.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accountId, email, verificationRequired, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterAccount201Response {\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    verificationRequired: ").append(toIndentedString(verificationRequired)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

