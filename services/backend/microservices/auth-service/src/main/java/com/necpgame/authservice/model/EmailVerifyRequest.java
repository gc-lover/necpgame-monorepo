package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EmailVerifyRequest
 */


public class EmailVerifyRequest {

  private String verificationToken;

  public EmailVerifyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EmailVerifyRequest(String verificationToken) {
    this.verificationToken = verificationToken;
  }

  public EmailVerifyRequest verificationToken(String verificationToken) {
    this.verificationToken = verificationToken;
    return this;
  }

  /**
   * Get verificationToken
   * @return verificationToken
   */
  @NotNull 
  @Schema(name = "verification_token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("verification_token")
  public String getVerificationToken() {
    return verificationToken;
  }

  public void setVerificationToken(String verificationToken) {
    this.verificationToken = verificationToken;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmailVerifyRequest emailVerifyRequest = (EmailVerifyRequest) o;
    return Objects.equals(this.verificationToken, emailVerifyRequest.verificationToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(verificationToken);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EmailVerifyRequest {\n");
    sb.append("    verificationToken: ").append(toIndentedString(verificationToken)).append("\n");
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

