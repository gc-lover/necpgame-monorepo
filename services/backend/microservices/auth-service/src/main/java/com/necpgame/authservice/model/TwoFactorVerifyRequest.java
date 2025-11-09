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
 * TwoFactorVerifyRequest
 */


public class TwoFactorVerifyRequest {

  private String code;

  private @Nullable String recoveryCode;

  public TwoFactorVerifyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TwoFactorVerifyRequest(String code) {
    this.code = code;
  }

  public TwoFactorVerifyRequest code(String code) {
    this.code = code;
    return this;
  }

  /**
   * 6-значный TOTP код
   * @return code
   */
  @NotNull 
  @Schema(name = "code", description = "6-значный TOTP код", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public TwoFactorVerifyRequest recoveryCode(@Nullable String recoveryCode) {
    this.recoveryCode = recoveryCode;
    return this;
  }

  /**
   * Можно использовать вместо кода для разовой активации
   * @return recoveryCode
   */
  
  @Schema(name = "recovery_code", description = "Можно использовать вместо кода для разовой активации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recovery_code")
  public @Nullable String getRecoveryCode() {
    return recoveryCode;
  }

  public void setRecoveryCode(@Nullable String recoveryCode) {
    this.recoveryCode = recoveryCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TwoFactorVerifyRequest twoFactorVerifyRequest = (TwoFactorVerifyRequest) o;
    return Objects.equals(this.code, twoFactorVerifyRequest.code) &&
        Objects.equals(this.recoveryCode, twoFactorVerifyRequest.recoveryCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, recoveryCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TwoFactorVerifyRequest {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    recoveryCode: ").append(toIndentedString(recoveryCode)).append("\n");
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

