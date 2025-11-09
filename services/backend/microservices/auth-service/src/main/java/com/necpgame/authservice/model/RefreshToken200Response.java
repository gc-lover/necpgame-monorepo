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
 * RefreshToken200Response
 */

@JsonTypeName("refreshToken_200_response")

public class RefreshToken200Response {

  private @Nullable String accessToken;

  private @Nullable String tokenType;

  private @Nullable Integer expiresIn;

  public RefreshToken200Response accessToken(@Nullable String accessToken) {
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

  public RefreshToken200Response tokenType(@Nullable String tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  /**
   * Get tokenType
   * @return tokenType
   */
  
  @Schema(name = "token_type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("token_type")
  public @Nullable String getTokenType() {
    return tokenType;
  }

  public void setTokenType(@Nullable String tokenType) {
    this.tokenType = tokenType;
  }

  public RefreshToken200Response expiresIn(@Nullable Integer expiresIn) {
    this.expiresIn = expiresIn;
    return this;
  }

  /**
   * Get expiresIn
   * @return expiresIn
   */
  
  @Schema(name = "expires_in", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expires_in")
  public @Nullable Integer getExpiresIn() {
    return expiresIn;
  }

  public void setExpiresIn(@Nullable Integer expiresIn) {
    this.expiresIn = expiresIn;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RefreshToken200Response refreshToken200Response = (RefreshToken200Response) o;
    return Objects.equals(this.accessToken, refreshToken200Response.accessToken) &&
        Objects.equals(this.tokenType, refreshToken200Response.tokenType) &&
        Objects.equals(this.expiresIn, refreshToken200Response.expiresIn);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessToken, tokenType, expiresIn);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RefreshToken200Response {\n");
    sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");
    sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");
    sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");
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

